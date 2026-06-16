package br.tec.suportes.ragdbamv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> dados;
    private int pagina;
    private int tamanhoPagina;
    private long total;
}
