package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quote.
 */
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "quote")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "estimated_amount")
    private Float estimatedAmount;

    @JsonIgnoreProperties(value = { "insurancePacks", "quote", "contract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "quotes", "contracts", "claims", "clientAddress" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Quote id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Quote date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Float getEstimatedAmount() {
        return this.estimatedAmount;
    }

    public Quote estimatedAmount(Float estimatedAmount) {
        this.setEstimatedAmount(estimatedAmount);
        return this;
    }

    public void setEstimatedAmount(Float estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Quote vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Quote client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quote)) {
            return false;
        }
        return getId() != null && getId().equals(((Quote) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quote{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", estimatedAmount=" + getEstimatedAmount() +
            "}";
    }
}
