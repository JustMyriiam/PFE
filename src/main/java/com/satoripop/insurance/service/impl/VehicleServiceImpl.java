package com.satoripop.insurance.service.impl;

import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.repository.VehicleRepository;
import com.satoripop.insurance.repository.search.VehicleSearchRepository;
import com.satoripop.insurance.service.VehicleService;
import com.satoripop.insurance.service.dto.VehicleDTO;
import com.satoripop.insurance.service.mapper.VehicleMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.satoripop.insurance.domain.Vehicle}.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    private final VehicleSearchRepository vehicleSearchRepository;

    public VehicleServiceImpl(
        VehicleRepository vehicleRepository,
        VehicleMapper vehicleMapper,
        VehicleSearchRepository vehicleSearchRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.vehicleSearchRepository = vehicleSearchRepository;
    }

    @Override
    public VehicleDTO save(VehicleDTO vehicleDTO) {
        LOG.debug("Request to save Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        vehicleSearchRepository.index(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        LOG.debug("Request to update Vehicle : {}", vehicleDTO);
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);
        vehicleSearchRepository.index(vehicle);
        return vehicleMapper.toDto(vehicle);
    }

    @Override
    public Optional<VehicleDTO> partialUpdate(VehicleDTO vehicleDTO) {
        LOG.debug("Request to partially update Vehicle : {}", vehicleDTO);

        return vehicleRepository
            .findById(vehicleDTO.getId())
            .map(existingVehicle -> {
                vehicleMapper.partialUpdate(existingVehicle, vehicleDTO);

                return existingVehicle;
            })
            .map(vehicleRepository::save)
            .map(savedVehicle -> {
                vehicleSearchRepository.index(savedVehicle);
                return savedVehicle;
            })
            .map(vehicleMapper::toDto);
    }

    /**
     *  Get all the vehicles where Quote is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> findAllWhereQuoteIsNull() {
        LOG.debug("Request to get all vehicles where Quote is null");
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
            .filter(vehicle -> vehicle.getQuote() == null)
            .map(vehicleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the vehicles where Contract is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleDTO> findAllWhereContractIsNull() {
        LOG.debug("Request to get all vehicles where Contract is null");
        return StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
            .filter(vehicle -> vehicle.getContract() == null)
            .map(vehicleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleDTO> findOne(UUID id) {
        LOG.debug("Request to get Vehicle : {}", id);
        return vehicleRepository.findById(id).map(vehicleMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        LOG.debug("Request to delete Vehicle : {}", id);
        vehicleRepository.deleteById(id);
        vehicleSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of Vehicles for query {}", query);
        return vehicleSearchRepository.search(query, pageable).map(vehicleMapper::toDto);
    }
}
