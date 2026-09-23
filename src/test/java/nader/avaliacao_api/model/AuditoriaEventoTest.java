package nader.avaliacao_api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AuditoriaEventoTest {

    @Test
    void gettersESetters_funcionamCorretamente() {
        AuditoriaEvento evento = new AuditoriaEvento();
        LocalDateTime agora = LocalDateTime.now();

        evento.setId(1L);
        evento.setTipoOperacao(TipoOperacao.CREATE);
        evento.setTimestamp(agora);

        assertThat(evento.getId()).isEqualTo(1L);
        assertThat(evento.getTipoOperacao()).isEqualTo(TipoOperacao.CREATE);
        assertThat(evento.getTimestamp()).isEqualTo(agora);
    }
}
