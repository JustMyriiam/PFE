package com.satoripop.insurance.domain;

import java.util.UUID;

public class InsurancePackTestSamples {

    public static InsurancePack getInsurancePackSample1() {
        return new InsurancePack().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).desciption("desciption1");
    }

    public static InsurancePack getInsurancePackSample2() {
        return new InsurancePack().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).desciption("desciption2");
    }

    public static InsurancePack getInsurancePackRandomSampleGenerator() {
        return new InsurancePack().id(UUID.randomUUID()).desciption(UUID.randomUUID().toString());
    }
}
