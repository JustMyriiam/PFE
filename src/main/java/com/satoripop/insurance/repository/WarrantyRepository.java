package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Warranty;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Warranty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WarrantyRepository extends JpaRepository<Warranty, UUID>, JpaSpecificationExecutor<Warranty> {}
