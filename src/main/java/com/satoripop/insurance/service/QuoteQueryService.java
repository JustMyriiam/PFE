package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Quote;
import com.satoripop.insurance.repository.QuoteRepository;
import com.satoripop.insurance.repository.search.QuoteSearchRepository;
import com.satoripop.insurance.service.criteria.QuoteCriteria;
import com.satoripop.insurance.service.dto.QuoteDTO;
import com.satoripop.insurance.service.mapper.QuoteMapper;
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
 * Service for executing complex queries for {@link Quote} entities in the database.
 * The main input is a {@link QuoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuoteQueryService extends QueryService<Quote> {

    private static final Logger LOG = LoggerFactory.getLogger(QuoteQueryService.class);

    private final QuoteRepository quoteRepository;

    private final QuoteMapper quoteMapper;

    private final QuoteSearchRepository quoteSearchRepository;

    public QuoteQueryService(QuoteRepository quoteRepository, QuoteMapper quoteMapper, QuoteSearchRepository quoteSearchRepository) {
        this.quoteRepository = quoteRepository;
        this.quoteMapper = quoteMapper;
        this.quoteSearchRepository = quoteSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link QuoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuoteDTO> findByCriteria(QuoteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quote> specification = createSpecification(criteria);
        return quoteRepository.findAll(specification, page).map(quoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuoteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Quote> specification = createSpecification(criteria);
        return quoteRepository.count(specification);
    }

    /**
     * Function to convert {@link QuoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quote> createSpecification(QuoteCriteria criteria) {
        Specification<Quote> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Quote_.id),
                buildRangeSpecification(criteria.getDate(), Quote_.date),
                buildRangeSpecification(criteria.getEstimatedAmount(), Quote_.estimatedAmount),
                buildSpecification(criteria.getVehicleId(), root -> root.join(Quote_.vehicle, JoinType.LEFT).get(Vehicle_.id)),
                buildSpecification(criteria.getClientId(), root -> root.join(Quote_.client, JoinType.LEFT).get(Client_.id))
            );
        }
        return specification;
    }
}
