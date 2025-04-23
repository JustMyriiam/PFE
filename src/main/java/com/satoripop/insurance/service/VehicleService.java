package com.satoripop.insurance.service;

import com.satoripop.insurance.service.dto.VehicleDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.satoripop.insurance.domain.Vehicle}.
 */
public interface VehicleService {
    /**
     * Save a vehicle.
     *
     * @param vehicleDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleDTO save(VehicleDTO vehicleDTO);

    /**
     * Updates a vehicle.
     *
     * @param vehicleDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleDTO update(VehicleDTO vehicleDTO);

    /**
     * Partially updates a vehicle.
     *
     * @param vehicleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO);

    /**
     * Get all the VehicleDTO where Quote is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<VehicleDTO> findAllWhereQuoteIsNull();
    /**
     * Get all the VehicleDTO where Contract is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<VehicleDTO> findAllWhereContractIsNull();

    /**
     * Get the "id" vehicle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleDTO> findOne(UUID id);

    /**
     * Delete the "id" vehicle.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    /**
     * Search for the vehicle corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleDTO> search(String query, Pageable pageable);
}
