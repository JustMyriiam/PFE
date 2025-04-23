package com.satoripop.insurance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Client getClientSample1() {
        return new Client()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .lastName("lastName1")
            .firstName("firstName1")
            .identityNumber("identityNumber1")
            .birthPlace("birthPlace1")
            .identityPlaceOfIssue("identityPlaceOfIssue1")
            .nbrOfchildren(1)
            .professionalEmail("professionalEmail1")
            .personalEmail("personalEmail1")
            .primaryPhoneNumber("primaryPhoneNumber1")
            .secondaryPhoneNumber("secondaryPhoneNumber1")
            .faxNumber("faxNumber1")
            .nationality("nationality1")
            .jobTitle("jobTitle1")
            .bank("bank1")
            .agency("agency1")
            .rib("rib1")
            .drivingLicenseNumber("drivingLicenseNumber1")
            .drivingLicenseCategory("drivingLicenseCategory1");
    }

    public static Client getClientSample2() {
        return new Client()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .lastName("lastName2")
            .firstName("firstName2")
            .identityNumber("identityNumber2")
            .birthPlace("birthPlace2")
            .identityPlaceOfIssue("identityPlaceOfIssue2")
            .nbrOfchildren(2)
            .professionalEmail("professionalEmail2")
            .personalEmail("personalEmail2")
            .primaryPhoneNumber("primaryPhoneNumber2")
            .secondaryPhoneNumber("secondaryPhoneNumber2")
            .faxNumber("faxNumber2")
            .nationality("nationality2")
            .jobTitle("jobTitle2")
            .bank("bank2")
            .agency("agency2")
            .rib("rib2")
            .drivingLicenseNumber("drivingLicenseNumber2")
            .drivingLicenseCategory("drivingLicenseCategory2");
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(UUID.randomUUID())
            .lastName(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .identityNumber(UUID.randomUUID().toString())
            .birthPlace(UUID.randomUUID().toString())
            .identityPlaceOfIssue(UUID.randomUUID().toString())
            .nbrOfchildren(intCount.incrementAndGet())
            .professionalEmail(UUID.randomUUID().toString())
            .personalEmail(UUID.randomUUID().toString())
            .primaryPhoneNumber(UUID.randomUUID().toString())
            .secondaryPhoneNumber(UUID.randomUUID().toString())
            .faxNumber(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .jobTitle(UUID.randomUUID().toString())
            .bank(UUID.randomUUID().toString())
            .agency(UUID.randomUUID().toString())
            .rib(UUID.randomUUID().toString())
            .drivingLicenseNumber(UUID.randomUUID().toString())
            .drivingLicenseCategory(UUID.randomUUID().toString());
    }
}
