package br.com.guiadegurupi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Empresa — testes unitários da entidade")
class EmpresaTest {

    @Test
    @DisplayName("Builder deve criar entidade com todos os campos preenchidos")
    void deveCriarEmpresaComBuilder() {
        Empresa empresa = Empresa.builder()
                .id(1L)
                .slug("distribuidora-martins")
                .nome("Distribuidora Martins")
                .tagline("O melhor preço de Gurupi")
                .descricao("Distribuidora com 20 anos de mercado")
                .urlLogo("https://cdn.gurupi.com/logo.jpg")
                .urlBanner("https://cdn.gurupi.com/banner.jpg")
                .nomeResponsavel("João Martins")
                .whatsapp("63999990000")
                .localizacao("Av. Pará, 1234 - Gurupi, TO")
                .tempoResposta("< 1 hora")
                .politicaEntrega("Entrega em até 2 dias úteis")
                .politicaDevolucao("Devolução em até 7 dias")
                .rating(4.8)
                .totalAvaliacoes(120)
                .totalSeguidores(350)
                .verificado(true)
                .destaque(true)
                .ativo(true)
                .build();

        assertThat(empresa.getId()).isEqualTo(1L);
        assertThat(empresa.getSlug()).isEqualTo("distribuidora-martins");
        assertThat(empresa.getNome()).isEqualTo("Distribuidora Martins");
        assertThat(empresa.getTagline()).isEqualTo("O melhor preço de Gurupi");
        assertThat(empresa.getDescricao()).isEqualTo("Distribuidora com 20 anos de mercado");
        assertThat(empresa.getUrlLogo()).isEqualTo("https://cdn.gurupi.com/logo.jpg");
        assertThat(empresa.getUrlBanner()).isEqualTo("https://cdn.gurupi.com/banner.jpg");
        assertThat(empresa.getNomeResponsavel()).isEqualTo("João Martins");
        assertThat(empresa.getWhatsapp()).isEqualTo("63999990000");
        assertThat(empresa.getLocalizacao()).isEqualTo("Av. Pará, 1234 - Gurupi, TO");
        assertThat(empresa.getTempoResposta()).isEqualTo("< 1 hora");
        assertThat(empresa.getPoliticaEntrega()).isEqualTo("Entrega em até 2 dias úteis");
        assertThat(empresa.getPoliticaDevolucao()).isEqualTo("Devolução em até 7 dias");
        assertThat(empresa.getRating()).isEqualTo(4.8);
        assertThat(empresa.getTotalAvaliacoes()).isEqualTo(120);
        assertThat(empresa.getTotalSeguidores()).isEqualTo(350);
        assertThat(empresa.getVerificado()).isTrue();
        assertThat(empresa.getDestaque()).isTrue();
        assertThat(empresa.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("@Builder.Default — rating deve ser 0.0 quando não informado")
    void deveInicializarRatingZeroPorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getRating()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("@Builder.Default — totalAvaliacoes deve ser 0 quando não informado")
    void deveInicializarTotalAvaliacoesZeroPorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getTotalAvaliacoes()).isEqualTo(0);
    }

    @Test
    @DisplayName("@Builder.Default — totalSeguidores deve ser 0 quando não informado")
    void deveInicializarTotalSeguidoresZeroPorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getTotalSeguidores()).isEqualTo(0);
    }

    @Test
    @DisplayName("@Builder.Default — verificado deve ser false quando não informado")
    void deveInicializarVerificadoFalsePorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getVerificado()).isFalse();
    }

    @Test
    @DisplayName("@Builder.Default — destaque deve ser false quando não informado")
    void deveInicializarDestaqueFalsePorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getDestaque()).isFalse();
    }

    @Test
    @DisplayName("@Builder.Default — ativo deve ser true quando não informado")
    void deveInicializarAtivoTruePorPadrao() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("@Builder.Default — categorias deve ser lista vazia quando não informado")
    void deveInicializarCategoriasComoListaVazia() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getCategorias()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("@Builder.Default — produtos deve ser lista vazia quando não informado")
    void deveInicializarProdutosComoListaVazia() {
        Empresa empresa = Empresa.builder().slug("teste").nome("Teste").build();

        assertThat(empresa.getProdutos()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("NoArgsConstructor — @Builder.Default também se aplica ao construtor vazio")
    void deveAplicarDefaultsNoNoArgsConstructor() {
        Empresa empresa = new Empresa();

        // Mesmo comportamento documentado em CategoriaTest:
        // @Builder.Default vaza para o NoArgsConstructor no Lombok
        assertThat(empresa.getRating()).isEqualTo(0.0);
        assertThat(empresa.getTotalAvaliacoes()).isEqualTo(0);
        assertThat(empresa.getTotalSeguidores()).isEqualTo(0);
        assertThat(empresa.getVerificado()).isFalse();
        assertThat(empresa.getDestaque()).isFalse();
        assertThat(empresa.getAtivo()).isTrue();
        assertThat(empresa.getCategorias()).isNotNull().isEmpty();
        assertThat(empresa.getProdutos()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Setters devem alterar campos corretamente")
    void deveAlterarCamposViaSetters() {
        Empresa empresa = new Empresa();

        empresa.setSlug("novo-slug");
        empresa.setNome("Novo Nome");
        empresa.setRating(3.5);
        empresa.setVerificado(true);
        empresa.setAtivo(false);

        assertThat(empresa.getSlug()).isEqualTo("novo-slug");
        assertThat(empresa.getNome()).isEqualTo("Novo Nome");
        assertThat(empresa.getRating()).isEqualTo(3.5);
        assertThat(empresa.getVerificado()).isTrue();
        assertThat(empresa.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Campos opcionais podem ser nulos sem quebrar a entidade")
    void devePersistirCamposOpcionaisComoNulo() {
        Empresa empresa = Empresa.builder()
                .slug("minimalista")
                .nome("Empresa Mínima")
                .build();

        assertThat(empresa.getTagline()).isNull();
        assertThat(empresa.getDescricao()).isNull();
        assertThat(empresa.getUrlLogo()).isNull();
        assertThat(empresa.getUrlBanner()).isNull();
        assertThat(empresa.getNomeResponsavel()).isNull();
        assertThat(empresa.getWhatsapp()).isNull();
        assertThat(empresa.getLocalizacao()).isNull();
        assertThat(empresa.getTempoResposta()).isNull();
        assertThat(empresa.getPoliticaEntrega()).isNull();
        assertThat(empresa.getPoliticaDevolucao()).isNull();
        assertThat(empresa.getPlano()).isNull();
        assertThat(empresa.getCriadoEm()).isNull();
        assertThat(empresa.getAtualizadoEm()).isNull();
    }

    @Test
    @DisplayName("Builder deve permitir adicionar categorias à lista")
    void deveAdicionarCategoriasNaLista() {
        Categoria categoria = Categoria.builder()
                .slug("eletronicos")
                .nome("Eletrônicos")
                .build();

        Empresa empresa = Empresa.builder()
                .slug("tech-gurupi")
                .nome("Tech Gurupi")
                .categorias(List.of(categoria))
                .build();

        assertThat(empresa.getCategorias()).hasSize(1);
        assertThat(empresa.getCategorias().get(0).getSlug()).isEqualTo("eletronicos");
    }

    @Test
    @DisplayName("Empresa inativa com destaque false representa estado de suspensão")
    void deveRepresentarEmpresaSuspensa() {
        Empresa empresa = Empresa.builder()
                .slug("suspensa")
                .nome("Empresa Suspensa")
                .ativo(false)
                .destaque(false)
                .verificado(false)
                .build();

        assertThat(empresa.getAtivo()).isFalse();
        assertThat(empresa.getDestaque()).isFalse();
        assertThat(empresa.getVerificado()).isFalse();
    }
}