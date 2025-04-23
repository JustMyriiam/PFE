package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.ClaimStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Claim.
 */
@Entity
@Table(name = "claim")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "claim")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String type;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ClaimStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "quotes", "contracts", "claims", "clientAddress" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vehicle", "documents", "claims", "client", "agency" }, allowSetters = true)
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Claim id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public Claim type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public Claim description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Claim date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ClaimStatus getStatus() {
        return this.status;
    }

    public Claim status(ClaimStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Claim client(Client client) {
        this.setClient(client);
        return this;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Claim contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Claim)) {
            return false;
        }
        return getId() != null && getId().equals(((Claim) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Claim{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
