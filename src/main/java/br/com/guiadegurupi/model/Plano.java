package br.com.guiadegurupi.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "planos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nome; // VITRINE, DESTAQUE, PREMIUM

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMensal;

    @Column(name = "max_produtos", nullable = false)
    private Integer maxProdutos; // -1 = ilimitado

    @Column(name = "aparece_em_destaque", nullable = false)
    @Builder.Default
    private Boolean apareceEmDestaque = false;

    @Column(name = "banner_na_home", nullable = false)
    @Builder.Default
    private Boolean bannerNaHome = false;

    @Column(name = "relatorio_visualizacoes", nullable = false)
    @Builder.Default
    private Boolean relatorioVisualizacoes = false;

    @Column(length = 500)
    private String descricao;
}
