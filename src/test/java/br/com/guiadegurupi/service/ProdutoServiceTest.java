package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.ProdutoResponse;
import br.com.guiadegurupi.model.Categoria;
import br.com.guiadegurupi.model.Empresa;
import br.com.guiadegurupi.model.Produto;
import br.com.guiadegurupi.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProdutoService")
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    // ── Fixtures ──────────────────────────────────────────────────────────

    private Empresa distribuidoraMartins;
    private Empresa lubrificantesBomPreco;
    private Categoria categoriaAlimentacao;
    private Categoria categoriaAutomotivo;
    private Categoria categoriaCerrado;

    private Produto biscoito;
    private Produto agua;
    private Produto oleoMobil;
    private Produto trocaDeOleo;
    private Produto pequiCongelado;

    @BeforeEach
    void setUp() {
        distribuidoraMartins = Empresa.builder()
                .id(1L)
                .slug("distribuidora-martins")
                .nome("Distribuidora Martins")
                .build();

        lubrificantesBomPreco = Empresa.builder()
                .id(3L)
                .slug("lubrificantes-bom-preco")
                .nome("Lubrificantes Bom Preço")
                .build();

        categoriaAlimentacao = Categoria.builder()
                .id(3L)
                .slug("alimentacao")
                .nome("Alimentação")
                .build();

        categoriaAutomotivo = Categoria.builder()
                .id(2L)
                .slug("servicos-automotivos")
                .nome("Serviços Automotivos")
                .build();

        categoriaCerrado = Categoria.builder()
                .id(1L)
                .slug("produtos-cerrado")
                .nome("Produtos do Cerrado")
                .build();

        biscoito = Produto.builder()
                .id(1L)
                .nome("Caixa de Biscoito Cream Cracker 20un")
                .descricao("Caixa com 20 pacotes de biscoito cream cracker.")
                .preco(new BigDecimal("45.90"))
                .precoOriginal(new BigDecimal("52.00"))
                .estoque(200)
                .rating(0.0)
                .totalAvaliacoes(0)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .tempoEntrega("1-2 dias úteis")
                .empresa(distribuidoraMartins)
                .categoria(categoriaAlimentacao)
                .build();

        agua = Produto.builder()
                .id(2L)
                .nome("Fardo Água Mineral 500ml 12un")
                .descricao("Fardo com 12 garrafas de água mineral 500ml.")
                .preco(new BigDecimal("18.50"))
                .estoque(500)
                .rating(0.0)
                .totalAvaliacoes(0)
                .eServico(false)
                .destaque(false)
                .ativo(true)
                .tempoEntrega("1 dia útil")
                .empresa(distribuidoraMartins)
                .categoria(categoriaAlimentacao)
                .build();

        oleoMobil = Produto.builder()
                .id(3L)
                .nome("Óleo Mobil Super 5W-30 1L")
                .descricao("Óleo lubrificante sintético para motores a gasolina e flex.")
                .preco(new BigDecimal("42.90"))
                .precoOriginal(new BigDecimal("49.90"))
                .estoque(80)
                .rating(4.5)
                .totalAvaliacoes(8)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .tempoEntrega("Retirada imediata")
                .empresa(lubrificantesBomPreco)
                .categoria(categoriaAutomotivo)
                .build();

        trocaDeOleo = Produto.builder()
                .id(4L)
                .nome("Troca de Óleo Completa")
                .descricao("Serviço de troca de óleo com dreno e filtro.")
                .preco(new BigDecimal("89.90"))
                .estoque(1)
                .rating(5.0)
                .totalAvaliacoes(3)
                .eServico(true)
                .destaque(true)
                .ativo(true)
                .tempoEntrega("Agendamento")
                .empresa(lubrificantesBomPreco)
                .categoria(categoriaAutomotivo)
                .build();

        pequiCongelado = Produto.builder()
                .id(5L)
                .nome("Pequi Congelado 500g")
                .descricao("Pequi nativo do cerrado tocantinense, congelado na hora da colheita.")
                .preco(new BigDecimal("22.00"))
                .estoque(50)
                .rating(4.8)
                .totalAvaliacoes(15)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .tempoEntrega("1-2 dias úteis")
                .epocaDisponivel("outubro a dezembro")
                .tags(List.of("cerrado", "nativo", "sazonal", "pequi"))
                .specs(Map.of("Peso", "500g", "Origem", "Cerrado tocantinense"))
                .empresa(distribuidoraMartins)
                .categoria(categoriaCerrado)
                .build();
    }

    // =========================================================================
    // listarDestaques()
    // =========================================================================

    @Nested
    @DisplayName("listarDestaques()")
    class ListarDestaques {

        @Test
        @DisplayName("deve mapear campos nulos quando categoria for null")
        void deveMapearCamposNulosQuandoCategoriaForNull() {
        // Arrange
                Produto semCategoria = Produto.builder()
                .id(10L)
                .nome("Produto sem categoria")
                .preco(null)
                .precoOriginal(null)      // sem desconto
                .rating(0.0)
                .totalAvaliacoes(0)
                .estoque(1)
                .eServico(false)
                .destaque(false)
                .ativo(true)
                .categoria(null)          // null!
                .empresa(distribuidoraMartins)
                .build();

                when(produtoRepository.findById(10L))
                .thenReturn(Optional.of(semCategoria));

                // Act
                ProdutoResponse response = produtoService.buscarPorId(10L);

                // Assert
                assertThat(response.getCategoriaId()).isNull();
                assertThat(response.getCategoriaSlug()).isNull();
                assertThat(response.getPrecoOriginal()).isNull();
        }

        @Test
        @DisplayName("deve mapear campos nulos quando empresa for null")
        void deveMapearCamposNulosQuandoEmpresaForNull() {
                // Arrange
                Produto semEmpresa = Produto.builder()
                        .id(11L)
                        .nome("Produto sem empresa")
                        .preco(new BigDecimal("15.00"))
                        .rating(0.0)
                        .totalAvaliacoes(0)
                        .estoque(1)
                        .eServico(false)
                        .destaque(false)
                        .ativo(true)
                        .empresa(null)            // null!
                        .categoria(categoriaAlimentacao)
                        .build();

                when(produtoRepository.findById(11L))
                        .thenReturn(Optional.of(semEmpresa));

                // Act
                ProdutoResponse response = produtoService.buscarPorId(11L);

                // Assert
                assertThat(response.getEmpresaId()).isNull();
                assertThat(response.getEmpresaSlug()).isNull();
                assertThat(response.getEmpresaNome()).isNull();
        }

        @Test
        @DisplayName("deve retornar lista com produtos em destaque")
        void deveRetornarListaComProdutosEmDestaque() {
            // Arrange
            when(produtoRepository.findAllByDestaqueAndAtivoTrue(true))
                    .thenReturn(List.of(biscoito, oleoMobil, trocaDeOleo, pequiCongelado));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarDestaques();

            // Assert
            assertThat(resultado).hasSize(4);
            assertThat(resultado)
                    .extracting(ProdutoResponse::getDestaque)
                    .containsOnly(true);
            verify(produtoRepository, times(1)).findAllByDestaqueAndAtivoTrue(true);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver destaques")
        void deveRetornarListaVaziaQuandoNaoHouverDestaques() {
            // Arrange
            when(produtoRepository.findAllByDestaqueAndAtivoTrue(true))
                    .thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponse> resultado = produtoService.listarDestaques();

            // Assert
            assertThat(resultado).isEmpty();
            verify(produtoRepository, times(1)).findAllByDestaqueAndAtivoTrue(true);
        }

        @Test
        @DisplayName("deve chamar findAllByDestaqueAndAtivoTrue e nunca findAll")
        void deveChamarMetodoCorreto() {
            // Arrange
            when(produtoRepository.findAllByDestaqueAndAtivoTrue(true))
                    .thenReturn(List.of());

            // Act
            produtoService.listarDestaques();

            // Assert
            verify(produtoRepository, times(1)).findAllByDestaqueAndAtivoTrue(true);
            verify(produtoRepository, never()).findAll();
        }
    }

    // =========================================================================
    // buscarPorId()
    // =========================================================================

    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar ProdutoResponse quando id existir")
        void deveRetornarResponseQuandoIdExistir() {
            // Arrange
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(biscoito));

            // Act
            ProdutoResponse resultado = produtoService.buscarPorId(1L);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("Caixa de Biscoito Cream Cracker 20un");
            verify(produtoRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando id não existir")
        void deveLancarRuntimeExceptionQuandoIdNaoExistir() {
            // Arrange
            when(produtoRepository.findById(999L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> produtoService.buscarPorId(999L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Produto não encontrado: 999");
        }

        @Test
        @DisplayName("deve incluir o id na mensagem de erro")
        void deveIncluirIdNaMensagemDeErro() {
            // Arrange
            when(produtoRepository.findById(42L)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> produtoService.buscarPorId(42L))
                    .hasMessageContaining("42");
        }

        @Test
        @DisplayName("deve chamar findById exatamente uma vez")
        void deveChamarFindByIdUmaVez() {
            // Arrange
            when(produtoRepository.findById(3L)).thenReturn(Optional.of(oleoMobil));

            // Act
            produtoService.buscarPorId(3L);

            // Assert
            verify(produtoRepository, times(1)).findById(3L);
            verifyNoMoreInteractions(produtoRepository);
        }
    }

    // =========================================================================
    // listarPorEmpresa()
    // =========================================================================

    @Nested
    @DisplayName("listarPorEmpresa()")
    class ListarPorEmpresa {

        @Test
        @DisplayName("deve retornar produtos da empresa informada")
        void deveRetornarProdutosDaEmpresa() {
            // Arrange
            when(produtoRepository.findAllByEmpresaIdAndAtivoTrue(1L))
                    .thenReturn(List.of(biscoito, agua));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorEmpresa(1L);

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado)
                    .extracting(ProdutoResponse::getEmpresaId)
                    .containsOnly(1L);
            verify(produtoRepository, times(1)).findAllByEmpresaIdAndAtivoTrue(1L);
        }

        @Test
        @DisplayName("deve retornar lista vazia para empresa sem produtos")
        void deveRetornarListaVaziaParaEmpresaSemProdutos() {
            // Arrange
            when(produtoRepository.findAllByEmpresaIdAndAtivoTrue(2L))
                    .thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorEmpresa(2L);

            // Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("deve retornar produtos físicos e serviços misturados da mesma empresa")
        void deveRetornarProdutosFisicosEServicosDaMesmaEmpresa() {
            // Arrange
            when(produtoRepository.findAllByEmpresaIdAndAtivoTrue(3L))
                    .thenReturn(List.of(oleoMobil, trocaDeOleo));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorEmpresa(3L);

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getEServico()).isFalse();
            assertThat(resultado.get(1).getEServico()).isTrue();
        }
    }

    // =========================================================================
    // listarPorCategoria()
    // =========================================================================

    @Nested
    @DisplayName("listarPorCategoria()")
    class ListarPorCategoria {

        @Test
        @DisplayName("deve retornar produtos da categoria informada")
        void deveRetornarProdutosDaCategoria() {
            // Arrange
            when(produtoRepository.findAllByCategoriaSlugAndAtivoTrue("alimentacao"))
                    .thenReturn(List.of(biscoito, agua));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorCategoria("alimentacao");

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado)
                    .extracting(ProdutoResponse::getCategoriaSlug)
                    .containsOnly("alimentacao");
            verify(produtoRepository, times(1))
                    .findAllByCategoriaSlugAndAtivoTrue("alimentacao");
        }

        @Test
        @DisplayName("deve retornar lista vazia para categoria sem produtos")
        void deveRetornarListaVaziaParaCategoriaSemProdutos() {
            // Arrange
            when(produtoRepository.findAllByCategoriaSlugAndAtivoTrue("educacao-cursos"))
                    .thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorCategoria("educacao-cursos");

            // Assert
            assertThat(resultado).isEmpty();
        }
    }

    // =========================================================================
    // pesquisar()
    // =========================================================================

    @Nested
    @DisplayName("pesquisar()")
    class Pesquisar {

        @Test
        @DisplayName("deve retornar produtos que correspondem ao termo")
        void deveRetornarProdutosQueCorrespondemAoTermo() {
            // Arrange
            when(produtoRepository.search("óleo"))
                    .thenReturn(List.of(oleoMobil, trocaDeOleo));

            // Act
            List<ProdutoResponse> resultado = produtoService.pesquisar("óleo");

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado)
                    .extracting(ProdutoResponse::getEmpresaSlug)
                    .containsOnly("lubrificantes-bom-preco");
            verify(produtoRepository, times(1)).search("óleo");
        }

        @Test
        @DisplayName("deve retornar lista vazia para termo não encontrado")
        void deveRetornarListaVaziaParaTermoNaoEncontrado() {
            // Arrange
            when(produtoRepository.search("smartphone"))
                    .thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponse> resultado = produtoService.pesquisar("smartphone");

            // Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("deve retornar múltiplos resultados para termo genérico")
        void deveRetornarMultiplosResultadosParaTermoGenerico() {
            // Arrange
            when(produtoRepository.search("500"))
                    .thenReturn(List.of(agua, pequiCongelado));

            // Act
            List<ProdutoResponse> resultado = produtoService.pesquisar("500");

            // Assert
            assertThat(resultado).hasSize(2);
        }
    }

    // =========================================================================
    // listarSazonais()
    // =========================================================================

    @Nested
    @DisplayName("listarSazonais()")
    class ListarSazonais {

        @Test
        @DisplayName("deve retornar produtos sazonais do cerrado")
        void deveRetornarProdutosSazonaisDoCerrado() {
            // Arrange
            when(produtoRepository.findProdutosSazonais())
                    .thenReturn(List.of(pequiCongelado));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarSazonais();

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getEpocaDisponivel())
                    .isEqualTo("outubro a dezembro");
            assertThat(resultado.get(0).getCategoriaSlug())
                    .isEqualTo("produtos-cerrado");
            verify(produtoRepository, times(1)).findProdutosSazonais();
        }

        @Test
        @DisplayName("deve retornar lista vazia fora de época")
        void deveRetornarListaVaziaForaDeEpoca() {
            // Arrange
            when(produtoRepository.findProdutosSazonais())
                    .thenReturn(Collections.emptyList());

            // Act
            List<ProdutoResponse> resultado = produtoService.listarSazonais();

            // Assert
            assertThat(resultado).isEmpty();
        }
    }

    // =========================================================================
    // toResponse() — mapeamento da entidade para DTO
    // =========================================================================

    @Nested
    @DisplayName("toResponse() — mapeamento Produto → ProdutoResponse")
    class ToResponse {

        @Test
        @DisplayName("deve mapear todos os campos corretamente")
        void deveMapearTodosCamposCorretamente() {
            // Arrange
            when(produtoRepository.findById(1L)).thenReturn(Optional.of(biscoito));

            // Act
            ProdutoResponse response = produtoService.buscarPorId(1L);

            // Assert
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getNome()).isEqualTo("Caixa de Biscoito Cream Cracker 20un");
            assertThat(response.getDescricao()).isEqualTo("Caixa com 20 pacotes de biscoito cream cracker.");
            assertThat(response.getPreco()).isEqualTo(45.90);
            assertThat(response.getPrecoOriginal()).isEqualTo(52.00);
            assertThat(response.getEstoque()).isEqualTo(200);
            assertThat(response.getRating()).isEqualTo(0.0);
            assertThat(response.getTotalAvaliacoes()).isEqualTo(0);
            assertThat(response.getEServico()).isFalse();
            assertThat(response.getDestaque()).isTrue();
            assertThat(response.getAtivo()).isTrue();
            assertThat(response.getTempoEntrega()).isEqualTo("1-2 dias úteis");
            assertThat(response.getEmpresaId()).isEqualTo(1L);
            assertThat(response.getEmpresaSlug()).isEqualTo("distribuidora-martins");
            assertThat(response.getEmpresaNome()).isEqualTo("Distribuidora Martins");
            assertThat(response.getCategoriaId()).isEqualTo(3L);
            assertThat(response.getCategoriaSlug()).isEqualTo("alimentacao");
        }

        @Test
        @DisplayName("deve converter BigDecimal para Double corretamente")
        void deveConverterBigDecimalParaDouble() {
            // Arrange
            when(produtoRepository.findById(3L)).thenReturn(Optional.of(oleoMobil));

            // Act
            ProdutoResponse response = produtoService.buscarPorId(3L);

            // Assert
            assertThat(response.getPreco()).isEqualTo(42.90);
            assertThat(response.getPrecoOriginal()).isEqualTo(49.90);
        }

        @Test
        @DisplayName("deve mapear precoOriginal como null quando não houver desconto")
        void deveMapearPrecoOriginalNullQuandoNaoHouverDesconto() {
            // Arrange
            when(produtoRepository.findById(2L)).thenReturn(Optional.of(agua));

            // Act
            ProdutoResponse response = produtoService.buscarPorId(2L);

            // Assert
            assertThat(response.getPrecoOriginal()).isNull();
        }

        @Test
        @DisplayName("deve mapear epocaDisponivel corretamente para produto sazonal")
        void deveMapearEpocaDisponivelParaProdutoSazonal() {
            // Arrange
            when(produtoRepository.findProdutosSazonais())
                    .thenReturn(List.of(pequiCongelado));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarSazonais();

            // Assert
            assertThat(resultado.get(0).getEpocaDisponivel())
                    .isEqualTo("outubro a dezembro");
            assertThat(resultado.get(0).getTags())
                    .containsExactly("cerrado", "nativo", "sazonal", "pequi");
            assertThat(resultado.get(0).getEspecificacoes())
                    .containsEntry("Peso", "500g")
                    .containsEntry("Origem", "Cerrado tocantinense");
        }

        @Test
        @DisplayName("deve mapear eServico true para produto que é serviço")
        void deveMapearEServicoProdutoQueEServico() {
            // Arrange
            when(produtoRepository.findById(4L)).thenReturn(Optional.of(trocaDeOleo));

            // Act
            ProdutoResponse response = produtoService.buscarPorId(4L);

            // Assert
            assertThat(response.getEServico()).isTrue();
            assertThat(response.getTempoEntrega()).isEqualTo("Agendamento");
        }

        @Test
        @DisplayName("deve preservar a ordem dos itens na lista")
        void devePreservarOrdemDosItensNaLista() {
            // Arrange
            when(produtoRepository.findAllByEmpresaIdAndAtivoTrue(1L))
                    .thenReturn(List.of(biscoito, agua));

            // Act
            List<ProdutoResponse> resultado = produtoService.listarPorEmpresa(1L);

            // Assert
            assertThat(resultado)
                    .extracting(ProdutoResponse::getId)
                    .containsExactly(1L, 2L);
        }
    }
}
