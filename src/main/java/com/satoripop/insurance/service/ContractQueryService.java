package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.repository.ContractRepository;
import com.satoripop.insurance.repository.search.ContractSearchRepository;
import com.satoripop.insurance.service.criteria.ContractCriteria;
import com.satoripop.insurance.service.dto.ContractDTO;
import com.satoripop.insurance.service.mapper.ContractMapper;
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
 * Service for executing complex queries for {@link Contract} entities in the database.
 * The main input is a {@link ContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractQueryService extends QueryService<Contract> {

    private static final Logger LOG = LoggerFactory.getLogger(ContractQueryService.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    private final ContractSearchRepository contractSearchRepository;

    public ContractQueryService(
        ContractRepository contractRepository,
        ContractMapper contractMapper,
        ContractSearchRepository contractSearchRepository
    ) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.contractSearchRepository = contractSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findByCriteria(ContractCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.findAll(specification, page).map(contractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Contract> specification = createSpecification(criteria);
        return contractRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Contract> createSpecification(ContractCriteria criteria) {
        Specification<Contract> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Contract_.id),
                buildStringSpecification(criteria.getContractNumber(), Contract_.contractNumber),
                buildStringSpecification(criteria.getDuration(), Contract_.duration),
                buildRangeSpecification(criteria.getStartDate(), Contract_.startDate),
                buildRangeSpecification(criteria.getEndDate(), Contract_.endDate),
                buildRangeSpecification(criteria.getNetPremium(), Contract_.netPremium),
                buildRangeSpecification(criteria.getUpfrontPremium(), Contract_.upfrontPremium),
                buildRangeSpecification(criteria.getCost(), Contract_.cost),
                buildRangeSpecification(criteria.getTaxes(), Contract_.taxes),
                buildRangeSpecification(criteria.getfSSR(), Contract_.fSSR),
                buildRangeSpecification(criteria.getfPAC(), Contract_.fPAC),
                buildRangeSpecification(criteria.gettFGA(), Contract_.tFGA),
                buildSpecification(criteria.getContractType(), Contract_.contractType),
                buildSpecification(criteria.getPaymentPlan(), Contract_.paymentPlan),
                buildSpecification(criteria.getVehicleId(), root -> root.join(Contract_.vehicle, JoinType.LEFT).get(Vehicle_.id)),
                buildSpecification(criteria.getDocumentsId(), root -> root.join(Contract_.documents, JoinType.LEFT).get(Document_.id)),
                buildSpecification(criteria.getClaimsId(), root -> root.join(Contract_.claims, JoinType.LEFT).get(Claim_.id)),
                buildSpecification(criteria.getClientId(), root -> root.join(Contract_.client, JoinType.LEFT).get(Client_.id)),
                buildSpecification(criteria.getAgencyId(), root -> root.join(Contract_.agency, JoinType.LEFT).get(Agency_.id))
            );
        }
        return specification;
    }
}
