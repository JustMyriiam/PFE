package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.City;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, UUID>, JpaSpecificationExecutor<City> {}
