package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.ClaimDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Claim}.
 */
public interface ClaimService {
    /**
     * Save a claim.
     *
     * @param claimDTO the entity to save.
     * @return the persisted entity.
     */
    ClaimDTO save(ClaimDTO claimDTO);

    /**
     * Updates a claim.
     *
     * @param claimDTO the entity to update.
     * @return the persisted entity.
     */
    ClaimDTO update(ClaimDTO claimDTO);

    /**
     * Partially updates a claim.
     *
     * @param claimDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClaimDTO> partialUpdate(ClaimDTO claimDTO);

    /**
     * Get the "id" claim.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClaimDTO> findOne(UUID id);

    /**
     * Delete the "id" claim.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the claim corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClaimDTO> search(String query, Pageable pageable);
}
