package br.tec.suportes.ragdbamv.service;

import br.tec.suportes.ragdbamv.dto.ConsultaRequest;
import br.tec.suportes.ragdbamv.dto.ConsultaResponse;
import br.tec.suportes.ragdbamv.model.Coluna;
import br.tec.suportes.ragdbamv.model.Relacionamento;
import br.tec.suportes.ragdbamv.model.Tabela;
import br.tec.suportes.ragdbamv.repository.RelacionamentoRepository;
import br.tec.suportes.ragdbamv.repository.TabelaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final TabelaRepository tabelaRepository;
    private final RelacionamentoRepository relacionamentoRepository;

    // Captura nomes de tabela Oracle em MAIÚSCULAS (min 3 chars, letras/números/_)
    private static final Pattern NOME_TABELA  = Pattern.compile("\\b([A-Z][A-Z0-9_]{2,})\\b");
    private static final Pattern MD_FENCE     = Pattern.compile("^```[a-zA-Z]*\\s*|\\s*```$", Pattern.MULTILINE);
    private static final Pattern SQL_INICIO   = Pattern.compile("(?i)^\\s*(SELECT|WITH|INSERT|UPDATE|DELETE|MERGE)\\b", Pattern.MULTILINE);

    public ConsultaResponse consultar(ConsultaRequest req) {

        // 1. Busca vetorial
        List<Document> docs = vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(req.getPergunta())
                .topK(req.getTopK())
                .build()
        );
        log.debug("RAG: {} docs do vector store para '{}'", docs.size(), req.getPergunta());

        // 2. Expande contexto: tabelas citadas nos JOINs dos docs + na pergunta do usuário
        Set<String> nomesContexto = new LinkedHashSet<>();
        for (Document d : docs) {
            extrairNomes(d.getText(), nomesContexto);
        }
        extrairNomes(req.getPergunta().toUpperCase(), nomesContexto);

        // 3. Busca direta no repositório para cada nome candidato
        Map<String, String> tabelasTexto = new LinkedHashMap<>();
        for (Document d : docs) {
            // documento já vem do vector store — extrai nome da metadata
            Object nome = d.getMetadata().get("nome");
            if (nome != null) tabelasTexto.put(nome.toString(), d.getText());
        }
        for (String nome : nomesContexto) {
            if (tabelasTexto.containsKey(nome)) continue;
            tabelaRepository.findByNomeIgnoreCase(nome).ifPresent(t ->
                tabelasTexto.put(t.getNome(), buildTabelaTexto(t))
            );
        }

        String contextoTexto = String.join("\n\n---\n\n", tabelasTexto.values());
        log.debug("RAG: contexto final com {} tabelas: {}", tabelasTexto.size(), tabelasTexto.keySet());

        // 4. Prompt estrito
        String prompt = """
                Você é um gerador de SQL Oracle para o banco DBAMV.

                REGRAS — SIGA À RISCA:
                1. Use SOMENTE as tabelas e colunas presentes no CONTEXTO abaixo.
                2. NUNCA invente ou assuma nomes de colunas — use exatamente os nomes do contexto.
                3. Se os metadados necessários não estiverem no contexto, responda apenas: METADADOS_INDISPONIVEIS
                4. Sempre qualifique tabelas com o schema: DBAMV.<TABELA>.
                5. Sua resposta deve conter EXCLUSIVAMENTE o SQL Oracle executável.
                   — Proibido qualquer frase, explicação ou comentário antes ou depois do SQL.
                   — Proibido blocos markdown (``` sql ```).
                   — A resposta começa com SELECT, WITH, INSERT, UPDATE ou DELETE.

                CONTEXTO DO BANCO DBAMV:
                %s

                PERGUNTA DO USUÁRIO:
                %s

                """.formatted(contextoTexto, req.getPergunta());

        String sql = chatClient.prompt(prompt).call().content();

        return ConsultaResponse.builder()
                .pergunta(req.getPergunta())
                .sql(limparSql(sql))
                .build();
    }

    private void extrairNomes(String texto, Set<String> destino) {
        if (texto == null) return;
        Matcher m = NOME_TABELA.matcher(texto);
        while (m.find()) destino.add(m.group(1));
    }

    private String buildTabelaTexto(Tabela t) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tabela: ").append(t.getSchemaOra()).append(".").append(t.getNome()).append("\n");
        if (t.getDescricao() != null) sb.append("Descrição: ").append(t.getDescricao()).append("\n");
        if (!t.getColunas().isEmpty()) {
            sb.append("Colunas:\n");
            for (Coluna c : t.getColunas()) {
                sb.append("  - ").append(c.getNome());
                if (c.getTipoDado() != null) sb.append(" (").append(c.getTipoDado()).append(")");
                if (Boolean.TRUE.equals(c.getChavePrimaria())) sb.append(" [PK]");
                if (c.getDescricao() != null && !c.getDescricao().isBlank())
                    sb.append(": ").append(c.getDescricao());
                sb.append("\n");
            }
        }
        for (Relacionamento r : relacionamentoRepository.findByTabelaOrigemId(t.getId())) {
            sb.append("JOIN: ").append(t.getNome()).append(".").append(r.getColunaOrigem())
              .append(" = ").append(r.getTabelaDestino().getNome()).append(".").append(r.getColunaDestino())
              .append("\n");
        }
        return sb.toString();
    }

    private String limparSql(String raw) {
        if (raw == null) return "";
        String s = MD_FENCE.matcher(raw.trim()).replaceAll("").trim();
        // Se o modelo incluiu texto antes do SQL, extrai a partir da primeira keyword DML
        Matcher m = SQL_INICIO.matcher(s);
        if (m.find()) {
            s = s.substring(m.start()).trim();
        }
        return s;
    }
}
