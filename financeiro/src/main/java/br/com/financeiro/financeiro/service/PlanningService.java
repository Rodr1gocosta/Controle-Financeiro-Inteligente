package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.record.PlanningRecord;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
     * Delete the "id" planning.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Save a planning.
     *
     * @param planningRecord the entity to save.
     * @return the persisted record.
     */
    PlanningRecord savePlanning(PlanningRecord planningRecord, UUID userId);

    /**
     * Get the "planning" by month and year.
     *
     * @param userId, the id of the user.
     * @param month, the month of the entity.
     * @param year, the year of the entity.
     * @return the entity.
     */
    Optional<PlanningRecord> findOnePlanningByMonthAndYear(Integer month, Integer year, UUID userId);

    /**
     * Get the "planning" by month and year.
     *
     * @param response, the response of the entity.
     * @param userId, the id of the user.
     * @param planningId, the planningId of the entity.
     */
    void downloadPlanning(HttpServletResponse response, UUID planningId, UUID userId) throws IOException;
}
