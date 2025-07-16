package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.ContractDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Contract}.
 */
public interface ContractService {
    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save.
     * @return the persisted entity.
     */
    ContractDTO save(ContractDTO contractDTO);

    /**
     * Updates a contract.
     *
     * @param contractDTO the entity to update.
     * @return the persisted entity.
     */
    ContractDTO update(ContractDTO contractDTO);

    /**
     * Partially updates a contract.
     *
     * @param contractDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContractDTO> partialUpdate(ContractDTO contractDTO);

    /**
     * Get the "id" contract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContractDTO> findOne(UUID id);

    /**
     * Delete the "id" contract.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the contract corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContractDTO> search(String query, Pageable pageable);

    double getTotalUpfrontPremium();
}
