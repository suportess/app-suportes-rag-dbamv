package br.tec.suportes.ragdbamv.controller;

import br.tec.suportes.ragdbamv.service.IndexacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/indexacao")
@RequiredArgsConstructor
public class IndexacaoController {

    private final IndexacaoService indexacaoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> indexar() {
        int total = indexacaoService.indexarTudo();
        return ResponseEntity.ok(Map.of("documentos_indexados", total));
    }

    @PostMapping("/{nome}")
    public ResponseEntity<Map<String, Object>> indexarTabela(@PathVariable String nome) {
        indexacaoService.indexarTabela(nome);
        return ResponseEntity.ok(Map.of("indexado", nome));
    }
}
