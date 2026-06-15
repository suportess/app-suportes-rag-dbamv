package br.tec.suportes.ragdbamv.service;

import br.tec.suportes.ragdbamv.model.Exemplo;
import br.tec.suportes.ragdbamv.model.Tabela;
import br.tec.suportes.ragdbamv.repository.ExemploRepository;
import br.tec.suportes.ragdbamv.repository.RelacionamentoRepository;
import br.tec.suportes.ragdbamv.repository.TabelaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexacaoService {

    private final TabelaRepository tabelaRepository;
    private final RelacionamentoRepository relacionamentoRepository;
    private final ExemploRepository exemploRepository;
    private final VectorStore vectorStore;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public int indexarTudo() {
        jdbcTemplate.update("DELETE FROM vector_store");

        List<Document> docs = new ArrayList<>();

        List<Tabela> tabelas = tabelaRepository.findAllByOrderByNomeAsc();
        for (Tabela t : tabelas) {
            docs.add(gerarDocTabela(t));
            t.setIndexadoEm(LocalDateTime.now());
        }
        tabelaRepository.saveAll(tabelas);

        for (Exemplo e : exemploRepository.findAll()) {
            docs.add(gerarDocExemplo(e));
        }

        if (!docs.isEmpty()) {
            vectorStore.add(docs);
        }

        log.info("Indexados {} documentos no PgVector", docs.size());
        return docs.size();
    }

    @Transactional
    public void indexarTabela(String nome) {
        Tabela tabela = tabelaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new IllegalArgumentException("Tabela não encontrada: " + nome));

        List<String> idsAntigos = jdbcTemplate.queryForList(
                "SELECT id::text FROM vector_store WHERE metadata->>'tabela_id' = ?",
                String.class, tabela.getId().toString());
        if (!idsAntigos.isEmpty()) {
            vectorStore.delete(idsAntigos);
        }

        vectorStore.add(List.of(gerarDocTabela(tabela)));
        tabela.setIndexadoEm(LocalDateTime.now());
        tabelaRepository.save(tabela);

        log.info("Tabela {} indexada", tabela.getNome());
    }

    private Document gerarDocTabela(Tabela t) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tabela: ").append(t.getSchemaOra()).append(".").append(t.getNome()).append("\n");
        if (t.getDescricao() != null) sb.append("Descrição: ").append(t.getDescricao()).append("\n");

        if (!t.getColunas().isEmpty()) {
            sb.append("Colunas:\n");
            t.getColunas().forEach(c -> {
                sb.append("  - ").append(c.getNome());
                if (c.getTipoDado() != null) sb.append(" (").append(c.getTipoDado()).append(")");
                if (Boolean.TRUE.equals(c.getChavePrimaria())) sb.append(" [PK]");
                if (c.getDescricao() != null) sb.append(": ").append(c.getDescricao());
                sb.append("\n");
            });
        }

        relacionamentoRepository.findByTabelaOrigemId(t.getId()).forEach(r ->
            sb.append("JOIN: ").append(t.getNome()).append(".").append(r.getColunaOrigem())
              .append(" = ").append(r.getTabelaDestino().getNome()).append(".").append(r.getColunaDestino())
              .append("\n")
        );

        return new Document(
            sb.toString(),
            Map.of("tipo", "tabela", "tabela_id", t.getId().toString(), "nome", t.getNome())
        );
    }

    private Document gerarDocExemplo(Exemplo e) {
        String conteudo = "Pergunta: " + e.getPergunta() + "\nSQL Oracle:\n" + e.getSqlGerado();
        return new Document(
            conteudo,
            Map.of("tipo", "exemplo", "exemplo_id", e.getId().toString())
        );
    }
}
