package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.EmpresaResponse;
import br.com.guiadegurupi.exception.ResourceNotFoundException;
import br.com.guiadegurupi.service.EmpresaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
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
        controllers = EmpresaController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ActiveProfiles("test")
@DisplayName("EmpresaController")
class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmpresaService empresaService;

    // ── Fixtures — alinhados com o seed V2__seed_initial_data.sql ─────────

    private EmpresaResponse distribuidoraMartins;
    private EmpresaResponse comafe;
    private EmpresaResponse lubrificantesBomPreco;

    @BeforeEach
    void setUp() {
        distribuidoraMartins = EmpresaResponse.builder()
                .id(1L)
                .slug("distribuidora-martins")
                .nome("Distribuidora Martins")
                .tagline("O abastecimento do sul do Tocantins")
                .whatsapp("6399999-0001")
                .localizacao("Gurupi, TO")
                .tempoResposta("< 2 horas")
                .rating(0.0)
                .totalAvaliacoes(0)
                .totalSeguidores(0)
                .verificado(true)
                .destaque(true)
                .ativo(true)
                .planoNome("PREMIUM")
                .categoriasSlugs(List.of("alimentacao", "comercio-geral"))
                .build();

        comafe = EmpresaResponse.builder()
                .id(2L)
                .slug("comafe")
                .nome("Comafe")
                .tagline("Seu parceiro em Gurupi e região")
                .whatsapp("6399999-0002")
                .localizacao("Gurupi, TO")
                .tempoResposta("< 1 hora")
                .rating(0.0)
                .totalAvaliacoes(0)
                .totalSeguidores(0)
                .verificado(true)
                .destaque(true)
                .ativo(true)
                .planoNome("DESTAQUE")
                .categoriasSlugs(List.of("comercio-geral"))
                .build();

        lubrificantesBomPreco = EmpresaResponse.builder()
                .id(3L)
                .slug("lubrificantes-bom-preco")
                .nome("Lubrificantes Bom Preço")
                .tagline("Cuide do seu veículo sem pesar no bolso")
                .whatsapp("6399999-0003")
                .localizacao("Gurupi, TO")
                .tempoResposta("< 30 min")
                .rating(0.0)
                .totalAvaliacoes(0)
                .totalSeguidores(0)
                .verificado(true)
                .destaque(true)
                .ativo(true)
                .planoNome("DESTAQUE")
                .categoriasSlugs(List.of("servicos-automotivos"))
                .build();
    }

    // =========================================================================
    // GET /empresas
    // =========================================================================

    @Nested
    @DisplayName("GET /empresas")
    class ListarTodas {

        @Test
        @DisplayName("deve retornar 200 com lista de todas as empresas")
        void deveRetornar200ComListaDeEmpresas() throws Exception {

            // Arrange
            List<EmpresaResponse> empresas = List.of(
                    distribuidoraMartins, comafe, lubrificantesBomPreco);
            when(empresaService.listarTodas()).thenReturn(empresas);

            // Act + Assert
            mockMvc.perform(get("/empresas")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[0].slug", is("distribuidora-martins")))
                    .andExpect(jsonPath("$[0].nome", is("Distribuidora Martins")))
                    .andExpect(jsonPath("$[0].planoNome", is("PREMIUM")))
                    .andExpect(jsonPath("$[1].slug", is("comafe")))
                    .andExpect(jsonPath("$[2].slug", is("lubrificantes-bom-preco")));

            verify(empresaService, times(1)).listarTodas();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não houver empresas")
        void deveRetornar200ComListaVazia() throws Exception {

            // Arrange
            when(empresaService.listarTodas()).thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/empresas")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(empresaService, times(1)).listarTodas();
        }
    }

    // =========================================================================
    // GET /empresas/destaques
    // =========================================================================

    @Nested
    @DisplayName("GET /empresas/destaques")
    class ListarDestaques {

        @Test
        @DisplayName("deve retornar 200 com empresas em destaque")
        void deveRetornar200ComEmpresasEmDestaque() throws Exception {

            // Arrange — todas as 3 do seed são destaque=true
            List<EmpresaResponse> destaques = List.of(
                    distribuidoraMartins, comafe, lubrificantesBomPreco);
            when(empresaService.listarDestaques()).thenReturn(destaques);

            // Act + Assert
            mockMvc.perform(get("/empresas/destaques")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[*].destaque", everyItem(is(true))))
                    .andExpect(jsonPath("$[*].verificado", everyItem(is(true))));

            verify(empresaService, times(1)).listarDestaques();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não houver destaques")
        void deveRetornar200ComListaVaziaQuandoNaoHouverDestaques() throws Exception {

            // Arrange
            when(empresaService.listarDestaques()).thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/empresas/destaques")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(empresaService, times(1)).listarDestaques();
        }
    }

    // =========================================================================
    // GET /empresas/{slug}
    // =========================================================================

    @Nested
    @DisplayName("GET /empresas/{slug}")
    class BuscarPorSlug {

        @Test
        @DisplayName("deve retornar 200 com empresa quando slug existir")
        void deveRetornar200QuandoSlugExistir() throws Exception {

            // Arrange
            when(empresaService.buscarPorSlug("distribuidora-martins"))
                    .thenReturn(distribuidoraMartins);

            // Act + Assert
            mockMvc.perform(get("/empresas/distribuidora-martins")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.slug", is("distribuidora-martins")))
                    .andExpect(jsonPath("$.nome", is("Distribuidora Martins")))
                    .andExpect(jsonPath("$.whatsapp", is("6399999-0001")))
                    .andExpect(jsonPath("$.localizacao", is("Gurupi, TO")))
                    .andExpect(jsonPath("$.planoNome", is("PREMIUM")))
                    .andExpect(jsonPath("$.categoriasSlugs", hasItems("alimentacao", "comercio-geral")));

            verify(empresaService, times(1)).buscarPorSlug("distribuidora-martins");
        }

        @Test
        @DisplayName("deve retornar 200 para slug com hífen composto")
        void deveRetornar200ParaSlugComHifenComposto() throws Exception {

            // Arrange
            when(empresaService.buscarPorSlug("lubrificantes-bom-preco"))
                    .thenReturn(lubrificantesBomPreco);

            // Act + Assert
            mockMvc.perform(get("/empresas/lubrificantes-bom-preco")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.slug", is("lubrificantes-bom-preco")))
                    .andExpect(jsonPath("$.tempoResposta", is("< 30 min")))
                    .andExpect(jsonPath("$.categoriasSlugs", hasItem("servicos-automotivos")));

            verify(empresaService, times(1)).buscarPorSlug("lubrificantes-bom-preco");
        }

        @Test
        @DisplayName("deve retornar 404 quando slug não existir")
        void deveRetornar404QuandoSlugNaoExistir() throws Exception {

            // Arrange
            String slugInexistente = "empresa-inexistente";
            when(empresaService.buscarPorSlug(slugInexistente))
                    .thenThrow(new ResourceNotFoundException(
                            "Empresa não encontrada para o slug: " + slugInexistente));

            // Act + Assert
            mockMvc.perform(get("/empresas/{slug}", slugInexistente)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(empresaService, times(1)).buscarPorSlug(slugInexistente);
        }

        @Test
        @DisplayName("deve chamar o service exatamente uma vez com o slug correto")
        void deveChamarServiceUmaVezComSlugCorreto() throws Exception {

            // Arrange
            when(empresaService.buscarPorSlug("comafe")).thenReturn(comafe);

            // Act
            mockMvc.perform(get("/empresas/comafe")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // Assert
            verify(empresaService, times(1)).buscarPorSlug("comafe");
            verifyNoMoreInteractions(empresaService);
        }
    }

    // =========================================================================
    // GET /empresas/categoria/{categoriaSlug}
    // =========================================================================

    @Nested
    @DisplayName("GET /empresas/categoria/{categoriaSlug}")
    class BuscarPorCategoria {

        @Test
        @DisplayName("deve retornar 200 com empresas da categoria informada")
        void deveRetornar200ComEmpresasDaCategoria() throws Exception {

            // Arrange
            when(empresaService.buscarPorCategoria("comercio-geral"))
                    .thenReturn(List.of(distribuidoraMartins, comafe));

            // Act + Assert
            mockMvc.perform(get("/empresas/categoria/comercio-geral")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].categoriasSlugs",
                            everyItem(hasItem("comercio-geral"))));

            verify(empresaService, times(1)).buscarPorCategoria("comercio-geral");
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia para categoria sem empresas")
        void deveRetornar200ComListaVaziaParaCategoriaSemEmpresas() throws Exception {

            // Arrange
            when(empresaService.buscarPorCategoria("educacao-cursos"))
                    .thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/empresas/categoria/educacao-cursos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(empresaService, times(1)).buscarPorCategoria("educacao-cursos");
        }

        @Test
        @DisplayName("deve retornar 200 com empresa única para categoria exclusiva")
        void deveRetornar200ComEmpresaUnicaParaCategoriaExclusiva() throws Exception {

            // Arrange
            when(empresaService.buscarPorCategoria("servicos-automotivos"))
                    .thenReturn(List.of(lubrificantesBomPreco));

            // Act + Assert
            mockMvc.perform(get("/empresas/categoria/servicos-automotivos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].slug", is("lubrificantes-bom-preco")));

            verify(empresaService, times(1)).buscarPorCategoria("servicos-automotivos");
        }
    }

    // =========================================================================
    // GET /empresas/buscar?q=
    // =========================================================================

    @Nested
    @DisplayName("GET /empresas/buscar")
    class Pesquisar {

        @Test
        @DisplayName("deve retornar 200 com resultados para termo encontrado")
        void deveRetornar200ComResultadosParaTermoEncontrado() throws Exception {

            // Arrange
            when(empresaService.pesquisar("lubrificante"))
                    .thenReturn(List.of(lubrificantesBomPreco));

            // Act + Assert
            mockMvc.perform(get("/empresas/buscar")
                            .param("q", "lubrificante")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].slug", is("lubrificantes-bom-preco")))
                    .andExpect(jsonPath("$[0].nome", is("Lubrificantes Bom Preço")));

            verify(empresaService, times(1)).pesquisar("lubrificante");
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia para termo não encontrado")
        void deveRetornar200ComListaVaziaParaTermoNaoEncontrado() throws Exception {

            // Arrange
            when(empresaService.pesquisar("pizzaria"))
                    .thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/empresas/buscar")
                            .param("q", "pizzaria")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(empresaService, times(1)).pesquisar("pizzaria");
        }

        @Test
        @DisplayName("deve retornar 200 com múltiplos resultados para termo genérico")
        void deveRetornar200ComMultiplosResultadosParaTermoGenerico() throws Exception {

            // Arrange — "Gurupi" aparece em todas as empresas do seed
            when(empresaService.pesquisar("Gurupi"))
                    .thenReturn(List.of(distribuidoraMartins, comafe, lubrificantesBomPreco));

            // Act + Assert
            mockMvc.perform(get("/empresas/buscar")
                            .param("q", "Gurupi")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[*].localizacao",
                            everyItem(containsString("Gurupi"))));

            verify(empresaService, times(1)).pesquisar("Gurupi");
        }

        @Test
        @DisplayName("deve retornar 200 pesquisando por nome parcial")
        void deveRetornar200PesquisandoPorNomeParcial() throws Exception {

            // Arrange
            when(empresaService.pesquisar("Martins"))
                    .thenReturn(List.of(distribuidoraMartins));

            // Act + Assert
            mockMvc.perform(get("/empresas/buscar")
                            .param("q", "Martins")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nome", is("Distribuidora Martins")));

            verify(empresaService, times(1)).pesquisar("Martins");
        }
    }
}
