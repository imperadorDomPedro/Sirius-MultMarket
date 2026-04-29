package br.com.guiadegurupi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para Categoria.
 *
 * Contrato alinhado com a interface Category do frontend (Bolt/React).
 * Campos em português seguindo o padrão do domínio da aplicação.
 *
 * Padrão: @Builder para construção fluente (clean code + testabilidade)
 *         @NoArgsConstructor para deserialização Jackson
 *         @AllArgsConstructor exigido internamente pelo @Builder
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponse {

    private Long   id;
    private String nome;
    private String slug;
    private String icone;
    private String cor;
    private String corFundo;
    private int    totalProdutos;
    private String imagem;
    private String descricao;
}