package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.CityTestSamples.*;
import static com.satoripop.insurance.domain.ClientAddressTestSamples.*;
import static com.satoripop.insurance.domain.ClientTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientAddress.class);
        ClientAddress clientAddress1 = getClientAddressSample1();
        ClientAddress clientAddress2 = new ClientAddress();
        assertThat(clientAddress1).isNotEqualTo(clientAddress2);

        clientAddress2.setId(clientAddress1.getId());
        assertThat(clientAddress1).isEqualTo(clientAddress2);

        clientAddress2 = getClientAddressSample2();
        assertThat(clientAddress1).isNotEqualTo(clientAddress2);
    }

    @Test
    void clientsTest() {
        ClientAddress clientAddress = getClientAddressRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        clientAddress.addClients(clientBack);
        assertThat(clientAddress.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getClientAddress()).isEqualTo(clientAddress);

        clientAddress.removeClients(clientBack);
        assertThat(clientAddress.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getClientAddress()).isNull();

        clientAddress.clients(new HashSet<>(Set.of(clientBack)));
        assertThat(clientAddress.getClients()).containsOnly(clientBack);
        assertThat(clientBack.getClientAddress()).isEqualTo(clientAddress);

        clientAddress.setClients(new HashSet<>());
        assertThat(clientAddress.getClients()).doesNotContain(clientBack);
        assertThat(clientBack.getClientAddress()).isNull();
    }

    @Test
    void cityTest() {
        ClientAddress clientAddress = getClientAddressRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        clientAddress.setCity(cityBack);
        assertThat(clientAddress.getCity()).isEqualTo(cityBack);

        clientAddress.city(null);
        assertThat(clientAddress.getCity()).isNull();
    }
}
