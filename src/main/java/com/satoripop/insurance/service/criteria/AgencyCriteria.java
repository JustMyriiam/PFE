package com.satoripop.insurance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Agency} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.AgencyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /agencies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter address;

    private StringFilter region;

    private StringFilter phoneNumber;

    private StringFilter managerName;

    private UUIDFilter contractsId;

    private UUIDFilter cityId;

    private Boolean distinct;

    public AgencyCriteria() {}

    public AgencyCriteria(AgencyCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.region = other.optionalRegion().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.managerName = other.optionalManagerName().map(StringFilter::copy).orElse(null);
        this.contractsId = other.optionalContractsId().map(UUIDFilter::copy).orElse(null);
        this.cityId = other.optionalCityId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AgencyCriteria copy() {
        return new AgencyCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getRegion() {
        return region;
    }

    public Optional<StringFilter> optionalRegion() {
        return Optional.ofNullable(region);
    }

    public StringFilter region() {
        if (region == null) {
            setRegion(new StringFilter());
        }
        return region;
    }

    public void setRegion(StringFilter region) {
        this.region = region;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getManagerName() {
        return managerName;
    }

    public Optional<StringFilter> optionalManagerName() {
        return Optional.ofNullable(managerName);
    }

    public StringFilter managerName() {
        if (managerName == null) {
            setManagerName(new StringFilter());
        }
        return managerName;
    }

    public void setManagerName(StringFilter managerName) {
        this.managerName = managerName;
    }

    public UUIDFilter getContractsId() {
        return contractsId;
    }

    public Optional<UUIDFilter> optionalContractsId() {
        return Optional.ofNullable(contractsId);
    }

    public UUIDFilter contractsId() {
        if (contractsId == null) {
            setContractsId(new UUIDFilter());
        }
        return contractsId;
    }

    public void setContractsId(UUIDFilter contractsId) {
        this.contractsId = contractsId;
    }

    public UUIDFilter getCityId() {
        return cityId;
    }

    public Optional<UUIDFilter> optionalCityId() {
        return Optional.ofNullable(cityId);
    }

    public UUIDFilter cityId() {
        if (cityId == null) {
            setCityId(new UUIDFilter());
        }
        return cityId;
    }

    public void setCityId(UUIDFilter cityId) {
        this.cityId = cityId;
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
        final AgencyCriteria that = (AgencyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(region, that.region) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(managerName, that.managerName) &&
            Objects.equals(contractsId, that.contractsId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, region, phoneNumber, managerName, contractsId, cityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalRegion().map(f -> "region=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalManagerName().map(f -> "managerName=" + f + ", ").orElse("") +
            optionalContractsId().map(f -> "contractsId=" + f + ", ").orElse("") +
            optionalCityId().map(f -> "cityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
