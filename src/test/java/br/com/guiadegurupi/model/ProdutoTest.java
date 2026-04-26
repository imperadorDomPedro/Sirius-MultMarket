package br.com.guiadegurupi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Produto — testes unitários da entidade")
class ProdutoTest {

    // ── helpers ──────────────────────────────────────────────────────────────

    private Empresa empresaFake() {
        return Empresa.builder().slug("loja-cerrado").nome("Loja Cerrado").build();
    }

    private Categoria categoriaFake() {
        return Categoria.builder().slug("alimentos").nome("Alimentos").build();
    }

    // ── builder completo ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Builder deve criar entidade com todos os campos preenchidos")
    void deveCriarProdutoComBuilder() {
        Produto produto = Produto.builder()
                .id(1L)
                .nome("Buriti do Cerrado")
                .descricao("Fruto nativo coletado em abril")
                .preco(new BigDecimal("19.90"))
                .precoOriginal(new BigDecimal("24.90"))
                .imagens(List.of("https://cdn.gurupi.com/buriti1.jpg"))
                .tags(List.of("cerrado", "buriti", "fruto nativo"))
                .rating(4.9)
                .totalAvaliacoes(87)
                .estoque(50)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .epocaDisponivel("Abril a Junho")
                .tempoEntrega("2 dias úteis")
                .specs(Map.of("Peso", "500g", "Origem", "Cerrado nativo"))
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(produto.getId()).isEqualTo(1L);
        assertThat(produto.getNome()).isEqualTo("Buriti do Cerrado");
        assertThat(produto.getDescricao()).isEqualTo("Fruto nativo coletado em abril");
        assertThat(produto.getPreco()).isEqualByComparingTo("19.90");
        assertThat(produto.getPrecoOriginal()).isEqualByComparingTo("24.90");
        assertThat(produto.getImagens()).containsExactly("https://cdn.gurupi.com/buriti1.jpg");
        assertThat(produto.getTags()).containsExactlyInAnyOrder("cerrado", "buriti", "fruto nativo");
        assertThat(produto.getRating()).isEqualTo(4.9);
        assertThat(produto.getTotalAvaliacoes()).isEqualTo(87);
        assertThat(produto.getEstoque()).isEqualTo(50);
        assertThat(produto.getEServico()).isFalse();
        assertThat(produto.getDestaque()).isTrue();
        assertThat(produto.getAtivo()).isTrue();
        assertThat(produto.getEpocaDisponivel()).isEqualTo("Abril a Junho");
        assertThat(produto.getTempoEntrega()).isEqualTo("2 dias úteis");
        assertThat(produto.getSpecs()).containsEntry("Peso", "500g").containsEntry("Origem", "Cerrado nativo");
        assertThat(produto.getEmpresa().getSlug()).isEqualTo("loja-cerrado");
        assertThat(produto.getCategoria().getSlug()).isEqualTo("alimentos");
    }

    // ── @Builder.Default — listas e map ──────────────────────────────────────

    @Test
    @DisplayName("@Builder.Default — imagens deve ser lista vazia quando não informado")
    void deveInicializarImagensComoListaVazia() {
        Produto produto = Produto.builder().nome("Teste").preco(BigDecimal.ONE)
                .empresa(empresaFake()).categoria(categoriaFake()).build();

        assertThat(produto.getImagens()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("@Builder.Default — tags deve ser lista vazia quando não informado")
    void deveInicializarTagsComoListaVazia() {
        Produto produto = Produto.builder().nome("Teste").preco(BigDecimal.ONE)
                .empresa(empresaFake()).categoria(categoriaFake()).build();

        assertThat(produto.getTags()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("@Builder.Default — specs deve ser mapa vazio quando não informado")
    void deveInicializarSpecsComoMapaVazio() {
        Produto produto = Produto.builder().nome("Teste").preco(BigDecimal.ONE)
                .empresa(empresaFake()).categoria(categoriaFake()).build();

        assertThat(produto.getSpecs()).isNotNull().isEmpty();
    }

    // ── @Builder.Default — numéricos e booleanos ─────────────────────────────

    @Test
    @DisplayName("@Builder.Default — rating deve ser 0.0, estoque 0, totalAvaliacoes 0 por padrão")
    void deveInicializarNumerosZeroPorPadrao() {
        Produto produto = Produto.builder().nome("Teste").preco(BigDecimal.ONE)
                .empresa(empresaFake()).categoria(categoriaFake()).build();

        assertThat(produto.getRating()).isEqualTo(0.0);
        assertThat(produto.getEstoque()).isEqualTo(0);
        assertThat(produto.getTotalAvaliacoes()).isEqualTo(0);
    }

    @Test
    @DisplayName("@Builder.Default — eServico e destaque false, ativo true por padrão")
    void deveInicializarBooleanosPorPadrao() {
        Produto produto = Produto.builder().nome("Teste").preco(BigDecimal.ONE)
                .empresa(empresaFake()).categoria(categoriaFake()).build();

        assertThat(produto.getEServico()).isFalse();
        assertThat(produto.getDestaque()).isFalse();
        assertThat(produto.getAtivo()).isTrue();
    }

    // ── NoArgsConstructor ────────────────────────────────────────────────────

    @Test
    @DisplayName("NoArgsConstructor — @Builder.Default se aplica ao construtor vazio")
    void deveAplicarDefaultsNoNoArgsConstructor() {
        Produto produto = new Produto();

        assertThat(produto.getRating()).isEqualTo(0.0);
        assertThat(produto.getEstoque()).isEqualTo(0);
        assertThat(produto.getTotalAvaliacoes()).isEqualTo(0);
        assertThat(produto.getEServico()).isFalse();
        assertThat(produto.getDestaque()).isFalse();
        assertThat(produto.getAtivo()).isTrue();
        assertThat(produto.getImagens()).isNotNull().isEmpty();
        assertThat(produto.getTags()).isNotNull().isEmpty();
        assertThat(produto.getSpecs()).isNotNull().isEmpty();
    }

    // ── regras de negócio específicas ────────────────────────────────────────

    @Test
    @DisplayName("Produto tipo serviço não deve ter estoque relevante")
    void deveRepresentarProdutoTipoServico() {
        Produto servico = Produto.builder()
                .nome("Troca de óleo")
                .preco(new BigDecimal("89.90"))
                .eServico(true)
                .estoque(-1) // serviços ignoram estoque
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(servico.getEServico()).isTrue();
        assertThat(servico.getEstoque()).isEqualTo(-1);
    }

    @Test
    @DisplayName("Desconto deve ser calculável quando precoOriginal está preenchido")
    void devePermitirCalculoDeDesconto() {
        Produto produto = Produto.builder()
                .nome("Pequi em conserva")
                .preco(new BigDecimal("15.90"))
                .precoOriginal(new BigDecimal("22.90"))
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        BigDecimal desconto = produto.getPrecoOriginal().subtract(produto.getPreco());

        assertThat(produto.getPrecoOriginal()).isGreaterThan(produto.getPreco());
        assertThat(desconto).isEqualByComparingTo("7.00");
    }

    @Test
    @DisplayName("Produto sem precoOriginal não deve ter desconto exibível")
    void deveRetornarPrecoOriginalNuloQuandoSemDesconto() {
        Produto produto = Produto.builder()
                .nome("Mel do Cerrado")
                .preco(new BigDecimal("35.00"))
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(produto.getPrecoOriginal()).isNull();
    }

    @Test
    @DisplayName("epocaDisponivel deve registrar sazonalidade do produto do cerrado")
    void deveRegistrarEpocaDisponivel() {
        Produto produto = Produto.builder()
                .nome("Cajuzinho do Cerrado")
                .preco(new BigDecimal("12.00"))
                .epocaDisponivel("Setembro a Novembro")
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(produto.getEpocaDisponivel()).isEqualTo("Setembro a Novembro");
    }

    @Test
    @DisplayName("specs deve armazenar atributos técnicos do produto")
    void deveArmazenarSpecsTecnicas() {
        Produto produto = Produto.builder()
                .nome("Azeite de Pequi")
                .preco(new BigDecimal("42.00"))
                .specs(Map.of(
                        "Volume", "250ml",
                        "Origem", "Cerrado nativo",
                        "Extração", "Prensagem a frio"
                ))
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(produto.getSpecs())
                .hasSize(3)
                .containsEntry("Volume", "250ml")
                .containsEntry("Extração", "Prensagem a frio");
    }

    @Test
    @DisplayName("Setters devem alterar campos corretamente")
    void deveAlterarCamposViaSetters() {
        Produto produto = new Produto();

        produto.setNome("Baru torrado");
        produto.setPreco(new BigDecimal("28.50"));
        produto.setEstoque(100);
        produto.setDestaque(true);
        produto.setAtivo(false);

        assertThat(produto.getNome()).isEqualTo("Baru torrado");
        assertThat(produto.getPreco()).isEqualByComparingTo("28.50");
        assertThat(produto.getEstoque()).isEqualTo(100);
        assertThat(produto.getDestaque()).isTrue();
        assertThat(produto.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Campos opcionais podem ser nulos sem quebrar a entidade")
    void devePersistirCamposOpcionaisComoNulo() {
        Produto produto = Produto.builder()
                .nome("Produto Mínimo")
                .preco(new BigDecimal("10.00"))
                .empresa(empresaFake())
                .categoria(categoriaFake())
                .build();

        assertThat(produto.getDescricao()).isNull();
        assertThat(produto.getPrecoOriginal()).isNull();
        assertThat(produto.getEpocaDisponivel()).isNull();
        assertThat(produto.getTempoEntrega()).isNull();
        assertThat(produto.getCriadoEm()).isNull();
    }
}