package com.satoripop.insurance.domain;

import java.util.UUID;

public class QuoteTestSamples {

    public static Quote getQuoteSample1() {
        return new Quote().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static Quote getQuoteSample2() {
        return new Quote().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static Quote getQuoteRandomSampleGenerator() {
        return new Quote().id(UUID.randomUUID());
    }
}
