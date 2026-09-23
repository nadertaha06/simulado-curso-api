package nader.curso_api.exception;

public class CursoNotFoundException extends RuntimeException {

    public CursoNotFoundException(Long id) {
        super("Curso não encontrado: " + id);
    }
}
