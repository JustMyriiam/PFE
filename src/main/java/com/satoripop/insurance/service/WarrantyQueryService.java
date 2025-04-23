package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.repository.WarrantyRepository;
import com.satoripop.insurance.repository.search.WarrantySearchRepository;
import com.satoripop.insurance.service.criteria.WarrantyCriteria;
import com.satoripop.insurance.service.dto.WarrantyDTO;
import com.satoripop.insurance.service.mapper.WarrantyMapper;
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
 * Service for executing complex queries for {@link Warranty} entities in the database.
 * The main input is a {@link WarrantyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link WarrantyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WarrantyQueryService extends QueryService<Warranty> {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyQueryService.class);

    private final WarrantyRepository warrantyRepository;

    private final WarrantyMapper warrantyMapper;

    private final WarrantySearchRepository warrantySearchRepository;

    public WarrantyQueryService(
        WarrantyRepository warrantyRepository,
        WarrantyMapper warrantyMapper,
        WarrantySearchRepository warrantySearchRepository
    ) {
        this.warrantyRepository = warrantyRepository;
        this.warrantyMapper = warrantyMapper;
        this.warrantySearchRepository = warrantySearchRepository;
    }

    /**
     * Return a {@link Page} of {@link WarrantyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WarrantyDTO> findByCriteria(WarrantyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Warranty> specification = createSpecification(criteria);
        return warrantyRepository.findAll(specification, page).map(warrantyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WarrantyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Warranty> specification = createSpecification(criteria);
        return warrantyRepository.count(specification);
    }

    /**
     * Function to convert {@link WarrantyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Warranty> createSpecification(WarrantyCriteria criteria) {
        Specification<Warranty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Warranty_.id),
                buildStringSpecification(criteria.getName(), Warranty_.name),
                buildRangeSpecification(criteria.getLimit(), Warranty_.limit),
                buildRangeSpecification(criteria.getFranchise(), Warranty_.franchise),
                buildRangeSpecification(criteria.getPrice(), Warranty_.price),
                buildSpecification(criteria.getMandatory(), Warranty_.mandatory),
                buildSpecification(criteria.getInsurancePacksId(), root ->
                    root.join(Warranty_.insurancePacks, JoinType.LEFT).get(InsurancePack_.id)
                )
            );
        }
        return specification;
    }
}
