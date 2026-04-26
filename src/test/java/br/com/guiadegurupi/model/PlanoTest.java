package br.com.guiadegurupi.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Plano — testes unitários da entidade")
class PlanoTest {

    @Test
    @DisplayName("Builder deve criar entidade com todos os campos preenchidos")
    void deveCriarPlanoComBuilder() {
        Plano plano = Plano.builder()
                .id(1L)
                .nome("PREMIUM")
                .valorMensal(new BigDecimal("299.90"))
                .maxProdutos(100)
                .apareceEmDestaque(true)
                .bannerNaHome(true)
                .relatorioVisualizacoes(true)
                .descricao("Plano completo com todos os recursos")
                .build();

        assertThat(plano.getId()).isEqualTo(1L);
        assertThat(plano.getNome()).isEqualTo("PREMIUM");
        assertThat(plano.getValorMensal()).isEqualByComparingTo("299.90");
        assertThat(plano.getMaxProdutos()).isEqualTo(100);
        assertThat(plano.getApareceEmDestaque()).isTrue();
        assertThat(plano.getBannerNaHome()).isTrue();
        assertThat(plano.getRelatorioVisualizacoes()).isTrue();
        assertThat(plano.getDescricao()).isEqualTo("Plano completo com todos os recursos");
    }

    @Test
    @DisplayName("@Builder.Default — apareceEmDestaque deve ser false quando não informado")
    void deveInicializarApareceEmDestaqueFalsePorPadrao() {
        Plano plano = Plano.builder().nome("VITRINE").valorMensal(BigDecimal.ZERO).maxProdutos(10).build();

        assertThat(plano.getApareceEmDestaque()).isFalse();
    }

    @Test
    @DisplayName("@Builder.Default — bannerNaHome deve ser false quando não informado")
    void deveInicializarBannerNaHomeFalsePorPadrao() {
        Plano plano = Plano.builder().nome("VITRINE").valorMensal(BigDecimal.ZERO).maxProdutos(10).build();

        assertThat(plano.getBannerNaHome()).isFalse();
    }

    @Test
    @DisplayName("@Builder.Default — relatorioVisualizacoes deve ser false quando não informado")
    void deveInicializarRelatorioVisualizacoesFalsePorPadrao() {
        Plano plano = Plano.builder().nome("VITRINE").valorMensal(BigDecimal.ZERO).maxProdutos(10).build();

        assertThat(plano.getRelatorioVisualizacoes()).isFalse();
    }

    @Test
    @DisplayName("NoArgsConstructor — @Builder.Default também se aplica ao construtor vazio")
    void deveAplicarDefaultsNoNoArgsConstructor() {
        Plano plano = new Plano();

        assertThat(plano.getApareceEmDestaque()).isFalse();
        assertThat(plano.getBannerNaHome()).isFalse();
        assertThat(plano.getRelatorioVisualizacoes()).isFalse();
    }

    @Test
    @DisplayName("maxProdutos = -1 deve representar produtos ilimitados")
    void deveRepresentarProdutosIlimitados() {
        Plano plano = Plano.builder()
                .nome("PREMIUM")
                .valorMensal(new BigDecimal("299.90"))
                .maxProdutos(-1)
                .build();

        assertThat(plano.getMaxProdutos()).isEqualTo(-1);
    }

    @Test
    @DisplayName("valorMensal deve preservar precisão de duas casas decimais")
    void devePreservarPrecisaoDoValorMensal() {
        Plano plano = Plano.builder()
                .nome("DESTAQUE")
                .valorMensal(new BigDecimal("149.99"))
                .maxProdutos(50)
                .build();

        // isEqualByComparingTo ignora escala (149.99 == 149.990)
        assertThat(plano.getValorMensal()).isEqualByComparingTo("149.99");
        // compareTo garante que não virou 150.00 por arredondamento
        assertThat(plano.getValorMensal().compareTo(new BigDecimal("150.00"))).isNegative();
    }

    @Test
    @DisplayName("Plano VITRINE — configuração mínima sem benefícios premium")
    void deveRepresentarPlanoVitrine() {
        Plano plano = Plano.builder()
                .nome("VITRINE")
                .valorMensal(new BigDecimal("49.90"))
                .maxProdutos(10)
                .build();

        assertThat(plano.getNome()).isEqualTo("VITRINE");
        assertThat(plano.getApareceEmDestaque()).isFalse();
        assertThat(plano.getBannerNaHome()).isFalse();
        assertThat(plano.getRelatorioVisualizacoes()).isFalse();
    }

    @Test
    @DisplayName("Setters devem alterar campos corretamente")
    void deveAlterarCamposViaSetters() {
        Plano plano = new Plano();

        plano.setNome("DESTAQUE");
        plano.setValorMensal(new BigDecimal("149.90"));
        plano.setMaxProdutos(50);
        plano.setApareceEmDestaque(true);

        assertThat(plano.getNome()).isEqualTo("DESTAQUE");
        assertThat(plano.getValorMensal()).isEqualByComparingTo("149.90");
        assertThat(plano.getMaxProdutos()).isEqualTo(50);
        assertThat(plano.getApareceEmDestaque()).isTrue();
    }

    @Test
    @DisplayName("Campos opcionais podem ser nulos sem quebrar a entidade")
    void devePersistirDescricaoComoNula() {
        Plano plano = Plano.builder()
                .nome("VITRINE")
                .valorMensal(new BigDecimal("49.90"))
                .maxProdutos(10)
                .build();

        assertThat(plano.getDescricao()).isNull();
    }
}