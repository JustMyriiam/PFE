package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.QuoteDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Quote}.
 */
public interface QuoteService {
    /**
     * Save a quote.
     *
     * @param quoteDTO the entity to save.
     * @return the persisted entity.
     */
    QuoteDTO save(QuoteDTO quoteDTO);

    /**
     * Updates a quote.
     *
     * @param quoteDTO the entity to update.
     * @return the persisted entity.
     */
    QuoteDTO update(QuoteDTO quoteDTO);

    /**
     * Partially updates a quote.
     *
     * @param quoteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<QuoteDTO> partialUpdate(QuoteDTO quoteDTO);

    /**
     * Get the "id" quote.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<QuoteDTO> findOne(UUID id);

    /**
     * Delete the "id" quote.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the quote corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<QuoteDTO> search(String query, Pageable pageable);
}
