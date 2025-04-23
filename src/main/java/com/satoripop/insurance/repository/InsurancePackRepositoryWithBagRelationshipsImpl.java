package com.satoripop.insurance.repository;

import com.satoripop.insurance.domain.InsurancePack;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class InsurancePackRepositoryWithBagRelationshipsImpl implements InsurancePackRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String INSURANCEPACKS_PARAMETER = "insurancePacks";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<InsurancePack> fetchBagRelationships(Optional<InsurancePack> insurancePack) {
        return insurancePack.map(this::fetchWarranties);
    }

    @Override
    public Page<InsurancePack> fetchBagRelationships(Page<InsurancePack> insurancePacks) {
        return new PageImpl<>(
            fetchBagRelationships(insurancePacks.getContent()),
            insurancePacks.getPageable(),
            insurancePacks.getTotalElements()
        );
    }

    @Override
    public List<InsurancePack> fetchBagRelationships(List<InsurancePack> insurancePacks) {
        return Optional.of(insurancePacks).map(this::fetchWarranties).orElse(Collections.emptyList());
    }

    InsurancePack fetchWarranties(InsurancePack result) {
        return entityManager
            .createQuery(
                "select insurancePack from InsurancePack insurancePack left join fetch insurancePack.warranties where insurancePack.id = :id",
                InsurancePack.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<InsurancePack> fetchWarranties(List<InsurancePack> insurancePacks) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, insurancePacks.size()).forEach(index -> order.put(insurancePacks.get(index).getId(), index));
        List<InsurancePack> result = entityManager
            .createQuery(
                "select insurancePack from InsurancePack insurancePack left join fetch insurancePack.warranties where insurancePack in :insurancePacks",
                InsurancePack.class
            )
            .setParameter(INSURANCEPACKS_PARAMETER, insurancePacks)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
