package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ExemploRequest {
    @NotBlank
    private String pergunta;
    @NotBlank
    private String sqlGerado;
    private String descricao;
}
