package nader.avaliacao_api.observer;

import nader.avaliacao_api.model.Avaliacao;

public interface AvaliacaoObserver {

    void aoCriar(Avaliacao avaliacao);

    void aoExcluir(Avaliacao avaliacao);
}
