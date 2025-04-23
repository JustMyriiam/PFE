package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.InsurancePack;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface InsurancePackRepositoryWithBagRelationships {
    Optional<InsurancePack> fetchBagRelationships(Optional<InsurancePack> insurancePack);

    List<InsurancePack> fetchBagRelationships(List<InsurancePack> insurancePacks);

    Page<InsurancePack> fetchBagRelationships(Page<InsurancePack> insurancePacks);
}
