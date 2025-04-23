package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InsurancePackDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InsurancePackDTO.class);
        InsurancePackDTO insurancePackDTO1 = new InsurancePackDTO();
        insurancePackDTO1.setId(UUID.randomUUID());
        InsurancePackDTO insurancePackDTO2 = new InsurancePackDTO();
        assertThat(insurancePackDTO1).isNotEqualTo(insurancePackDTO2);
        insurancePackDTO2.setId(insurancePackDTO1.getId());
        assertThat(insurancePackDTO1).isEqualTo(insurancePackDTO2);
        insurancePackDTO2.setId(UUID.randomUUID());
        assertThat(insurancePackDTO1).isNotEqualTo(insurancePackDTO2);
        insurancePackDTO1.setId(null);
        assertThat(insurancePackDTO1).isNotEqualTo(insurancePackDTO2);
    }
}
