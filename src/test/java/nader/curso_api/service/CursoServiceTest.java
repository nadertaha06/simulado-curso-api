package nader.curso_api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nader.curso_api.dto.CursoRequestDTO;
import nader.curso_api.exception.CursoNotFoundException;
import nader.curso_api.model.Curso;
import nader.curso_api.repository.CursoRepository;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    private CursoService cursoService;

    @BeforeEach
    void setUp() {
        cursoService = new CursoService(cursoRepository);
    }

    @Test
    void listar_semFiltro_retornaTodosNaoDeletados() {
        Curso curso = new Curso();
        curso.setNome("Java Básico");
        when(cursoRepository.findByDeletadoFalse()).thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listar(null);

        assertThat(resultado).containsExactly(curso);
        verify(cursoRepository, never()).findByDeletadoFalseAndNomeStartingWith(any());
    }

    @Test
    void listar_comFiltroEmBranco_retornaTodosNaoDeletados() {
        when(cursoRepository.findByDeletadoFalse()).thenReturn(List.of());

        cursoService.listar("   ");

        verify(cursoRepository).findByDeletadoFalse();
        verify(cursoRepository, never()).findByDeletadoFalseAndNomeStartingWith(any());
    }

    @Test
    void listar_comFiltro_retornaApenasComPrefixo() {
        Curso curso = new Curso();
        curso.setNome("Java Avançado");
        when(cursoRepository.findByDeletadoFalseAndNomeStartingWith("Java"))
                .thenReturn(List.of(curso));

        List<Curso> resultado = cursoService.listar("Java");

        assertThat(resultado).containsExactly(curso);
        verify(cursoRepository, never()).findByDeletadoFalse();
    }

    @Test
    void criar_salvaERetornaCurso() {
        CursoRequestDTO dto = new CursoRequestDTO();
        dto.setNome("Spring Boot");
        dto.setDescricao("Curso de Spring Boot");
        dto.setCargaHoraria(40);

        ArgumentCaptor<Curso> captor = ArgumentCaptor.forClass(Curso.class);
        when(cursoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        Curso resultado = cursoService.criar(dto);

        assertThat(resultado.getNome()).isEqualTo("Spring Boot");
        assertThat(resultado.getDescricao()).isEqualTo("Curso de Spring Boot");
        assertThat(resultado.getCargaHoraria()).isEqualTo(40);
        assertThat(captor.getValue().isDeletado()).isFalse();
    }

    @Test
    void deletar_marcaComoDeletado() {
        Curso curso = new Curso();
        curso.setId(1L);
        curso.setNome("Java Básico");
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenAnswer(invocation -> invocation.getArgument(0));

        cursoService.deletar(1L);

        assertThat(curso.isDeletado()).isTrue();
        verify(cursoRepository).save(curso);
    }

    @Test
    void deletar_idInexistente_lancaExcecao() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.deletar(99L))
                .isInstanceOf(CursoNotFoundException.class);

        verify(cursoRepository, never()).save(any());
    }

    @Test
    void deletar_jaDeletado_lancaExcecao() {
        Curso curso = new Curso();
        curso.setId(2L);
        curso.setDeletado(true);
        when(cursoRepository.findById(2L)).thenReturn(Optional.of(curso));

        assertThatThrownBy(() -> cursoService.deletar(2L))
                .isInstanceOf(CursoNotFoundException.class);

        verify(cursoRepository, never()).save(any());
    }
}
