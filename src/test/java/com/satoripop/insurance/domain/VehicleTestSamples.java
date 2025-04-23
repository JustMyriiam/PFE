package com.satoripop.insurance.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .registrationNumber("registrationNumber1")
            .model("model1")
            .fiscalPower(1)
            .chassisNumber("chassisNumber1")
            .genre("genre1")
            .nbrOfSeats(1)
            .nbrOfStandingPlaces(1)
            .emptyWeight(1)
            .payload(1)
            .bonusMalus(1)
            .vehicleAge("vehicleAge1")
            .mileage(1)
            .numberOfDoors(1L)
            .color("color1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .registrationNumber("registrationNumber2")
            .model("model2")
            .fiscalPower(2)
            .chassisNumber("chassisNumber2")
            .genre("genre2")
            .nbrOfSeats(2)
            .nbrOfStandingPlaces(2)
            .emptyWeight(2)
            .payload(2)
            .bonusMalus(2)
            .vehicleAge("vehicleAge2")
            .mileage(2)
            .numberOfDoors(2L)
            .color("color2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(UUID.randomUUID())
            .registrationNumber(UUID.randomUUID().toString())
            .model(UUID.randomUUID().toString())
            .fiscalPower(intCount.incrementAndGet())
            .chassisNumber(UUID.randomUUID().toString())
            .genre(UUID.randomUUID().toString())
            .nbrOfSeats(intCount.incrementAndGet())
            .nbrOfStandingPlaces(intCount.incrementAndGet())
            .emptyWeight(intCount.incrementAndGet())
            .payload(intCount.incrementAndGet())
            .bonusMalus(intCount.incrementAndGet())
            .vehicleAge(UUID.randomUUID().toString())
            .mileage(intCount.incrementAndGet())
            .numberOfDoors(longCount.incrementAndGet())
            .color(UUID.randomUUID().toString());
    }
}
