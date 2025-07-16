package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.Contract;
import java.util.UUID;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends JpaRepository<Contract, UUID>, JpaSpecificationExecutor<Contract> {
    @Query("SELECT COALESCE(SUM(c.upfrontPremium), 0) FROM Contract c")
    Double findTotalUpfront();
}
