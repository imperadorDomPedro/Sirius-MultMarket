package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.CategoriaResponse;
import br.com.guiadegurupi.model.Categoria;
import br.com.guiadegurupi.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários puros do CategoriaService.
 *
 * Estratégia: Mockito puro com @ExtendWith(MockitoExtension.class) —
 * sem Spring context, sem banco, sem MockMvc.
 * Testamos: lógica de negócio, mapeamento toResponse() e tratamento de exceções.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CategoriaService")
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    // ── Fixtures ──────────────────────────────────────────────────────────

    private Categoria categoriaCerrado;
    private Categoria categoriaAlimentacao;
    @SuppressWarnings("unused")
    private Categoria categoriaInativa;

    @BeforeEach
    void setUp() {
        categoriaCerrado = Categoria.builder()
                .id(1L)
                .slug("produtos-cerrado")
                .nome("Produtos do Cerrado")
                .descricao("Frutos nativos, doces artesanais e produtos do bioma cerrado")
                .icone("Leaf")
                .cor("text-green-700")
                .bgCor("bg-green-50")
                .urlImagem(null)
                .ativo(true)
                .build();

        categoriaAlimentacao = Categoria.builder()
                .id(3L)
                .slug("alimentacao")
                .nome("Alimentação")
                .descricao("Restaurantes, mercados, distribuidoras e gêneros alimentícios")
                .icone("UtensilsCrossed")
                .cor("text-orange-600")
                .bgCor("bg-orange-50")
                .urlImagem(null)
                .ativo(true)
                .build();

        categoriaInativa = Categoria.builder()
                .id(99L)
                .slug("categoria-inativa")
                .nome("Categoria Inativa")
                .descricao("Categoria desativada")
                .ativo(false)
                .build();
    }

    // =========================================================================
    // listarTodas()
    // =========================================================================

    @Nested
    @DisplayName("listarTodas()")
    class ListarTodas {

        @Test
        @DisplayName("deve retornar lista com todas as categorias ativas")
        void deveRetornarListaComCategoriasAtivas() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue())
                    .thenReturn(List.of(categoriaCerrado, categoriaAlimentacao));

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodas();

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getSlug()).isEqualTo("produtos-cerrado");
            assertThat(resultado.get(1).getSlug()).isEqualTo("alimentacao");

            verify(categoriaRepository, times(1)).findAllByAtivoTrue();
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver categorias ativas")
        void deveRetornarListaVaziaQuandoNaoHouverCategoriasAtivas() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue())
                    .thenReturn(Collections.emptyList());

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodas();

            // Assert
            assertThat(resultado).isEmpty();
            verify(categoriaRepository, times(1)).findAllByAtivoTrue();
        }

        @Test
        @DisplayName("deve retornar lista com um único item")
        void deveRetornarListaComUmItem() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue())
                    .thenReturn(List.of(categoriaCerrado));

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodas();

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("Produtos do Cerrado");
        }

        @Test
        @DisplayName("deve chamar findAllByAtivoTrue e não findAll")
        void deveChamarFindAllByAtivoTrue() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue()).thenReturn(List.of());

            // Act
            categoriaService.listarTodas();

            // Assert — garante que só filtra ativas, nunca busca todas
            verify(categoriaRepository, times(1)).findAllByAtivoTrue();
            verify(categoriaRepository, never()).findAll();
        }
    }

    // =========================================================================
    // buscarPorSlug()
    // =========================================================================

    @Nested
    @DisplayName("buscarPorSlug()")
    class BuscarPorSlug {

        @Test
        @DisplayName("deve retornar CategoriaResponse quando slug existir")
        void deveRetornarResponseQuandoSlugExistir() {
            // Arrange
            when(categoriaRepository.findBySlug("produtos-cerrado"))
                    .thenReturn(Optional.of(categoriaCerrado));

            // Act
            CategoriaResponse resultado = categoriaService.buscarPorSlug("produtos-cerrado");

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getSlug()).isEqualTo("produtos-cerrado");
            assertThat(resultado.getNome()).isEqualTo("Produtos do Cerrado");

            verify(categoriaRepository, times(1)).findBySlug("produtos-cerrado");
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando slug não existir")
        void deveLancarRuntimeExceptionQuandoSlugNaoExistir() {
            // Arrange
            when(categoriaRepository.findBySlug("slug-inexistente"))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> categoriaService.buscarPorSlug("slug-inexistente"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Categoria não encontrada: slug-inexistente");

            verify(categoriaRepository, times(1)).findBySlug("slug-inexistente");
        }

        @Test
        @DisplayName("deve incluir o slug na mensagem de erro")
        void deveIncluirSlugNaMensagemDeErro() {
            // Arrange
            String slugInexistente = "cerrado-xpto-123";
            when(categoriaRepository.findBySlug(slugInexistente))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> categoriaService.buscarPorSlug(slugInexistente))
                    .hasMessageContaining(slugInexistente);
        }

        @Test
        @DisplayName("deve chamar findBySlug com o slug exato recebido")
        void deveChamarFindBySlugComSlugExato() {
            // Arrange
            when(categoriaRepository.findBySlug("alimentacao"))
                    .thenReturn(Optional.of(categoriaAlimentacao));

            // Act
            categoriaService.buscarPorSlug("alimentacao");

            // Assert
            verify(categoriaRepository, times(1)).findBySlug("alimentacao");
            verifyNoMoreInteractions(categoriaRepository);
        }
    }

    // =========================================================================
    // toResponse() — mapeamento da entidade para DTO
    // =========================================================================

    @Nested
    @DisplayName("toResponse() — mapeamento Categoria → CategoriaResponse")
    class ToResponse {

        @Test
        @DisplayName("deve mapear todos os campos corretamente")
        void deveMapearTodosCamposCorretamente() {
            // Arrange
            when(categoriaRepository.findBySlug("produtos-cerrado"))
                    .thenReturn(Optional.of(categoriaCerrado));

            // Act
            CategoriaResponse response = categoriaService.buscarPorSlug("produtos-cerrado");

            // Assert — verifica cada campo do mapeamento
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getNome()).isEqualTo("Produtos do Cerrado");
            assertThat(response.getSlug()).isEqualTo("produtos-cerrado");
            assertThat(response.getIcone()).isEqualTo("Leaf");
            assertThat(response.getCor()).isEqualTo("text-green-700");
            assertThat(response.getCorFundo()).isEqualTo("bg-green-50");
            assertThat(response.getTotalProdutos()).isEqualTo(0);
            assertThat(response.getImagem()).isNull();
            assertThat(response.getDescricao())
                    .isEqualTo("Frutos nativos, doces artesanais e produtos do bioma cerrado");
        }

        @Test
        @DisplayName("deve sempre retornar totalProdutos como zero")
        void deveSempreRetornarTotalProdutosComoZero() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue())
                    .thenReturn(List.of(categoriaCerrado, categoriaAlimentacao));

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodas();

            // Assert — totalProdutos é hardcoded como 0 no toResponse()
            assertThat(resultado)
                    .extracting(CategoriaResponse::getTotalProdutos)
                    .containsOnly(0);
        }

        @Test
        @DisplayName("deve mapear urlImagem null corretamente")
        void deveMapearUrlImagemNullCorretamente() {
            // Arrange
            when(categoriaRepository.findBySlug("produtos-cerrado"))
                    .thenReturn(Optional.of(categoriaCerrado));

            // Act
            CategoriaResponse response = categoriaService.buscarPorSlug("produtos-cerrado");

            // Assert
            assertThat(response.getImagem()).isNull();
        }

        @Test
        @DisplayName("deve mapear todos os itens da lista preservando a ordem")
        void deveMapearTodosItensPreservandoOrdem() {
            // Arrange
            when(categoriaRepository.findAllByAtivoTrue())
                    .thenReturn(List.of(categoriaCerrado, categoriaAlimentacao));

            // Act
            List<CategoriaResponse> resultado = categoriaService.listarTodas();

            // Assert
            assertThat(resultado)
                    .extracting(CategoriaResponse::getSlug)
                    .containsExactly("produtos-cerrado", "alimentacao");
        }
    }
}
