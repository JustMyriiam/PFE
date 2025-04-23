package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.repository.ClientAddressRepository;
import com.satoripop.insurance.repository.search.ClientAddressSearchRepository;
import com.satoripop.insurance.service.criteria.ClientAddressCriteria;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
import com.satoripop.insurance.service.mapper.ClientAddressMapper;
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
 * Service for executing complex queries for {@link ClientAddress} entities in the database.
 * The main input is a {@link ClientAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClientAddressDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientAddressQueryService extends QueryService<ClientAddress> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientAddressQueryService.class);

    private final ClientAddressRepository clientAddressRepository;

    private final ClientAddressMapper clientAddressMapper;

    private final ClientAddressSearchRepository clientAddressSearchRepository;

    public ClientAddressQueryService(
        ClientAddressRepository clientAddressRepository,
        ClientAddressMapper clientAddressMapper,
        ClientAddressSearchRepository clientAddressSearchRepository
    ) {
        this.clientAddressRepository = clientAddressRepository;
        this.clientAddressMapper = clientAddressMapper;
        this.clientAddressSearchRepository = clientAddressSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ClientAddressDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientAddressDTO> findByCriteria(ClientAddressCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClientAddress> specification = createSpecification(criteria);
        return clientAddressRepository.findAll(specification, page).map(clientAddressMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientAddressCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ClientAddress> specification = createSpecification(criteria);
        return clientAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClientAddress> createSpecification(ClientAddressCriteria criteria) {
        Specification<ClientAddress> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), ClientAddress_.id),
                buildStringSpecification(criteria.getAddress(), ClientAddress_.address),
                buildStringSpecification(criteria.getRegion(), ClientAddress_.region),
                buildSpecification(criteria.getClientsId(), root -> root.join(ClientAddress_.clients, JoinType.LEFT).get(Client_.id)),
                buildSpecification(criteria.getCityId(), root -> root.join(ClientAddress_.city, JoinType.LEFT).get(City_.id))
            );
        }
        return specification;
    }
}
