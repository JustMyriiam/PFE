package com.satoripop.insurance.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Governorate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GovernorateDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GovernorateDTO)) {
            return false;
        }

        GovernorateDTO governorateDTO = (GovernorateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, governorateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GovernorateDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
