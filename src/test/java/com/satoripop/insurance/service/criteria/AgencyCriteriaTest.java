package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgencyCriteriaTest {

    @Test
    void newAgencyCriteriaHasAllFiltersNullTest() {
        var agencyCriteria = new AgencyCriteria();
        assertThat(agencyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agencyCriteriaFluentMethodsCreatesFiltersTest() {
        var agencyCriteria = new AgencyCriteria();

        setAllFilters(agencyCriteria);

        assertThat(agencyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agencyCriteriaCopyCreatesNullFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void agencyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agencyCriteria = new AgencyCriteria();
        setAllFilters(agencyCriteria);

        var copy = agencyCriteria.copy();

        assertThat(agencyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agencyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agencyCriteria = new AgencyCriteria();

        assertThat(agencyCriteria).hasToString("AgencyCriteria{}");
    }

    private static void setAllFilters(AgencyCriteria agencyCriteria) {
        agencyCriteria.id();
        agencyCriteria.name();
        agencyCriteria.address();
        agencyCriteria.region();
        agencyCriteria.phoneNumber();
        agencyCriteria.managerName();
        agencyCriteria.contractsId();
        agencyCriteria.cityId();
        agencyCriteria.distinct();
    }

    private static Condition<AgencyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getRegion()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getManagerName()) &&
                condition.apply(criteria.getContractsId()) &&
                condition.apply(criteria.getCityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgencyCriteria> copyFiltersAre(AgencyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getRegion(), copy.getRegion()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getManagerName(), copy.getManagerName()) &&
                condition.apply(criteria.getContractsId(), copy.getContractsId()) &&
                condition.apply(criteria.getCityId(), copy.getCityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
