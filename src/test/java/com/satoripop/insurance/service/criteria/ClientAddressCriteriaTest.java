package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClientAddressCriteriaTest {

    @Test
    void newClientAddressCriteriaHasAllFiltersNullTest() {
        var clientAddressCriteria = new ClientAddressCriteria();
        assertThat(clientAddressCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientAddressCriteriaFluentMethodsCreatesFiltersTest() {
        var clientAddressCriteria = new ClientAddressCriteria();

        setAllFilters(clientAddressCriteria);

        assertThat(clientAddressCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientAddressCriteriaCopyCreatesNullFilterTest() {
        var clientAddressCriteria = new ClientAddressCriteria();
        var copy = clientAddressCriteria.copy();

        assertThat(clientAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientAddressCriteria)
        );
    }

    @Test
    void clientAddressCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientAddressCriteria = new ClientAddressCriteria();
        setAllFilters(clientAddressCriteria);

        var copy = clientAddressCriteria.copy();

        assertThat(clientAddressCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientAddressCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientAddressCriteria = new ClientAddressCriteria();

        assertThat(clientAddressCriteria).hasToString("ClientAddressCriteria{}");
    }

    private static void setAllFilters(ClientAddressCriteria clientAddressCriteria) {
        clientAddressCriteria.id();
        clientAddressCriteria.address();
        clientAddressCriteria.region();
        clientAddressCriteria.clientsId();
        clientAddressCriteria.cityId();
        clientAddressCriteria.distinct();
    }

    private static Condition<ClientAddressCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getRegion()) &&
                condition.apply(criteria.getClientsId()) &&
                condition.apply(criteria.getCityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientAddressCriteria> copyFiltersAre(
        ClientAddressCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getRegion(), copy.getRegion()) &&
                condition.apply(criteria.getClientsId(), copy.getClientsId()) &&
                condition.apply(criteria.getCityId(), copy.getCityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
