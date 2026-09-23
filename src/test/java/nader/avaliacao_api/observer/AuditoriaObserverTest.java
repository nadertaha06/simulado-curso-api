package nader.avaliacao_api.observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nader.avaliacao_api.model.AuditoriaEvento;
import nader.avaliacao_api.model.Avaliacao;
import nader.avaliacao_api.model.TipoOperacao;
import nader.avaliacao_api.repository.AuditoriaEventoRepository;

@ExtendWith(MockitoExtension.class)
class AuditoriaObserverTest {

    @Mock
    private AuditoriaEventoRepository auditoriaEventoRepository;

    private AuditoriaObserver auditoriaObserver;

    @BeforeEach
    void setUp() {
        auditoriaObserver = new AuditoriaObserver(auditoriaEventoRepository);
    }

    @Test
    void aoCriar_registraEventoDeCriacao() {
        ArgumentCaptor<AuditoriaEvento> captor = ArgumentCaptor.forClass(AuditoriaEvento.class);
        when(auditoriaEventoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        auditoriaObserver.aoCriar(new Avaliacao());

        assertThat(captor.getValue().getTipoOperacao()).isEqualTo(TipoOperacao.CREATE);
        assertThat(captor.getValue().getTimestamp()).isNotNull();
    }

    @Test
    void aoExcluir_registraEventoDeExclusao() {
        ArgumentCaptor<AuditoriaEvento> captor = ArgumentCaptor.forClass(AuditoriaEvento.class);
        when(auditoriaEventoRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        auditoriaObserver.aoExcluir(new Avaliacao());

        assertThat(captor.getValue().getTipoOperacao()).isEqualTo(TipoOperacao.DELETE);
        assertThat(captor.getValue().getTimestamp()).isNotNull();
    }
}
