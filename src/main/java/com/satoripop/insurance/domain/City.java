package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A City.
 */
@Entity
@Table(name = "city")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "city")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "postal_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String postalCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "clients", "city" }, allowSetters = true)
    private Set<ClientAddress> clientAddresses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "contracts", "city" }, allowSetters = true)
    private Set<Agency> agencies = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cities" }, allowSetters = true)
    private Governorate governorate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public City id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public City name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public City postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Set<ClientAddress> getClientAddresses() {
        return this.clientAddresses;
    }

    public void setClientAddresses(Set<ClientAddress> clientAddresses) {
        if (this.clientAddresses != null) {
            this.clientAddresses.forEach(i -> i.setCity(null));
        }
        if (clientAddresses != null) {
            clientAddresses.forEach(i -> i.setCity(this));
        }
        this.clientAddresses = clientAddresses;
    }

    public City clientAddresses(Set<ClientAddress> clientAddresses) {
        this.setClientAddresses(clientAddresses);
        return this;
    }

    public City addClientAddresses(ClientAddress clientAddress) {
        this.clientAddresses.add(clientAddress);
        clientAddress.setCity(this);
        return this;
    }

    public City removeClientAddresses(ClientAddress clientAddress) {
        this.clientAddresses.remove(clientAddress);
        clientAddress.setCity(null);
        return this;
    }

    public Set<Agency> getAgencies() {
        return this.agencies;
    }

    public void setAgencies(Set<Agency> agencies) {
        if (this.agencies != null) {
            this.agencies.forEach(i -> i.setCity(null));
        }
        if (agencies != null) {
            agencies.forEach(i -> i.setCity(this));
        }
        this.agencies = agencies;
    }

    public City agencies(Set<Agency> agencies) {
        this.setAgencies(agencies);
        return this;
    }

    public City addAgencies(Agency agency) {
        this.agencies.add(agency);
        agency.setCity(this);
        return this;
    }

    public City removeAgencies(Agency agency) {
        this.agencies.remove(agency);
        agency.setCity(null);
        return this;
    }

    public Governorate getGovernorate() {
        return this.governorate;
    }

    public void setGovernorate(Governorate governorate) {
        this.governorate = governorate;
    }

    public City governorate(Governorate governorate) {
        this.setGovernorate(governorate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        return getId() != null && getId().equals(((City) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "City{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            "}";
    }
}
