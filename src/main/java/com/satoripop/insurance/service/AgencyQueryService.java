package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.repository.AgencyRepository;
import com.satoripop.insurance.repository.search.AgencySearchRepository;
import com.satoripop.insurance.service.criteria.AgencyCriteria;
import com.satoripop.insurance.service.dto.AgencyDTO;
import com.satoripop.insurance.service.mapper.AgencyMapper;
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
 * Service for executing complex queries for {@link Agency} entities in the database.
 * The main input is a {@link AgencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AgencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgencyQueryService extends QueryService<Agency> {

    private static final Logger LOG = LoggerFactory.getLogger(AgencyQueryService.class);

    private final AgencyRepository agencyRepository;

    private final AgencyMapper agencyMapper;

    private final AgencySearchRepository agencySearchRepository;

    public AgencyQueryService(AgencyRepository agencyRepository, AgencyMapper agencyMapper, AgencySearchRepository agencySearchRepository) {
        this.agencyRepository = agencyRepository;
        this.agencyMapper = agencyMapper;
        this.agencySearchRepository = agencySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link AgencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgencyDTO> findByCriteria(AgencyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Agency> specification = createSpecification(criteria);
        return agencyRepository.findAll(specification, page).map(agencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgencyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Agency> specification = createSpecification(criteria);
        return agencyRepository.count(specification);
    }

    /**
     * Function to convert {@link AgencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Agency> createSpecification(AgencyCriteria criteria) {
        Specification<Agency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Agency_.id),
                buildStringSpecification(criteria.getName(), Agency_.name),
                buildStringSpecification(criteria.getAddress(), Agency_.address),
                buildStringSpecification(criteria.getRegion(), Agency_.region),
                buildStringSpecification(criteria.getPhoneNumber(), Agency_.phoneNumber),
                buildStringSpecification(criteria.getManagerName(), Agency_.managerName),
                buildSpecification(criteria.getContractsId(), root -> root.join(Agency_.contracts, JoinType.LEFT).get(Contract_.id)),
                buildSpecification(criteria.getCityId(), root -> root.join(Agency_.city, JoinType.LEFT).get(City_.id))
            );
        }
        return specification;
    }
}
