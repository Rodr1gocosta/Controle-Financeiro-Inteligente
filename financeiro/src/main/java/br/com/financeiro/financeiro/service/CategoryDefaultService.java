package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.domain.CategoryDefault;

import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing {@link br.com.financeiro.financeiro.domain.CategoryDefault}.
 */
public interface CategoryDefaultService {

    /**
     * Get all the categoryDefault.
     *
     * @return the list of entities.
     */
    List<CategoryDefault> findAll();

    /**
     * Save a categoryDefault.
     *
     * @param categoryDefault the entity to save.
     * @return the persisted entity.
     */
    CategoryDefault save(CategoryDefault categoryDefault);

    /**
     * Updates a categoryDefault.
     *
     * @param categoryDefault the entity to update.
     * @return the persisted entity.
     */
    CategoryDefault update(CategoryDefault categoryDefault);

    /**
     * Delete the "id" categoryDefault.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

}
