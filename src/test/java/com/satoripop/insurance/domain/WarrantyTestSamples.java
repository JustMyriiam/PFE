package com.satoripop.insurance.domain;

import java.util.UUID;

public class WarrantyTestSamples {

    public static Warranty getWarrantySample1() {
        return new Warranty().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).name("name1");
    }

    public static Warranty getWarrantySample2() {
        return new Warranty().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).name("name2");
    }

    public static Warranty getWarrantyRandomSampleGenerator() {
        return new Warranty().id(UUID.randomUUID()).name(UUID.randomUUID().toString());
    }
}
