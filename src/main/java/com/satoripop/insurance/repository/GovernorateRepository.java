package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Governorate;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Governorate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GovernorateRepository extends JpaRepository<Governorate, UUID>, JpaSpecificationExecutor<Governorate> {}
