package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.ClientDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Client}.
 */
public interface ClientService {
    /**
     * Save a client.
     *
     * @param clientDTO the entity to save.
     * @return the persisted entity.
     */
    ClientDTO save(ClientDTO clientDTO);

    /**
     * Updates a client.
     *
     * @param clientDTO the entity to update.
     * @return the persisted entity.
     */
    ClientDTO update(ClientDTO clientDTO);

    /**
     * Partially updates a client.
     *
     * @param clientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientDTO> partialUpdate(ClientDTO clientDTO);

    /**
     * Get all the clients with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" client.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientDTO> findOne(UUID id);

    /**
     * Delete the "id" client.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the client corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientDTO> search(String query, Pageable pageable);
}
