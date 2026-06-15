package br.tec.suportes.ragdbamv.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultaResponse {
    private String pergunta;
    private String sql;
}
