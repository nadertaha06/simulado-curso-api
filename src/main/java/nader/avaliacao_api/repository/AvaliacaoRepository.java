package nader.avaliacao_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nader.avaliacao_api.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
}
