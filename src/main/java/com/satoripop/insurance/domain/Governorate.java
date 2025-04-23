package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Governorate.
 */
@Entity
@Table(name = "governorate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "governorate")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Governorate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "governorate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "clientAddresses", "agencies", "governorate" }, allowSetters = true)
    private Set<City> cities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Governorate id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Governorate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<City> getCities() {
        return this.cities;
    }

    public void setCities(Set<City> cities) {
        if (this.cities != null) {
            this.cities.forEach(i -> i.setGovernorate(null));
        }
        if (cities != null) {
            cities.forEach(i -> i.setGovernorate(this));
        }
        this.cities = cities;
    }

    public Governorate cities(Set<City> cities) {
        this.setCities(cities);
        return this;
    }

    public Governorate addCities(City city) {
        this.cities.add(city);
        city.setGovernorate(this);
        return this;
    }

    public Governorate removeCities(City city) {
        this.cities.remove(city);
        city.setGovernorate(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Governorate)) {
            return false;
        }
        return getId() != null && getId().equals(((Governorate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Governorate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
