package br.com.guiadegurupi.controller;

import br.com.guiadegurupi.dto.response.ProdutoResponse;
import br.com.guiadegurupi.exception.ResourceNotFoundException;
import br.com.guiadegurupi.service.ProdutoService;
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
        controllers = ProdutoController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ActiveProfiles("test")
@DisplayName("ProdutoController")
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    // ── Fixtures — alinhados com o seed V2__seed_initial_data.sql ─────────

    private ProdutoResponse biscoito;
    private ProdutoResponse agua;
    private ProdutoResponse oleoMobil;
    private ProdutoResponse trocaDeOleo;
    private ProdutoResponse pequiCongelado;

    @BeforeEach
    void setUp() {

        biscoito = ProdutoResponse.builder()
                .id(1L)
                .nome("Caixa de Biscoito Cream Cracker 20un")
                .descricao("Caixa com 20 pacotes de biscoito cream cracker.")
                .preco(45.90)
                .precoOriginal(52.00)
                .estoque(200)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .empresaId(1L)
                .empresaSlug("distribuidora-martins")
                .empresaNome("Distribuidora Martins")
                .categoriaSlug("alimentacao")
                .tempoEntrega("1-2 dias úteis")
                .build();

        agua = ProdutoResponse.builder()
                .id(2L)
                .nome("Fardo Água Mineral 500ml 12un")
                .descricao("Fardo com 12 garrafas de água mineral 500ml.")
                .preco(18.50)
                .estoque(500)
                .eServico(false)
                .destaque(false)
                .ativo(true)
                .empresaId(1L)
                .empresaSlug("distribuidora-martins")
                .empresaNome("Distribuidora Martins")
                .categoriaSlug("alimentacao")
                .tempoEntrega("1 dia útil")
                .build();

        oleoMobil = ProdutoResponse.builder()
                .id(3L)
                .nome("Óleo Mobil Super 5W-30 1L")
                .descricao("Óleo lubrificante sintético para motores a gasolina e flex.")
                .preco(42.90)
                .precoOriginal(49.90)
                .estoque(80)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .empresaId(3L)
                .empresaSlug("lubrificantes-bom-preco")
                .empresaNome("Lubrificantes Bom Preço")
                .categoriaSlug("servicos-automotivos")
                .tempoEntrega("Retirada imediata")
                .build();

        trocaDeOleo = ProdutoResponse.builder()
                .id(4L)
                .nome("Troca de Óleo Completa")
                .descricao("Serviço de troca de óleo com dreno e filtro.")
                .preco(89.90)
                .estoque(1)
                .eServico(true)
                .destaque(true)
                .ativo(true)
                .empresaId(3L)
                .empresaSlug("lubrificantes-bom-preco")
                .empresaNome("Lubrificantes Bom Preço")
                .categoriaSlug("servicos-automotivos")
                .tempoEntrega("Agendamento")
                .build();

        // Produto sazonal do cerrado — endpoint especial /sazonais
        pequiCongelado = ProdutoResponse.builder()
                .id(5L)
                .nome("Pequi Congelado 500g")
                .descricao("Pequi nativo do cerrado tocantinense, congelado na hora da colheita.")
                .preco(22.00)
                .estoque(50)
                .eServico(false)
                .destaque(true)
                .ativo(true)
                .categoriaSlug("produtos-cerrado")
                .tempoEntrega("1-2 dias úteis")
                .epocaDisponivel("outubro a dezembro")
                .tags(List.of("cerrado", "nativo", "sazonal", "pequi"))
                .build();
    }

    // =========================================================================
    // GET /produtos  — lista destaques
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos")
    class ListarDestaques {

        @Test
        @DisplayName("deve retornar 200 com produtos em destaque")
        void deveRetornar200ComProdutosEmDestaque() throws Exception {

            // Arrange
            List<ProdutoResponse> destaques = List.of(biscoito, oleoMobil, trocaDeOleo);
            when(produtoService.listarDestaques()).thenReturn(destaques);

            // Act + Assert
            mockMvc.perform(get("/produtos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andExpect(jsonPath("$[*].destaque", everyItem(is(true))))
                    .andExpect(jsonPath("$[0].nome", is("Caixa de Biscoito Cream Cracker 20un")))
                    .andExpect(jsonPath("$[1].nome", is("Óleo Mobil Super 5W-30 1L")))
                    .andExpect(jsonPath("$[2].eServico", is(true)));

            verify(produtoService, times(1)).listarDestaques();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia quando não houver destaques")
        void deveRetornar200ComListaVaziaQuandoNaoHouverDestaques() throws Exception {

            // Arrange
            when(produtoService.listarDestaques()).thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/produtos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(produtoService, times(1)).listarDestaques();
        }
    }

    // =========================================================================
    // GET /produtos/{id}
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("deve retornar 200 com produto quando id existir")
        void deveRetornar200QuandoIdExistir() throws Exception {

            // Arrange
            when(produtoService.buscarPorId(1L)).thenReturn(biscoito);

            // Act + Assert
            mockMvc.perform(get("/produtos/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.nome", is("Caixa de Biscoito Cream Cracker 20un")))
                    .andExpect(jsonPath("$.preco", is(45.90)))
                    .andExpect(jsonPath("$.precoOriginal", is(52.00)))
                    .andExpect(jsonPath("$.eServico", is(false)))
                    .andExpect(jsonPath("$.empresaSlug", is("distribuidora-martins")))
                    .andExpect(jsonPath("$.tempoEntrega", is("1-2 dias úteis")));

            verify(produtoService, times(1)).buscarPorId(1L);
        }

        @Test
        @DisplayName("deve retornar 200 para produto que é serviço")
        void deveRetornar200ParaProdutoQueEServico() throws Exception {

            // Arrange
            when(produtoService.buscarPorId(4L)).thenReturn(trocaDeOleo);

            // Act + Assert
            mockMvc.perform(get("/produtos/4")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(4)))
                    .andExpect(jsonPath("$.eServico", is(true)))
                    .andExpect(jsonPath("$.tempoEntrega", is("Agendamento")));

            verify(produtoService, times(1)).buscarPorId(4L);
        }

        @Test
        @DisplayName("deve retornar 404 quando id não existir")
        void deveRetornar404QuandoIdNaoExistir() throws Exception {

            // Arrange
            when(produtoService.buscarPorId(999L))
                    .thenThrow(new ResourceNotFoundException(
                            "Produto não encontrado com id: 999"));

            // Act + Assert
            mockMvc.perform(get("/produtos/999")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            verify(produtoService, times(1)).buscarPorId(999L);
        }

        @Test
        @DisplayName("deve chamar o service exatamente uma vez com o id correto")
        void deveChamarServiceUmaVezComIdCorreto() throws Exception {

            // Arrange
            when(produtoService.buscarPorId(3L)).thenReturn(oleoMobil);

            // Act
            mockMvc.perform(get("/produtos/3")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // Assert
            verify(produtoService, times(1)).buscarPorId(3L);
            verifyNoMoreInteractions(produtoService);
        }
    }

    // =========================================================================
    // GET /produtos/empresa/{empresaId}
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos/empresa/{empresaId}")
    class ListarPorEmpresa {

        @Test
        @DisplayName("deve retornar 200 com produtos da empresa informada")
        void deveRetornar200ComProdutosDaEmpresa() throws Exception {

            // Arrange — empresaId=1 é a Distribuidora Martins
            when(produtoService.listarPorEmpresa(1L))
                    .thenReturn(List.of(biscoito, agua));

            // Act + Assert
            mockMvc.perform(get("/produtos/empresa/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].empresaId", everyItem(is(1))))
                    .andExpect(jsonPath("$[*].empresaSlug",
                            everyItem(is("distribuidora-martins"))));

            verify(produtoService, times(1)).listarPorEmpresa(1L);
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia para empresa sem produtos")
        void deveRetornar200ComListaVaziaParaEmpresaSemProdutos() throws Exception {

            // Arrange
            when(produtoService.listarPorEmpresa(2L))
                    .thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/produtos/empresa/2")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(produtoService, times(1)).listarPorEmpresa(2L);
        }

        @Test
        @DisplayName("deve retornar 200 com produtos físicos e serviços misturados")
        void deveRetornar200ComProdutosFisicosEServicos() throws Exception {

            // Arrange — empresaId=3 tem produto físico e serviço
            when(produtoService.listarPorEmpresa(3L))
                    .thenReturn(List.of(oleoMobil, trocaDeOleo));

            // Act + Assert
            mockMvc.perform(get("/produtos/empresa/3")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].eServico", is(false)))
                    .andExpect(jsonPath("$[1].eServico", is(true)));

            verify(produtoService, times(1)).listarPorEmpresa(3L);
        }
    }

    // =========================================================================
    // GET /produtos/categoria/{categoriaSlug}
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos/categoria/{categoriaSlug}")
    class ListarPorCategoria {

        @Test
        @DisplayName("deve retornar 200 com produtos da categoria informada")
        void deveRetornar200ComProdutosDaCategoria() throws Exception {

            // Arrange
            when(produtoService.listarPorCategoria("alimentacao"))
                    .thenReturn(List.of(biscoito, agua));

            // Act + Assert
            mockMvc.perform(get("/produtos/categoria/alimentacao")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].categoriaSlug",
                            everyItem(is("alimentacao"))));

            verify(produtoService, times(1)).listarPorCategoria("alimentacao");
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia para categoria sem produtos")
        void deveRetornar200ComListaVaziaParaCategoriaSemProdutos() throws Exception {

            // Arrange
            when(produtoService.listarPorCategoria("educacao-cursos"))
                    .thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/produtos/categoria/educacao-cursos")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(produtoService, times(1)).listarPorCategoria("educacao-cursos");
        }
    }

    // =========================================================================
    // GET /produtos/buscar?q=
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos/buscar")
    class Pesquisar {

        @Test
        @DisplayName("deve retornar 200 com resultados para termo encontrado")
        void deveRetornar200ComResultadosParaTermoEncontrado() throws Exception {

            // Arrange
            when(produtoService.pesquisar("óleo"))
                    .thenReturn(List.of(oleoMobil, trocaDeOleo));

            // Act + Assert
            mockMvc.perform(get("/produtos/buscar")
                            .param("q", "óleo")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].empresaSlug",
                            everyItem(is("lubrificantes-bom-preco"))));

            verify(produtoService, times(1)).pesquisar("óleo");
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia para termo não encontrado")
        void deveRetornar200ComListaVaziaParaTermoNaoEncontrado() throws Exception {

            // Arrange
            when(produtoService.pesquisar("smartphone"))
                    .thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/produtos/buscar")
                            .param("q", "smartphone")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(produtoService, times(1)).pesquisar("smartphone");
        }

        @Test
        @DisplayName("deve retornar 200 pesquisando por nome parcial")
        void deveRetornar200PesquisandoPorNomeParcial() throws Exception {

            // Arrange
            when(produtoService.pesquisar("Biscoito"))
                    .thenReturn(List.of(biscoito));

            // Act + Assert
            mockMvc.perform(get("/produtos/buscar")
                            .param("q", "Biscoito")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nome",
                            is("Caixa de Biscoito Cream Cracker 20un")));

            verify(produtoService, times(1)).pesquisar("Biscoito");
        }
    }

    // =========================================================================
    // GET /produtos/sazonais  — endpoint especial do cerrado!
    // =========================================================================

    @Nested
    @DisplayName("GET /produtos/sazonais")
    class ListarSazonais {

        @Test
        @DisplayName("deve retornar 200 com produtos sazonais do cerrado")
        void deveRetornar200ComProdutosSazonaisDoCerrado() throws Exception {

            // Arrange
            when(produtoService.listarSazonais()).thenReturn(List.of(pequiCongelado));

            // Act + Assert
            mockMvc.perform(get("/produtos/sazonais")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nome", is("Pequi Congelado 500g")))
                    .andExpect(jsonPath("$[0].categoriaSlug", is("produtos-cerrado")))
                    .andExpect(jsonPath("$[0].epocaDisponivel", is("outubro a dezembro")))
                    .andExpect(jsonPath("$[0].tags", hasItems("cerrado", "nativo", "sazonal")));

            verify(produtoService, times(1)).listarSazonais();
        }

        @Test
        @DisplayName("deve retornar 200 com lista vazia fora de época")
        void deveRetornar200ComListaVaziaForaDeEpoca() throws Exception {

            // Arrange — fora da época de colheita, nenhum produto disponível
            when(produtoService.listarSazonais()).thenReturn(Collections.emptyList());

            // Act + Assert
            mockMvc.perform(get("/produtos/sazonais")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(produtoService, times(1)).listarSazonais();
        }

        @Test
        @DisplayName("deve garantir que produtos sazonais têm epocaDisponivel preenchido")
        void deveGarantirQueEpocaDisponivelEstaPreenchida() throws Exception {

            // Arrange
            when(produtoService.listarSazonais()).thenReturn(List.of(pequiCongelado));

            // Act + Assert
            mockMvc.perform(get("/produtos/sazonais")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[*].epocaDisponivel",
                            everyItem(not(emptyOrNullString()))));

            verify(produtoService, times(1)).listarSazonais();
        }
    }
}
