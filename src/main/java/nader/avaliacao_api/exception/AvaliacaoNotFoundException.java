package nader.avaliacao_api.exception;

public class AvaliacaoNotFoundException extends RuntimeException {

    public AvaliacaoNotFoundException(Long id) {
        super("Avaliacoes não encontrada: " + id);
    }
}
