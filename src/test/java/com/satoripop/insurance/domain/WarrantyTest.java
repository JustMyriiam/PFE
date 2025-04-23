package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.InsurancePackTestSamples.*;
import static com.satoripop.insurance.domain.WarrantyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class WarrantyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Warranty.class);
        Warranty warranty1 = getWarrantySample1();
        Warranty warranty2 = new Warranty();
        assertThat(warranty1).isNotEqualTo(warranty2);

        warranty2.setId(warranty1.getId());
        assertThat(warranty1).isEqualTo(warranty2);

        warranty2 = getWarrantySample2();
        assertThat(warranty1).isNotEqualTo(warranty2);
    }

    @Test
    void insurancePacksTest() {
        Warranty warranty = getWarrantyRandomSampleGenerator();
        InsurancePack insurancePackBack = getInsurancePackRandomSampleGenerator();

        warranty.addInsurancePacks(insurancePackBack);
        assertThat(warranty.getInsurancePacks()).containsOnly(insurancePackBack);
        assertThat(insurancePackBack.getWarranties()).containsOnly(warranty);

        warranty.removeInsurancePacks(insurancePackBack);
        assertThat(warranty.getInsurancePacks()).doesNotContain(insurancePackBack);
        assertThat(insurancePackBack.getWarranties()).doesNotContain(warranty);

        warranty.insurancePacks(new HashSet<>(Set.of(insurancePackBack)));
        assertThat(warranty.getInsurancePacks()).containsOnly(insurancePackBack);
        assertThat(insurancePackBack.getWarranties()).containsOnly(warranty);

        warranty.setInsurancePacks(new HashSet<>());
        assertThat(warranty.getInsurancePacks()).doesNotContain(insurancePackBack);
        assertThat(insurancePackBack.getWarranties()).doesNotContain(warranty);
    }
}
