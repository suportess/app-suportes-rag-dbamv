package br.tec.suportes.ragdbamv.repository;

import br.tec.suportes.ragdbamv.model.Relacionamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RelacionamentoRepository extends JpaRepository<Relacionamento, UUID> {
    List<Relacionamento> findByTabelaOrigemId(UUID tabelaOrigemId);
    List<Relacionamento> findByTabelaDestinoId(UUID tabelaDestinoId);
}
