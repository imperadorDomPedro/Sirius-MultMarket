package br.com.guiadegurupi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos que o React/Bolt espera — mantemos os mesmos nomes de contrato
    @Column(nullable = false, unique = true, length = 100)
    private String slug; // ex: "distribuidora-martins"

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 200)
    private String tagline; // slogan curto exibido no card

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "url_logo")
    private String urlLogo; // MinIO

    @Column(name = "url_banner")
    private String urlBanner; // MinIO

    @Column(name = "nome_responsavel", length = 150)
    private String nomeResponsavel;

    // Whatsapp para redirect direto — diferencial do Guia de Gurupi
    @Column(name = "whatsapp", length = 20)
    private String whatsapp;

    @Column(length = 200)
    private String localizacao; // ex: "Av. Pará, 1234 - Gurupi, TO"

    @Column(name = "tempo_resposta", length = 50)
    private String tempoResposta; // ex: "< 1 hora"

    @Column(name = "politica_entrega", length = 300)
    private String politicaEntrega;

    @Column(name = "politica_devolucao", length = 300)
    private String politicaDevolucao;

    // Avaliação calculada dinamicamente — desnormalizado para performance
    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0.0;

    @Column(name = "total_avaliacoes", nullable = false)
    @Builder.Default
    private Integer totalAvaliacoes = 0;

    @Column(name = "total_seguidores", nullable = false)
    @Builder.Default
    private Integer totalSeguidores = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean verificado = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean destaque = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    // Relacionamentos
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id")
    private Plano plano;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "empresa_categorias",
        joinColumns = @JoinColumn(name = "empresa_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @Builder.Default
    private List<Categoria> categorias = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @UpdateTimestamp
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;
}
