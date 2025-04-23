package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class WarrantyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarrantyDTO.class);
        WarrantyDTO warrantyDTO1 = new WarrantyDTO();
        warrantyDTO1.setId(UUID.randomUUID());
        WarrantyDTO warrantyDTO2 = new WarrantyDTO();
        assertThat(warrantyDTO1).isNotEqualTo(warrantyDTO2);
        warrantyDTO2.setId(warrantyDTO1.getId());
        assertThat(warrantyDTO1).isEqualTo(warrantyDTO2);
        warrantyDTO2.setId(UUID.randomUUID());
        assertThat(warrantyDTO1).isNotEqualTo(warrantyDTO2);
        warrantyDTO1.setId(null);
        assertThat(warrantyDTO1).isNotEqualTo(warrantyDTO2);
    }
}
