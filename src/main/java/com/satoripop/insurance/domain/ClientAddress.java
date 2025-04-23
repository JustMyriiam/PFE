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
 * A ClientAddress.
 */
@Entity
@Table(name = "client_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "clientaddress")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "address")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String address;

    @Column(name = "region")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String region;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientAddress")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "user", "quotes", "contracts", "claims", "clientAddress" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clientAddresses", "agencies", "governorate" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public ClientAddress id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public ClientAddress address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return this.region;
    }

    public ClientAddress region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.setClientAddress(null));
        }
        if (clients != null) {
            clients.forEach(i -> i.setClientAddress(this));
        }
        this.clients = clients;
    }

    public ClientAddress clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public ClientAddress addClients(Client client) {
        this.clients.add(client);
        client.setClientAddress(this);
        return this;
    }

    public ClientAddress removeClients(Client client) {
        this.clients.remove(client);
        client.setClientAddress(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public ClientAddress city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientAddress)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientAddress) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientAddress{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", region='" + getRegion() + "'" +
            "}";
    }
}
