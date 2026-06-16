package br.tec.suportes.ragdbamv.controller;

import br.tec.suportes.ragdbamv.dto.PagedResponse;
import br.tec.suportes.ragdbamv.dto.TabelaDescricaoRequest;
import br.tec.suportes.ragdbamv.dto.TabelaRequest;
import br.tec.suportes.ragdbamv.dto.TabelaResponse;
import br.tec.suportes.ragdbamv.service.TabelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tabelas")
@RequiredArgsConstructor
public class TabelaController {

    private final TabelaService tabelaService;

    @PostMapping
    public ResponseEntity<TabelaResponse> salvar(@Valid @RequestBody TabelaRequest req) {
        return ResponseEntity.ok(tabelaService.salvar(req));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<TabelaResponse>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "20") int tamanhoPagina) {
        return ResponseEntity.ok(tabelaService.listar(pagina, tamanhoPagina));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TabelaResponse> buscar(@PathVariable UUID id) {
        return ResponseEntity.ok(tabelaService.buscar(id));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<TabelaResponse> buscarPorNome(@PathVariable String nome) {
        return ResponseEntity.ok(tabelaService.buscarPorNome(nome));
    }

    @PatchMapping("/{nome}/descricao")
    public ResponseEntity<TabelaResponse> atualizarDescricao(
            @PathVariable String nome,
            @Valid @RequestBody TabelaDescricaoRequest req) {
        return ResponseEntity.ok(tabelaService.atualizarDescricao(nome, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        tabelaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
