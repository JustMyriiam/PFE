package com.satoripop.insurance.domain;

import java.util.UUID;

public class ClaimTestSamples {

    public static Claim getClaimSample1() {
        return new Claim().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).type("type1").description("description1");
    }

    public static Claim getClaimSample2() {
        return new Claim().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).type("type2").description("description2");
    }

    public static Claim getClaimRandomSampleGenerator() {
        return new Claim().id(UUID.randomUUID()).type(UUID.randomUUID().toString()).description(UUID.randomUUID().toString());
    }
}
