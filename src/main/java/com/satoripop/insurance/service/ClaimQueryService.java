package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Claim;
import com.satoripop.insurance.repository.ClaimRepository;
import com.satoripop.insurance.repository.search.ClaimSearchRepository;
import com.satoripop.insurance.service.criteria.ClaimCriteria;
import com.satoripop.insurance.service.dto.ClaimDTO;
import com.satoripop.insurance.service.mapper.ClaimMapper;
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
 * Service for executing complex queries for {@link Claim} entities in the database.
 * The main input is a {@link ClaimCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClaimDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClaimQueryService extends QueryService<Claim> {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimQueryService.class);

    private final ClaimRepository claimRepository;

    private final ClaimMapper claimMapper;

    private final ClaimSearchRepository claimSearchRepository;

    public ClaimQueryService(ClaimRepository claimRepository, ClaimMapper claimMapper, ClaimSearchRepository claimSearchRepository) {
        this.claimRepository = claimRepository;
        this.claimMapper = claimMapper;
        this.claimSearchRepository = claimSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ClaimDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClaimDTO> findByCriteria(ClaimCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Claim> specification = createSpecification(criteria);
        return claimRepository.findAll(specification, page).map(claimMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClaimCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Claim> specification = createSpecification(criteria);
        return claimRepository.count(specification);
    }

    /**
     * Function to convert {@link ClaimCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Claim> createSpecification(ClaimCriteria criteria) {
        Specification<Claim> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Claim_.id),
                buildStringSpecification(criteria.getType(), Claim_.type),
                buildStringSpecification(criteria.getDescription(), Claim_.description),
                buildRangeSpecification(criteria.getDate(), Claim_.date),
                buildSpecification(criteria.getStatus(), Claim_.status),
                buildSpecification(criteria.getClientId(), root -> root.join(Claim_.client, JoinType.LEFT).get(Client_.id)),
                buildSpecification(criteria.getContractId(), root -> root.join(Claim_.contract, JoinType.LEFT).get(Contract_.id))
            );
        }
        return specification;
    }
}
