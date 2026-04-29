package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.CategoriaResponse;
import br.com.guiadegurupi.exception.ResourceNotFoundException;
import br.com.guiadegurupi.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CategoriaController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@DisplayName("CategoriaController")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaService categoriaService;

    // ── Fixtures ──────────────────────────────────────────────────────────
    // Apenas os campos relevantes para os testes são populados.
    // Campos opcionais (icone, cor, imagem etc.) ficam null — o @Builder
    // permite isso sem precisar de construtores longos (clean code).

    private CategoriaResponse categoriaRestaurantes;
    private CategoriaResponse categoriaCerrado;

    @BeforeEach
    void setUp() {
        categoriaRestaurantes = CategoriaResponse.builder()
                .id(1L)
                .nome("Restaurantes")
                .slug("restaurantes")
                .descricao("Melhores restaurantes de Gurupi")
                .build();

        categoriaCerrado = CategoriaResponse.builder()
                .id(2L)
                .nome("Produtos do Cerrado")
                .slug("produtos-cerrado")
                .descricao("Produtos nativos do Cerrado tocantinense")
                .build();
    }

    // =========================================================================
    // GET /categorias
    // =========================================================================

    @Nested
    @DisplayName("GET /categorias")
    class ListarTodas {

        @Test
        @DisplayName("deve retornar 200 com lista de categorias quando existirem registros")
        void deveRetornar200ComListaDeCategorias() throws Exception {

            // Arrange
            List<CategoriaResponse> categorias = List.of(categoriaRestaurantes, categoriaCerrado);
            when(categoriaService.listarTodas()).thenReturn(categorias);

            // Act + Assert
            mockMvc.perform(get("/categorias")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[0].nome", is("Restaurantes")))
                    .andExpect(jsonPath("$[0].slug", is("restaurantes")))
                    .andExpect(jsonPath("$[1].id", is(2)))
                    .andExpect(jsonPath("$[1].nome", is("Produtos do Cerrado")))
                    .andExpect(jsonPath("$[1].slug", is("produtos-cerrado")));

            verify(categoriaService, times(1)).listarTodas();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não houver categorias cadastradas")
        void deveRetornar200ComListaVaziaQuandoNaoHouverCategorias() throws Exception {

            // Arrange
            when(categoriaService.listarTodas()).thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/categorias")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(categoriaService, times(1)).listarTodas();
        }

        @Test
        @DisplayName("deve retornar 200 com lista contendo apenas um item")
        void deveRetornar200ComUmItemNaLista() throws Exception {

            // Arrange
            when(categoriaService.listarTodas()).thenReturn(List.of(categoriaRestaurantes));

            // Act + Assert
            mockMvc.perform(get("/categorias")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].slug", is("restaurantes")));

            verify(categoriaService, times(1)).listarTodas();
        }
    }

    // =========================================================================
    // GET /categorias/{slug}
    // =========================================================================

    @Nested
    @DisplayName("GET /categorias/{slug}")
    class BuscarPorSlug {

        @Test
        @DisplayName("deve retornar 200 com a categoria quando o slug existir")
        void deveRetornar200QuandoSlugExistir() throws Exception {

            // Arrange
            when(categoriaService.buscarPorSlug("restaurantes"))
                    .thenReturn(categoriaRestaurantes);

            // Act + Assert
            mockMvc.perform(get("/categorias/restaurantes")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nome", is("Restaurantes")))
                    .andExpect(jsonPath("$.slug", is("restaurantes")))
                    .andExpect(jsonPath("$.descricao", is("Melhores restaurantes de Gurupi")));

            verify(categoriaService, times(1)).buscarPorSlug("restaurantes");
        }

        @Test
        @DisplayName("deve retornar 200 para slug com hífen composto")
        void deveRetornar200ParaSlugComHifen() throws Exception {

            // Arrange
            when(categoriaService.buscarPorSlug("produtos-cerrado"))
                    .thenReturn(categoriaCerrado);

            // Act + Assert
            mockMvc.perform(get("/categorias/produtos-cerrado")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.slug", is("produtos-cerrado")))
                    .andExpect(jsonPath("$.nome", is("Produtos do Cerrado")));

            verify(categoriaService, times(1)).buscarPorSlug("produtos-cerrado");
        }

        @Test
        @DisplayName("deve retornar 404 quando o slug não existir")
        void deveRetornar404QuandoSlugNaoExistir() throws Exception {

            // Arrange
            String slugInexistente = "slug-inexistente";
            when(categoriaService.buscarPorSlug(slugInexistente))
                    .thenThrow(new ResourceNotFoundException(
                            "Categoria não encontrada para o slug: " + slugInexistente));

            // Act + Assert
            mockMvc.perform(get("/categorias/{slug}", slugInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(categoriaService, times(1)).buscarPorSlug(slugInexistente);
        }

        @Test
        @DisplayName("deve chamar o service exatamente uma vez com o slug correto")
        void deveChamarServiceUmaVezComSlugCorreto() throws Exception {

            // Arrange
            when(categoriaService.buscarPorSlug("restaurantes"))
                    .thenReturn(categoriaRestaurantes);

            // Act
            mockMvc.perform(get("/categorias/restaurantes")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // Assert
            verify(categoriaService, times(1)).buscarPorSlug("restaurantes");
            verifyNoMoreInteractions(categoriaService);
        }
    }
}