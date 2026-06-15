package br.tec.suportes.ragdbamv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "exemplo")
@Getter @Setter @NoArgsConstructor
public class Exemplo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pergunta;

    @Column(name = "sql_gerado", nullable = false, columnDefinition = "TEXT")
    private String sqlGerado;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}
