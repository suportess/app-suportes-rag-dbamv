package br.tec.suportes.ragdbamv.service;

import br.tec.suportes.ragdbamv.dto.ExemploRequest;
import br.tec.suportes.ragdbamv.model.Exemplo;
import br.tec.suportes.ragdbamv.repository.ExemploRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExemploService {

    private final ExemploRepository exemploRepository;

    @Transactional
    public Exemplo salvar(ExemploRequest req) {
        Exemplo e = new Exemplo();
        e.setPergunta(req.getPergunta());
        e.setSqlGerado(req.getSqlGerado());
        e.setDescricao(req.getDescricao());
        return exemploRepository.save(e);
    }

    @Transactional(readOnly = true)
    public List<Exemplo> listar() {
        return exemploRepository.findAll();
    }

    @Transactional
    public void deletar(UUID id) {
        exemploRepository.deleteById(id);
    }
}
