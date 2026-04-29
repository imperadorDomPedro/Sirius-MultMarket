package br.com.guiadegurupi.service;

import br.com.guiadegurupi.dto.response.EmpresaResponse;
import br.com.guiadegurupi.model.Categoria;
import br.com.guiadegurupi.model.Empresa;
import br.com.guiadegurupi.model.Plano;
import br.com.guiadegurupi.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmpresaService")
class EmpresaServiceTest {

    @Mock
    private EmpresaRepository empresaRepository;

    @InjectMocks
    private EmpresaService empresaService;

    // ── Fixtures ──────────────────────────────────────────────────────────

    private Plano planoPremium;
    private Plano planoDestaque;
    private Categoria categoriaAlimentacao;
    private Categoria categoriaComercioGeral;
    private Categoria categoriaAutomotivo;
    private Empresa distribuidoraMartins;
    private Empresa comafe;
    private Empresa lubrificantesBomPreco;

    @BeforeEach
    void setUp() {
        planoPremium = Plano.builder()
                .id(3L)
                .nome("PREMIUM")
                .build();

        planoDestaque = Plano.builder()
                .id(2L)
                .nome("DESTAQUE")
                .build();

        categoriaAlimentacao = Categoria.builder()
                .id(3L)
                .slug("alimentacao")
                .nome("Alimentação")
                .ativo(true)
                .build();

        categoriaComercioGeral = Categoria.builder()
                .id(5L)
                .slug("comercio-geral")
                .nome("Comércio Geral")
                .ativo(true)
                .build();

        categoriaAutomotivo = Categoria.builder()
                .id(2L)
                .slug("servicos-automotivos")
                .nome("Serviços Automotivos")
                .ativo(true)
                .build();

        distribuidoraMartins = Empresa.builder()
                .id(1L)
                .slug("distribuidora-martins")
                .nome("Distribuidora Martins")
                .tagline("O abastecimento do sul do Tocantins")
                .descricao("Distribuidora com mais de 20 anos atendendo Gurupi e região.")
                .whatsapp("6399999-0001")
                .localizacao("Gurupi, TO")
                .tempoResposta("< 2 horas")
                .politicaEntrega("Entrega em Gurupi e região sul do TO")
                .politicaDevolucao("Troca mediante nota fiscal em até 7 dias")
                .rating(0.0)
                .totalAvaliacoes(0)
                .totalSeguidores(0)
                .verificado(true)
                .destaque(true)
                .ativo(true)
                .plano(planoPremium)
                .categorias(List.of(categoriaAlimentacao, categoriaComercioGeral))
                .criadoEm(LocalDateTime.of(2026, 1, 1, 0, 0))
                .build();

        comafe = Empresa.builder()
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
                .plano(planoDestaque)
                .categorias(List.of(categoriaComercioGeral))
                .build();

        lubrificantesBomPreco = Empresa.builder()
                .id(3L)
                .slug("lubrificantes-bom-preco")
                .nome("Lubrificantes Bom Preço")
                .tagline("Cuide do seu veículo sem pesar no bolso")
                .whatsapp("6399999-0003")
                .localizacao("Gurupi, TO")
                .tempoResposta("< 30 min")
                .rating(4.8)
                .totalAvaliacoes(12)
                .totalSeguidores(5)
                .verificado(true)
                .destaque(false)
                .ativo(true)
                .plano(planoDestaque)
                .categorias(List.of(categoriaAutomotivo))
                .build();
    }

    // =========================================================================
    // listarTodas()
    // =========================================================================

    @Nested
    @DisplayName("listarTodas()")
    class ListarTodas {

        @Test
        @DisplayName("deve retornar lista com todas as empresas ativas")
        void deveRetornarListaComEmpresasAtivas() {
            // Arrange
            when(empresaRepository.findAllByAtivoTrue())
                    .thenReturn(List.of(distribuidoraMartins, comafe, lubrificantesBomPreco));

            // Act
            List<EmpresaResponse> resultado = empresaService.listarTodas();

            // Assert
            assertThat(resultado).hasSize(3);
            assertThat(resultado)
                    .extracting(EmpresaResponse::getSlug)
                    .containsExactly(
                            "distribuidora-martins",
                            "comafe",
                            "lubrificantes-bom-preco"
                    );
            verify(empresaRepository, times(1)).findAllByAtivoTrue();
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver empresas ativas")
        void deveRetornarListaVaziaQuandoNaoHouverEmpresasAtivas() {
            // Arrange
            when(empresaRepository.findAllByAtivoTrue())
                    .thenReturn(Collections.emptyList());

            // Act
            List<EmpresaResponse> resultado = empresaService.listarTodas();

            // Assert
            assertThat(resultado).isEmpty();
            verify(empresaRepository, times(1)).findAllByAtivoTrue();
        }

        @Test
        @DisplayName("deve chamar findAllByAtivoTrue e nunca findAll")
        void deveChamarFindAllByAtivoTrue() {
            // Arrange
            when(empresaRepository.findAllByAtivoTrue()).thenReturn(List.of());

            // Act
            empresaService.listarTodas();

            // Assert
            verify(empresaRepository, times(1)).findAllByAtivoTrue();
            verify(empresaRepository, never()).findAll();
        }
    }

    // =========================================================================
    // listarDestaques()
    // =========================================================================

    @Nested
    @DisplayName("listarDestaques()")
    class ListarDestaques {

        @Test
        @DisplayName("deve retornar somente empresas com destaque=true")
        void deveRetornarSomenteEmpresasComDestaque() {
            // Arrange — Martins e Comafe são destaque, Bom Preço não
            when(empresaRepository.findAllByDestaqueAndAtivoTrue(true))
                    .thenReturn(List.of(distribuidoraMartins, comafe));

            // Act
            List<EmpresaResponse> resultado = empresaService.listarDestaques();

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado)
                    .extracting(EmpresaResponse::getDestaque)
                    .containsOnly(true);
            verify(empresaRepository, times(1)).findAllByDestaqueAndAtivoTrue(true);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver destaques")
        void deveRetornarListaVaziaQuandoNaoHouverDestaques() {
            // Arrange
            when(empresaRepository.findAllByDestaqueAndAtivoTrue(true))
                    .thenReturn(Collections.emptyList());

            // Act
            List<EmpresaResponse> resultado = empresaService.listarDestaques();

            // Assert
            assertThat(resultado).isEmpty();
        }
    }

    // =========================================================================
    // buscarPorSlug()
    // =========================================================================

    @Nested
    @DisplayName("buscarPorSlug()")
    class BuscarPorSlug {

        @Test
        @DisplayName("deve retornar EmpresaResponse quando slug existir")
        void deveRetornarResponseQuandoSlugExistir() {
            // Arrange
            when(empresaRepository.findBySlug("distribuidora-martins"))
                    .thenReturn(Optional.of(distribuidoraMartins));

            // Act
            EmpresaResponse resultado = empresaService.buscarPorSlug("distribuidora-martins");

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getSlug()).isEqualTo("distribuidora-martins");
            assertThat(resultado.getNome()).isEqualTo("Distribuidora Martins");
            verify(empresaRepository, times(1)).findBySlug("distribuidora-martins");
        }

        @Test
        @DisplayName("deve lançar RuntimeException quando slug não existir")
        void deveLancarRuntimeExceptionQuandoSlugNaoExistir() {
            // Arrange
            when(empresaRepository.findBySlug("slug-inexistente"))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> empresaService.buscarPorSlug("slug-inexistente"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Empresa não encontrada: slug-inexistente");
        }

        @Test
        @DisplayName("deve incluir o slug na mensagem de erro")
        void deveIncluirSlugNaMensagemDeErro() {
            // Arrange
            String slugInexistente = "empresa-xpto-123";
            when(empresaRepository.findBySlug(slugInexistente))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> empresaService.buscarPorSlug(slugInexistente))
                    .hasMessageContaining(slugInexistente);
        }
    }

    // =========================================================================
    // buscarPorCategoria()
    // =========================================================================

    @Nested
    @DisplayName("buscarPorCategoria()")
    class BuscarPorCategoria {

        @Test
        @DisplayName("deve retornar empresas da categoria informada")
        void deveRetornarEmpresasDaCategoria() {
            // Arrange
            when(empresaRepository.findByCategoriaSlug("comercio-geral"))
                    .thenReturn(List.of(distribuidoraMartins, comafe));

            // Act
            List<EmpresaResponse> resultado = empresaService.buscarPorCategoria("comercio-geral");

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado)
                    .extracting(EmpresaResponse::getSlug)
                    .containsExactly("distribuidora-martins", "comafe");
            verify(empresaRepository, times(1)).findByCategoriaSlug("comercio-geral");
        }

        @Test
        @DisplayName("deve retornar lista vazia para categoria sem empresas")
        void deveRetornarListaVaziaParaCategoriaSemEmpresas() {
            // Arrange
            when(empresaRepository.findByCategoriaSlug("educacao-cursos"))
                    .thenReturn(Collections.emptyList());

            // Act
            List<EmpresaResponse> resultado = empresaService.buscarPorCategoria("educacao-cursos");

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
        @DisplayName("deve retornar empresas que correspondem ao termo de busca")
        void deveRetornarEmpresasQueCorespondoemAoTermo() {
            // Arrange
            when(empresaRepository.search("Martins"))
                    .thenReturn(List.of(distribuidoraMartins));

            // Act
            List<EmpresaResponse> resultado = empresaService.pesquisar("Martins");

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("Distribuidora Martins");
            verify(empresaRepository, times(1)).search("Martins");
        }

        @Test
        @DisplayName("deve retornar lista vazia para termo não encontrado")
        void deveRetornarListaVaziaParaTermoNaoEncontrado() {
            // Arrange
            when(empresaRepository.search("pizzaria"))
                    .thenReturn(Collections.emptyList());

            // Act
            List<EmpresaResponse> resultado = empresaService.pesquisar("pizzaria");

            // Assert
            assertThat(resultado).isEmpty();
        }

        @Test
        @DisplayName("deve retornar múltiplos resultados para termo genérico")
        void deveRetornarMultiplosResultadosParaTermoGenerico() {
            // Arrange
            when(empresaRepository.search("Gurupi"))
                    .thenReturn(List.of(distribuidoraMartins, comafe, lubrificantesBomPreco));

            // Act
            List<EmpresaResponse> resultado = empresaService.pesquisar("Gurupi");

            // Assert
            assertThat(resultado).hasSize(3);
            assertThat(resultado)
                    .extracting(EmpresaResponse::getLocalizacao)
                    .allMatch(loc -> loc.contains("Gurupi"));
        }
    }

    // =========================================================================
    // toResponse() — mapeamento da entidade para DTO
    // =========================================================================

    @Nested
    @DisplayName("toResponse() — mapeamento Empresa → EmpresaResponse")
    class ToResponse {

        @Test
        @DisplayName("deve mapear todos os campos corretamente")
        void deveMapearTodosCamposCorretamente() {
            // Arrange
            when(empresaRepository.findBySlug("distribuidora-martins"))
                    .thenReturn(Optional.of(distribuidoraMartins));

            // Act
            EmpresaResponse response = empresaService.buscarPorSlug("distribuidora-martins");

            // Assert — verifica cada campo do mapeamento
            assertThat(response.getId()).isEqualTo(1L);
            assertThat(response.getSlug()).isEqualTo("distribuidora-martins");
            assertThat(response.getNome()).isEqualTo("Distribuidora Martins");
            assertThat(response.getTagline()).isEqualTo("O abastecimento do sul do Tocantins");
            assertThat(response.getWhatsapp()).isEqualTo("6399999-0001");
            assertThat(response.getLocalizacao()).isEqualTo("Gurupi, TO");
            assertThat(response.getTempoResposta()).isEqualTo("< 2 horas");
            assertThat(response.getRating()).isEqualTo(0.0);
            assertThat(response.getTotalAvaliacoes()).isEqualTo(0);
            assertThat(response.getTotalSeguidores()).isEqualTo(0);
            assertThat(response.getVerificado()).isTrue();
            assertThat(response.getDestaque()).isTrue();
            assertThat(response.getAtivo()).isTrue();
            assertThat(response.getPlanoNome()).isEqualTo("PREMIUM");
            assertThat(response.getCriadoEm()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
        }

        @Test
        @DisplayName("deve mapear categoriasSlugs corretamente")
        void deveMapearCategoriasSlugsCorretamente() {
            // Arrange
            when(empresaRepository.findBySlug("distribuidora-martins"))
                    .thenReturn(Optional.of(distribuidoraMartins));

            // Act
            EmpresaResponse response = empresaService.buscarPorSlug("distribuidora-martins");

            // Assert
            assertThat(response.getCategoriasSlugs())
                    .containsExactly("alimentacao", "comercio-geral");
        }

        @Test
        @DisplayName("deve retornar categoriasSlugs vazio quando empresa não tem categorias")
        void deveRetornarCategoriasSlugsVazioQuandoEmpresaNaoTemCategorias() {
            // Arrange
            Empresa empresaSemCategoria = Empresa.builder()
                    .id(99L)
                    .slug("empresa-sem-categoria")
                    .nome("Empresa Sem Categoria")
                    .rating(0.0)
                    .totalAvaliacoes(0)
                    .totalSeguidores(0)
                    .verificado(false)
                    .destaque(false)
                    .ativo(true)
                    .categorias(List.of())
                    .build();

            when(empresaRepository.findBySlug("empresa-sem-categoria"))
                    .thenReturn(Optional.of(empresaSemCategoria));

            // Act
            EmpresaResponse response = empresaService.buscarPorSlug("empresa-sem-categoria");

            // Assert
            assertThat(response.getCategoriasSlugs()).isEmpty();
        }

        @Test
        @DisplayName("deve retornar planoNome null quando empresa não tem plano")
        void deveRetornarPlanoNomeNullQuandoEmpresaNaoTemPlano() {
            // Arrange
            Empresa empresaSemPlano = Empresa.builder()
                    .id(98L)
                    .slug("empresa-sem-plano")
                    .nome("Empresa Sem Plano")
                    .rating(0.0)
                    .totalAvaliacoes(0)
                    .totalSeguidores(0)
                    .verificado(false)
                    .destaque(false)
                    .ativo(true)
                    .plano(null)
                    .categorias(List.of())
                    .build();

            when(empresaRepository.findBySlug("empresa-sem-plano"))
                    .thenReturn(Optional.of(empresaSemPlano));

            // Act
            EmpresaResponse response = empresaService.buscarPorSlug("empresa-sem-plano");

            // Assert
            assertThat(response.getPlanoNome()).isNull();
        }

        @Test
        @DisplayName("deve mapear rating e métricas corretamente")
        void deveMapearRatingEMetricasCorretamente() {
            // Arrange
            when(empresaRepository.findBySlug("lubrificantes-bom-preco"))
                    .thenReturn(Optional.of(lubrificantesBomPreco));

            // Act
            EmpresaResponse response = empresaService.buscarPorSlug("lubrificantes-bom-preco");

            // Assert
            assertThat(response.getRating()).isEqualTo(4.8);
            assertThat(response.getTotalAvaliacoes()).isEqualTo(12);
            assertThat(response.getTotalSeguidores()).isEqualTo(5);
        }
    }
}
