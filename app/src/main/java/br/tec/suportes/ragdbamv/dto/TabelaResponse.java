package br.tec.suportes.ragdbamv.dto;

import br.tec.suportes.ragdbamv.model.Coluna;
import br.tec.suportes.ragdbamv.model.Tabela;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TabelaResponse {
    private UUID id;
    private String nome;
    private String schemaOra;
    private String descricao;
    private LocalDateTime indexadoEm;
    private List<ColunaResponse> colunas;

    @Data
    public static class ColunaResponse {
        private UUID id;
        private String nome;
        private String tipoDado;
        private String descricao;
        private Boolean nullable;
        private Boolean chavePrimaria;

        public static ColunaResponse from(Coluna c) {
            ColunaResponse r = new ColunaResponse();
            r.id = c.getId();
            r.nome = c.getNome();
            r.tipoDado = c.getTipoDado();
            r.descricao = c.getDescricao();
            r.nullable = c.getNullable();
            r.chavePrimaria = c.getChavePrimaria();
            return r;
        }
    }

    public static TabelaResponse from(Tabela t) {
        TabelaResponse r = new TabelaResponse();
        r.id = t.getId();
        r.nome = t.getNome();
        r.schemaOra = t.getSchemaOra();
        r.descricao = t.getDescricao();
        r.indexadoEm = t.getIndexadoEm();
        r.colunas = t.getColunas().stream().map(ColunaResponse::from).toList();
        return r;
    }
}
