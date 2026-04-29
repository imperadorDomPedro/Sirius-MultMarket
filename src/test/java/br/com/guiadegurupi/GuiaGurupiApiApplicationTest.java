package br.com.guiadegurupi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Teste de sanidade do contexto Spring Boot.
 *
 * Objetivo: garantir que o ApplicationContext sobe corretamente —
 * ou seja, que não há beans mal configurados, dependências circulares,
 * propriedades faltando ou erros de configuração que impeçam a inicialização.
 *
 * Usa perfil "test" com H2 em memória para não depender do PostgreSQL.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("GuiaGurupiApiApplication")
class GuiaGurupiApiApplicationTest {

    @Test
    @DisplayName("deve carregar o contexto Spring Boot sem erros")
    void contextLoads() {
        // Se o contexto falhar ao subir, o teste falha automaticamente.
        // Não precisa de nenhuma asserção explícita — o próprio @SpringBootTest
        // garante que o contexto foi inicializado com sucesso.
    }

    @Test
    @DisplayName("deve executar o método main sem lançar exceção")
    void mainDeveFuncionarSemExcecao() {
        // Garante que o método main é invocável — cobre a linha do SpringApplication.run
        // que o JaCoCo aponta como não coberta quando só usamos contextLoads().
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                GuiaGurupiApiApplication.main(new String[]{
                        "--spring.main.web-application-type=none"
                })
        );
    }
}
