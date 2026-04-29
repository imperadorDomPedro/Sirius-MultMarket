package br.com.guiadegurupi.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Estratégia: usamos um @RestController interno (FakeController) que lança
 * exceções propositalmente — isso permite testar o GlobalExceptionHandler
 * de forma isolada, sem depender de nenhum service ou banco de dados.
 */
@WebMvcTest(
        controllers = GlobalExceptionHandlerTest.FakeController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@ActiveProfiles("test")
@DisplayName("GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Controller falso para provocar as exceções ─────────────────────────

    @RestController
    static class FakeController {

        @GetMapping("/test/runtime-exception")
        public void lancarRuntimeException() {
            throw new RuntimeException("Recurso não encontrado no sistema");
        }

        @GetMapping("/test/resource-not-found")
        public void lancarResourceNotFoundException() {
            throw new ResourceNotFoundException("Categoria não encontrada para o slug: cerrado-xpto");
        }

        @GetMapping("/test/generic-exception")
        public void lancarExcecaoGenerica() throws Exception {
            throw new Exception("Erro inesperado no sistema");
        }

        @GetMapping("/test/null-pointer")
        public void lancarNullPointer() {
            throw new NullPointerException("Campo nulo inesperado");
        }

        @GetMapping("/test/sem-erro")
        public String semErro() {
            return "ok";
        }
    }

    // =========================================================================
    // RuntimeException → 404
    // =========================================================================

    @Nested
    @DisplayName("handleNotFound — RuntimeException → 404")
    class HandleNotFound {

        @Test
        @DisplayName("deve retornar 404 quando RuntimeException for lançada")
        void deveRetornar404QuandoRuntimeExceptionForLancada() throws Exception {

            mockMvc.perform(get("/test/runtime-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.erro", is("Recurso não encontrado no sistema")))
                    .andExpect(jsonPath("$.timestamp", not(emptyOrNullString())));
        }

        @Test
        @DisplayName("deve retornar 404 quando ResourceNotFoundException for lançada")
        void deveRetornar404QuandoResourceNotFoundExceptionForLancada() throws Exception {

            mockMvc.perform(get("/test/resource-not-found")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.erro",
                            is("Categoria não encontrada para o slug: cerrado-xpto")));
        }

        @Test
        @DisplayName("deve retornar 404 quando NullPointerException for lançada")
        void deveRetornar404QuandoNullPointerExceptionForLancada() throws Exception {

            mockMvc.perform(get("/test/null-pointer")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status", is(404)))
                    .andExpect(jsonPath("$.erro", is("Campo nulo inesperado")));
        }

        @Test
        @DisplayName("deve conter timestamp no formato ISO no corpo da resposta 404")
        void deveConterTimestampNoFormatoISO() throws Exception {

            mockMvc.perform(get("/test/runtime-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    // Timestamp deve ser uma string ISO (ex: 2026-04-29T01:23:45.123)
                    .andExpect(jsonPath("$.timestamp",
                            matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")));
        }

        @Test
        @DisplayName("deve retornar os três campos obrigatórios: timestamp, status e erro")
        void deveRetornarTresCamposObrigatorios() throws Exception {

            mockMvc.perform(get("/test/runtime-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.erro").exists());
        }
    }

    // =========================================================================
    // Exception → 500
    // =========================================================================

    @Nested
    @DisplayName("handleGeneric — Exception → 500")
    class HandleGeneric {

        @Test
        @DisplayName("deve retornar 500 quando Exception genérica for lançada")
        void deveRetornar500QuandoExcecaoGenericaForLancada() throws Exception {

            mockMvc.perform(get("/test/generic-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.status", is(500)))
                    .andExpect(jsonPath("$.erro", is("Erro interno. Contate o suporte.")))
                    .andExpect(jsonPath("$.timestamp", not(emptyOrNullString())));
        }

        @Test
        @DisplayName("deve retornar mensagem genérica no 500 — não expõe detalhes internos")
        void deveRetornarMensagemGenericaNo500() throws Exception {

            mockMvc.perform(get("/test/generic-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    // Garante que a mensagem real da exceção NÃO é exposta — boas práticas de segurança
                    .andExpect(jsonPath("$.erro",
                            not(containsString("Erro inesperado no sistema"))))
                    .andExpect(jsonPath("$.erro", is("Erro interno. Contate o suporte.")));
        }

        @Test
        @DisplayName("deve conter os três campos obrigatórios no erro 500")
        void deveConterTresCamposObrigatoriosNo500() throws Exception {

            mockMvc.perform(get("/test/generic-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.erro").exists());
        }

        @Test
        @DisplayName("deve conter timestamp no formato ISO no corpo da resposta 500")
        void deveConterTimestampISO500() throws Exception {

            mockMvc.perform(get("/test/generic-exception")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.timestamp",
                            matchesPattern("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")));
        }
    }

    // =========================================================================
    // Caminho feliz — sem exceção
    // =========================================================================

    @Nested
    @DisplayName("Caminho feliz — sem exceção")
    class CaminhoFeliz {

        @Test
        @DisplayName("deve retornar 200 quando nenhuma exceção for lançada")
        void deveRetornar200QuandoNenhumaExcecaoForLancada() throws Exception {

            mockMvc.perform(get("/test/sem-erro")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}
