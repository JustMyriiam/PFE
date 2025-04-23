package com.satoripop.insurance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Quote} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.QuoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /quotes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private LocalDateFilter date;

    private FloatFilter estimatedAmount;

    private UUIDFilter vehicleId;

    private UUIDFilter clientId;

    private Boolean distinct;

    public QuoteCriteria() {}

    public QuoteCriteria(QuoteCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.estimatedAmount = other.optionalEstimatedAmount().map(FloatFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(UUIDFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public QuoteCriteria copy() {
        return new QuoteCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public Optional<LocalDateFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public LocalDateFilter date() {
        if (date == null) {
            setDate(new LocalDateFilter());
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public FloatFilter getEstimatedAmount() {
        return estimatedAmount;
    }

    public Optional<FloatFilter> optionalEstimatedAmount() {
        return Optional.ofNullable(estimatedAmount);
    }

    public FloatFilter estimatedAmount() {
        if (estimatedAmount == null) {
            setEstimatedAmount(new FloatFilter());
        }
        return estimatedAmount;
    }

    public void setEstimatedAmount(FloatFilter estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public UUIDFilter getVehicleId() {
        return vehicleId;
    }

    public Optional<UUIDFilter> optionalVehicleId() {
        return Optional.ofNullable(vehicleId);
    }

    public UUIDFilter vehicleId() {
        if (vehicleId == null) {
            setVehicleId(new UUIDFilter());
        }
        return vehicleId;
    }

    public void setVehicleId(UUIDFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public UUIDFilter getClientId() {
        return clientId;
    }

    public Optional<UUIDFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public UUIDFilter clientId() {
        if (clientId == null) {
            setClientId(new UUIDFilter());
        }
        return clientId;
    }

    public void setClientId(UUIDFilter clientId) {
        this.clientId = clientId;
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
        final QuoteCriteria that = (QuoteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(estimatedAmount, that.estimatedAmount) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, estimatedAmount, vehicleId, clientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalEstimatedAmount().map(f -> "estimatedAmount=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
