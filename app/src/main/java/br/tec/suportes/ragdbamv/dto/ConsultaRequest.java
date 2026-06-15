package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsultaRequest {
    @NotBlank
    private String pergunta;
    private int topK = 5;
}
