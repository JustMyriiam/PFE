package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.ClientAddressAsserts.*;
import static com.satoripop.insurance.domain.ClientAddressTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientAddressMapperTest {

    private ClientAddressMapper clientAddressMapper;

    @BeforeEach
    void setUp() {
        clientAddressMapper = new ClientAddressMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientAddressSample1();
        var actual = clientAddressMapper.toEntity(clientAddressMapper.toDto(expected));
        assertClientAddressAllPropertiesEquals(expected, actual);
    }
}
