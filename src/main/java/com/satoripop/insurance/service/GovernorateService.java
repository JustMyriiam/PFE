package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.GovernorateDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Governorate}.
 */
public interface GovernorateService {
    /**
     * Save a governorate.
     *
     * @param governorateDTO the entity to save.
     * @return the persisted entity.
     */
    GovernorateDTO save(GovernorateDTO governorateDTO);

    /**
     * Updates a governorate.
     *
     * @param governorateDTO the entity to update.
     * @return the persisted entity.
     */
    GovernorateDTO update(GovernorateDTO governorateDTO);

    /**
     * Partially updates a governorate.
     *
     * @param governorateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<GovernorateDTO> partialUpdate(GovernorateDTO governorateDTO);

    /**
     * Get the "id" governorate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GovernorateDTO> findOne(UUID id);

    /**
     * Delete the "id" governorate.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the governorate corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<GovernorateDTO> search(String query, Pageable pageable);
}
