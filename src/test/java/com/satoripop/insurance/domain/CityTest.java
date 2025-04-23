package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.AgencyTestSamples.*;
import static com.satoripop.insurance.domain.CityTestSamples.*;
import static com.satoripop.insurance.domain.ClientAddressTestSamples.*;
import static com.satoripop.insurance.domain.GovernorateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void clientAddressesTest() {
        City city = getCityRandomSampleGenerator();
        ClientAddress clientAddressBack = getClientAddressRandomSampleGenerator();

        city.addClientAddresses(clientAddressBack);
        assertThat(city.getClientAddresses()).containsOnly(clientAddressBack);
        assertThat(clientAddressBack.getCity()).isEqualTo(city);

        city.removeClientAddresses(clientAddressBack);
        assertThat(city.getClientAddresses()).doesNotContain(clientAddressBack);
        assertThat(clientAddressBack.getCity()).isNull();

        city.clientAddresses(new HashSet<>(Set.of(clientAddressBack)));
        assertThat(city.getClientAddresses()).containsOnly(clientAddressBack);
        assertThat(clientAddressBack.getCity()).isEqualTo(city);

        city.setClientAddresses(new HashSet<>());
        assertThat(city.getClientAddresses()).doesNotContain(clientAddressBack);
        assertThat(clientAddressBack.getCity()).isNull();
    }

    @Test
    void agenciesTest() {
        City city = getCityRandomSampleGenerator();
        Agency agencyBack = getAgencyRandomSampleGenerator();

        city.addAgencies(agencyBack);
        assertThat(city.getAgencies()).containsOnly(agencyBack);
        assertThat(agencyBack.getCity()).isEqualTo(city);

        city.removeAgencies(agencyBack);
        assertThat(city.getAgencies()).doesNotContain(agencyBack);
        assertThat(agencyBack.getCity()).isNull();

        city.agencies(new HashSet<>(Set.of(agencyBack)));
        assertThat(city.getAgencies()).containsOnly(agencyBack);
        assertThat(agencyBack.getCity()).isEqualTo(city);

        city.setAgencies(new HashSet<>());
        assertThat(city.getAgencies()).doesNotContain(agencyBack);
        assertThat(agencyBack.getCity()).isNull();
    }

    @Test
    void governorateTest() {
        City city = getCityRandomSampleGenerator();
        Governorate governorateBack = getGovernorateRandomSampleGenerator();

        city.setGovernorate(governorateBack);
        assertThat(city.getGovernorate()).isEqualTo(governorateBack);

        city.governorate(null);
        assertThat(city.getGovernorate()).isNull();
    }
}
