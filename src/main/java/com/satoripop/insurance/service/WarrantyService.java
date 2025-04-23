package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.WarrantyDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Warranty}.
 */
public interface WarrantyService {
    /**
     * Save a warranty.
     *
     * @param warrantyDTO the entity to save.
     * @return the persisted entity.
     */
    WarrantyDTO save(WarrantyDTO warrantyDTO);

    /**
     * Updates a warranty.
     *
     * @param warrantyDTO the entity to update.
     * @return the persisted entity.
     */
    WarrantyDTO update(WarrantyDTO warrantyDTO);

    /**
     * Partially updates a warranty.
     *
     * @param warrantyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WarrantyDTO> partialUpdate(WarrantyDTO warrantyDTO);

    /**
     * Get the "id" warranty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WarrantyDTO> findOne(UUID id);

    /**
     * Delete the "id" warranty.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the warranty corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WarrantyDTO> search(String query, Pageable pageable);
}
