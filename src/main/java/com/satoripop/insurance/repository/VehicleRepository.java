package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Vehicle;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID>, JpaSpecificationExecutor<Vehicle> {}
