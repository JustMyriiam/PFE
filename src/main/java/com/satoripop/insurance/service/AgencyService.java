package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.AgencyDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Agency}.
 */
public interface AgencyService {
    /**
     * Save a agency.
     *
     * @param agencyDTO the entity to save.
     * @return the persisted entity.
     */
    AgencyDTO save(AgencyDTO agencyDTO);

    /**
     * Updates a agency.
     *
     * @param agencyDTO the entity to update.
     * @return the persisted entity.
     */
    AgencyDTO update(AgencyDTO agencyDTO);

    /**
     * Partially updates a agency.
     *
     * @param agencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AgencyDTO> partialUpdate(AgencyDTO agencyDTO);

    /**
     * Get the "id" agency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AgencyDTO> findOne(UUID id);

    /**
     * Delete the "id" agency.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the agency corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AgencyDTO> search(String query, Pageable pageable);
}
