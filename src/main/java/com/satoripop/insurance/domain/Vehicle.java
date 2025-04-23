package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.Brand;
import com.satoripop.insurance.domain.enumeration.Energy;
import com.satoripop.insurance.domain.enumeration.Gearbox;
import com.satoripop.insurance.domain.enumeration.RegistrationType;
import com.satoripop.insurance.domain.enumeration.TechnicalInspectionStatus;
import com.satoripop.insurance.domain.enumeration.VehicleUsage;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "vehicle")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "registration_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private RegistrationType registrationType;

    @Column(name = "first_registration_date")
    private LocalDate firstRegistrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "technical_inspection_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TechnicalInspectionStatus technicalInspectionStatus;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "new_value")
    private Float newValue;

    @Column(name = "market_value")
    private Float marketValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Brand brand;

    @Column(name = "model")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String model;

    @Column(name = "fiscal_power")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer fiscalPower;

    @Column(name = "chassis_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String chassisNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Energy energy;

    @Column(name = "genre")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String genre;

    @Column(name = "nbr_of_seats")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbrOfSeats;

    @Column(name = "nbr_of_standing_places")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbrOfStandingPlaces;

    @Column(name = "empty_weight")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer emptyWeight;

    @Column(name = "payload")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer payload;

    @Column(name = "bonus_malus")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer bonusMalus;

    @Column(name = "vehicle_age")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String vehicleAge;

    @Column(name = "mileage")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer mileage;

    @Column(name = "number_of_doors")
    private Long numberOfDoors;

    @Enumerated(EnumType.STRING)
    @Column(name = "gearbox")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Gearbox gearbox;

    @Column(name = "color")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private VehicleUsage usage;

    @Column(name = "is_new")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isNew;

    @Column(name = "has_garage")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasGarage;

    @Column(name = "has_parking")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasParking;

    @Column(name = "has_alarm_system")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasAlarmSystem;

    @Column(name = "has_seatbelt_alarm")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasSeatbeltAlarm;

    @Column(name = "has_rear_camera")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasRearCamera;

    @Column(name = "has_rear_radar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasRearRadar;

    @Column(name = "has_abs_system")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasAbsSystem;

    @Column(name = "has_gps")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasGPS;

    @Column(name = "has_airbag")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean hasAirbag;

    @Column(name = "navette")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean navette;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "warranties", "vehicle" }, allowSetters = true)
    private Set<InsurancePack> insurancePacks = new HashSet<>();

    @JsonIgnoreProperties(value = { "vehicle", "client" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @org.springframework.data.annotation.Transient
    private Quote quote;

    @JsonIgnoreProperties(value = { "vehicle", "documents", "claims", "client", "agency" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @org.springframework.data.annotation.Transient
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Vehicle id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Vehicle registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public RegistrationType getRegistrationType() {
        return this.registrationType;
    }

    public Vehicle registrationType(RegistrationType registrationType) {
        this.setRegistrationType(registrationType);
        return this;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public LocalDate getFirstRegistrationDate() {
        return this.firstRegistrationDate;
    }

    public Vehicle firstRegistrationDate(LocalDate firstRegistrationDate) {
        this.setFirstRegistrationDate(firstRegistrationDate);
        return this;
    }

    public void setFirstRegistrationDate(LocalDate firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
    }

    public TechnicalInspectionStatus getTechnicalInspectionStatus() {
        return this.technicalInspectionStatus;
    }

    public Vehicle technicalInspectionStatus(TechnicalInspectionStatus technicalInspectionStatus) {
        this.setTechnicalInspectionStatus(technicalInspectionStatus);
        return this;
    }

    public void setTechnicalInspectionStatus(TechnicalInspectionStatus technicalInspectionStatus) {
        this.technicalInspectionStatus = technicalInspectionStatus;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public Vehicle expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Float getNewValue() {
        return this.newValue;
    }

    public Vehicle newValue(Float newValue) {
        this.setNewValue(newValue);
        return this;
    }

    public void setNewValue(Float newValue) {
        this.newValue = newValue;
    }

    public Float getMarketValue() {
        return this.marketValue;
    }

    public Vehicle marketValue(Float marketValue) {
        this.setMarketValue(marketValue);
        return this;
    }

    public void setMarketValue(Float marketValue) {
        this.marketValue = marketValue;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public Vehicle brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Vehicle model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getFiscalPower() {
        return this.fiscalPower;
    }

    public Vehicle fiscalPower(Integer fiscalPower) {
        this.setFiscalPower(fiscalPower);
        return this;
    }

    public void setFiscalPower(Integer fiscalPower) {
        this.fiscalPower = fiscalPower;
    }

    public String getChassisNumber() {
        return this.chassisNumber;
    }

    public Vehicle chassisNumber(String chassisNumber) {
        this.setChassisNumber(chassisNumber);
        return this;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public Energy getEnergy() {
        return this.energy;
    }

    public Vehicle energy(Energy energy) {
        this.setEnergy(energy);
        return this;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public String getGenre() {
        return this.genre;
    }

    public Vehicle genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getNbrOfSeats() {
        return this.nbrOfSeats;
    }

    public Vehicle nbrOfSeats(Integer nbrOfSeats) {
        this.setNbrOfSeats(nbrOfSeats);
        return this;
    }

    public void setNbrOfSeats(Integer nbrOfSeats) {
        this.nbrOfSeats = nbrOfSeats;
    }

    public Integer getNbrOfStandingPlaces() {
        return this.nbrOfStandingPlaces;
    }

    public Vehicle nbrOfStandingPlaces(Integer nbrOfStandingPlaces) {
        this.setNbrOfStandingPlaces(nbrOfStandingPlaces);
        return this;
    }

    public void setNbrOfStandingPlaces(Integer nbrOfStandingPlaces) {
        this.nbrOfStandingPlaces = nbrOfStandingPlaces;
    }

    public Integer getEmptyWeight() {
        return this.emptyWeight;
    }

    public Vehicle emptyWeight(Integer emptyWeight) {
        this.setEmptyWeight(emptyWeight);
        return this;
    }

    public void setEmptyWeight(Integer emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public Integer getPayload() {
        return this.payload;
    }

    public Vehicle payload(Integer payload) {
        this.setPayload(payload);
        return this;
    }

    public void setPayload(Integer payload) {
        this.payload = payload;
    }

    public Integer getBonusMalus() {
        return this.bonusMalus;
    }

    public Vehicle bonusMalus(Integer bonusMalus) {
        this.setBonusMalus(bonusMalus);
        return this;
    }

    public void setBonusMalus(Integer bonusMalus) {
        this.bonusMalus = bonusMalus;
    }

    public String getVehicleAge() {
        return this.vehicleAge;
    }

    public Vehicle vehicleAge(String vehicleAge) {
        this.setVehicleAge(vehicleAge);
        return this;
    }

    public void setVehicleAge(String vehicleAge) {
        this.vehicleAge = vehicleAge;
    }

    public Integer getMileage() {
        return this.mileage;
    }

    public Vehicle mileage(Integer mileage) {
        this.setMileage(mileage);
        return this;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public Long getNumberOfDoors() {
        return this.numberOfDoors;
    }

    public Vehicle numberOfDoors(Long numberOfDoors) {
        this.setNumberOfDoors(numberOfDoors);
        return this;
    }

    public void setNumberOfDoors(Long numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public Gearbox getGearbox() {
        return this.gearbox;
    }

    public Vehicle gearbox(Gearbox gearbox) {
        this.setGearbox(gearbox);
        return this;
    }

    public void setGearbox(Gearbox gearbox) {
        this.gearbox = gearbox;
    }

    public String getColor() {
        return this.color;
    }

    public Vehicle color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleUsage getUsage() {
        return this.usage;
    }

    public Vehicle usage(VehicleUsage usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(VehicleUsage usage) {
        this.usage = usage;
    }

    public Boolean getIsNew() {
        return this.isNew;
    }

    public Vehicle isNew(Boolean isNew) {
        this.setIsNew(isNew);
        return this;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Boolean getHasGarage() {
        return this.hasGarage;
    }

    public Vehicle hasGarage(Boolean hasGarage) {
        this.setHasGarage(hasGarage);
        return this;
    }

    public void setHasGarage(Boolean hasGarage) {
        this.hasGarage = hasGarage;
    }

    public Boolean getHasParking() {
        return this.hasParking;
    }

    public Vehicle hasParking(Boolean hasParking) {
        this.setHasParking(hasParking);
        return this;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking;
    }

    public Boolean getHasAlarmSystem() {
        return this.hasAlarmSystem;
    }

    public Vehicle hasAlarmSystem(Boolean hasAlarmSystem) {
        this.setHasAlarmSystem(hasAlarmSystem);
        return this;
    }

    public void setHasAlarmSystem(Boolean hasAlarmSystem) {
        this.hasAlarmSystem = hasAlarmSystem;
    }

    public Boolean getHasSeatbeltAlarm() {
        return this.hasSeatbeltAlarm;
    }

    public Vehicle hasSeatbeltAlarm(Boolean hasSeatbeltAlarm) {
        this.setHasSeatbeltAlarm(hasSeatbeltAlarm);
        return this;
    }

    public void setHasSeatbeltAlarm(Boolean hasSeatbeltAlarm) {
        this.hasSeatbeltAlarm = hasSeatbeltAlarm;
    }

    public Boolean getHasRearCamera() {
        return this.hasRearCamera;
    }

    public Vehicle hasRearCamera(Boolean hasRearCamera) {
        this.setHasRearCamera(hasRearCamera);
        return this;
    }

    public void setHasRearCamera(Boolean hasRearCamera) {
        this.hasRearCamera = hasRearCamera;
    }

    public Boolean getHasRearRadar() {
        return this.hasRearRadar;
    }

    public Vehicle hasRearRadar(Boolean hasRearRadar) {
        this.setHasRearRadar(hasRearRadar);
        return this;
    }

    public void setHasRearRadar(Boolean hasRearRadar) {
        this.hasRearRadar = hasRearRadar;
    }

    public Boolean getHasAbsSystem() {
        return this.hasAbsSystem;
    }

    public Vehicle hasAbsSystem(Boolean hasAbsSystem) {
        this.setHasAbsSystem(hasAbsSystem);
        return this;
    }

    public void setHasAbsSystem(Boolean hasAbsSystem) {
        this.hasAbsSystem = hasAbsSystem;
    }

    public Boolean getHasGPS() {
        return this.hasGPS;
    }

    public Vehicle hasGPS(Boolean hasGPS) {
        this.setHasGPS(hasGPS);
        return this;
    }

    public void setHasGPS(Boolean hasGPS) {
        this.hasGPS = hasGPS;
    }

    public Boolean getHasAirbag() {
        return this.hasAirbag;
    }

    public Vehicle hasAirbag(Boolean hasAirbag) {
        this.setHasAirbag(hasAirbag);
        return this;
    }

    public void setHasAirbag(Boolean hasAirbag) {
        this.hasAirbag = hasAirbag;
    }

    public Boolean getNavette() {
        return this.navette;
    }

    public Vehicle navette(Boolean navette) {
        this.setNavette(navette);
        return this;
    }

    public void setNavette(Boolean navette) {
        this.navette = navette;
    }

    public Set<InsurancePack> getInsurancePacks() {
        return this.insurancePacks;
    }

    public void setInsurancePacks(Set<InsurancePack> insurancePacks) {
        if (this.insurancePacks != null) {
            this.insurancePacks.forEach(i -> i.setVehicle(null));
        }
        if (insurancePacks != null) {
            insurancePacks.forEach(i -> i.setVehicle(this));
        }
        this.insurancePacks = insurancePacks;
    }

    public Vehicle insurancePacks(Set<InsurancePack> insurancePacks) {
        this.setInsurancePacks(insurancePacks);
        return this;
    }

    public Vehicle addInsurancePack(InsurancePack insurancePack) {
        this.insurancePacks.add(insurancePack);
        insurancePack.setVehicle(this);
        return this;
    }

    public Vehicle removeInsurancePack(InsurancePack insurancePack) {
        this.insurancePacks.remove(insurancePack);
        insurancePack.setVehicle(null);
        return this;
    }

    public Quote getQuote() {
        return this.quote;
    }

    public void setQuote(Quote quote) {
        if (this.quote != null) {
            this.quote.setVehicle(null);
        }
        if (quote != null) {
            quote.setVehicle(this);
        }
        this.quote = quote;
    }

    public Vehicle quote(Quote quote) {
        this.setQuote(quote);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        if (this.contract != null) {
            this.contract.setVehicle(null);
        }
        if (contract != null) {
            contract.setVehicle(this);
        }
        this.contract = contract;
    }

    public Vehicle contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
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
