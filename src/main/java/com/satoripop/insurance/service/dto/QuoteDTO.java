package com.satoripop.insurance.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Quote} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuoteDTO implements Serializable {

    private UUID id;

    private LocalDate date;

    private Float estimatedAmount;

    private VehicleDTO vehicle;

    private ClientDTO client;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(Float estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuoteDTO)) {
            return false;
        }

        QuoteDTO quoteDTO = (QuoteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, quoteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuoteDTO{" +
            "id='" + getId() + "'" +
            ", date='" + getDate() + "'" +
            ", estimatedAmount=" + getEstimatedAmount() +
            ", vehicle=" + getVehicle() +
            ", client=" + getClient() +
            "}";
    }
}
