package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClientCriteriaTest {

    @Test
    void newClientCriteriaHasAllFiltersNullTest() {
        var clientCriteria = new ClientCriteria();
        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void clientCriteriaFluentMethodsCreatesFiltersTest() {
        var clientCriteria = new ClientCriteria();

        setAllFilters(clientCriteria);

        assertThat(clientCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void clientCriteriaCopyCreatesNullFilterTest() {
        var clientCriteria = new ClientCriteria();
        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void clientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var clientCriteria = new ClientCriteria();
        setAllFilters(clientCriteria);

        var copy = clientCriteria.copy();

        assertThat(clientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(clientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var clientCriteria = new ClientCriteria();

        assertThat(clientCriteria).hasToString("ClientCriteria{}");
    }

    private static void setAllFilters(ClientCriteria clientCriteria) {
        clientCriteria.id();
        clientCriteria.lastName();
        clientCriteria.firstName();
        clientCriteria.identityType();
        clientCriteria.identityNumber();
        clientCriteria.identityEmissionDate();
        clientCriteria.birthDate();
        clientCriteria.birthPlace();
        clientCriteria.identityIssueDate();
        clientCriteria.identityPlaceOfIssue();
        clientCriteria.maritalStatus();
        clientCriteria.nbrOfchildren();
        clientCriteria.professionalEmail();
        clientCriteria.personalEmail();
        clientCriteria.primaryPhoneNumber();
        clientCriteria.secondaryPhoneNumber();
        clientCriteria.faxNumber();
        clientCriteria.nationality();
        clientCriteria.gender();
        clientCriteria.jobTitle();
        clientCriteria.professionalStatus();
        clientCriteria.bank();
        clientCriteria.agency();
        clientCriteria.rib();
        clientCriteria.drivingLicenseNumber();
        clientCriteria.drivingLicenseIssueDate();
        clientCriteria.drivingLicenseCategory();
        clientCriteria.userId();
        clientCriteria.quotesId();
        clientCriteria.contractsId();
        clientCriteria.claimsId();
        clientCriteria.clientAddressId();
        clientCriteria.distinct();
    }

    private static Condition<ClientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getIdentityType()) &&
                condition.apply(criteria.getIdentityNumber()) &&
                condition.apply(criteria.getIdentityEmissionDate()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace()) &&
                condition.apply(criteria.getIdentityIssueDate()) &&
                condition.apply(criteria.getIdentityPlaceOfIssue()) &&
                condition.apply(criteria.getMaritalStatus()) &&
                condition.apply(criteria.getNbrOfchildren()) &&
                condition.apply(criteria.getProfessionalEmail()) &&
                condition.apply(criteria.getPersonalEmail()) &&
                condition.apply(criteria.getPrimaryPhoneNumber()) &&
                condition.apply(criteria.getSecondaryPhoneNumber()) &&
                condition.apply(criteria.getFaxNumber()) &&
                condition.apply(criteria.getNationality()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getJobTitle()) &&
                condition.apply(criteria.getProfessionalStatus()) &&
                condition.apply(criteria.getBank()) &&
                condition.apply(criteria.getAgency()) &&
                condition.apply(criteria.getRib()) &&
                condition.apply(criteria.getDrivingLicenseNumber()) &&
                condition.apply(criteria.getDrivingLicenseIssueDate()) &&
                condition.apply(criteria.getDrivingLicenseCategory()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getQuotesId()) &&
                condition.apply(criteria.getContractsId()) &&
                condition.apply(criteria.getClaimsId()) &&
                condition.apply(criteria.getClientAddressId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClientCriteria> copyFiltersAre(ClientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getIdentityType(), copy.getIdentityType()) &&
                condition.apply(criteria.getIdentityNumber(), copy.getIdentityNumber()) &&
                condition.apply(criteria.getIdentityEmissionDate(), copy.getIdentityEmissionDate()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace(), copy.getBirthPlace()) &&
                condition.apply(criteria.getIdentityIssueDate(), copy.getIdentityIssueDate()) &&
                condition.apply(criteria.getIdentityPlaceOfIssue(), copy.getIdentityPlaceOfIssue()) &&
                condition.apply(criteria.getMaritalStatus(), copy.getMaritalStatus()) &&
                condition.apply(criteria.getNbrOfchildren(), copy.getNbrOfchildren()) &&
                condition.apply(criteria.getProfessionalEmail(), copy.getProfessionalEmail()) &&
                condition.apply(criteria.getPersonalEmail(), copy.getPersonalEmail()) &&
                condition.apply(criteria.getPrimaryPhoneNumber(), copy.getPrimaryPhoneNumber()) &&
                condition.apply(criteria.getSecondaryPhoneNumber(), copy.getSecondaryPhoneNumber()) &&
                condition.apply(criteria.getFaxNumber(), copy.getFaxNumber()) &&
                condition.apply(criteria.getNationality(), copy.getNationality()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getJobTitle(), copy.getJobTitle()) &&
                condition.apply(criteria.getProfessionalStatus(), copy.getProfessionalStatus()) &&
                condition.apply(criteria.getBank(), copy.getBank()) &&
                condition.apply(criteria.getAgency(), copy.getAgency()) &&
                condition.apply(criteria.getRib(), copy.getRib()) &&
                condition.apply(criteria.getDrivingLicenseNumber(), copy.getDrivingLicenseNumber()) &&
                condition.apply(criteria.getDrivingLicenseIssueDate(), copy.getDrivingLicenseIssueDate()) &&
                condition.apply(criteria.getDrivingLicenseCategory(), copy.getDrivingLicenseCategory()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getQuotesId(), copy.getQuotesId()) &&
                condition.apply(criteria.getContractsId(), copy.getContractsId()) &&
                condition.apply(criteria.getClaimsId(), copy.getClaimsId()) &&
                condition.apply(criteria.getClientAddressId(), copy.getClientAddressId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
