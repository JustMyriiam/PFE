package com.satoripop.insurance.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.City} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CityDTO implements Serializable {

    private UUID id;

    private String name;

    private String postalCode;

    private GovernorateDTO governorate;

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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public GovernorateDTO getGovernorate() {
        return governorate;
    }

    public void setGovernorate(GovernorateDTO governorate) {
        this.governorate = governorate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDTO)) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", governorate=" + getGovernorate() +
            "}";
    }
}
