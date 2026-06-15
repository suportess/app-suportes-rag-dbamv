package br.tec.suportes.ragdbamv.controller;

import br.tec.suportes.ragdbamv.dto.ConsultaRequest;
import br.tec.suportes.ragdbamv.dto.ConsultaResponse;
import br.tec.suportes.ragdbamv.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consulta")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaResponse> consultar(@Valid @RequestBody ConsultaRequest req) {
        return ResponseEntity.ok(consultaService.consultar(req));
    }
}
