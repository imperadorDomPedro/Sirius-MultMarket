package br.com.guiadegurupi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("SecurityConfig — testes de autorização e CORS")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Endpoints públicos ────────────────────────────────────────────────────

    @Test
    @DisplayName("/categorias/** deve ser acessível sem autenticação")
    void categoriasDeveSerPublico() throws Exception {
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk()); // 200 — controller existe e retorna lista vazia
    }

    @Test
    @DisplayName("/empresas/** deve ser acessível sem autenticação")
    void empresasDeveSerPublico() throws Exception {
        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("/produtos/** deve ser acessível sem autenticação")
    void produtosDeveSerPublico() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    // ── Endpoints protegidos ──────────────────────────────────────────────────

    @Test
    @DisplayName("Rota desconhecida sem token deve retornar 401")
    void rotaProtegidaSemTokenDeveRetornar401() throws Exception {
        mockMvc.perform(get("/admin/qualquer-coisa"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Rota /api/** sem token deve retornar 401")
    void apiSemTokenDeveRetornar401() throws Exception {
        mockMvc.perform(get("/api/privado"))
                .andExpect(status().isUnauthorized());
    }

    // ── CORS ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Preflight de origem permitida deve retornar 200 com headers CORS")
    void preflightOrigemPermitidaDeveRetornarHeadersCors() throws Exception {
        mockMvc.perform(options("/categorias")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    @DisplayName("Preflight de origem não permitida não deve retornar header Allow-Origin")
    void preflightOrigemNaoPermitidaNaoDeveRetornarHeaderCors() throws Exception {
        mockMvc.perform(options("/categorias")
                        .header("Origin", "https://site-malicioso.com")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    @DisplayName("CORS deve permitir métodos GET, POST, PUT, DELETE e OPTIONS")
    void devePermitirMetodosConfigurados() throws Exception {
        mockMvc.perform(options("/categorias")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "DELETE"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Methods",
                        org.hamcrest.Matchers.containsString("DELETE")));
    }

    @Test
    @DisplayName("CORS deve permitir credentials")
    void devePermitirCredentials() throws Exception {
        mockMvc.perform(options("/categorias")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }
}