package nader.avaliacao_api.observer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import nader.avaliacao_api.model.AuditoriaEvento;
import nader.avaliacao_api.model.Avaliacao;
import nader.avaliacao_api.model.TipoOperacao;
import nader.avaliacao_api.repository.AuditoriaEventoRepository;

@Component
public class AuditoriaObserver implements AvaliacaoObserver {

    private final AuditoriaEventoRepository auditoriaEventoRepository;

    public AuditoriaObserver(AuditoriaEventoRepository auditoriaEventoRepository) {
        this.auditoriaEventoRepository = auditoriaEventoRepository;
    }

    @Override
    public void aoCriar(Avaliacao avaliacao) {
        registrar(TipoOperacao.CREATE);
    }

    @Override
    public void aoExcluir(Avaliacao avaliacao) {
        registrar(TipoOperacao.DELETE);
    }

    private void registrar(TipoOperacao tipo) {
        AuditoriaEvento evento = new AuditoriaEvento();
        evento.setTipoOperacao(tipo);
        evento.setTimestamp(LocalDateTime.now());
        auditoriaEventoRepository.save(evento);
    }
}
