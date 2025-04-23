package com.satoripop.insurance.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class VehicleCriteriaTest {

    @Test
    void newVehicleCriteriaHasAllFiltersNullTest() {
        var vehicleCriteria = new VehicleCriteria();
        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void vehicleCriteriaFluentMethodsCreatesFiltersTest() {
        var vehicleCriteria = new VehicleCriteria();

        setAllFilters(vehicleCriteria);

        assertThat(vehicleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void vehicleCriteriaCopyCreatesNullFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void vehicleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var vehicleCriteria = new VehicleCriteria();
        setAllFilters(vehicleCriteria);

        var copy = vehicleCriteria.copy();

        assertThat(vehicleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(vehicleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var vehicleCriteria = new VehicleCriteria();

        assertThat(vehicleCriteria).hasToString("VehicleCriteria{}");
    }

    private static void setAllFilters(VehicleCriteria vehicleCriteria) {
        vehicleCriteria.id();
        vehicleCriteria.registrationNumber();
        vehicleCriteria.registrationType();
        vehicleCriteria.firstRegistrationDate();
        vehicleCriteria.technicalInspectionStatus();
        vehicleCriteria.expirationDate();
        vehicleCriteria.newValue();
        vehicleCriteria.marketValue();
        vehicleCriteria.brand();
        vehicleCriteria.model();
        vehicleCriteria.fiscalPower();
        vehicleCriteria.chassisNumber();
        vehicleCriteria.energy();
        vehicleCriteria.genre();
        vehicleCriteria.nbrOfSeats();
        vehicleCriteria.nbrOfStandingPlaces();
        vehicleCriteria.emptyWeight();
        vehicleCriteria.payload();
        vehicleCriteria.bonusMalus();
        vehicleCriteria.vehicleAge();
        vehicleCriteria.mileage();
        vehicleCriteria.numberOfDoors();
        vehicleCriteria.gearbox();
        vehicleCriteria.color();
        vehicleCriteria.usage();
        vehicleCriteria.isNew();
        vehicleCriteria.hasGarage();
        vehicleCriteria.hasParking();
        vehicleCriteria.hasAlarmSystem();
        vehicleCriteria.hasSeatbeltAlarm();
        vehicleCriteria.hasRearCamera();
        vehicleCriteria.hasRearRadar();
        vehicleCriteria.hasAbsSystem();
        vehicleCriteria.hasGPS();
        vehicleCriteria.hasAirbag();
        vehicleCriteria.navette();
        vehicleCriteria.insurancePackId();
        vehicleCriteria.quoteId();
        vehicleCriteria.contractId();
        vehicleCriteria.distinct();
    }

    private static Condition<VehicleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getRegistrationNumber()) &&
                condition.apply(criteria.getRegistrationType()) &&
                condition.apply(criteria.getFirstRegistrationDate()) &&
                condition.apply(criteria.getTechnicalInspectionStatus()) &&
                condition.apply(criteria.getExpirationDate()) &&
                condition.apply(criteria.getNewValue()) &&
                condition.apply(criteria.getMarketValue()) &&
                condition.apply(criteria.getBrand()) &&
                condition.apply(criteria.getModel()) &&
                condition.apply(criteria.getFiscalPower()) &&
                condition.apply(criteria.getChassisNumber()) &&
                condition.apply(criteria.getEnergy()) &&
                condition.apply(criteria.getGenre()) &&
                condition.apply(criteria.getNbrOfSeats()) &&
                condition.apply(criteria.getNbrOfStandingPlaces()) &&
                condition.apply(criteria.getEmptyWeight()) &&
                condition.apply(criteria.getPayload()) &&
                condition.apply(criteria.getBonusMalus()) &&
                condition.apply(criteria.getVehicleAge()) &&
                condition.apply(criteria.getMileage()) &&
                condition.apply(criteria.getNumberOfDoors()) &&
                condition.apply(criteria.getGearbox()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getUsage()) &&
                condition.apply(criteria.getIsNew()) &&
                condition.apply(criteria.getHasGarage()) &&
                condition.apply(criteria.getHasParking()) &&
                condition.apply(criteria.getHasAlarmSystem()) &&
                condition.apply(criteria.getHasSeatbeltAlarm()) &&
                condition.apply(criteria.getHasRearCamera()) &&
                condition.apply(criteria.getHasRearRadar()) &&
                condition.apply(criteria.getHasAbsSystem()) &&
                condition.apply(criteria.getHasGPS()) &&
                condition.apply(criteria.getHasAirbag()) &&
                condition.apply(criteria.getNavette()) &&
                condition.apply(criteria.getInsurancePackId()) &&
                condition.apply(criteria.getQuoteId()) &&
                condition.apply(criteria.getContractId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<VehicleCriteria> copyFiltersAre(VehicleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getRegistrationNumber(), copy.getRegistrationNumber()) &&
                condition.apply(criteria.getRegistrationType(), copy.getRegistrationType()) &&
                condition.apply(criteria.getFirstRegistrationDate(), copy.getFirstRegistrationDate()) &&
                condition.apply(criteria.getTechnicalInspectionStatus(), copy.getTechnicalInspectionStatus()) &&
                condition.apply(criteria.getExpirationDate(), copy.getExpirationDate()) &&
                condition.apply(criteria.getNewValue(), copy.getNewValue()) &&
                condition.apply(criteria.getMarketValue(), copy.getMarketValue()) &&
                condition.apply(criteria.getBrand(), copy.getBrand()) &&
                condition.apply(criteria.getModel(), copy.getModel()) &&
                condition.apply(criteria.getFiscalPower(), copy.getFiscalPower()) &&
                condition.apply(criteria.getChassisNumber(), copy.getChassisNumber()) &&
                condition.apply(criteria.getEnergy(), copy.getEnergy()) &&
                condition.apply(criteria.getGenre(), copy.getGenre()) &&
                condition.apply(criteria.getNbrOfSeats(), copy.getNbrOfSeats()) &&
                condition.apply(criteria.getNbrOfStandingPlaces(), copy.getNbrOfStandingPlaces()) &&
                condition.apply(criteria.getEmptyWeight(), copy.getEmptyWeight()) &&
                condition.apply(criteria.getPayload(), copy.getPayload()) &&
                condition.apply(criteria.getBonusMalus(), copy.getBonusMalus()) &&
                condition.apply(criteria.getVehicleAge(), copy.getVehicleAge()) &&
                condition.apply(criteria.getMileage(), copy.getMileage()) &&
                condition.apply(criteria.getNumberOfDoors(), copy.getNumberOfDoors()) &&
                condition.apply(criteria.getGearbox(), copy.getGearbox()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getUsage(), copy.getUsage()) &&
                condition.apply(criteria.getIsNew(), copy.getIsNew()) &&
                condition.apply(criteria.getHasGarage(), copy.getHasGarage()) &&
                condition.apply(criteria.getHasParking(), copy.getHasParking()) &&
                condition.apply(criteria.getHasAlarmSystem(), copy.getHasAlarmSystem()) &&
                condition.apply(criteria.getHasSeatbeltAlarm(), copy.getHasSeatbeltAlarm()) &&
                condition.apply(criteria.getHasRearCamera(), copy.getHasRearCamera()) &&
                condition.apply(criteria.getHasRearRadar(), copy.getHasRearRadar()) &&
                condition.apply(criteria.getHasAbsSystem(), copy.getHasAbsSystem()) &&
                condition.apply(criteria.getHasGPS(), copy.getHasGPS()) &&
                condition.apply(criteria.getHasAirbag(), copy.getHasAirbag()) &&
                condition.apply(criteria.getNavette(), copy.getNavette()) &&
                condition.apply(criteria.getInsurancePackId(), copy.getInsurancePackId()) &&
                condition.apply(criteria.getQuoteId(), copy.getQuoteId()) &&
                condition.apply(criteria.getContractId(), copy.getContractId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
