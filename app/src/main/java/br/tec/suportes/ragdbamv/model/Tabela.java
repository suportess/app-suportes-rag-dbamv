package br.tec.suportes.ragdbamv.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tabela")
@Getter @Setter @NoArgsConstructor
public class Tabela {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 128)
    private String nome;

    @Column(name = "schema_ora", nullable = false, length = 64)
    private String schemaOra = "DBAMV";

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "indexado_em")
    private LocalDateTime indexadoEm;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @OneToMany(mappedBy = "tabela", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Coluna> colunas = new ArrayList<>();

    @OneToMany(mappedBy = "tabelaOrigem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Relacionamento> relacionamentos = new ArrayList<>();
}
