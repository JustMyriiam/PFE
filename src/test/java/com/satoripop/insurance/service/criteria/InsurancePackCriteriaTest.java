package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class InsurancePackCriteriaTest {

    @Test
    void newInsurancePackCriteriaHasAllFiltersNullTest() {
        var insurancePackCriteria = new InsurancePackCriteria();
        assertThat(insurancePackCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void insurancePackCriteriaFluentMethodsCreatesFiltersTest() {
        var insurancePackCriteria = new InsurancePackCriteria();

        setAllFilters(insurancePackCriteria);

        assertThat(insurancePackCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void insurancePackCriteriaCopyCreatesNullFilterTest() {
        var insurancePackCriteria = new InsurancePackCriteria();
        var copy = insurancePackCriteria.copy();

        assertThat(insurancePackCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePackCriteria)
        );
    }

    @Test
    void insurancePackCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var insurancePackCriteria = new InsurancePackCriteria();
        setAllFilters(insurancePackCriteria);

        var copy = insurancePackCriteria.copy();

        assertThat(insurancePackCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(insurancePackCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var insurancePackCriteria = new InsurancePackCriteria();

        assertThat(insurancePackCriteria).hasToString("InsurancePackCriteria{}");
    }

    private static void setAllFilters(InsurancePackCriteria insurancePackCriteria) {
        insurancePackCriteria.id();
        insurancePackCriteria.name();
        insurancePackCriteria.desciption();
        insurancePackCriteria.price();
        insurancePackCriteria.warrantiesId();
        insurancePackCriteria.vehicleId();
        insurancePackCriteria.distinct();
    }

    private static Condition<InsurancePackCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDesciption()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getWarrantiesId()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<InsurancePackCriteria> copyFiltersAre(
        InsurancePackCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDesciption(), copy.getDesciption()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getWarrantiesId(), copy.getWarrantiesId()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
