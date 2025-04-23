package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GovernorateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GovernorateDTO.class);
        GovernorateDTO governorateDTO1 = new GovernorateDTO();
        governorateDTO1.setId(UUID.randomUUID());
        GovernorateDTO governorateDTO2 = new GovernorateDTO();
        assertThat(governorateDTO1).isNotEqualTo(governorateDTO2);
        governorateDTO2.setId(governorateDTO1.getId());
        assertThat(governorateDTO1).isEqualTo(governorateDTO2);
        governorateDTO2.setId(UUID.randomUUID());
        assertThat(governorateDTO1).isNotEqualTo(governorateDTO2);
        governorateDTO1.setId(null);
        assertThat(governorateDTO1).isNotEqualTo(governorateDTO2);
    }
}
