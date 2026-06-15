package br.tec.suportes.ragdbamv.repository;

import br.tec.suportes.ragdbamv.model.Coluna;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ColunaRepository extends JpaRepository<Coluna, UUID> {
    List<Coluna> findByTabelaIdOrderByNomeAsc(UUID tabelaId);
}
