package br.tec.suportes.ragdbamv.controller;

import br.tec.suportes.ragdbamv.dto.ExemploRequest;
import br.tec.suportes.ragdbamv.model.Exemplo;
import br.tec.suportes.ragdbamv.service.ExemploService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exemplos")
@RequiredArgsConstructor
public class ExemploController {

    private final ExemploService exemploService;

    @PostMapping
    public ResponseEntity<Exemplo> salvar(@Valid @RequestBody ExemploRequest req) {
        return ResponseEntity.ok(exemploService.salvar(req));
    }

    @GetMapping
    public ResponseEntity<List<Exemplo>> listar() {
        return ResponseEntity.ok(exemploService.listar());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        exemploService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
