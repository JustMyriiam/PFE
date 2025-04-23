package com.satoripop.insurance.domain;

import java.util.UUID;

public class AgencyTestSamples {

    public static Agency getAgencySample1() {
        return new Agency()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .address("address1")
            .region("region1")
            .phoneNumber("phoneNumber1")
            .managerName("managerName1");
    }

    public static Agency getAgencySample2() {
        return new Agency()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .address("address2")
            .region("region2")
            .phoneNumber("phoneNumber2")
            .managerName("managerName2");
    }

    public static Agency getAgencyRandomSampleGenerator() {
        return new Agency()
            .id(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .region(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .managerName(UUID.randomUUID().toString());
    }
}
