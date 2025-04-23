package com.satoripop.insurance.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Warranty} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.WarrantyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /warranties?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarrantyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private FloatFilter limit;

    private FloatFilter franchise;

    private FloatFilter price;

    private BooleanFilter mandatory;

    private UUIDFilter insurancePacksId;

    private Boolean distinct;

    public WarrantyCriteria() {}

    public WarrantyCriteria(WarrantyCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.limit = other.optionalLimit().map(FloatFilter::copy).orElse(null);
        this.franchise = other.optionalFranchise().map(FloatFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(FloatFilter::copy).orElse(null);
        this.mandatory = other.optionalMandatory().map(BooleanFilter::copy).orElse(null);
        this.insurancePacksId = other.optionalInsurancePacksId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public WarrantyCriteria copy() {
        return new WarrantyCriteria(this);
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

    public FloatFilter getLimit() {
        return limit;
    }

    public Optional<FloatFilter> optionalLimit() {
        return Optional.ofNullable(limit);
    }

    public FloatFilter limit() {
        if (limit == null) {
            setLimit(new FloatFilter());
        }
        return limit;
    }

    public void setLimit(FloatFilter limit) {
        this.limit = limit;
    }

    public FloatFilter getFranchise() {
        return franchise;
    }

    public Optional<FloatFilter> optionalFranchise() {
        return Optional.ofNullable(franchise);
    }

    public FloatFilter franchise() {
        if (franchise == null) {
            setFranchise(new FloatFilter());
        }
        return franchise;
    }

    public void setFranchise(FloatFilter franchise) {
        this.franchise = franchise;
    }

    public FloatFilter getPrice() {
        return price;
    }

    public Optional<FloatFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public FloatFilter price() {
        if (price == null) {
            setPrice(new FloatFilter());
        }
        return price;
    }

    public void setPrice(FloatFilter price) {
        this.price = price;
    }

    public BooleanFilter getMandatory() {
        return mandatory;
    }

    public Optional<BooleanFilter> optionalMandatory() {
        return Optional.ofNullable(mandatory);
    }

    public BooleanFilter mandatory() {
        if (mandatory == null) {
            setMandatory(new BooleanFilter());
        }
        return mandatory;
    }

    public void setMandatory(BooleanFilter mandatory) {
        this.mandatory = mandatory;
    }

    public UUIDFilter getInsurancePacksId() {
        return insurancePacksId;
    }

    public Optional<UUIDFilter> optionalInsurancePacksId() {
        return Optional.ofNullable(insurancePacksId);
    }

    public UUIDFilter insurancePacksId() {
        if (insurancePacksId == null) {
            setInsurancePacksId(new UUIDFilter());
        }
        return insurancePacksId;
    }

    public void setInsurancePacksId(UUIDFilter insurancePacksId) {
        this.insurancePacksId = insurancePacksId;
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
        final WarrantyCriteria that = (WarrantyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(limit, that.limit) &&
            Objects.equals(franchise, that.franchise) &&
            Objects.equals(price, that.price) &&
            Objects.equals(mandatory, that.mandatory) &&
            Objects.equals(insurancePacksId, that.insurancePacksId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, limit, franchise, price, mandatory, insurancePacksId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarrantyCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalLimit().map(f -> "limit=" + f + ", ").orElse("") +
            optionalFranchise().map(f -> "franchise=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalMandatory().map(f -> "mandatory=" + f + ", ").orElse("") +
            optionalInsurancePacksId().map(f -> "insurancePacksId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
