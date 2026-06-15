package br.tec.suportes.ragdbamv.repository;

import br.tec.suportes.ragdbamv.model.Exemplo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExemploRepository extends JpaRepository<Exemplo, UUID> {
}
