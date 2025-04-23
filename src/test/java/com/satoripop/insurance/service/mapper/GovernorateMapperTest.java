package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.GovernorateAsserts.*;
import static com.satoripop.insurance.domain.GovernorateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GovernorateMapperTest {

    private GovernorateMapper governorateMapper;

    @BeforeEach
    void setUp() {
        governorateMapper = new GovernorateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGovernorateSample1();
        var actual = governorateMapper.toEntity(governorateMapper.toDto(expected));
        assertGovernorateAllPropertiesEquals(expected, actual);
    }
}
