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
 * A Warranty.
 */
@Entity
@Table(name = "warranty")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "warranty")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Warranty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "jhi_limit")
    private Float limit;

    @Column(name = "franchise")
    private Float franchise;

    @Column(name = "price")
    private Float price;

    @Column(name = "mandatory")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean mandatory;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "warranties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "warranties", "vehicle" }, allowSetters = true)
    private Set<InsurancePack> insurancePacks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Warranty id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Warranty name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLimit() {
        return this.limit;
    }

    public Warranty limit(Float limit) {
        this.setLimit(limit);
        return this;
    }

    public void setLimit(Float limit) {
        this.limit = limit;
    }

    public Float getFranchise() {
        return this.franchise;
    }

    public Warranty franchise(Float franchise) {
        this.setFranchise(franchise);
        return this;
    }

    public void setFranchise(Float franchise) {
        this.franchise = franchise;
    }

    public Float getPrice() {
        return this.price;
    }

    public Warranty price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Boolean getMandatory() {
        return this.mandatory;
    }

    public Warranty mandatory(Boolean mandatory) {
        this.setMandatory(mandatory);
        return this;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Set<InsurancePack> getInsurancePacks() {
        return this.insurancePacks;
    }

    public void setInsurancePacks(Set<InsurancePack> insurancePacks) {
        if (this.insurancePacks != null) {
            this.insurancePacks.forEach(i -> i.removeWarranties(this));
        }
        if (insurancePacks != null) {
            insurancePacks.forEach(i -> i.addWarranties(this));
        }
        this.insurancePacks = insurancePacks;
    }

    public Warranty insurancePacks(Set<InsurancePack> insurancePacks) {
        this.setInsurancePacks(insurancePacks);
        return this;
    }

    public Warranty addInsurancePacks(InsurancePack insurancePack) {
        this.insurancePacks.add(insurancePack);
        insurancePack.getWarranties().add(this);
        return this;
    }

    public Warranty removeInsurancePacks(InsurancePack insurancePack) {
        this.insurancePacks.remove(insurancePack);
        insurancePack.getWarranties().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warranty)) {
            return false;
        }
        return getId() != null && getId().equals(((Warranty) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warranty{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", limit=" + getLimit() +
            ", franchise=" + getFranchise() +
            ", price=" + getPrice() +
            ", mandatory='" + getMandatory() + "'" +
            "}";
    }
}
