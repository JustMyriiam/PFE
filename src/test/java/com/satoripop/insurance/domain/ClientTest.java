package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.ClaimTestSamples.*;
import static com.satoripop.insurance.domain.ClientAddressTestSamples.*;
import static com.satoripop.insurance.domain.ClientTestSamples.*;
import static com.satoripop.insurance.domain.ContractTestSamples.*;
import static com.satoripop.insurance.domain.QuoteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void quotesTest() {
        Client client = getClientRandomSampleGenerator();
        Quote quoteBack = getQuoteRandomSampleGenerator();

        client.addQuotes(quoteBack);
        assertThat(client.getQuotes()).containsOnly(quoteBack);
        assertThat(quoteBack.getClient()).isEqualTo(client);

        client.removeQuotes(quoteBack);
        assertThat(client.getQuotes()).doesNotContain(quoteBack);
        assertThat(quoteBack.getClient()).isNull();

        client.quotes(new HashSet<>(Set.of(quoteBack)));
        assertThat(client.getQuotes()).containsOnly(quoteBack);
        assertThat(quoteBack.getClient()).isEqualTo(client);

        client.setQuotes(new HashSet<>());
        assertThat(client.getQuotes()).doesNotContain(quoteBack);
        assertThat(quoteBack.getClient()).isNull();
    }

    @Test
    void contractsTest() {
        Client client = getClientRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        client.addContracts(contractBack);
        assertThat(client.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClient()).isEqualTo(client);

        client.removeContracts(contractBack);
        assertThat(client.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClient()).isNull();

        client.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(client.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getClient()).isEqualTo(client);

        client.setContracts(new HashSet<>());
        assertThat(client.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getClient()).isNull();
    }

    @Test
    void claimsTest() {
        Client client = getClientRandomSampleGenerator();
        Claim claimBack = getClaimRandomSampleGenerator();

        client.addClaims(claimBack);
        assertThat(client.getClaims()).containsOnly(claimBack);
        assertThat(claimBack.getClient()).isEqualTo(client);

        client.removeClaims(claimBack);
        assertThat(client.getClaims()).doesNotContain(claimBack);
        assertThat(claimBack.getClient()).isNull();

        client.claims(new HashSet<>(Set.of(claimBack)));
        assertThat(client.getClaims()).containsOnly(claimBack);
        assertThat(claimBack.getClient()).isEqualTo(client);

        client.setClaims(new HashSet<>());
        assertThat(client.getClaims()).doesNotContain(claimBack);
        assertThat(claimBack.getClient()).isNull();
    }

    @Test
    void clientAddressTest() {
        Client client = getClientRandomSampleGenerator();
        ClientAddress clientAddressBack = getClientAddressRandomSampleGenerator();

        client.setClientAddress(clientAddressBack);
        assertThat(client.getClientAddress()).isEqualTo(clientAddressBack);

        client.clientAddress(null);
        assertThat(client.getClientAddress()).isNull();
    }
}
