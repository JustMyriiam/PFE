package com.satoripop.insurance.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.ClientAddress} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientAddressDTO implements Serializable {

    private UUID id;

    private String address;

    private String region;

    private CityDTO city;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
        if (!(o instanceof ClientAddressDTO)) {
            return false;
        }

        ClientAddressDTO clientAddressDTO = (ClientAddressDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientAddressDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientAddressDTO{" +
            "id='" + getId() + "'" +
            ", address='" + getAddress() + "'" +
            ", region='" + getRegion() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
