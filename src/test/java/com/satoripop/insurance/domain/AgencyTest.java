package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.AgencyTestSamples.*;
import static com.satoripop.insurance.domain.CityTestSamples.*;
import static com.satoripop.insurance.domain.ContractTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AgencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agency.class);
        Agency agency1 = getAgencySample1();
        Agency agency2 = new Agency();
        assertThat(agency1).isNotEqualTo(agency2);

        agency2.setId(agency1.getId());
        assertThat(agency1).isEqualTo(agency2);

        agency2 = getAgencySample2();
        assertThat(agency1).isNotEqualTo(agency2);
    }

    @Test
    void contractsTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        Contract contractBack = getContractRandomSampleGenerator();

        agency.addContracts(contractBack);
        assertThat(agency.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getAgency()).isEqualTo(agency);

        agency.removeContracts(contractBack);
        assertThat(agency.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getAgency()).isNull();

        agency.contracts(new HashSet<>(Set.of(contractBack)));
        assertThat(agency.getContracts()).containsOnly(contractBack);
        assertThat(contractBack.getAgency()).isEqualTo(agency);

        agency.setContracts(new HashSet<>());
        assertThat(agency.getContracts()).doesNotContain(contractBack);
        assertThat(contractBack.getAgency()).isNull();
    }

    @Test
    void cityTest() {
        Agency agency = getAgencyRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        agency.setCity(cityBack);
        assertThat(agency.getCity()).isEqualTo(cityBack);

        agency.city(null);
        assertThat(agency.getCity()).isNull();
    }
}
