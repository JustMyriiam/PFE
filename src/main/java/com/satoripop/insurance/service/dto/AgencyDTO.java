package com.satoripop.insurance.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Agency} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AgencyDTO implements Serializable {

    private UUID id;

    private String name;

    private String address;

    private String region;

    private String phoneNumber;

    private String managerName;

    private CityDTO city;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AgencyDTO)) {
            return false;
        }

        AgencyDTO agencyDTO = (AgencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, agencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AgencyDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", region='" + getRegion() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", managerName='" + getManagerName() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
