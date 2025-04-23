package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.ClaimTestSamples.*;
import static com.satoripop.insurance.domain.ClientTestSamples.*;
import static com.satoripop.insurance.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClaimTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Claim.class);
        Claim claim1 = getClaimSample1();
        Claim claim2 = new Claim();
        assertThat(claim1).isNotEqualTo(claim2);

        claim2.setId(claim1.getId());
        assertThat(claim1).isEqualTo(claim2);

        claim2 = getClaimSample2();
        assertThat(claim1).isNotEqualTo(claim2);
    }

    @Test
    void clientTest() {
        Claim claim = getClaimRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        claim.setClient(clientBack);
        assertThat(claim.getClient()).isEqualTo(clientBack);

        claim.client(null);
        assertThat(claim.getClient()).isNull();
    }

    @Test
    void contractTest() {
        Claim claim = getClaimRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        claim.setContract(contractBack);
        assertThat(claim.getContract()).isEqualTo(contractBack);

        claim.contract(null);
        assertThat(claim.getContract()).isNull();
    }
}
