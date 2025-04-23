package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.InsurancePackName;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InsurancePack.
 */
@Entity
@Table(name = "insurance_pack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "insurancepack")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InsurancePack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private InsurancePackName name;

    @Column(name = "desciption")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String desciption;

    @Column(name = "price")
    private Float price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_insurance_pack__warranties",
        joinColumns = @JoinColumn(name = "insurance_pack_id"),
        inverseJoinColumns = @JoinColumn(name = "warranties_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "insurancePacks" }, allowSetters = true)
    private Set<Warranty> warranties = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "insurancePacks", "quote", "contract" }, allowSetters = true)
    private Vehicle vehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public InsurancePack id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public InsurancePackName getName() {
        return this.name;
    }

    public InsurancePack name(InsurancePackName name) {
        this.setName(name);
        return this;
    }

    public void setName(InsurancePackName name) {
        this.name = name;
    }

    public String getDesciption() {
        return this.desciption;
    }

    public InsurancePack desciption(String desciption) {
        this.setDesciption(desciption);
        return this;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Float getPrice() {
        return this.price;
    }

    public InsurancePack price(Float price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Set<Warranty> getWarranties() {
        return this.warranties;
    }

    public void setWarranties(Set<Warranty> warranties) {
        this.warranties = warranties;
    }

    public InsurancePack warranties(Set<Warranty> warranties) {
        this.setWarranties(warranties);
        return this;
    }

    public InsurancePack addWarranties(Warranty warranty) {
        this.warranties.add(warranty);
        return this;
    }

    public InsurancePack removeWarranties(Warranty warranty) {
        this.warranties.remove(warranty);
        return this;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public InsurancePack vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsurancePack)) {
            return false;
        }
        return getId() != null && getId().equals(((InsurancePack) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InsurancePack{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", desciption='" + getDesciption() + "'" +
            ", price=" + getPrice() +
            "}";
    }
}
