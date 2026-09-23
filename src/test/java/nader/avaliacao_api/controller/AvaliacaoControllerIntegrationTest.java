package nader.avaliacao_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

import nader.avaliacao_api.model.Avaliacao;
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

    @Test
    void getAvaliacoes_listaTodasAsAvaliacoes() throws Exception {
        avaliacaoRepository.save(criarAvaliacao("Maria", "Bom atendimento", 4));

        mockMvc.perform(get("/avaliacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].autor").value("Maria"));
    }

    @Test
    void getAvaliacaoPorId_existente_retornaAvaliacao() throws Exception {
        Avaliacao salva = avaliacaoRepository.save(criarAvaliacao("João", "Ótimo serviço", 5));

        mockMvc.perform(get("/avaliacoes/{id}", salva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.autor").value("João"));
    }

    @Test
    void getAvaliacaoPorId_inexistente_retorna404() throws Exception {
        mockMvc.perform(get("/avaliacoes/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void deleteAvaliacao_existente_remove() throws Exception {
        Avaliacao salva = avaliacaoRepository.save(criarAvaliacao("Ana", "Atendimento regular", 2));

        mockMvc.perform(delete("/avaliacoes/{id}", salva.getId()))
                .andExpect(status().isNoContent());

        assertThat(avaliacaoRepository.findById(salva.getId())).isEmpty();
    }

    @Test
    void deleteAvaliacao_inexistente_retorna404() throws Exception {
        mockMvc.perform(delete("/avaliacoes/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    private Avaliacao criarAvaliacao(String autor, String conteudo, Integer nota) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor(autor);
        avaliacao.setConteudo(conteudo);
        avaliacao.setNota(nota);
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        return avaliacao;
    }
}
