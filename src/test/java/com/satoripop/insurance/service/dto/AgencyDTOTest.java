package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AgencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgencyDTO.class);
        AgencyDTO agencyDTO1 = new AgencyDTO();
        agencyDTO1.setId(UUID.randomUUID());
        AgencyDTO agencyDTO2 = new AgencyDTO();
        assertThat(agencyDTO1).isNotEqualTo(agencyDTO2);
        agencyDTO2.setId(agencyDTO1.getId());
        assertThat(agencyDTO1).isEqualTo(agencyDTO2);
        agencyDTO2.setId(UUID.randomUUID());
        assertThat(agencyDTO1).isNotEqualTo(agencyDTO2);
        agencyDTO1.setId(null);
        assertThat(agencyDTO1).isNotEqualTo(agencyDTO2);
    }
}
