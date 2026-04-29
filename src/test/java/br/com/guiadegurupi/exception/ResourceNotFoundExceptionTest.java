package br.com.guiadegurupi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes puramente unitários — sem MockMvc, sem Spring context.
 * ResourceNotFoundException só tem lógica de construção de mensagem,
 * herança e anotação — JUnit puro é suficiente e muito mais rápido.
 */
@DisplayName("ResourceNotFoundException")
class ResourceNotFoundExceptionTest {

    // =========================================================================
    // Construtor com mensagem livre
    // =========================================================================

    @Nested
    @DisplayName("Construtor ResourceNotFoundException(String message)")
    class ConstrutorComMensagemLivre {

        @Test
        @DisplayName("deve armazenar a mensagem informada")
        void deveArmazenarAMensagemInformada() {
            // Arrange + Act
            var ex = new ResourceNotFoundException("Categoria não encontrada para o slug: cerrado");

            // Assert
            assertThat(ex.getMessage())
                    .isEqualTo("Categoria não encontrada para o slug: cerrado");
        }

        @Test
        @DisplayName("deve ser instância de RuntimeException")
        void deveSerInstanciaDeRuntimeException() {
            var ex = new ResourceNotFoundException("qualquer mensagem");

            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("deve aceitar mensagem com caracteres especiais e acentos")
        void deveAceitarMensagemComAcentos() {
            var mensagem = "Empresa não encontrada: ação inválida no Gurupi-TO";
            var ex = new ResourceNotFoundException(mensagem);

            assertThat(ex.getMessage()).isEqualTo(mensagem);
        }

        @Test
        @DisplayName("deve ser lançável e capturável como RuntimeException")
        void deveSerLancavelComoRuntimeException() {
            assertThatThrownBy(() -> {
                throw new ResourceNotFoundException("Produto não encontrado com id: 999");
            })
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Produto não encontrado com id: 999");
        }

        @Test
        @DisplayName("deve ser lançável e capturável como ResourceNotFoundException")
        void deveSerLancavelComoResourceNotFoundException() {
            assertThatThrownBy(() -> {
                throw new ResourceNotFoundException("Slug inexistente");
            })
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Slug inexistente");
        }
    }

    // =========================================================================
    // Construtor padronizado (resource, field, value)
    // =========================================================================

    @Nested
    @DisplayName("Construtor ResourceNotFoundException(String resource, String field, Object value)")
    class ConstrutorPadronizado {

        @Test
        @DisplayName("deve formatar a mensagem no padrão esperado")
        void deveFormatarAMensagemNoPadraoEsperado() {
            var ex = new ResourceNotFoundException("Categoria", "slug", "produtos-cerrado");

            assertThat(ex.getMessage())
                    .isEqualTo("Categoria não encontrado(a) com slug: 'produtos-cerrado'");
        }

        @Test
        @DisplayName("deve funcionar com id Long como value")
        void deveFuncionarComIdLongComoValue() {
            var ex = new ResourceNotFoundException("Empresa", "id", 42L);

            assertThat(ex.getMessage())
                    .isEqualTo("Empresa não encontrado(a) com id: '42'");
        }

        @Test
        @DisplayName("deve funcionar com id Integer como value")
        void deveFuncionarComIdIntegerComoValue() {
            var ex = new ResourceNotFoundException("Produto", "id", 7);

            assertThat(ex.getMessage())
                    .isEqualTo("Produto não encontrado(a) com id: '7'");
        }

        @Test
        @DisplayName("deve funcionar com String como value")
        void deveFuncionarComStringComoValue() {
            var ex = new ResourceNotFoundException("Empresa", "slug", "distribuidora-martins");

            assertThat(ex.getMessage())
                    .isEqualTo("Empresa não encontrado(a) com slug: 'distribuidora-martins'");
        }

        @Test
        @DisplayName("deve conter o nome do recurso na mensagem formatada")
        void deveConterNomeDoRecursoNaMensagem() {
            var ex = new ResourceNotFoundException("Plano", "nome", "PREMIUM");

            assertThat(ex.getMessage()).contains("Plano");
        }

        @Test
        @DisplayName("deve conter o nome do campo na mensagem formatada")
        void deveConterNomeDoCampoNaMensagem() {
            var ex = new ResourceNotFoundException("Plano", "nome", "PREMIUM");

            assertThat(ex.getMessage()).contains("nome");
        }

        @Test
        @DisplayName("deve conter o valor entre aspas simples na mensagem formatada")
        void deveConterValorEntreAspasSimplesNaMensagem() {
            var ex = new ResourceNotFoundException("Plano", "nome", "PREMIUM");

            assertThat(ex.getMessage()).contains("'PREMIUM'");
        }

        @Test
        @DisplayName("deve ser lançável e capturável com mensagem formatada correta")
        void deveSerLancavelComMensagemFormatada() {
            assertThatThrownBy(() -> {
                throw new ResourceNotFoundException("Categoria", "slug", "slug-errado");
            })
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessage("Categoria não encontrado(a) com slug: 'slug-errado'");
        }
    }

    // =========================================================================
    // Anotação @ResponseStatus
    // =========================================================================

    @Nested
    @DisplayName("@ResponseStatus")
    class ResponseStatusAnnotation {

        @Test
        @DisplayName("deve ter @ResponseStatus anotado na classe")
        void deveTerResponseStatusAnotado() {
            var annotation = ResourceNotFoundException.class
                    .getAnnotation(ResponseStatus.class);

            assertThat(annotation).isNotNull();
        }

        @Test
        @DisplayName("deve ter @ResponseStatus com valor NOT_FOUND (404)")
        void deveTerResponseStatusComValorNotFound() {
            var annotation = ResourceNotFoundException.class
                    .getAnnotation(ResponseStatus.class);

            assertThat(annotation.value()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }
}
