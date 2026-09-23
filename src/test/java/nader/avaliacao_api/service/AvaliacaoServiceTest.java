package nader.avaliacao_api.service;

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

import nader.avaliacao_api.dto.AvaliacaoRequestDTO;
import nader.avaliacao_api.exception.AvaliacaoNotFoundException;
import nader.avaliacao_api.model.Avaliacao;
import nader.avaliacao_api.observer.AvaliacaoObserver;
import nader.avaliacao_api.repository.AvaliacaoRepository;
import nader.avaliacao_api.service.AvaliacaoService;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @Mock
    private AvaliacaoObserver observer;

    private AvaliacaoService avaliacaoService;

    @BeforeEach
    void setUp() {
        avaliacaoService = new AvaliacaoService(avaliacaoRepository, List.of(observer));
    }

    @Test
    void listar_retornaTodasAsAvaliacoes() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor("Maria");
        when(avaliacaoRepository.findAll()).thenReturn(List.of(avaliacao));

        List<Avaliacao> resultado = avaliacaoService.listar();

        assertThat(resultado).containsExactly(avaliacao);
    }

    @Test
    void buscarPorId_existente_retornaAvaliacao() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        Avaliacao resultado = avaliacaoService.buscarPorId(1L);

        assertThat(resultado).isEqualTo(avaliacao);
    }

    @Test
    void buscarPorId_inexistente_lancaExcecao() {
        when(avaliacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avaliacaoService.buscarPorId(99L))
                .isInstanceOf(AvaliacaoNotFoundException.class);
    }

    @Test
    void criar_salvaNotificaObservadoresERetornaAvaliacao() {
        AvaliacaoRequestDTO dto = new AvaliacaoRequestDTO();
        dto.setAutor("João");
        dto.setConteudo("Ótimo serviço");
        dto.setNota(5);

        ArgumentCaptor<Avaliacao> captor = ArgumentCaptor.forClass(Avaliacao.class);
        when(avaliacaoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        Avaliacao resultado = avaliacaoService.criar(dto);

        assertThat(resultado.getAutor()).isEqualTo("João");
        assertThat(resultado.getConteudo()).isEqualTo("Ótimo serviço");
        assertThat(resultado.getNota()).isEqualTo(5);
        assertThat(resultado.getDataAvaliacao()).isNotNull();
        assertThat(captor.getValue()).isSameAs(resultado);
        verify(observer).aoCriar(resultado);
    }

    @Test
    void excluir_removeENotificaObservadores() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1L);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacao));

        avaliacaoService.excluir(1L);

        verify(avaliacaoRepository).deleteById(1L);
        verify(observer).aoExcluir(avaliacao);
    }

    @Test
    void excluir_idInexistente_lancaExcecao() {
        when(avaliacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> avaliacaoService.excluir(99L))
                .isInstanceOf(AvaliacaoNotFoundException.class);

        verify(avaliacaoRepository, never()).deleteById(any());
        verify(observer, never()).aoExcluir(any());
    }
}
