package com.satoripop.insurance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.City} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.CityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter postalCode;

    private UUIDFilter clientAddressesId;

    private UUIDFilter agenciesId;

    private UUIDFilter governorateId;

    private Boolean distinct;

    public CityCriteria() {}

    public CityCriteria(CityCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(StringFilter::copy).orElse(null);
        this.clientAddressesId = other.optionalClientAddressesId().map(UUIDFilter::copy).orElse(null);
        this.agenciesId = other.optionalAgenciesId().map(UUIDFilter::copy).orElse(null);
        this.governorateId = other.optionalGovernorateId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CityCriteria copy() {
        return new CityCriteria(this);
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

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public Optional<StringFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new StringFilter());
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public UUIDFilter getClientAddressesId() {
        return clientAddressesId;
    }

    public Optional<UUIDFilter> optionalClientAddressesId() {
        return Optional.ofNullable(clientAddressesId);
    }

    public UUIDFilter clientAddressesId() {
        if (clientAddressesId == null) {
            setClientAddressesId(new UUIDFilter());
        }
        return clientAddressesId;
    }

    public void setClientAddressesId(UUIDFilter clientAddressesId) {
        this.clientAddressesId = clientAddressesId;
    }

    public UUIDFilter getAgenciesId() {
        return agenciesId;
    }

    public Optional<UUIDFilter> optionalAgenciesId() {
        return Optional.ofNullable(agenciesId);
    }

    public UUIDFilter agenciesId() {
        if (agenciesId == null) {
            setAgenciesId(new UUIDFilter());
        }
        return agenciesId;
    }

    public void setAgenciesId(UUIDFilter agenciesId) {
        this.agenciesId = agenciesId;
    }

    public UUIDFilter getGovernorateId() {
        return governorateId;
    }

    public Optional<UUIDFilter> optionalGovernorateId() {
        return Optional.ofNullable(governorateId);
    }

    public UUIDFilter governorateId() {
        if (governorateId == null) {
            setGovernorateId(new UUIDFilter());
        }
        return governorateId;
    }

    public void setGovernorateId(UUIDFilter governorateId) {
        this.governorateId = governorateId;
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
        final CityCriteria that = (CityCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(clientAddressesId, that.clientAddressesId) &&
            Objects.equals(agenciesId, that.agenciesId) &&
            Objects.equals(governorateId, that.governorateId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, postalCode, clientAddressesId, agenciesId, governorateId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalClientAddressesId().map(f -> "clientAddressesId=" + f + ", ").orElse("") +
            optionalAgenciesId().map(f -> "agenciesId=" + f + ", ").orElse("") +
            optionalGovernorateId().map(f -> "governorateId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
