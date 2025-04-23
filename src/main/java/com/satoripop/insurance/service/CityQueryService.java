package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.repository.CityRepository;
import com.satoripop.insurance.repository.search.CitySearchRepository;
import com.satoripop.insurance.service.criteria.CityCriteria;
import com.satoripop.insurance.service.dto.CityDTO;
import com.satoripop.insurance.service.mapper.CityMapper;
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
 * Service for executing complex queries for {@link City} entities in the database.
 * The main input is a {@link CityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CityQueryService extends QueryService<City> {

    private static final Logger LOG = LoggerFactory.getLogger(CityQueryService.class);

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    private final CitySearchRepository citySearchRepository;

    public CityQueryService(CityRepository cityRepository, CityMapper cityMapper, CitySearchRepository citySearchRepository) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
        this.citySearchRepository = citySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link CityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CityDTO> findByCriteria(CityCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<City> specification = createSpecification(criteria);
        return cityRepository.findAll(specification, page).map(cityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CityCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<City> specification = createSpecification(criteria);
        return cityRepository.count(specification);
    }

    /**
     * Function to convert {@link CityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<City> createSpecification(CityCriteria criteria) {
        Specification<City> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), City_.id),
                buildStringSpecification(criteria.getName(), City_.name),
                buildStringSpecification(criteria.getPostalCode(), City_.postalCode),
                buildSpecification(criteria.getClientAddressesId(), root ->
                    root.join(City_.clientAddresses, JoinType.LEFT).get(ClientAddress_.id)
                ),
                buildSpecification(criteria.getAgenciesId(), root -> root.join(City_.agencies, JoinType.LEFT).get(Agency_.id)),
                buildSpecification(criteria.getGovernorateId(), root -> root.join(City_.governorate, JoinType.LEFT).get(Governorate_.id))
            );
        }
        return specification;
    }
}
