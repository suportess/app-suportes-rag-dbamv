package br.tec.suportes.ragdbamv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RelacionamentoRequest {
    @NotNull
    private UUID tabelaOrigemId;
    @NotBlank
    private String colunaOrigem;
    @NotNull
    private UUID tabelaDestinoId;
    @NotBlank
    private String colunaDestino;
    private String descricao;
}
