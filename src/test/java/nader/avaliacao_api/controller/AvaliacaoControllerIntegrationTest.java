package nader.curso_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import nader.avaliacao_api.repository.AvaliacaoRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class AvaliacaoControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @BeforeEach
    void limparBanco() {
        avaliacaoRepository.deleteAll();
    }

    @Test
    void postAvaliacoes_criaAvaliacao() throws Exception {
        String corpo = """
                {
                  "autor": "Maria",
                  "conteudo": "Excelente atendimento",
                  "nota": 5
                }
                """;

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.autor").value("Maria"))
                .andExpect(jsonPath("$.conteudo").value("Excelente atendimento"))
                .andExpect(jsonPath("$.nota").value(5))
                .andExpect(jsonPath("$.dataAvaliacao").exists());

        assertThat(avaliacaoRepository.count()).isEqualTo(1);
    }

    @Test
    void postAvaliacoes_notaForaDoIntervaloRetorna400() throws Exception {
        String corpo = """
                {
                  "autor": "Maria",
                  "conteudo": "Nota inválida",
                  "nota": 6
                }
                """;

        mockMvc.perform(post("/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isBadRequest());
    }
}
