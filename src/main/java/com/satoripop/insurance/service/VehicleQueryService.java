package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.repository.VehicleRepository;
import com.satoripop.insurance.repository.search.VehicleSearchRepository;
import com.satoripop.insurance.service.criteria.VehicleCriteria;
import com.satoripop.insurance.service.dto.VehicleDTO;
import com.satoripop.insurance.service.mapper.VehicleMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Vehicle} entities in the database.
 * The main input is a {@link VehicleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link VehicleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleQueryService extends QueryService<Vehicle> {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleQueryService.class);

    private final VehicleRepository vehicleRepository;

    private final VehicleMapper vehicleMapper;

    private final VehicleSearchRepository vehicleSearchRepository;

    public VehicleQueryService(
        VehicleRepository vehicleRepository,
        VehicleMapper vehicleMapper,
        VehicleSearchRepository vehicleSearchRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.vehicleSearchRepository = vehicleSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link VehicleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleDTO> findByCriteria(VehicleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.findAll(specification, page).map(vehicleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Vehicle> specification = createSpecification(criteria);
        return vehicleRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Vehicle> createSpecification(VehicleCriteria criteria) {
        Specification<Vehicle> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Vehicle_.id),
                buildStringSpecification(criteria.getRegistrationNumber(), Vehicle_.registrationNumber),
                buildSpecification(criteria.getRegistrationType(), Vehicle_.registrationType),
                buildRangeSpecification(criteria.getFirstRegistrationDate(), Vehicle_.firstRegistrationDate),
                buildSpecification(criteria.getTechnicalInspectionStatus(), Vehicle_.technicalInspectionStatus),
                buildRangeSpecification(criteria.getExpirationDate(), Vehicle_.expirationDate),
                buildRangeSpecification(criteria.getNewValue(), Vehicle_.newValue),
                buildRangeSpecification(criteria.getMarketValue(), Vehicle_.marketValue),
                buildSpecification(criteria.getBrand(), Vehicle_.brand),
                buildStringSpecification(criteria.getModel(), Vehicle_.model),
                buildRangeSpecification(criteria.getFiscalPower(), Vehicle_.fiscalPower),
                buildStringSpecification(criteria.getChassisNumber(), Vehicle_.chassisNumber),
                buildSpecification(criteria.getEnergy(), Vehicle_.energy),
                buildStringSpecification(criteria.getGenre(), Vehicle_.genre),
                buildRangeSpecification(criteria.getNbrOfSeats(), Vehicle_.nbrOfSeats),
                buildRangeSpecification(criteria.getNbrOfStandingPlaces(), Vehicle_.nbrOfStandingPlaces),
                buildRangeSpecification(criteria.getEmptyWeight(), Vehicle_.emptyWeight),
                buildRangeSpecification(criteria.getPayload(), Vehicle_.payload),
                buildRangeSpecification(criteria.getBonusMalus(), Vehicle_.bonusMalus),
                buildStringSpecification(criteria.getVehicleAge(), Vehicle_.vehicleAge),
                buildRangeSpecification(criteria.getMileage(), Vehicle_.mileage),
                buildRangeSpecification(criteria.getNumberOfDoors(), Vehicle_.numberOfDoors),
                buildSpecification(criteria.getGearbox(), Vehicle_.gearbox),
                buildStringSpecification(criteria.getColor(), Vehicle_.color),
                buildSpecification(criteria.getUsage(), Vehicle_.usage),
                buildSpecification(criteria.getIsNew(), Vehicle_.isNew),
                buildSpecification(criteria.getHasGarage(), Vehicle_.hasGarage),
                buildSpecification(criteria.getHasParking(), Vehicle_.hasParking),
                buildSpecification(criteria.getHasAlarmSystem(), Vehicle_.hasAlarmSystem),
                buildSpecification(criteria.getHasSeatbeltAlarm(), Vehicle_.hasSeatbeltAlarm),
                buildSpecification(criteria.getHasRearCamera(), Vehicle_.hasRearCamera),
                buildSpecification(criteria.getHasRearRadar(), Vehicle_.hasRearRadar),
                buildSpecification(criteria.getHasAbsSystem(), Vehicle_.hasAbsSystem),
                buildSpecification(criteria.getHasGPS(), Vehicle_.hasGPS),
                buildSpecification(criteria.getHasAirbag(), Vehicle_.hasAirbag),
                buildSpecification(criteria.getNavette(), Vehicle_.navette),
                buildSpecification(criteria.getInsurancePackId(), root ->
                    root.join(Vehicle_.insurancePacks, JoinType.LEFT).get(InsurancePack_.id)
                ),
                buildSpecification(criteria.getQuoteId(), root -> root.join(Vehicle_.quote, JoinType.LEFT).get(Quote_.id)),
                buildSpecification(criteria.getContractId(), root -> root.join(Vehicle_.contract, JoinType.LEFT).get(Contract_.id))
            );
        }
        return specification;
    }
}
