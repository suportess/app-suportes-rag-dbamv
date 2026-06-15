package br.tec.suportes.ragdbamv.service;

import br.tec.suportes.ragdbamv.dto.RelacionamentoRequest;
import br.tec.suportes.ragdbamv.model.Relacionamento;
import br.tec.suportes.ragdbamv.repository.RelacionamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RelacionamentoService {

    private final RelacionamentoRepository relacionamentoRepository;
    private final TabelaService tabelaService;

    @Transactional
    public Relacionamento salvar(RelacionamentoRequest req) {
        Relacionamento r = new Relacionamento();
        r.setTabelaOrigem(tabelaService.buscarEntidade(req.getTabelaOrigemId()));
        r.setColunaOrigem(req.getColunaOrigem().toUpperCase());
        r.setTabelaDestino(tabelaService.buscarEntidade(req.getTabelaDestinoId()));
        r.setColunaDestino(req.getColunaDestino().toUpperCase());
        r.setDescricao(req.getDescricao());
        return relacionamentoRepository.save(r);
    }

    @Transactional
    public void deletar(UUID id) {
        relacionamentoRepository.deleteById(id);
    }
}
