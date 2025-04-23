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
 * A Agency.
 */
@Entity
@Table(name = "agency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "agency")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Agency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "address")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String address;

    @Column(name = "region")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String region;

    @Column(name = "phone_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String phoneNumber;

    @Column(name = "manager_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String managerName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "vehicle", "documents", "claims", "client", "agency" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clientAddresses", "agencies", "governorate" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Agency id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Agency name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Agency address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return this.region;
    }

    public Agency region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Agency phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getManagerName() {
        return this.managerName;
    }

    public Agency managerName(String managerName) {
        this.setManagerName(managerName);
        return this;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setAgency(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setAgency(this));
        }
        this.contracts = contracts;
    }

    public Agency contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Agency addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setAgency(this);
        return this;
    }

    public Agency removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setAgency(null);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Agency city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Agency)) {
            return false;
        }
        return getId() != null && getId().equals(((Agency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Agency{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", region='" + getRegion() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", managerName='" + getManagerName() + "'" +
            "}";
    }
}
