package nader.avaliacao_api.observer;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;

import nader.avaliacao_api.model.Avaliacao;

class ModeracaoObserverTest {

    private final ModeracaoObserver observer = new ModeracaoObserver();

    @Test
    void aoCriar_notaBaixa_naoLancaExcecao() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor("Maria");
        avaliacao.setNota(1);

        assertThatCode(() -> observer.aoCriar(avaliacao)).doesNotThrowAnyException();
    }

    @Test
    void aoCriar_notaAlta_naoLancaExcecao() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor("João");
        avaliacao.setNota(5);

        assertThatCode(() -> observer.aoCriar(avaliacao)).doesNotThrowAnyException();
    }

    @Test
    void aoCriar_notaNula_naoLancaExcecao() {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor("Pedro");

        assertThatCode(() -> observer.aoCriar(avaliacao)).doesNotThrowAnyException();
    }

    @Test
    void aoExcluir_naoLancaExcecao() {
        Avaliacao avaliacao = new Avaliacao();

        assertThatCode(() -> observer.aoExcluir(avaliacao)).doesNotThrowAnyException();
    }
}
