package br.com.guiadegurupi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de resposta para Produto.
 *
 * Convertido de record para class para suportar @Builder
 * (padrão clean code do projeto — igual a CategoriaResponse e EmpresaResponse).
 *
 * Campos renomeados para português alinhando com o domínio da aplicação.
 * O mapeamento para o contrato React/Bolt é feito no mapper ou service.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {

    private Long   id;
    private String nome;
    private String descricao;
    private Double preco;
    private Double precoOriginal;   // null = sem desconto
    private List<String> imagens;

    // Relacionamentos resumidos
    private Long   categoriaId;
    private String categoriaSlug;
    private Long   empresaId;
    private String empresaSlug;
    private String empresaNome;

    // Métricas
    private Double  rating;
    private Integer totalAvaliacoes;
    private Integer estoque;

    // Tags e especificações
    private List<String>        tags;
    private Map<String, String> especificacoes;

    // Flags
    @JsonProperty("eServico")
    private Boolean eServico;

    @JsonProperty("destaque")
    private Boolean destaque;

    @JsonProperty("ativo")
    private Boolean ativo;

    // Logística
    private String tempoEntrega;

    // Campo especial — sazonalidade do cerrado (ex: "maio a agosto")
    private String epocaDisponivel;
}
