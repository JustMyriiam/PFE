package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.ContractTestSamples.*;
import static com.satoripop.insurance.domain.InsurancePackTestSamples.*;
import static com.satoripop.insurance.domain.QuoteTestSamples.*;
import static com.satoripop.insurance.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void insurancePackTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        InsurancePack insurancePackBack = getInsurancePackRandomSampleGenerator();

        vehicle.addInsurancePack(insurancePackBack);
        assertThat(vehicle.getInsurancePacks()).containsOnly(insurancePackBack);
        assertThat(insurancePackBack.getVehicle()).isEqualTo(vehicle);

        vehicle.removeInsurancePack(insurancePackBack);
        assertThat(vehicle.getInsurancePacks()).doesNotContain(insurancePackBack);
        assertThat(insurancePackBack.getVehicle()).isNull();

        vehicle.insurancePacks(new HashSet<>(Set.of(insurancePackBack)));
        assertThat(vehicle.getInsurancePacks()).containsOnly(insurancePackBack);
        assertThat(insurancePackBack.getVehicle()).isEqualTo(vehicle);

        vehicle.setInsurancePacks(new HashSet<>());
        assertThat(vehicle.getInsurancePacks()).doesNotContain(insurancePackBack);
        assertThat(insurancePackBack.getVehicle()).isNull();
    }

    @Test
    void quoteTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        vehicle.setQuote(quoteBack);
        assertThat(vehicle.getQuote()).isEqualTo(quoteBack);
        assertThat(quoteBack.getVehicle()).isEqualTo(vehicle);

        vehicle.quote(null);
        assertThat(vehicle.getQuote()).isNull();
        assertThat(quoteBack.getVehicle()).isNull();
    }

    @Test
    void contractTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        vehicle.setContract(contractBack);
        assertThat(vehicle.getContract()).isEqualTo(contractBack);
        assertThat(contractBack.getVehicle()).isEqualTo(vehicle);

        vehicle.contract(null);
        assertThat(vehicle.getContract()).isNull();
        assertThat(contractBack.getVehicle()).isNull();
    }
}
