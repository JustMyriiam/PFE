package com.satoripop.insurance.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Warranty} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WarrantyDTO implements Serializable {

    private UUID id;

    private String name;

    private Float limit;

    private Float franchise;

    private Float price;

    private Boolean mandatory;

    private Set<InsurancePackDTO> insurancePacks = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLimit() {
        return limit;
    }

    public void setLimit(Float limit) {
        this.limit = limit;
    }

    public Float getFranchise() {
        return franchise;
    }

    public void setFranchise(Float franchise) {
        this.franchise = franchise;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Set<InsurancePackDTO> getInsurancePacks() {
        return insurancePacks;
    }

    public void setInsurancePacks(Set<InsurancePackDTO> insurancePacks) {
        this.insurancePacks = insurancePacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WarrantyDTO)) {
            return false;
        }

        WarrantyDTO warrantyDTO = (WarrantyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, warrantyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WarrantyDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", limit=" + getLimit() +
            ", franchise=" + getFranchise() +
            ", price=" + getPrice() +
            ", mandatory='" + getMandatory() + "'" +
            ", insurancePacks=" + getInsurancePacks() +
            "}";
    }
}
