package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.WarrantyAsserts.*;
import static com.satoripop.insurance.domain.WarrantyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WarrantyMapperTest {

    private WarrantyMapper warrantyMapper;

    @BeforeEach
    void setUp() {
        warrantyMapper = new WarrantyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWarrantySample1();
        var actual = warrantyMapper.toEntity(warrantyMapper.toDto(expected));
        assertWarrantyAllPropertiesEquals(expected, actual);
    }
}
