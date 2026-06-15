package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TabelaDescricaoRequest {
    @NotBlank
    private String descricao;
}
