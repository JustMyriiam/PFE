package com.satoripop.insurance.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClientAddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientAddressDTO.class);
        ClientAddressDTO clientAddressDTO1 = new ClientAddressDTO();
        clientAddressDTO1.setId(UUID.randomUUID());
        ClientAddressDTO clientAddressDTO2 = new ClientAddressDTO();
        assertThat(clientAddressDTO1).isNotEqualTo(clientAddressDTO2);
        clientAddressDTO2.setId(clientAddressDTO1.getId());
        assertThat(clientAddressDTO1).isEqualTo(clientAddressDTO2);
        clientAddressDTO2.setId(UUID.randomUUID());
        assertThat(clientAddressDTO1).isNotEqualTo(clientAddressDTO2);
        clientAddressDTO1.setId(null);
        assertThat(clientAddressDTO1).isNotEqualTo(clientAddressDTO2);
    }
}
