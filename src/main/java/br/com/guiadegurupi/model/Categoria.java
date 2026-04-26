package br.com.guiadegurupi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // slug único usado pelo React: ex. "produtos-cerrado"
    @Column(nullable = false, unique = true, length = 80)
    private String slug;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 300)
    private String descricao;

    // Nome do ícone Lucide usado no frontend: ex. "Leaf", "Wrench", "Car"
    @Column(length = 50)
    private String icone;

    // Classes Tailwind vindas do Bolt: ex. "text-green-600"
    @Column(length = 60)
    private String cor;

    @Column(name = "bg_cor", length = 60)
    private String bgCor;

    @Column(name = "url_imagem")
    private String urlImagem;

    @Column(nullable = false)
    @Builder.Default
    // Atenção: @Builder.Default também aplica o valor no NoArgsConstructor gerado pelo Lombok
    private Boolean ativo = true;
}
