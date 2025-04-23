package com.satoripop.insurance.domain;

import java.util.UUID;

public class ClientAddressTestSamples {

    public static ClientAddress getClientAddressSample1() {
        return new ClientAddress().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).address("address1").region("region1");
    }

    public static ClientAddress getClientAddressSample2() {
        return new ClientAddress().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).address("address2").region("region2");
    }

    public static ClientAddress getClientAddressRandomSampleGenerator() {
        return new ClientAddress().id(UUID.randomUUID()).address(UUID.randomUUID().toString()).region(UUID.randomUUID().toString());
    }
}
