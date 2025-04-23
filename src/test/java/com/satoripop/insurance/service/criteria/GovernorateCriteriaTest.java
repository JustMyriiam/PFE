package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GovernorateCriteriaTest {

    @Test
    void newGovernorateCriteriaHasAllFiltersNullTest() {
        var governorateCriteria = new GovernorateCriteria();
        assertThat(governorateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void governorateCriteriaFluentMethodsCreatesFiltersTest() {
        var governorateCriteria = new GovernorateCriteria();

        setAllFilters(governorateCriteria);

        assertThat(governorateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void governorateCriteriaCopyCreatesNullFilterTest() {
        var governorateCriteria = new GovernorateCriteria();
        var copy = governorateCriteria.copy();

        assertThat(governorateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(governorateCriteria)
        );
    }

    @Test
    void governorateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var governorateCriteria = new GovernorateCriteria();
        setAllFilters(governorateCriteria);

        var copy = governorateCriteria.copy();

        assertThat(governorateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(governorateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var governorateCriteria = new GovernorateCriteria();

        assertThat(governorateCriteria).hasToString("GovernorateCriteria{}");
    }

    private static void setAllFilters(GovernorateCriteria governorateCriteria) {
        governorateCriteria.id();
        governorateCriteria.name();
        governorateCriteria.citiesId();
        governorateCriteria.distinct();
    }

    private static Condition<GovernorateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCitiesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GovernorateCriteria> copyFiltersAre(GovernorateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCitiesId(), copy.getCitiesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
