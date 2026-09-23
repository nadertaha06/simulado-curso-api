package nader.avaliacao_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nader.avaliacao_api.model.AuditoriaEvento;

public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEvento, Long> {
}

