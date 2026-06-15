package br.tec.suportes.ragdbamv.controller;

import br.tec.suportes.ragdbamv.dto.RelacionamentoRequest;
import br.tec.suportes.ragdbamv.model.Relacionamento;
import br.tec.suportes.ragdbamv.service.RelacionamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/relacionamentos")
@RequiredArgsConstructor
public class RelacionamentoController {

    private final RelacionamentoService relacionamentoService;

    @PostMapping
    public ResponseEntity<Relacionamento> salvar(@Valid @RequestBody RelacionamentoRequest req) {
        return ResponseEntity.ok(relacionamentoService.salvar(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        relacionamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
