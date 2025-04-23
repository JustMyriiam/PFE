package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Agency;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Agency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgencyRepository extends JpaRepository<Agency, UUID>, JpaSpecificationExecutor<Agency> {}
