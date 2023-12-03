package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.PlanningRecord;

import java.util.Optional;
import java.util.UUID;


/**
 * Service Interface for managing {@link br.com.financeiro.financeiro.domain.Planning}.
 */
public interface PlanningService {

    /**
     * Save a planning.
     *
     * @param planning the entity to save.
     * @return the persisted entity.
     */
    Planning save(Planning planning);

    /**
     * Get the "id" planning.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanningRecord> findOne(UUID id);

    /**
     * Save a planning.
     *
     * @param planningRecord the entity to save.
     * @return the persisted record.
     */
    PlanningRecord savePlanning(PlanningRecord planningRecord, UUID userId);
}
