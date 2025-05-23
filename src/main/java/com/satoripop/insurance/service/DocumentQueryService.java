package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Document;
import com.satoripop.insurance.repository.DocumentRepository;
import com.satoripop.insurance.repository.search.DocumentSearchRepository;
import com.satoripop.insurance.service.criteria.DocumentCriteria;
import com.satoripop.insurance.service.dto.DocumentDTO;
import com.satoripop.insurance.service.mapper.DocumentMapper;
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
 * Service for executing complex queries for {@link Document} entities in the database.
 * The main input is a {@link DocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentQueryService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentSearchRepository documentSearchRepository;

    public DocumentQueryService(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        DocumentSearchRepository documentSearchRepository
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentSearchRepository = documentSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link DocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification, page).map(documentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Document> createSpecification(DocumentCriteria criteria) {
        Specification<Document> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Document_.id),
                buildStringSpecification(criteria.getName(), Document_.name),
                buildStringSpecification(criteria.getDocPath(), Document_.docPath),
                buildSpecification(criteria.getType(), Document_.type),
                buildSpecification(criteria.getRequestedDocType(), Document_.requestedDocType),
                buildRangeSpecification(criteria.getCreationDate(), Document_.creationDate),
                buildSpecification(criteria.getContractId(), root -> root.join(Document_.contract, JoinType.LEFT).get(Contract_.id))
            );
        }
        return specification;
    }
}
