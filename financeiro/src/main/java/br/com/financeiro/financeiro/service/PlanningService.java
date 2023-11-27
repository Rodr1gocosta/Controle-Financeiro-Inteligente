package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.PlanningRecord;


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
     * Save a planning.
     *
     * @param planningRecord the entity to save.
     * @return the persisted record.
     */
    PlanningRecord savePlanning(PlanningRecord planningRecord);
}
