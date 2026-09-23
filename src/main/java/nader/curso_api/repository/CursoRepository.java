package nader.curso_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import nader.curso_api.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByDeletadoFalse();

    List<Curso> findByDeletadoFalseAndNomeStartingWith(String nome);
}
