package nader.avaliacao_api.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import nader.avaliacao_api.model.Avaliacao;

@Component
public class ModeracaoObserver implements AvaliacaoObserver {

    private static final int NOTA_LIMITE_NEGATIVA = 2;

    private static final Logger log = LoggerFactory.getLogger(ModeracaoObserver.class);

    @Override
    public void aoCriar(Avaliacao avaliacao) {
        if (avaliacao.getNota() != null && avaliacao.getNota() <= NOTA_LIMITE_NEGATIVA) {
            log.warn("Avaliação negativa registrada: autor={}, nota={}", avaliacao.getAutor(), avaliacao.getNota());
        }
    }

    @Override
    public void aoExcluir(Avaliacao avaliacao) {
    }
}
