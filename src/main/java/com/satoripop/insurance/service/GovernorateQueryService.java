package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.repository.GovernorateRepository;
import com.satoripop.insurance.repository.search.GovernorateSearchRepository;
import com.satoripop.insurance.service.criteria.GovernorateCriteria;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import com.satoripop.insurance.service.mapper.GovernorateMapper;
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
 * Service for executing complex queries for {@link Governorate} entities in the database.
 * The main input is a {@link GovernorateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link GovernorateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GovernorateQueryService extends QueryService<Governorate> {

    private static final Logger LOG = LoggerFactory.getLogger(GovernorateQueryService.class);

    private final GovernorateRepository governorateRepository;

    private final GovernorateMapper governorateMapper;

    private final GovernorateSearchRepository governorateSearchRepository;

    public GovernorateQueryService(
        GovernorateRepository governorateRepository,
        GovernorateMapper governorateMapper,
        GovernorateSearchRepository governorateSearchRepository
    ) {
        this.governorateRepository = governorateRepository;
        this.governorateMapper = governorateMapper;
        this.governorateSearchRepository = governorateSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link GovernorateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GovernorateDTO> findByCriteria(GovernorateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Governorate> specification = createSpecification(criteria);
        return governorateRepository.findAll(specification, page).map(governorateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GovernorateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Governorate> specification = createSpecification(criteria);
        return governorateRepository.count(specification);
    }

    /**
     * Function to convert {@link GovernorateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Governorate> createSpecification(GovernorateCriteria criteria) {
        Specification<Governorate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Governorate_.id),
                buildStringSpecification(criteria.getName(), Governorate_.name),
                buildSpecification(criteria.getCitiesId(), root -> root.join(Governorate_.cities, JoinType.LEFT).get(City_.id))
            );
        }
        return specification;
    }
}
