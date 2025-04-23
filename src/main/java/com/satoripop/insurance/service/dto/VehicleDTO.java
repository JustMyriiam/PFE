package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.Brand;
import com.satoripop.insurance.domain.enumeration.Energy;
import com.satoripop.insurance.domain.enumeration.Gearbox;
import com.satoripop.insurance.domain.enumeration.RegistrationType;
import com.satoripop.insurance.domain.enumeration.TechnicalInspectionStatus;
import com.satoripop.insurance.domain.enumeration.VehicleUsage;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Vehicle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDTO implements Serializable {

    private UUID id;

    private String registrationNumber;

    private RegistrationType registrationType;

    private LocalDate firstRegistrationDate;

    private TechnicalInspectionStatus technicalInspectionStatus;

    private LocalDate expirationDate;

    private Float newValue;

    private Float marketValue;

    private Brand brand;

    private String model;

    private Integer fiscalPower;

    private String chassisNumber;

    private Energy energy;

    private String genre;

    private Integer nbrOfSeats;

    private Integer nbrOfStandingPlaces;

    private Integer emptyWeight;

    private Integer payload;

    private Integer bonusMalus;

    private String vehicleAge;

    private Integer mileage;

    private Long numberOfDoors;

    private Gearbox gearbox;

    private String color;

    private VehicleUsage usage;

    private Boolean isNew;

    private Boolean hasGarage;

    private Boolean hasParking;

    private Boolean hasAlarmSystem;

    private Boolean hasSeatbeltAlarm;

    private Boolean hasRearCamera;

    private Boolean hasRearRadar;

    private Boolean hasAbsSystem;

    private Boolean hasGPS;

    private Boolean hasAirbag;

    private Boolean navette;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public LocalDate getFirstRegistrationDate() {
        return firstRegistrationDate;
    }

    public void setFirstRegistrationDate(LocalDate firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
    }

    public TechnicalInspectionStatus getTechnicalInspectionStatus() {
        return technicalInspectionStatus;
    }

    public void setTechnicalInspectionStatus(TechnicalInspectionStatus technicalInspectionStatus) {
        this.technicalInspectionStatus = technicalInspectionStatus;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Float getNewValue() {
        return newValue;
    }

    public void setNewValue(Float newValue) {
        this.newValue = newValue;
    }

    public Float getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(Float marketValue) {
        this.marketValue = marketValue;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getFiscalPower() {
        return fiscalPower;
    }

    public void setFiscalPower(Integer fiscalPower) {
        this.fiscalPower = fiscalPower;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getNbrOfSeats() {
        return nbrOfSeats;
    }

    public void setNbrOfSeats(Integer nbrOfSeats) {
        this.nbrOfSeats = nbrOfSeats;
    }

    public Integer getNbrOfStandingPlaces() {
        return nbrOfStandingPlaces;
    }

    public void setNbrOfStandingPlaces(Integer nbrOfStandingPlaces) {
        this.nbrOfStandingPlaces = nbrOfStandingPlaces;
    }

    public Integer getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(Integer emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public Integer getPayload() {
        return payload;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
    }

    public Integer getBonusMalus() {
        return bonusMalus;
    }

    public void setBonusMalus(Integer bonusMalus) {
        this.bonusMalus = bonusMalus;
    }

    public String getVehicleAge() {
        return vehicleAge;
    }

    public void setVehicleAge(String vehicleAge) {
        this.vehicleAge = vehicleAge;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Long getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(Long numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public Gearbox getGearbox() {
        return gearbox;
    }

    public void setGearbox(Gearbox gearbox) {
        this.gearbox = gearbox;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleUsage getUsage() {
        return usage;
    }

    public void setUsage(VehicleUsage usage) {
        this.usage = usage;
    }

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getHasGarage() {
        return hasGarage;
    }

    public void setHasGarage(Boolean hasGarage) {
        this.hasGarage = hasGarage;
    }

    public Boolean getHasParking() {
        return hasParking;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    public Boolean getHasAlarmSystem() {
        return hasAlarmSystem;
    }

    public void setHasAlarmSystem(Boolean hasAlarmSystem) {
        this.hasAlarmSystem = hasAlarmSystem;
    }

    public Boolean getHasSeatbeltAlarm() {
        return hasSeatbeltAlarm;
    }

    public void setHasSeatbeltAlarm(Boolean hasSeatbeltAlarm) {
        this.hasSeatbeltAlarm = hasSeatbeltAlarm;
    }

    public Boolean getHasRearCamera() {
        return hasRearCamera;
    }

    public void setHasRearCamera(Boolean hasRearCamera) {
        this.hasRearCamera = hasRearCamera;
    }

    public Boolean getHasRearRadar() {
        return hasRearRadar;
    }

    public void setHasRearRadar(Boolean hasRearRadar) {
        this.hasRearRadar = hasRearRadar;
    }

    public Boolean getHasAbsSystem() {
        return hasAbsSystem;
    }

    public void setHasAbsSystem(Boolean hasAbsSystem) {
        this.hasAbsSystem = hasAbsSystem;
    }

    public Boolean getHasGPS() {
        return hasGPS;
    }

    public void setHasGPS(Boolean hasGPS) {
        this.hasGPS = hasGPS;
    }

    public Boolean getHasAirbag() {
        return hasAirbag;
    }

    public void setHasAirbag(Boolean hasAirbag) {
        this.hasAirbag = hasAirbag;
    }

    public Boolean getNavette() {
        return navette;
    }

    public void setNavette(Boolean navette) {
        this.navette = navette;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDTO)) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id='" + getId() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", registrationType='" + getRegistrationType() + "'" +
            ", firstRegistrationDate='" + getFirstRegistrationDate() + "'" +
            ", technicalInspectionStatus='" + getTechnicalInspectionStatus() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", newValue=" + getNewValue() +
            ", marketValue=" + getMarketValue() +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", fiscalPower=" + getFiscalPower() +
            ", chassisNumber='" + getChassisNumber() + "'" +
            ", energy='" + getEnergy() + "'" +
            ", genre='" + getGenre() + "'" +
            ", nbrOfSeats=" + getNbrOfSeats() +
            ", nbrOfStandingPlaces=" + getNbrOfStandingPlaces() +
            ", emptyWeight=" + getEmptyWeight() +
            ", payload=" + getPayload() +
            ", bonusMalus=" + getBonusMalus() +
            ", vehicleAge='" + getVehicleAge() + "'" +
            ", mileage=" + getMileage() +
            ", numberOfDoors=" + getNumberOfDoors() +
            ", gearbox='" + getGearbox() + "'" +
            ", color='" + getColor() + "'" +
            ", usage='" + getUsage() + "'" +
            ", isNew='" + getIsNew() + "'" +
            ", hasGarage='" + getHasGarage() + "'" +
            ", hasParking='" + getHasParking() + "'" +
            ", hasAlarmSystem='" + getHasAlarmSystem() + "'" +
            ", hasSeatbeltAlarm='" + getHasSeatbeltAlarm() + "'" +
            ", hasRearCamera='" + getHasRearCamera() + "'" +
            ", hasRearRadar='" + getHasRearRadar() + "'" +
            ", hasAbsSystem='" + getHasAbsSystem() + "'" +
            ", hasGPS='" + getHasGPS() + "'" +
            ", hasAirbag='" + getHasAirbag() + "'" +
            ", navette='" + getNavette() + "'" +
            "}";
    }
}
