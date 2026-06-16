package br.tec.suportes.ragdbamv.repository;

import br.tec.suportes.ragdbamv.model.Tabela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TabelaRepository extends JpaRepository<Tabela, UUID> {
    Optional<Tabela> findByNomeAndSchemaOra(String nome, String schemaOra);
    Optional<Tabela> findByNomeIgnoreCase(String nome);
    List<Tabela> findAllByOrderByNomeAsc();
    org.springframework.data.domain.Page<Tabela> findByNomeContainingIgnoreCase(String nome, org.springframework.data.domain.Pageable pageable);
}
