package com.satoripop.insurance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.ClientAddress} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.ClientAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /client-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter address;

    private StringFilter region;

    private UUIDFilter clientsId;

    private UUIDFilter cityId;

    private Boolean distinct;

    public ClientAddressCriteria() {}

    public ClientAddressCriteria(ClientAddressCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.region = other.optionalRegion().map(StringFilter::copy).orElse(null);
        this.clientsId = other.optionalClientsId().map(UUIDFilter::copy).orElse(null);
        this.cityId = other.optionalCityId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientAddressCriteria copy() {
        return new ClientAddressCriteria(this);
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

    public UUIDFilter getClientsId() {
        return clientsId;
    }

    public Optional<UUIDFilter> optionalClientsId() {
        return Optional.ofNullable(clientsId);
    }

    public UUIDFilter clientsId() {
        if (clientsId == null) {
            setClientsId(new UUIDFilter());
        }
        return clientsId;
    }

    public void setClientsId(UUIDFilter clientsId) {
        this.clientsId = clientsId;
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
        final ClientAddressCriteria that = (ClientAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(address, that.address) &&
            Objects.equals(region, that.region) &&
            Objects.equals(clientsId, that.clientsId) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address, region, clientsId, cityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientAddressCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalRegion().map(f -> "region=" + f + ", ").orElse("") +
            optionalClientsId().map(f -> "clientsId=" + f + ", ").orElse("") +
            optionalCityId().map(f -> "cityId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
