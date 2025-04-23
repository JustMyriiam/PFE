package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.ClientAddressDTO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.ClientAddress}.
 */
public interface ClientAddressService {
    /**
     * Save a clientAddress.
     *
     * @param clientAddressDTO the entity to save.
     * @return the persisted entity.
     */
    ClientAddressDTO save(ClientAddressDTO clientAddressDTO);

    /**
     * Updates a clientAddress.
     *
     * @param clientAddressDTO the entity to update.
     * @return the persisted entity.
     */
    ClientAddressDTO update(ClientAddressDTO clientAddressDTO);

    /**
     * Partially updates a clientAddress.
     *
     * @param clientAddressDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClientAddressDTO> partialUpdate(ClientAddressDTO clientAddressDTO);

    /**
     * Get the "id" clientAddress.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClientAddressDTO> findOne(UUID id);

    /**
     * Delete the "id" clientAddress.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the clientAddress corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClientAddressDTO> search(String query, Pageable pageable);
}
