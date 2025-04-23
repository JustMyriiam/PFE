package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.ClientAddress;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClientAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientAddressRepository extends JpaRepository<ClientAddress, UUID>, JpaSpecificationExecutor<ClientAddress> {}
