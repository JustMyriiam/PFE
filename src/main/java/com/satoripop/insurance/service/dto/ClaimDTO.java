package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.ClaimStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Claim} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClaimDTO implements Serializable {

    private UUID id;

    private String type;

    private String description;

    private LocalDate date;

    private ClaimStatus status;

    private ClientDTO client;

    private ContractDTO contract;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClaimDTO)) {
            return false;
        }

        ClaimDTO claimDTO = (ClaimDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, claimDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClaimDTO{" +
            "id='" + getId() + "'" +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", client=" + getClient() +
            ", contract=" + getContract() +
            "}";
    }
}
