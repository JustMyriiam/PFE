package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClaimCriteriaTest {

    @Test
    void newClaimCriteriaHasAllFiltersNullTest() {
        var claimCriteria = new ClaimCriteria();
        assertThat(claimCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void claimCriteriaFluentMethodsCreatesFiltersTest() {
        var claimCriteria = new ClaimCriteria();

        setAllFilters(claimCriteria);

        assertThat(claimCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void claimCriteriaCopyCreatesNullFilterTest() {
        var claimCriteria = new ClaimCriteria();
        var copy = claimCriteria.copy();

        assertThat(claimCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(claimCriteria)
        );
    }

    @Test
    void claimCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var claimCriteria = new ClaimCriteria();
        setAllFilters(claimCriteria);

        var copy = claimCriteria.copy();

        assertThat(claimCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(claimCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var claimCriteria = new ClaimCriteria();

        assertThat(claimCriteria).hasToString("ClaimCriteria{}");
    }

    private static void setAllFilters(ClaimCriteria claimCriteria) {
        claimCriteria.id();
        claimCriteria.type();
        claimCriteria.description();
        claimCriteria.date();
        claimCriteria.status();
        claimCriteria.clientId();
        claimCriteria.contractId();
        claimCriteria.distinct();
    }

    private static Condition<ClaimCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getContractId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClaimCriteria> copyFiltersAre(ClaimCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getContractId(), copy.getContractId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
