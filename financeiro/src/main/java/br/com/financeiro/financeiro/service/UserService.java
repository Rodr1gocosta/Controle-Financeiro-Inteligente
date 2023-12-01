package br.com.financeiro.financeiro.service;

import br.com.financeiro.financeiro.record.UserRecord;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link br.com.financeiro.financeiro.domain.User}.
 */
public interface UserService {

    /**
     * Save a planning.
     *
     * @param userRecord the entity to save.
     * @return the persisted entity.
     */
    UserRecord save(UserRecord userRecord);

    /**
     * Updates a pessoa.
     *
     * @param userRecord the entity to update.
     * @return the persisted entity.
     */
    UserRecord update(UserRecord userRecord);

    /**
     * Get the "id" user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserRecord> findOne(UUID id);

    /**
     * Delete the "id" user.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

}
