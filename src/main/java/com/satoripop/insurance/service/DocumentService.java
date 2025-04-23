package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.DocumentDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Document}.
 */
public interface DocumentService {
    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentDTO save(DocumentDTO documentDTO);

    /**
     * Updates a document.
     *
     * @param documentDTO the entity to update.
     * @return the persisted entity.
     */
    DocumentDTO update(DocumentDTO documentDTO);

    /**
     * Partially updates a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO);

    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentDTO> findOne(UUID id);

    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the document corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DocumentDTO> search(String query, Pageable pageable);
}
