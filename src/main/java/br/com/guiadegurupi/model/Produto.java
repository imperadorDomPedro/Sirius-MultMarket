package br.com.guiadegurupi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "preco_original", precision = 10, scale = 2)
    private BigDecimal precoOriginal; // para exibir desconto

    // URLs das imagens no MinIO — separadas por vírgula
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "url_imagem")
    @Builder.Default
    private List<String> imagens = new ArrayList<>();

    // Tags para busca: "buriti", "cerrado", "doce artesanal"
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "produto_tags", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "tag")
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0.0;

    @Column(name = "total_avaliacoes", nullable = false)
    @Builder.Default
    private Integer totalAvaliacoes = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer estoque = 0;

    // true = serviço (ex: "Troca de óleo"), false = produto físico
    @Column(name = "e_servico", nullable = false)
    @Builder.Default
    private Boolean eServico = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean destaque = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    // Sazonalidade — chave do Guia de Gurupi!
    // ex: "Abril a Junho" para frutos do cerrado
    @Column(name = "epoca_disponivel", length = 100)
    private String epocaDisponivel;

    @Column(name = "tempo_entrega", length = 100)
    private String tempoEntrega;

    // Specs extras: {"Peso": "500g", "Origem": "Cerrado nativo"}
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "produto_specs", joinColumns = @JoinColumn(name = "produto_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    @Builder.Default
    private java.util.Map<String, String> specs = new java.util.HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @CreationTimestamp
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;
}
