package com.satoripop.insurance.domain;

import static com.satoripop.insurance.domain.ClientTestSamples.*;
import static com.satoripop.insurance.domain.QuoteTestSamples.*;
import static com.satoripop.insurance.domain.VehicleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.satoripop.insurance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quote.class);
        Quote quote1 = getQuoteSample1();
        Quote quote2 = new Quote();
        assertThat(quote1).isNotEqualTo(quote2);

        quote2.setId(quote1.getId());
        assertThat(quote1).isEqualTo(quote2);

        quote2 = getQuoteSample2();
        assertThat(quote1).isNotEqualTo(quote2);
    }

    @Test
    void vehicleTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        quote.setVehicle(vehicleBack);
        assertThat(quote.getVehicle()).isEqualTo(vehicleBack);

        quote.vehicle(null);
        assertThat(quote.getVehicle()).isNull();
    }

    @Test
    void clientTest() {
        Quote quote = getQuoteRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        quote.setClient(clientBack);
        assertThat(quote.getClient()).isEqualTo(clientBack);

        quote.client(null);
        assertThat(quote.getClient()).isNull();
    }
}
