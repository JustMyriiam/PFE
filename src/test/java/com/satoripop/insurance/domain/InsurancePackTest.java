package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.InsurancePackTestSamples.*;
import static com.satoripop.insurance.domain.VehicleTestSamples.*;
import static com.satoripop.insurance.domain.WarrantyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InsurancePackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancePack.class);
        InsurancePack insurancePack1 = getInsurancePackSample1();
        InsurancePack insurancePack2 = new InsurancePack();
        assertThat(insurancePack1).isNotEqualTo(insurancePack2);

        insurancePack2.setId(insurancePack1.getId());
        assertThat(insurancePack1).isEqualTo(insurancePack2);

        insurancePack2 = getInsurancePackSample2();
        assertThat(insurancePack1).isNotEqualTo(insurancePack2);
    }

    @Test
    void warrantiesTest() {
        InsurancePack insurancePack = getInsurancePackRandomSampleGenerator();
        Warranty warrantyBack = getWarrantyRandomSampleGenerator();

        insurancePack.addWarranties(warrantyBack);
        assertThat(insurancePack.getWarranties()).containsOnly(warrantyBack);

        insurancePack.removeWarranties(warrantyBack);
        assertThat(insurancePack.getWarranties()).doesNotContain(warrantyBack);

        insurancePack.warranties(new HashSet<>(Set.of(warrantyBack)));
        assertThat(insurancePack.getWarranties()).containsOnly(warrantyBack);

        insurancePack.setWarranties(new HashSet<>());
        assertThat(insurancePack.getWarranties()).doesNotContain(warrantyBack);
    }

    @Test
    void vehicleTest() {
        InsurancePack insurancePack = getInsurancePackRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        insurancePack.setVehicle(vehicleBack);
        assertThat(insurancePack.getVehicle()).isEqualTo(vehicleBack);

        insurancePack.vehicle(null);
        assertThat(insurancePack.getVehicle()).isNull();
    }
}
