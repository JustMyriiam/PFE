package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class QuoteCriteriaTest {

    @Test
    void newQuoteCriteriaHasAllFiltersNullTest() {
        var quoteCriteria = new QuoteCriteria();
        assertThat(quoteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void quoteCriteriaFluentMethodsCreatesFiltersTest() {
        var quoteCriteria = new QuoteCriteria();

        setAllFilters(quoteCriteria);

        assertThat(quoteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void quoteCriteriaCopyCreatesNullFilterTest() {
        var quoteCriteria = new QuoteCriteria();
        var copy = quoteCriteria.copy();

        assertThat(quoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteCriteria)
        );
    }

    @Test
    void quoteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var quoteCriteria = new QuoteCriteria();
        setAllFilters(quoteCriteria);

        var copy = quoteCriteria.copy();

        assertThat(quoteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(quoteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var quoteCriteria = new QuoteCriteria();

        assertThat(quoteCriteria).hasToString("QuoteCriteria{}");
    }

    private static void setAllFilters(QuoteCriteria quoteCriteria) {
        quoteCriteria.id();
        quoteCriteria.date();
        quoteCriteria.estimatedAmount();
        quoteCriteria.vehicleId();
        quoteCriteria.clientId();
        quoteCriteria.distinct();
    }

    private static Condition<QuoteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getEstimatedAmount()) &&
                condition.apply(criteria.getVehicleId()) &&
                condition.apply(criteria.getClientId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<QuoteCriteria> copyFiltersAre(QuoteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getEstimatedAmount(), copy.getEstimatedAmount()) &&
                condition.apply(criteria.getVehicleId(), copy.getVehicleId()) &&
                condition.apply(criteria.getClientId(), copy.getClientId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
