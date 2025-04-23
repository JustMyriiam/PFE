package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.CityTestSamples.*;
import static com.satoripop.insurance.domain.GovernorateTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class GovernorateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Governorate.class);
        Governorate governorate1 = getGovernorateSample1();
        Governorate governorate2 = new Governorate();
        assertThat(governorate1).isNotEqualTo(governorate2);

        governorate2.setId(governorate1.getId());
        assertThat(governorate1).isEqualTo(governorate2);

        governorate2 = getGovernorateSample2();
        assertThat(governorate1).isNotEqualTo(governorate2);
    }

    @Test
    void citiesTest() {
        Governorate governorate = getGovernorateRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        governorate.addCities(cityBack);
        assertThat(governorate.getCities()).containsOnly(cityBack);
        assertThat(cityBack.getGovernorate()).isEqualTo(governorate);

        governorate.removeCities(cityBack);
        assertThat(governorate.getCities()).doesNotContain(cityBack);
        assertThat(cityBack.getGovernorate()).isNull();

        governorate.cities(new HashSet<>(Set.of(cityBack)));
        assertThat(governorate.getCities()).containsOnly(cityBack);
        assertThat(cityBack.getGovernorate()).isEqualTo(governorate);

        governorate.setCities(new HashSet<>());
        assertThat(governorate.getCities()).doesNotContain(cityBack);
        assertThat(cityBack.getGovernorate()).isNull();
    }
}
