package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.InsurancePackName;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.InsurancePack} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePackDTO implements Serializable {

    private UUID id;

    private InsurancePackName name;

    private String desciption;

    private Float price;

    private Set<WarrantyDTO> warranties = new HashSet<>();

    private VehicleDTO vehicle;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public InsurancePackName getName() {
        return name;
    }

    public void setName(InsurancePackName name) {
        this.name = name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Set<WarrantyDTO> getWarranties() {
        return warranties;
    }

    public void setWarranties(Set<WarrantyDTO> warranties) {
        this.warranties = warranties;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsurancePackDTO)) {
            return false;
        }

        InsurancePackDTO insurancePackDTO = (InsurancePackDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, insurancePackDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePackDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", desciption='" + getDesciption() + "'" +
            ", price=" + getPrice() +
            ", warranties=" + getWarranties() +
            ", vehicle=" + getVehicle() +
            "}";
    }
}
