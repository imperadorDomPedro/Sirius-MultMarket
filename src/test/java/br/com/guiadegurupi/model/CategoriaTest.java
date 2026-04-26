package br.com.guiadegurupi.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Categoria — testes unitários da entidade")
class CategoriaTest {

    @Test
    @DisplayName("Builder deve criar entidade com todos os campos preenchidos")
    void deveCriarCategoriaComBuilder() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .slug("produtos-cerrado")
                .nome("Produtos do Cerrado")
                .descricao("Artigos nativos do cerrado tocantinense")
                .icone("Leaf")
                .cor("text-green-600")
                .bgCor("bg-green-100")
                .urlImagem("https://cdn.gurupi.com/cerrado.jpg")
                .ativo(true)
                .build();

        assertThat(categoria.getId()).isEqualTo(1L);
        assertThat(categoria.getSlug()).isEqualTo("produtos-cerrado");
        assertThat(categoria.getNome()).isEqualTo("Produtos do Cerrado");
        assertThat(categoria.getDescricao()).isEqualTo("Artigos nativos do cerrado tocantinense");
        assertThat(categoria.getIcone()).isEqualTo("Leaf");
        assertThat(categoria.getCor()).isEqualTo("text-green-600");
        assertThat(categoria.getBgCor()).isEqualTo("bg-green-100");
        assertThat(categoria.getUrlImagem()).isEqualTo("https://cdn.gurupi.com/cerrado.jpg");
        assertThat(categoria.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("@Builder.Default deve inicializar ativo como true quando não informado")
    void deveInicializarAtivoComTruePorPadrao() {
        Categoria categoria = Categoria.builder()
                .slug("servicos")
                .nome("Serviços")
                .build();

        assertThat(categoria.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("NoArgsConstructor deve criar instância com todos os campos nulos exceto primitivos")
    void deveCriarInstanciaVaziaComNoArgsConstructor() {
        Categoria categoria = new Categoria();

        assertThat(categoria.getId()).isNull();
        assertThat(categoria.getSlug()).isNull();
        assertThat(categoria.getNome()).isNull();
        assertThat(categoria.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("AllArgsConstructor deve preencher todos os campos na ordem correta")
    void deveCriarCategoriaComAllArgsConstructor() {
        Categoria categoria = new Categoria(
                2L,
                "veiculos",
                "Veículos",
                "Carros e motos",
                "Car",
                "text-blue-600",
                "bg-blue-100",
                "https://cdn.gurupi.com/veiculos.jpg",
                true
        );

        assertThat(categoria.getId()).isEqualTo(2L);
        assertThat(categoria.getSlug()).isEqualTo("veiculos");
        assertThat(categoria.getNome()).isEqualTo("Veículos");
        assertThat(categoria.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Setters devem alterar os campos corretamente")
    void deveAlterarCamposViaSetters() {
        Categoria categoria = new Categoria();

        categoria.setSlug("ferramentas");
        categoria.setNome("Ferramentas");
        categoria.setIcone("Wrench");
        categoria.setAtivo(false);

        assertThat(categoria.getSlug()).isEqualTo("ferramentas");
        assertThat(categoria.getNome()).isEqualTo("Ferramentas");
        assertThat(categoria.getIcone()).isEqualTo("Wrench");
        assertThat(categoria.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Builder com ativo=false deve respeitar o valor explícito")
    void devePermitirDesativarCategoriaNoBuilder() {
        Categoria categoria = Categoria.builder()
                .slug("inativa")
                .nome("Categoria Inativa")
                .ativo(false)
                .build();

        assertThat(categoria.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Campos opcionais podem ser nulos sem quebrar a entidade")
    void devePersistirCamposOpcionaisComoNulo() {
        Categoria categoria = Categoria.builder()
                .slug("minimalista")
                .nome("Categoria Mínima")
                .build();

        assertThat(categoria.getDescricao()).isNull();
        assertThat(categoria.getIcone()).isNull();
        assertThat(categoria.getCor()).isNull();
        assertThat(categoria.getBgCor()).isNull();
        assertThat(categoria.getUrlImagem()).isNull();
    }
}