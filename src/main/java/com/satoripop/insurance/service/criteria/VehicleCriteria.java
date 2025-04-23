package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.Brand;
import com.satoripop.insurance.domain.enumeration.Energy;
import com.satoripop.insurance.domain.enumeration.Gearbox;
import com.satoripop.insurance.domain.enumeration.RegistrationType;
import com.satoripop.insurance.domain.enumeration.TechnicalInspectionStatus;
import com.satoripop.insurance.domain.enumeration.VehicleUsage;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Vehicle} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.VehicleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RegistrationType
     */
    public static class RegistrationTypeFilter extends Filter<RegistrationType> {

        public RegistrationTypeFilter() {}

        public RegistrationTypeFilter(RegistrationTypeFilter filter) {
            super(filter);
        }

        @Override
        public RegistrationTypeFilter copy() {
            return new RegistrationTypeFilter(this);
        }
    }

    /**
     * Class for filtering TechnicalInspectionStatus
     */
    public static class TechnicalInspectionStatusFilter extends Filter<TechnicalInspectionStatus> {

        public TechnicalInspectionStatusFilter() {}

        public TechnicalInspectionStatusFilter(TechnicalInspectionStatusFilter filter) {
            super(filter);
        }

        @Override
        public TechnicalInspectionStatusFilter copy() {
            return new TechnicalInspectionStatusFilter(this);
        }
    }

    /**
     * Class for filtering Brand
     */
    public static class BrandFilter extends Filter<Brand> {

        public BrandFilter() {}

        public BrandFilter(BrandFilter filter) {
            super(filter);
        }

        @Override
        public BrandFilter copy() {
            return new BrandFilter(this);
        }
    }

    /**
     * Class for filtering Energy
     */
    public static class EnergyFilter extends Filter<Energy> {

        public EnergyFilter() {}

        public EnergyFilter(EnergyFilter filter) {
            super(filter);
        }

        @Override
        public EnergyFilter copy() {
            return new EnergyFilter(this);
        }
    }

    /**
     * Class for filtering Gearbox
     */
    public static class GearboxFilter extends Filter<Gearbox> {

        public GearboxFilter() {}

        public GearboxFilter(GearboxFilter filter) {
            super(filter);
        }

        @Override
        public GearboxFilter copy() {
            return new GearboxFilter(this);
        }
    }

    /**
     * Class for filtering VehicleUsage
     */
    public static class VehicleUsageFilter extends Filter<VehicleUsage> {

        public VehicleUsageFilter() {}

        public VehicleUsageFilter(VehicleUsageFilter filter) {
            super(filter);
        }

        @Override
        public VehicleUsageFilter copy() {
            return new VehicleUsageFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter registrationNumber;

    private RegistrationTypeFilter registrationType;

    private LocalDateFilter firstRegistrationDate;

    private TechnicalInspectionStatusFilter technicalInspectionStatus;

    private LocalDateFilter expirationDate;

    private FloatFilter newValue;

    private FloatFilter marketValue;

    private BrandFilter brand;

    private StringFilter model;

    private IntegerFilter fiscalPower;

    private StringFilter chassisNumber;

    private EnergyFilter energy;

    private StringFilter genre;

    private IntegerFilter nbrOfSeats;

    private IntegerFilter nbrOfStandingPlaces;

    private IntegerFilter emptyWeight;

    private IntegerFilter payload;

    private IntegerFilter bonusMalus;

    private StringFilter vehicleAge;

    private IntegerFilter mileage;

    private LongFilter numberOfDoors;

    private GearboxFilter gearbox;

    private StringFilter color;

    private VehicleUsageFilter usage;

    private BooleanFilter isNew;

    private BooleanFilter hasGarage;

    private BooleanFilter hasParking;

    private BooleanFilter hasAlarmSystem;

    private BooleanFilter hasSeatbeltAlarm;

    private BooleanFilter hasRearCamera;

    private BooleanFilter hasRearRadar;

    private BooleanFilter hasAbsSystem;

    private BooleanFilter hasGPS;

    private BooleanFilter hasAirbag;

    private BooleanFilter navette;

    private UUIDFilter insurancePackId;

    private UUIDFilter quoteId;

    private UUIDFilter contractId;

    private Boolean distinct;

    public VehicleCriteria() {}

    public VehicleCriteria(VehicleCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.registrationNumber = other.optionalRegistrationNumber().map(StringFilter::copy).orElse(null);
        this.registrationType = other.optionalRegistrationType().map(RegistrationTypeFilter::copy).orElse(null);
        this.firstRegistrationDate = other.optionalFirstRegistrationDate().map(LocalDateFilter::copy).orElse(null);
        this.technicalInspectionStatus = other.optionalTechnicalInspectionStatus().map(TechnicalInspectionStatusFilter::copy).orElse(null);
        this.expirationDate = other.optionalExpirationDate().map(LocalDateFilter::copy).orElse(null);
        this.newValue = other.optionalNewValue().map(FloatFilter::copy).orElse(null);
        this.marketValue = other.optionalMarketValue().map(FloatFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(BrandFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.fiscalPower = other.optionalFiscalPower().map(IntegerFilter::copy).orElse(null);
        this.chassisNumber = other.optionalChassisNumber().map(StringFilter::copy).orElse(null);
        this.energy = other.optionalEnergy().map(EnergyFilter::copy).orElse(null);
        this.genre = other.optionalGenre().map(StringFilter::copy).orElse(null);
        this.nbrOfSeats = other.optionalNbrOfSeats().map(IntegerFilter::copy).orElse(null);
        this.nbrOfStandingPlaces = other.optionalNbrOfStandingPlaces().map(IntegerFilter::copy).orElse(null);
        this.emptyWeight = other.optionalEmptyWeight().map(IntegerFilter::copy).orElse(null);
        this.payload = other.optionalPayload().map(IntegerFilter::copy).orElse(null);
        this.bonusMalus = other.optionalBonusMalus().map(IntegerFilter::copy).orElse(null);
        this.vehicleAge = other.optionalVehicleAge().map(StringFilter::copy).orElse(null);
        this.mileage = other.optionalMileage().map(IntegerFilter::copy).orElse(null);
        this.numberOfDoors = other.optionalNumberOfDoors().map(LongFilter::copy).orElse(null);
        this.gearbox = other.optionalGearbox().map(GearboxFilter::copy).orElse(null);
        this.color = other.optionalColor().map(StringFilter::copy).orElse(null);
        this.usage = other.optionalUsage().map(VehicleUsageFilter::copy).orElse(null);
        this.isNew = other.optionalIsNew().map(BooleanFilter::copy).orElse(null);
        this.hasGarage = other.optionalHasGarage().map(BooleanFilter::copy).orElse(null);
        this.hasParking = other.optionalHasParking().map(BooleanFilter::copy).orElse(null);
        this.hasAlarmSystem = other.optionalHasAlarmSystem().map(BooleanFilter::copy).orElse(null);
        this.hasSeatbeltAlarm = other.optionalHasSeatbeltAlarm().map(BooleanFilter::copy).orElse(null);
        this.hasRearCamera = other.optionalHasRearCamera().map(BooleanFilter::copy).orElse(null);
        this.hasRearRadar = other.optionalHasRearRadar().map(BooleanFilter::copy).orElse(null);
        this.hasAbsSystem = other.optionalHasAbsSystem().map(BooleanFilter::copy).orElse(null);
        this.hasGPS = other.optionalHasGPS().map(BooleanFilter::copy).orElse(null);
        this.hasAirbag = other.optionalHasAirbag().map(BooleanFilter::copy).orElse(null);
        this.navette = other.optionalNavette().map(BooleanFilter::copy).orElse(null);
        this.insurancePackId = other.optionalInsurancePackId().map(UUIDFilter::copy).orElse(null);
        this.quoteId = other.optionalQuoteId().map(UUIDFilter::copy).orElse(null);
        this.contractId = other.optionalContractId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public VehicleCriteria copy() {
        return new VehicleCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getRegistrationNumber() {
        return registrationNumber;
    }

    public Optional<StringFilter> optionalRegistrationNumber() {
        return Optional.ofNullable(registrationNumber);
    }

    public StringFilter registrationNumber() {
        if (registrationNumber == null) {
            setRegistrationNumber(new StringFilter());
        }
        return registrationNumber;
    }

    public void setRegistrationNumber(StringFilter registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public RegistrationTypeFilter getRegistrationType() {
        return registrationType;
    }

    public Optional<RegistrationTypeFilter> optionalRegistrationType() {
        return Optional.ofNullable(registrationType);
    }

    public RegistrationTypeFilter registrationType() {
        if (registrationType == null) {
            setRegistrationType(new RegistrationTypeFilter());
        }
        return registrationType;
    }

    public void setRegistrationType(RegistrationTypeFilter registrationType) {
        this.registrationType = registrationType;
    }

    public LocalDateFilter getFirstRegistrationDate() {
        return firstRegistrationDate;
    }

    public Optional<LocalDateFilter> optionalFirstRegistrationDate() {
        return Optional.ofNullable(firstRegistrationDate);
    }

    public LocalDateFilter firstRegistrationDate() {
        if (firstRegistrationDate == null) {
            setFirstRegistrationDate(new LocalDateFilter());
        }
        return firstRegistrationDate;
    }

    public void setFirstRegistrationDate(LocalDateFilter firstRegistrationDate) {
        this.firstRegistrationDate = firstRegistrationDate;
    }

    public TechnicalInspectionStatusFilter getTechnicalInspectionStatus() {
        return technicalInspectionStatus;
    }

    public Optional<TechnicalInspectionStatusFilter> optionalTechnicalInspectionStatus() {
        return Optional.ofNullable(technicalInspectionStatus);
    }

    public TechnicalInspectionStatusFilter technicalInspectionStatus() {
        if (technicalInspectionStatus == null) {
            setTechnicalInspectionStatus(new TechnicalInspectionStatusFilter());
        }
        return technicalInspectionStatus;
    }

    public void setTechnicalInspectionStatus(TechnicalInspectionStatusFilter technicalInspectionStatus) {
        this.technicalInspectionStatus = technicalInspectionStatus;
    }

    public LocalDateFilter getExpirationDate() {
        return expirationDate;
    }

    public Optional<LocalDateFilter> optionalExpirationDate() {
        return Optional.ofNullable(expirationDate);
    }

    public LocalDateFilter expirationDate() {
        if (expirationDate == null) {
            setExpirationDate(new LocalDateFilter());
        }
        return expirationDate;
    }

    public void setExpirationDate(LocalDateFilter expirationDate) {
        this.expirationDate = expirationDate;
    }

    public FloatFilter getNewValue() {
        return newValue;
    }

    public Optional<FloatFilter> optionalNewValue() {
        return Optional.ofNullable(newValue);
    }

    public FloatFilter newValue() {
        if (newValue == null) {
            setNewValue(new FloatFilter());
        }
        return newValue;
    }

    public void setNewValue(FloatFilter newValue) {
        this.newValue = newValue;
    }

    public FloatFilter getMarketValue() {
        return marketValue;
    }

    public Optional<FloatFilter> optionalMarketValue() {
        return Optional.ofNullable(marketValue);
    }

    public FloatFilter marketValue() {
        if (marketValue == null) {
            setMarketValue(new FloatFilter());
        }
        return marketValue;
    }

    public void setMarketValue(FloatFilter marketValue) {
        this.marketValue = marketValue;
    }

    public BrandFilter getBrand() {
        return brand;
    }

    public Optional<BrandFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public BrandFilter brand() {
        if (brand == null) {
            setBrand(new BrandFilter());
        }
        return brand;
    }

    public void setBrand(BrandFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public IntegerFilter getFiscalPower() {
        return fiscalPower;
    }

    public Optional<IntegerFilter> optionalFiscalPower() {
        return Optional.ofNullable(fiscalPower);
    }

    public IntegerFilter fiscalPower() {
        if (fiscalPower == null) {
            setFiscalPower(new IntegerFilter());
        }
        return fiscalPower;
    }

    public void setFiscalPower(IntegerFilter fiscalPower) {
        this.fiscalPower = fiscalPower;
    }

    public StringFilter getChassisNumber() {
        return chassisNumber;
    }

    public Optional<StringFilter> optionalChassisNumber() {
        return Optional.ofNullable(chassisNumber);
    }

    public StringFilter chassisNumber() {
        if (chassisNumber == null) {
            setChassisNumber(new StringFilter());
        }
        return chassisNumber;
    }

    public void setChassisNumber(StringFilter chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public EnergyFilter getEnergy() {
        return energy;
    }

    public Optional<EnergyFilter> optionalEnergy() {
        return Optional.ofNullable(energy);
    }

    public EnergyFilter energy() {
        if (energy == null) {
            setEnergy(new EnergyFilter());
        }
        return energy;
    }

    public void setEnergy(EnergyFilter energy) {
        this.energy = energy;
    }

    public StringFilter getGenre() {
        return genre;
    }

    public Optional<StringFilter> optionalGenre() {
        return Optional.ofNullable(genre);
    }

    public StringFilter genre() {
        if (genre == null) {
            setGenre(new StringFilter());
        }
        return genre;
    }

    public void setGenre(StringFilter genre) {
        this.genre = genre;
    }

    public IntegerFilter getNbrOfSeats() {
        return nbrOfSeats;
    }

    public Optional<IntegerFilter> optionalNbrOfSeats() {
        return Optional.ofNullable(nbrOfSeats);
    }

    public IntegerFilter nbrOfSeats() {
        if (nbrOfSeats == null) {
            setNbrOfSeats(new IntegerFilter());
        }
        return nbrOfSeats;
    }

    public void setNbrOfSeats(IntegerFilter nbrOfSeats) {
        this.nbrOfSeats = nbrOfSeats;
    }

    public IntegerFilter getNbrOfStandingPlaces() {
        return nbrOfStandingPlaces;
    }

    public Optional<IntegerFilter> optionalNbrOfStandingPlaces() {
        return Optional.ofNullable(nbrOfStandingPlaces);
    }

    public IntegerFilter nbrOfStandingPlaces() {
        if (nbrOfStandingPlaces == null) {
            setNbrOfStandingPlaces(new IntegerFilter());
        }
        return nbrOfStandingPlaces;
    }

    public void setNbrOfStandingPlaces(IntegerFilter nbrOfStandingPlaces) {
        this.nbrOfStandingPlaces = nbrOfStandingPlaces;
    }

    public IntegerFilter getEmptyWeight() {
        return emptyWeight;
    }

    public Optional<IntegerFilter> optionalEmptyWeight() {
        return Optional.ofNullable(emptyWeight);
    }

    public IntegerFilter emptyWeight() {
        if (emptyWeight == null) {
            setEmptyWeight(new IntegerFilter());
        }
        return emptyWeight;
    }

    public void setEmptyWeight(IntegerFilter emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public IntegerFilter getPayload() {
        return payload;
    }

    public Optional<IntegerFilter> optionalPayload() {
        return Optional.ofNullable(payload);
    }

    public IntegerFilter payload() {
        if (payload == null) {
            setPayload(new IntegerFilter());
        }
        return payload;
    }

    public void setPayload(IntegerFilter payload) {
        this.payload = payload;
    }

    public IntegerFilter getBonusMalus() {
        return bonusMalus;
    }

    public Optional<IntegerFilter> optionalBonusMalus() {
        return Optional.ofNullable(bonusMalus);
    }

    public IntegerFilter bonusMalus() {
        if (bonusMalus == null) {
            setBonusMalus(new IntegerFilter());
        }
        return bonusMalus;
    }

    public void setBonusMalus(IntegerFilter bonusMalus) {
        this.bonusMalus = bonusMalus;
    }

    public StringFilter getVehicleAge() {
        return vehicleAge;
    }

    public Optional<StringFilter> optionalVehicleAge() {
        return Optional.ofNullable(vehicleAge);
    }

    public StringFilter vehicleAge() {
        if (vehicleAge == null) {
            setVehicleAge(new StringFilter());
        }
        return vehicleAge;
    }

    public void setVehicleAge(StringFilter vehicleAge) {
        this.vehicleAge = vehicleAge;
    }

    public IntegerFilter getMileage() {
        return mileage;
    }

    public Optional<IntegerFilter> optionalMileage() {
        return Optional.ofNullable(mileage);
    }

    public IntegerFilter mileage() {
        if (mileage == null) {
            setMileage(new IntegerFilter());
        }
        return mileage;
    }

    public void setMileage(IntegerFilter mileage) {
        this.mileage = mileage;
    }

    public LongFilter getNumberOfDoors() {
        return numberOfDoors;
    }

    public Optional<LongFilter> optionalNumberOfDoors() {
        return Optional.ofNullable(numberOfDoors);
    }

    public LongFilter numberOfDoors() {
        if (numberOfDoors == null) {
            setNumberOfDoors(new LongFilter());
        }
        return numberOfDoors;
    }

    public void setNumberOfDoors(LongFilter numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public GearboxFilter getGearbox() {
        return gearbox;
    }

    public Optional<GearboxFilter> optionalGearbox() {
        return Optional.ofNullable(gearbox);
    }

    public GearboxFilter gearbox() {
        if (gearbox == null) {
            setGearbox(new GearboxFilter());
        }
        return gearbox;
    }

    public void setGearbox(GearboxFilter gearbox) {
        this.gearbox = gearbox;
    }

    public StringFilter getColor() {
        return color;
    }

    public Optional<StringFilter> optionalColor() {
        return Optional.ofNullable(color);
    }

    public StringFilter color() {
        if (color == null) {
            setColor(new StringFilter());
        }
        return color;
    }

    public void setColor(StringFilter color) {
        this.color = color;
    }

    public VehicleUsageFilter getUsage() {
        return usage;
    }

    public Optional<VehicleUsageFilter> optionalUsage() {
        return Optional.ofNullable(usage);
    }

    public VehicleUsageFilter usage() {
        if (usage == null) {
            setUsage(new VehicleUsageFilter());
        }
        return usage;
    }

    public void setUsage(VehicleUsageFilter usage) {
        this.usage = usage;
    }

    public BooleanFilter getIsNew() {
        return isNew;
    }

    public Optional<BooleanFilter> optionalIsNew() {
        return Optional.ofNullable(isNew);
    }

    public BooleanFilter isNew() {
        if (isNew == null) {
            setIsNew(new BooleanFilter());
        }
        return isNew;
    }

    public void setIsNew(BooleanFilter isNew) {
        this.isNew = isNew;
    }

    public BooleanFilter getHasGarage() {
        return hasGarage;
    }

    public Optional<BooleanFilter> optionalHasGarage() {
        return Optional.ofNullable(hasGarage);
    }

    public BooleanFilter hasGarage() {
        if (hasGarage == null) {
            setHasGarage(new BooleanFilter());
        }
        return hasGarage;
    }

    public void setHasGarage(BooleanFilter hasGarage) {
        this.hasGarage = hasGarage;
    }

    public BooleanFilter getHasParking() {
        return hasParking;
    }

    public Optional<BooleanFilter> optionalHasParking() {
        return Optional.ofNullable(hasParking);
    }

    public BooleanFilter hasParking() {
        if (hasParking == null) {
            setHasParking(new BooleanFilter());
        }
        return hasParking;
    }

    public void setHasParking(BooleanFilter hasParking) {
        this.hasParking = hasParking;
    }

    public BooleanFilter getHasAlarmSystem() {
        return hasAlarmSystem;
    }

    public Optional<BooleanFilter> optionalHasAlarmSystem() {
        return Optional.ofNullable(hasAlarmSystem);
    }

    public BooleanFilter hasAlarmSystem() {
        if (hasAlarmSystem == null) {
            setHasAlarmSystem(new BooleanFilter());
        }
        return hasAlarmSystem;
    }

    public void setHasAlarmSystem(BooleanFilter hasAlarmSystem) {
        this.hasAlarmSystem = hasAlarmSystem;
    }

    public BooleanFilter getHasSeatbeltAlarm() {
        return hasSeatbeltAlarm;
    }

    public Optional<BooleanFilter> optionalHasSeatbeltAlarm() {
        return Optional.ofNullable(hasSeatbeltAlarm);
    }

    public BooleanFilter hasSeatbeltAlarm() {
        if (hasSeatbeltAlarm == null) {
            setHasSeatbeltAlarm(new BooleanFilter());
        }
        return hasSeatbeltAlarm;
    }

    public void setHasSeatbeltAlarm(BooleanFilter hasSeatbeltAlarm) {
        this.hasSeatbeltAlarm = hasSeatbeltAlarm;
    }

    public BooleanFilter getHasRearCamera() {
        return hasRearCamera;
    }

    public Optional<BooleanFilter> optionalHasRearCamera() {
        return Optional.ofNullable(hasRearCamera);
    }

    public BooleanFilter hasRearCamera() {
        if (hasRearCamera == null) {
            setHasRearCamera(new BooleanFilter());
        }
        return hasRearCamera;
    }

    public void setHasRearCamera(BooleanFilter hasRearCamera) {
        this.hasRearCamera = hasRearCamera;
    }

    public BooleanFilter getHasRearRadar() {
        return hasRearRadar;
    }

    public Optional<BooleanFilter> optionalHasRearRadar() {
        return Optional.ofNullable(hasRearRadar);
    }

    public BooleanFilter hasRearRadar() {
        if (hasRearRadar == null) {
            setHasRearRadar(new BooleanFilter());
        }
        return hasRearRadar;
    }

    public void setHasRearRadar(BooleanFilter hasRearRadar) {
        this.hasRearRadar = hasRearRadar;
    }

    public BooleanFilter getHasAbsSystem() {
        return hasAbsSystem;
    }

    public Optional<BooleanFilter> optionalHasAbsSystem() {
        return Optional.ofNullable(hasAbsSystem);
    }

    public BooleanFilter hasAbsSystem() {
        if (hasAbsSystem == null) {
            setHasAbsSystem(new BooleanFilter());
        }
        return hasAbsSystem;
    }

    public void setHasAbsSystem(BooleanFilter hasAbsSystem) {
        this.hasAbsSystem = hasAbsSystem;
    }

    public BooleanFilter getHasGPS() {
        return hasGPS;
    }

    public Optional<BooleanFilter> optionalHasGPS() {
        return Optional.ofNullable(hasGPS);
    }

    public BooleanFilter hasGPS() {
        if (hasGPS == null) {
            setHasGPS(new BooleanFilter());
        }
        return hasGPS;
    }

    public void setHasGPS(BooleanFilter hasGPS) {
        this.hasGPS = hasGPS;
    }

    public BooleanFilter getHasAirbag() {
        return hasAirbag;
    }

    public Optional<BooleanFilter> optionalHasAirbag() {
        return Optional.ofNullable(hasAirbag);
    }

    public BooleanFilter hasAirbag() {
        if (hasAirbag == null) {
            setHasAirbag(new BooleanFilter());
        }
        return hasAirbag;
    }

    public void setHasAirbag(BooleanFilter hasAirbag) {
        this.hasAirbag = hasAirbag;
    }

    public BooleanFilter getNavette() {
        return navette;
    }

    public Optional<BooleanFilter> optionalNavette() {
        return Optional.ofNullable(navette);
    }

    public BooleanFilter navette() {
        if (navette == null) {
            setNavette(new BooleanFilter());
        }
        return navette;
    }

    public void setNavette(BooleanFilter navette) {
        this.navette = navette;
    }

    public UUIDFilter getInsurancePackId() {
        return insurancePackId;
    }

    public Optional<UUIDFilter> optionalInsurancePackId() {
        return Optional.ofNullable(insurancePackId);
    }

    public UUIDFilter insurancePackId() {
        if (insurancePackId == null) {
            setInsurancePackId(new UUIDFilter());
        }
        return insurancePackId;
    }

    public void setInsurancePackId(UUIDFilter insurancePackId) {
        this.insurancePackId = insurancePackId;
    }

    public UUIDFilter getQuoteId() {
        return quoteId;
    }

    public Optional<UUIDFilter> optionalQuoteId() {
        return Optional.ofNullable(quoteId);
    }

    public UUIDFilter quoteId() {
        if (quoteId == null) {
            setQuoteId(new UUIDFilter());
        }
        return quoteId;
    }

    public void setQuoteId(UUIDFilter quoteId) {
        this.quoteId = quoteId;
    }

    public UUIDFilter getContractId() {
        return contractId;
    }

    public Optional<UUIDFilter> optionalContractId() {
        return Optional.ofNullable(contractId);
    }

    public UUIDFilter contractId() {
        if (contractId == null) {
            setContractId(new UUIDFilter());
        }
        return contractId;
    }

    public void setContractId(UUIDFilter contractId) {
        this.contractId = contractId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VehicleCriteria that = (VehicleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(registrationNumber, that.registrationNumber) &&
            Objects.equals(registrationType, that.registrationType) &&
            Objects.equals(firstRegistrationDate, that.firstRegistrationDate) &&
            Objects.equals(technicalInspectionStatus, that.technicalInspectionStatus) &&
            Objects.equals(expirationDate, that.expirationDate) &&
            Objects.equals(newValue, that.newValue) &&
            Objects.equals(marketValue, that.marketValue) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(fiscalPower, that.fiscalPower) &&
            Objects.equals(chassisNumber, that.chassisNumber) &&
            Objects.equals(energy, that.energy) &&
            Objects.equals(genre, that.genre) &&
            Objects.equals(nbrOfSeats, that.nbrOfSeats) &&
            Objects.equals(nbrOfStandingPlaces, that.nbrOfStandingPlaces) &&
            Objects.equals(emptyWeight, that.emptyWeight) &&
            Objects.equals(payload, that.payload) &&
            Objects.equals(bonusMalus, that.bonusMalus) &&
            Objects.equals(vehicleAge, that.vehicleAge) &&
            Objects.equals(mileage, that.mileage) &&
            Objects.equals(numberOfDoors, that.numberOfDoors) &&
            Objects.equals(gearbox, that.gearbox) &&
            Objects.equals(color, that.color) &&
            Objects.equals(usage, that.usage) &&
            Objects.equals(isNew, that.isNew) &&
            Objects.equals(hasGarage, that.hasGarage) &&
            Objects.equals(hasParking, that.hasParking) &&
            Objects.equals(hasAlarmSystem, that.hasAlarmSystem) &&
            Objects.equals(hasSeatbeltAlarm, that.hasSeatbeltAlarm) &&
            Objects.equals(hasRearCamera, that.hasRearCamera) &&
            Objects.equals(hasRearRadar, that.hasRearRadar) &&
            Objects.equals(hasAbsSystem, that.hasAbsSystem) &&
            Objects.equals(hasGPS, that.hasGPS) &&
            Objects.equals(hasAirbag, that.hasAirbag) &&
            Objects.equals(navette, that.navette) &&
            Objects.equals(insurancePackId, that.insurancePackId) &&
            Objects.equals(quoteId, that.quoteId) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            registrationNumber,
            registrationType,
            firstRegistrationDate,
            technicalInspectionStatus,
            expirationDate,
            newValue,
            marketValue,
            brand,
            model,
            fiscalPower,
            chassisNumber,
            energy,
            genre,
            nbrOfSeats,
            nbrOfStandingPlaces,
            emptyWeight,
            payload,
            bonusMalus,
            vehicleAge,
            mileage,
            numberOfDoors,
            gearbox,
            color,
            usage,
            isNew,
            hasGarage,
            hasParking,
            hasAlarmSystem,
            hasSeatbeltAlarm,
            hasRearCamera,
            hasRearRadar,
            hasAbsSystem,
            hasGPS,
            hasAirbag,
            navette,
            insurancePackId,
            quoteId,
            contractId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalRegistrationNumber().map(f -> "registrationNumber=" + f + ", ").orElse("") +
            optionalRegistrationType().map(f -> "registrationType=" + f + ", ").orElse("") +
            optionalFirstRegistrationDate().map(f -> "firstRegistrationDate=" + f + ", ").orElse("") +
            optionalTechnicalInspectionStatus().map(f -> "technicalInspectionStatus=" + f + ", ").orElse("") +
            optionalExpirationDate().map(f -> "expirationDate=" + f + ", ").orElse("") +
            optionalNewValue().map(f -> "newValue=" + f + ", ").orElse("") +
            optionalMarketValue().map(f -> "marketValue=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalFiscalPower().map(f -> "fiscalPower=" + f + ", ").orElse("") +
            optionalChassisNumber().map(f -> "chassisNumber=" + f + ", ").orElse("") +
            optionalEnergy().map(f -> "energy=" + f + ", ").orElse("") +
            optionalGenre().map(f -> "genre=" + f + ", ").orElse("") +
            optionalNbrOfSeats().map(f -> "nbrOfSeats=" + f + ", ").orElse("") +
            optionalNbrOfStandingPlaces().map(f -> "nbrOfStandingPlaces=" + f + ", ").orElse("") +
            optionalEmptyWeight().map(f -> "emptyWeight=" + f + ", ").orElse("") +
            optionalPayload().map(f -> "payload=" + f + ", ").orElse("") +
            optionalBonusMalus().map(f -> "bonusMalus=" + f + ", ").orElse("") +
            optionalVehicleAge().map(f -> "vehicleAge=" + f + ", ").orElse("") +
            optionalMileage().map(f -> "mileage=" + f + ", ").orElse("") +
            optionalNumberOfDoors().map(f -> "numberOfDoors=" + f + ", ").orElse("") +
            optionalGearbox().map(f -> "gearbox=" + f + ", ").orElse("") +
            optionalColor().map(f -> "color=" + f + ", ").orElse("") +
            optionalUsage().map(f -> "usage=" + f + ", ").orElse("") +
            optionalIsNew().map(f -> "isNew=" + f + ", ").orElse("") +
            optionalHasGarage().map(f -> "hasGarage=" + f + ", ").orElse("") +
            optionalHasParking().map(f -> "hasParking=" + f + ", ").orElse("") +
            optionalHasAlarmSystem().map(f -> "hasAlarmSystem=" + f + ", ").orElse("") +
            optionalHasSeatbeltAlarm().map(f -> "hasSeatbeltAlarm=" + f + ", ").orElse("") +
            optionalHasRearCamera().map(f -> "hasRearCamera=" + f + ", ").orElse("") +
            optionalHasRearRadar().map(f -> "hasRearRadar=" + f + ", ").orElse("") +
            optionalHasAbsSystem().map(f -> "hasAbsSystem=" + f + ", ").orElse("") +
            optionalHasGPS().map(f -> "hasGPS=" + f + ", ").orElse("") +
            optionalHasAirbag().map(f -> "hasAirbag=" + f + ", ").orElse("") +
            optionalNavette().map(f -> "navette=" + f + ", ").orElse("") +
            optionalInsurancePackId().map(f -> "insurancePackId=" + f + ", ").orElse("") +
            optionalQuoteId().map(f -> "quoteId=" + f + ", ").orElse("") +
            optionalContractId().map(f -> "contractId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
