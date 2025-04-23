package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.InsurancePack;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InsurancePack entity.
 *
 * When extending this class, extend InsurancePackRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface InsurancePackRepository
    extends InsurancePackRepositoryWithBagRelationships, JpaRepository<InsurancePack, UUID>, JpaSpecificationExecutor<InsurancePack> {
    default Optional<InsurancePack> findOneWithEagerRelationships(UUID id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<InsurancePack> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<InsurancePack> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
