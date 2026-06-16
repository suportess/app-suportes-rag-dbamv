package br.tec.suportes.ragdbamv.service;

import br.tec.suportes.ragdbamv.dto.PagedResponse;
import br.tec.suportes.ragdbamv.dto.TabelaDescricaoRequest;
import br.tec.suportes.ragdbamv.dto.TabelaRequest;
import br.tec.suportes.ragdbamv.dto.TabelaResponse;
import br.tec.suportes.ragdbamv.model.Coluna;
import br.tec.suportes.ragdbamv.model.Tabela;
import br.tec.suportes.ragdbamv.repository.TabelaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TabelaService {

    private final TabelaRepository tabelaRepository;

    @Transactional
    public TabelaResponse salvar(TabelaRequest req) {
        Tabela tabela = tabelaRepository
                .findByNomeAndSchemaOra(req.getNome().toUpperCase(), req.getSchemaOra().toUpperCase())
                .orElse(new Tabela());

        tabela.setNome(req.getNome().toUpperCase());
        tabela.setSchemaOra(req.getSchemaOra().toUpperCase());
        tabela.setDescricao(req.getDescricao());
        tabela.setIndexadoEm(null); // força re-indexação

        tabela.getColunas().clear();
        req.getColunas().forEach(cr -> {
            Coluna c = new Coluna();
            c.setNome(cr.getNome().toUpperCase());
            c.setTipoDado(cr.getTipoDado());
            c.setDescricao(cr.getDescricao());
            c.setNullable(cr.getNullable());
            c.setChavePrimaria(cr.getChavePrimaria());
            c.setTabela(tabela);
            tabela.getColunas().add(c);
        });

        return TabelaResponse.from(tabelaRepository.save(tabela));
    }

    @Transactional(readOnly = true)
    public PagedResponse<TabelaResponse> listar(int pagina, int tamanhoPagina) {
        var pageable = PageRequest.of(pagina, tamanhoPagina, Sort.by("nome").ascending());
        var page = tabelaRepository.findAll(pageable);
        var dados = page.getContent().stream().map(TabelaResponse::from).toList();
        return new PagedResponse<>(dados, pagina, tamanhoPagina, page.getTotalElements());
    }

    @Transactional(readOnly = true)
    public TabelaResponse buscar(UUID id) {
        return tabelaRepository.findById(id)
                .map(TabelaResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Tabela não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public TabelaResponse buscarPorNome(String nome) {
        return tabelaRepository.findByNomeIgnoreCase(nome)
                .map(TabelaResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Tabela não encontrada: " + nome));
    }

    @Transactional
    public TabelaResponse atualizarDescricao(String nome, TabelaDescricaoRequest req) {
        Tabela tabela = tabelaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new IllegalArgumentException("Tabela não encontrada: " + nome));
        tabela.setDescricao(req.getDescricao());
        tabela.setIndexadoEm(null);
        return TabelaResponse.from(tabelaRepository.save(tabela));
    }

    @Transactional
    public void deletar(UUID id) {
        tabelaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Tabela buscarEntidade(UUID id) {
        return tabelaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tabela não encontrada: " + id));
    }
}
