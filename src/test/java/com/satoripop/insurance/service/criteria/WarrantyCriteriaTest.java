package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class WarrantyCriteriaTest {

    @Test
    void newWarrantyCriteriaHasAllFiltersNullTest() {
        var warrantyCriteria = new WarrantyCriteria();
        assertThat(warrantyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void warrantyCriteriaFluentMethodsCreatesFiltersTest() {
        var warrantyCriteria = new WarrantyCriteria();

        setAllFilters(warrantyCriteria);

        assertThat(warrantyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void warrantyCriteriaCopyCreatesNullFilterTest() {
        var warrantyCriteria = new WarrantyCriteria();
        var copy = warrantyCriteria.copy();

        assertThat(warrantyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(warrantyCriteria)
        );
    }

    @Test
    void warrantyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var warrantyCriteria = new WarrantyCriteria();
        setAllFilters(warrantyCriteria);

        var copy = warrantyCriteria.copy();

        assertThat(warrantyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(warrantyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var warrantyCriteria = new WarrantyCriteria();

        assertThat(warrantyCriteria).hasToString("WarrantyCriteria{}");
    }

    private static void setAllFilters(WarrantyCriteria warrantyCriteria) {
        warrantyCriteria.id();
        warrantyCriteria.name();
        warrantyCriteria.limit();
        warrantyCriteria.franchise();
        warrantyCriteria.price();
        warrantyCriteria.mandatory();
        warrantyCriteria.insurancePacksId();
        warrantyCriteria.distinct();
    }

    private static Condition<WarrantyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLimit()) &&
                condition.apply(criteria.getFranchise()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getMandatory()) &&
                condition.apply(criteria.getInsurancePacksId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<WarrantyCriteria> copyFiltersAre(WarrantyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLimit(), copy.getLimit()) &&
                condition.apply(criteria.getFranchise(), copy.getFranchise()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getMandatory(), copy.getMandatory()) &&
                condition.apply(criteria.getInsurancePacksId(), copy.getInsurancePacksId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
