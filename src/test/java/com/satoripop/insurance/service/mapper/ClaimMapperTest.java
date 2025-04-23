package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.ClaimAsserts.*;
import static com.satoripop.insurance.domain.ClaimTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClaimMapperTest {

    private ClaimMapper claimMapper;

    @BeforeEach
    void setUp() {
        claimMapper = new ClaimMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClaimSample1();
        var actual = claimMapper.toEntity(claimMapper.toDto(expected));
        assertClaimAllPropertiesEquals(expected, actual);
    }
}
