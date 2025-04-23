package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.InsurancePackName;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.InsurancePack} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.InsurancePackResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /insurance-packs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePackCriteria implements Serializable, Criteria {

    /**
     * Class for filtering InsurancePackName
     */
    public static class InsurancePackNameFilter extends Filter<InsurancePackName> {

        public InsurancePackNameFilter() {}

        public InsurancePackNameFilter(InsurancePackNameFilter filter) {
            super(filter);
        }

        @Override
        public InsurancePackNameFilter copy() {
            return new InsurancePackNameFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private InsurancePackNameFilter name;

    private StringFilter desciption;

    private FloatFilter price;

    private UUIDFilter warrantiesId;

    private UUIDFilter vehicleId;

    private Boolean distinct;

    public InsurancePackCriteria() {}

    public InsurancePackCriteria(InsurancePackCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(InsurancePackNameFilter::copy).orElse(null);
        this.desciption = other.optionalDesciption().map(StringFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(FloatFilter::copy).orElse(null);
        this.warrantiesId = other.optionalWarrantiesId().map(UUIDFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InsurancePackCriteria copy() {
        return new InsurancePackCriteria(this);
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

    public InsurancePackNameFilter getName() {
        return name;
    }

    public Optional<InsurancePackNameFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public InsurancePackNameFilter name() {
        if (name == null) {
            setName(new InsurancePackNameFilter());
        }
        return name;
    }

    public void setName(InsurancePackNameFilter name) {
        this.name = name;
    }

    public StringFilter getDesciption() {
        return desciption;
    }

    public Optional<StringFilter> optionalDesciption() {
        return Optional.ofNullable(desciption);
    }

    public StringFilter desciption() {
        if (desciption == null) {
            setDesciption(new StringFilter());
        }
        return desciption;
    }

    public void setDesciption(StringFilter desciption) {
        this.desciption = desciption;
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

    public UUIDFilter getWarrantiesId() {
        return warrantiesId;
    }

    public Optional<UUIDFilter> optionalWarrantiesId() {
        return Optional.ofNullable(warrantiesId);
    }

    public UUIDFilter warrantiesId() {
        if (warrantiesId == null) {
            setWarrantiesId(new UUIDFilter());
        }
        return warrantiesId;
    }

    public void setWarrantiesId(UUIDFilter warrantiesId) {
        this.warrantiesId = warrantiesId;
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
        final InsurancePackCriteria that = (InsurancePackCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(desciption, that.desciption) &&
            Objects.equals(price, that.price) &&
            Objects.equals(warrantiesId, that.warrantiesId) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, desciption, price, warrantiesId, vehicleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePackCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDesciption().map(f -> "desciption=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalWarrantiesId().map(f -> "warrantiesId=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
