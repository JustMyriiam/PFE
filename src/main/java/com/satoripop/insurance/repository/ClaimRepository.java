package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Claim;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Claim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID>, JpaSpecificationExecutor<Claim> {}
