package br.tec.suportes.ragdbamv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "coluna")
@Getter @Setter @NoArgsConstructor
public class Coluna {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_id", nullable = false)
    private Tabela tabela;

    @Column(nullable = false, length = 128)
    private String nome;

    @Column(name = "tipo_dado", length = 64)
    private String tipoDado;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private Boolean nullable = true;

    @Column(name = "chave_primaria", nullable = false)
    private Boolean chavePrimaria = false;
}
