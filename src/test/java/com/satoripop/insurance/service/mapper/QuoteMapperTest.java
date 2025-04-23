package com.satoripop.insurance.service.mapper;

import static com.satoripop.insurance.domain.QuoteAsserts.*;
import static com.satoripop.insurance.domain.QuoteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteMapperTest {

    private QuoteMapper quoteMapper;

    @BeforeEach
    void setUp() {
        quoteMapper = new QuoteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getQuoteSample1();
        var actual = quoteMapper.toEntity(quoteMapper.toDto(expected));
        assertQuoteAllPropertiesEquals(expected, actual);
    }
}
