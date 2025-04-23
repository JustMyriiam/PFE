package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.AgencyTestSamples.*;
import static com.satoripop.insurance.domain.ClaimTestSamples.*;
import static com.satoripop.insurance.domain.ClientTestSamples.*;
import static com.satoripop.insurance.domain.ContractTestSamples.*;
import static com.satoripop.insurance.domain.DocumentTestSamples.*;
import static com.satoripop.insurance.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contract.class);
        Contract contract1 = getContractSample1();
        Contract contract2 = new Contract();
        assertThat(contract1).isNotEqualTo(contract2);

        contract2.setId(contract1.getId());
        assertThat(contract1).isEqualTo(contract2);

        contract2 = getContractSample2();
        assertThat(contract1).isNotEqualTo(contract2);
    }

    @Test
    void vehicleTest() {
        Contract contract = getContractRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        contract.setVehicle(vehicleBack);
        assertThat(contract.getVehicle()).isEqualTo(vehicleBack);

        contract.vehicle(null);
        assertThat(contract.getVehicle()).isNull();
    }

    @Test
    void documentsTest() {
        Contract contract = getContractRandomSampleGenerator();
        Document documentBack = getDocumentRandomSampleGenerator();

        contract.addDocuments(documentBack);
        assertThat(contract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getContract()).isEqualTo(contract);

        contract.removeDocuments(documentBack);
        assertThat(contract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getContract()).isNull();

        contract.documents(new HashSet<>(Set.of(documentBack)));
        assertThat(contract.getDocuments()).containsOnly(documentBack);
        assertThat(documentBack.getContract()).isEqualTo(contract);

        contract.setDocuments(new HashSet<>());
        assertThat(contract.getDocuments()).doesNotContain(documentBack);
        assertThat(documentBack.getContract()).isNull();
    }

    @Test
    void claimsTest() {
        Contract contract = getContractRandomSampleGenerator();
        Claim claimBack = getClaimRandomSampleGenerator();

        contract.addClaims(claimBack);
        assertThat(contract.getClaims()).containsOnly(claimBack);
        assertThat(claimBack.getContract()).isEqualTo(contract);

        contract.removeClaims(claimBack);
        assertThat(contract.getClaims()).doesNotContain(claimBack);
        assertThat(claimBack.getContract()).isNull();

        contract.claims(new HashSet<>(Set.of(claimBack)));
        assertThat(contract.getClaims()).containsOnly(claimBack);
        assertThat(claimBack.getContract()).isEqualTo(contract);

        contract.setClaims(new HashSet<>());
        assertThat(contract.getClaims()).doesNotContain(claimBack);
        assertThat(claimBack.getContract()).isNull();
    }

    @Test
    void clientTest() {
        Contract contract = getContractRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        contract.setClient(clientBack);
        assertThat(contract.getClient()).isEqualTo(clientBack);

        contract.client(null);
        assertThat(contract.getClient()).isNull();
    }

    @Test
    void agencyTest() {
        Contract contract = getContractRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        contract.setAgency(agencyBack);
        assertThat(contract.getAgency()).isEqualTo(agencyBack);

        contract.agency(null);
        assertThat(contract.getAgency()).isNull();
    }
}
