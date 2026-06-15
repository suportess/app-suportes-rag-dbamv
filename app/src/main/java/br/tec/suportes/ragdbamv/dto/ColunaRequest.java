package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ColunaRequest {
    @NotBlank
    private String nome;
    private String tipoDado;
    private String descricao;
    private Boolean nullable = true;
    private Boolean chavePrimaria = false;
}
