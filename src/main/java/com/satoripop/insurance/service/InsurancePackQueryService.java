package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.repository.InsurancePackRepository;
import com.satoripop.insurance.repository.search.InsurancePackSearchRepository;
import com.satoripop.insurance.service.criteria.InsurancePackCriteria;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
import com.satoripop.insurance.service.mapper.InsurancePackMapper;
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
 * Service for executing complex queries for {@link InsurancePack} entities in the database.
 * The main input is a {@link InsurancePackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InsurancePackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsurancePackQueryService extends QueryService<InsurancePack> {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePackQueryService.class);

    private final InsurancePackRepository insurancePackRepository;

    private final InsurancePackMapper insurancePackMapper;

    private final InsurancePackSearchRepository insurancePackSearchRepository;

    public InsurancePackQueryService(
        InsurancePackRepository insurancePackRepository,
        InsurancePackMapper insurancePackMapper,
        InsurancePackSearchRepository insurancePackSearchRepository
    ) {
        this.insurancePackRepository = insurancePackRepository;
        this.insurancePackMapper = insurancePackMapper;
        this.insurancePackSearchRepository = insurancePackSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link InsurancePackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsurancePackDTO> findByCriteria(InsurancePackCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsurancePack> specification = createSpecification(criteria);
        return insurancePackRepository
            .fetchBagRelationships(insurancePackRepository.findAll(specification, page))
            .map(insurancePackMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsurancePackCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InsurancePack> specification = createSpecification(criteria);
        return insurancePackRepository.count(specification);
    }

    /**
     * Function to convert {@link InsurancePackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsurancePack> createSpecification(InsurancePackCriteria criteria) {
        Specification<InsurancePack> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), InsurancePack_.id),
                buildSpecification(criteria.getName(), InsurancePack_.name),
                buildStringSpecification(criteria.getDesciption(), InsurancePack_.desciption),
                buildRangeSpecification(criteria.getPrice(), InsurancePack_.price),
                buildSpecification(criteria.getWarrantiesId(), root -> root.join(InsurancePack_.warranties, JoinType.LEFT).get(Warranty_.id)
                ),
                buildSpecification(criteria.getVehicleId(), root -> root.join(InsurancePack_.vehicle, JoinType.LEFT).get(Vehicle_.id))
            );
        }
        return specification;
    }
}
