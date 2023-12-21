package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.record.CategoriesRecord;
import br.com.financeiro.financeiro.record.PlanningRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link br.com.financeiro.financeiro.domain.Categories}.
 */
public interface CategoriesService {

    /**
     * Save a planning.
     *
     * @param categories the entity to save.
     * @return the persisted entity.
     */
    Categories save(Categories categories);

    /**
     * Save a planning.
     *
     * @param categoriesRecordList the entity to save.
     * @return the persisted record.
     */
    Optional<PlanningRecord> saveCategoriesList(List<CategoriesRecord> categoriesRecordList, UUID planningId);
}
