package com.satoripop.insurance.service;

import com.satoripop.insurance.domain.*; // for static metamodels
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.repository.ClientRepository;
import com.satoripop.insurance.repository.search.ClientSearchRepository;
import com.satoripop.insurance.service.criteria.ClientCriteria;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.mapper.ClientMapper;
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
 * Service for executing complex queries for {@link Client} entities in the database.
 * The main input is a {@link ClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService extends QueryService<Client> {

    private static final Logger LOG = LoggerFactory.getLogger(ClientQueryService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final ClientSearchRepository clientSearchRepository;

    public ClientQueryService(ClientRepository clientRepository, ClientMapper clientMapper, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientSearchRepository = clientSearchRepository;
    }

    /**
     * Return a {@link Page} of {@link ClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findByCriteria(ClientCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification, page).map(clientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.count(specification);
    }

    /**
     * Function to convert {@link ClientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Client> createSpecification(ClientCriteria criteria) {
        Specification<Client> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildSpecification(criteria.getId(), Client_.id),
                buildStringSpecification(criteria.getLastName(), Client_.lastName),
                buildStringSpecification(criteria.getFirstName(), Client_.firstName),
                buildSpecification(criteria.getIdentityType(), Client_.identityType),
                buildStringSpecification(criteria.getIdentityNumber(), Client_.identityNumber),
                buildRangeSpecification(criteria.getIdentityEmissionDate(), Client_.identityEmissionDate),
                buildRangeSpecification(criteria.getBirthDate(), Client_.birthDate),
                buildStringSpecification(criteria.getBirthPlace(), Client_.birthPlace),
                buildRangeSpecification(criteria.getIdentityIssueDate(), Client_.identityIssueDate),
                buildStringSpecification(criteria.getIdentityPlaceOfIssue(), Client_.identityPlaceOfIssue),
                buildSpecification(criteria.getMaritalStatus(), Client_.maritalStatus),
                buildRangeSpecification(criteria.getNbrOfchildren(), Client_.nbrOfchildren),
                buildStringSpecification(criteria.getProfessionalEmail(), Client_.professionalEmail),
                buildStringSpecification(criteria.getPersonalEmail(), Client_.personalEmail),
                buildStringSpecification(criteria.getPrimaryPhoneNumber(), Client_.primaryPhoneNumber),
                buildStringSpecification(criteria.getSecondaryPhoneNumber(), Client_.secondaryPhoneNumber),
                buildStringSpecification(criteria.getFaxNumber(), Client_.faxNumber),
                buildStringSpecification(criteria.getNationality(), Client_.nationality),
                buildSpecification(criteria.getGender(), Client_.gender),
                buildStringSpecification(criteria.getJobTitle(), Client_.jobTitle),
                buildSpecification(criteria.getProfessionalStatus(), Client_.professionalStatus),
                buildStringSpecification(criteria.getBank(), Client_.bank),
                buildStringSpecification(criteria.getAgency(), Client_.agency),
                buildStringSpecification(criteria.getRib(), Client_.rib),
                buildStringSpecification(criteria.getDrivingLicenseNumber(), Client_.drivingLicenseNumber),
                buildRangeSpecification(criteria.getDrivingLicenseIssueDate(), Client_.drivingLicenseIssueDate),
                buildStringSpecification(criteria.getDrivingLicenseCategory(), Client_.drivingLicenseCategory),
                buildSpecification(criteria.getUserId(), root -> root.join(Client_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getQuotesId(), root -> root.join(Client_.quotes, JoinType.LEFT).get(Quote_.id)),
                buildSpecification(criteria.getContractsId(), root -> root.join(Client_.contracts, JoinType.LEFT).get(Contract_.id)),
                buildSpecification(criteria.getClaimsId(), root -> root.join(Client_.claims, JoinType.LEFT).get(Claim_.id)),
                buildSpecification(criteria.getClientAddressId(), root ->
                    root.join(Client_.clientAddress, JoinType.LEFT).get(ClientAddress_.id)
                )
            );
        }
        return specification;
    }
}
