package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.InsurancePackDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.InsurancePack}.
 */
public interface InsurancePackService {
    /**
     * Save a insurancePack.
     *
     * @param insurancePackDTO the entity to save.
     * @return the persisted entity.
     */
    InsurancePackDTO save(InsurancePackDTO insurancePackDTO);

    /**
     * Updates a insurancePack.
     *
     * @param insurancePackDTO the entity to update.
     * @return the persisted entity.
     */
    InsurancePackDTO update(InsurancePackDTO insurancePackDTO);

    /**
     * Partially updates a insurancePack.
     *
     * @param insurancePackDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InsurancePackDTO> partialUpdate(InsurancePackDTO insurancePackDTO);

    /**
     * Get all the insurancePacks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsurancePackDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" insurancePack.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InsurancePackDTO> findOne(UUID id);

    /**
     * Delete the "id" insurancePack.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the insurancePack corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InsurancePackDTO> search(String query, Pageable pageable);
}
