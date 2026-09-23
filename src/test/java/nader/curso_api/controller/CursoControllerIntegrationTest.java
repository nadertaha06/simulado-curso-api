package nader.curso_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import nader.curso_api.model.Curso;
import nader.curso_api.repository.CursoRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class CursoControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CursoRepository cursoRepository;

    @BeforeEach
    void limparBanco() {
        cursoRepository.deleteAll();
    }

    @Test
    void getCursos_naoRetornaDeletados() throws Exception {
        Curso ativo = new Curso();
        ativo.setNome("Java Básico");
        ativo.setCargaHoraria(20);
        cursoRepository.save(ativo);

        Curso deletado = new Curso();
        deletado.setNome("Java Avançado");
        deletado.setCargaHoraria(30);
        deletado.setDeletado(true);
        cursoRepository.save(deletado);

        mockMvc.perform(get("/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Java Básico"));
    }

    @Test
    void getCursos_filtraPorPrefixoDoNome() throws Exception {
        Curso java = new Curso();
        java.setNome("Java Básico");
        java.setCargaHoraria(20);
        cursoRepository.save(java);

        Curso python = new Curso();
        python.setNome("Python Básico");
        python.setCargaHoraria(20);
        cursoRepository.save(python);

        mockMvc.perform(get("/cursos").param("nome", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Java Básico"));
    }

    @Test
    void postCursos_criaCurso() throws Exception {
        String corpo = """
                {
                  "nome": "Spring Boot",
                  "descricao": "Curso de Spring Boot",
                  "cargaHoraria": 40
                }
                """;

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Spring Boot"))
                .andExpect(jsonPath("$.deletado").value(false));

        assertThat(cursoRepository.findByDeletadoFalse()).hasSize(1);
    }

    @Test
    void postCursos_semNomeRetorna400() throws Exception {
        String corpo = """
                {
                  "cargaHoraria": 40
                }
                """;

        mockMvc.perform(post("/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(corpo))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteCursos_marcaComoDeletadoLogicamente() throws Exception {
        Curso curso = new Curso();
        curso.setNome("Java Básico");
        curso.setCargaHoraria(20);
        curso = cursoRepository.save(curso);

        mockMvc.perform(delete("/cursos/" + curso.getId()))
                .andExpect(status().isNoContent());

        Curso atualizado = cursoRepository.findById(curso.getId()).orElseThrow();
        assertThat(atualizado.isDeletado()).isTrue();
        assertThat(cursoRepository.count()).isEqualTo(1);
    }

    @Test
    void deleteCursos_idInexistenteRetorna404() throws Exception {
        mockMvc.perform(delete("/cursos/999999"))
                .andExpect(status().isNotFound());
    }
}
