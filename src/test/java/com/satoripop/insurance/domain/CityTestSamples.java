package com.satoripop.insurance.domain;

import java.util.UUID;

public class CityTestSamples {

    public static City getCitySample1() {
        return new City().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1").postalCode("postalCode1");
    }

    public static City getCitySample2() {
        return new City().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2").postalCode("postalCode2");
    }

    public static City getCityRandomSampleGenerator() {
        return new City().id(UUID.randomUUID()).name(UUID.randomUUID().toString()).postalCode(UUID.randomUUID().toString());
    }
}
