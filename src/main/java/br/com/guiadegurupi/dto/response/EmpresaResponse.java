package br.com.guiadegurupi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de resposta para Empresa.
 *
 * Campos alinhados com a entidade Empresa e com o contrato
 * esperado pelo frontend React/Bolt.
 *
 * Padrão: @Builder para construção fluente (clean code + testabilidade)
 *         @NoArgsConstructor para deserialização Jackson
 *         @AllArgsConstructor exigido internamente pelo @Builder
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponse {

    private Long   id;
    private String slug;
    private String nome;
    private String tagline;
    private String descricao;
    private String urlLogo;
    private String urlBanner;
    private String nomeResponsavel;
    private String whatsapp;
    private String localizacao;
    private String tempoResposta;
    private String politicaEntrega;
    private String politicaDevolucao;

    // Métricas calculadas
    private Double  rating;
    private Integer totalAvaliacoes;
    private Integer totalSeguidores;

    // Flags
    private Boolean verificado;
    private Boolean destaque;
    private Boolean ativo;

    // Relacionamentos resumidos — evita N+1 e ciclo de serialização
    private String              planoNome;
    private List<String>        categoriasSlugs;

    // Auditoria
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;
}
