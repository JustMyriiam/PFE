package com.satoripop.insurance.domain;

import java.util.UUID;

public class GovernorateTestSamples {

    public static Governorate getGovernorateSample1() {
        return new Governorate().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1");
    }

    public static Governorate getGovernorateSample2() {
        return new Governorate().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2");
    }

    public static Governorate getGovernorateRandomSampleGenerator() {
        return new Governorate().id(UUID.randomUUID()).name(UUID.randomUUID().toString());
    }
}
