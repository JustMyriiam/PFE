package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ContractCriteriaTest {

    @Test
    void newContractCriteriaHasAllFiltersNullTest() {
        var contractCriteria = new ContractCriteria();
        assertThat(contractCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void contractCriteriaFluentMethodsCreatesFiltersTest() {
        var contractCriteria = new ContractCriteria();

        setAllFilters(contractCriteria);

        assertThat(contractCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void contractCriteriaCopyCreatesNullFilterTest() {
        var contractCriteria = new ContractCriteria();
        var copy = contractCriteria.copy();

        assertThat(contractCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(contractCriteria)
        );
    }

    @Test
    void contractCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var contractCriteria = new ContractCriteria();
        setAllFilters(contractCriteria);

        var copy = contractCriteria.copy();

        assertThat(contractCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(contractCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var contractCriteria = new ContractCriteria();

        assertThat(contractCriteria).hasToString("ContractCriteria{}");
    }

    private static void setAllFilters(ContractCriteria contractCriteria) {
        contractCriteria.id();
        contractCriteria.contractNumber();
        contractCriteria.duration();
        contractCriteria.startDate();
        contractCriteria.endDate();
        contractCriteria.netPremium();
        contractCriteria.upfrontPremium();
        contractCriteria.cost();
        contractCriteria.taxes();
        contractCriteria.fSSR();
        contractCriteria.fPAC();
        contractCriteria.tFGA();
        contractCriteria.contractType();
        contractCriteria.paymentPlan();
        contractCriteria.vehicleId();
        contractCriteria.documentsId();
        contractCriteria.claimsId();
        contractCriteria.clientId();
        contractCriteria.agencyId();
        contractCriteria.distinct();
    }

    private static Condition<ContractCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getContractNumber()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getNetPremium()) &&
                condition.apply(criteria.getUpfrontPremium()) &&
                condition.apply(criteria.getCost()) &&
                condition.apply(criteria.getTaxes()) &&
                condition.apply(criteria.getfSSR()) &&
                condition.apply(criteria.getfPAC()) &&
                condition.apply(criteria.gettFGA()) &&
                condition.apply(criteria.getContractType()) &&
                condition.apply(criteria.getPaymentPlan()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getDocumentsId()) &&
                condition.apply(criteria.getClaimsId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getAgencyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ContractCriteria> copyFiltersAre(ContractCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getContractNumber(), copy.getContractNumber()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getNetPremium(), copy.getNetPremium()) &&
                condition.apply(criteria.getUpfrontPremium(), copy.getUpfrontPremium()) &&
                condition.apply(criteria.getCost(), copy.getCost()) &&
                condition.apply(criteria.getTaxes(), copy.getTaxes()) &&
                condition.apply(criteria.getfSSR(), copy.getfSSR()) &&
                condition.apply(criteria.getfPAC(), copy.getfPAC()) &&
                condition.apply(criteria.gettFGA(), copy.gettFGA()) &&
                condition.apply(criteria.getContractType(), copy.getContractType()) &&
                condition.apply(criteria.getPaymentPlan(), copy.getPaymentPlan()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getDocumentsId(), copy.getDocumentsId()) &&
                condition.apply(criteria.getClaimsId(), copy.getClaimsId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getAgencyId(), copy.getAgencyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
