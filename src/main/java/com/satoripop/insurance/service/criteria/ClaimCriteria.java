package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.ClaimStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Claim} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.ClaimResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /claims?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClaimCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ClaimStatus
     */
    public static class ClaimStatusFilter extends Filter<ClaimStatus> {

        public ClaimStatusFilter() {}

        public ClaimStatusFilter(ClaimStatusFilter filter) {
            super(filter);
        }

        @Override
        public ClaimStatusFilter copy() {
            return new ClaimStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter type;

    private StringFilter description;

    private LocalDateFilter date;

    private ClaimStatusFilter status;

    private UUIDFilter clientId;

    private UUIDFilter contractId;

    private Boolean distinct;

    public ClaimCriteria() {}

    public ClaimCriteria(ClaimCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.type = other.optionalType().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.date = other.optionalDate().map(LocalDateFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ClaimStatusFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.contractId = other.optionalContractId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClaimCriteria copy() {
        return new ClaimCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public Optional<StringFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public StringFilter type() {
        if (type == null) {
            setType(new StringFilter());
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public ClaimStatusFilter getStatus() {
        return status;
    }

    public Optional<ClaimStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ClaimStatusFilter status() {
        if (status == null) {
            setStatus(new ClaimStatusFilter());
        }
        return status;
    }

    public void setStatus(ClaimStatusFilter status) {
        this.status = status;
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
        final ClaimCriteria that = (ClaimCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(description, that.description) &&
            Objects.equals(date, that.date) &&
            Objects.equals(status, that.status) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, date, status, clientId, contractId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClaimCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalContractId().map(f -> "contractId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
