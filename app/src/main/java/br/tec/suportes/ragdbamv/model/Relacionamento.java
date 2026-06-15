package br.tec.suportes.ragdbamv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "relacionamento")
@Getter @Setter @NoArgsConstructor
public class Relacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_origem_id", nullable = false)
    private Tabela tabelaOrigem;

    @Column(name = "coluna_origem", nullable = false, length = 128)
    private String colunaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_destino_id", nullable = false)
    private Tabela tabelaDestino;

    @Column(name = "coluna_destino", nullable = false, length = 128)
    private String colunaDestino;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}
