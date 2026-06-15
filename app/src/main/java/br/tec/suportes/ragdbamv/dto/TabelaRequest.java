package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TabelaRequest {
    @NotBlank
    private String nome;
    private String schemaOra = "DBAMV";
    @NotBlank
    private String descricao;

    @Valid
    private List<ColunaRequest> colunas = new ArrayList<>();
}
