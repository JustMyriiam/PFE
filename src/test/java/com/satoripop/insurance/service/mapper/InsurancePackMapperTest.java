package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.InsurancePackAsserts.*;
import static com.satoripop.insurance.domain.InsurancePackTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InsurancePackMapperTest {

    private InsurancePackMapper insurancePackMapper;

    @BeforeEach
    void setUp() {
        insurancePackMapper = new InsurancePackMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInsurancePackSample1();
        var actual = insurancePackMapper.toEntity(insurancePackMapper.toDto(expected));
        assertInsurancePackAllPropertiesEquals(expected, actual);
    }
}
