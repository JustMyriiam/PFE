package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.ContractType;
import com.satoripop.insurance.domain.enumeration.PaymentPlan;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "contract")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Contract extends AbstractAuditingEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "contract_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contractNumber;

    @Column(name = "duration")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String duration;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "net_premium")
    private Float netPremium;

    @Column(name = "upfront_premium")
    private Float upfrontPremium;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "taxes")
    private Float taxes;

    @Column(name = "f_ssr")
    private Float fSSR;

    @Column(name = "f_pac")
    private Float fPAC;

    @Column(name = "t_fga")
    private Float tFGA;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_plan")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private PaymentPlan paymentPlan;

    @JsonIgnoreProperties(value = { "insurancePacks", "quote", "contract" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Vehicle vehicle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<Document> documents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "client", "contract" }, allowSetters = true)
    private Set<Claim> claims = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "quotes", "contracts", "claims", "clientAddress" }, allowSetters = true)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contracts", "city" }, allowSetters = true)
    private Agency agency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Contract id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContractNumber() {
        return this.contractNumber;
    }

    public Contract contractNumber(String contractNumber) {
        this.setContractNumber(contractNumber);
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getDuration() {
        return this.duration;
    }

    public Contract duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public Contract startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public Contract endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getNetPremium() {
        return this.netPremium;
    }

    public Contract netPremium(Float netPremium) {
        this.setNetPremium(netPremium);
        return this;
    }

    public void setNetPremium(Float netPremium) {
        this.netPremium = netPremium;
    }

    public Float getUpfrontPremium() {
        return this.upfrontPremium;
    }

    public Contract upfrontPremium(Float upfrontPremium) {
        this.setUpfrontPremium(upfrontPremium);
        return this;
    }

    public void setUpfrontPremium(Float upfrontPremium) {
        this.upfrontPremium = upfrontPremium;
    }

    public Float getCost() {
        return this.cost;
    }

    public Contract cost(Float cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getTaxes() {
        return this.taxes;
    }

    public Contract taxes(Float taxes) {
        this.setTaxes(taxes);
        return this;
    }

    public void setTaxes(Float taxes) {
        this.taxes = taxes;
    }

    public Float getfSSR() {
        return this.fSSR;
    }

    public Contract fSSR(Float fSSR) {
        this.setfSSR(fSSR);
        return this;
    }

    public void setfSSR(Float fSSR) {
        this.fSSR = fSSR;
    }

    public Float getfPAC() {
        return this.fPAC;
    }

    public Contract fPAC(Float fPAC) {
        this.setfPAC(fPAC);
        return this;
    }

    public void setfPAC(Float fPAC) {
        this.fPAC = fPAC;
    }

    public Float gettFGA() {
        return this.tFGA;
    }

    public Contract tFGA(Float tFGA) {
        this.settFGA(tFGA);
        return this;
    }

    public void settFGA(Float tFGA) {
        this.tFGA = tFGA;
    }

    public ContractType getContractType() {
        return this.contractType;
    }

    public Contract contractType(ContractType contractType) {
        this.setContractType(contractType);
        return this;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public PaymentPlan getPaymentPlan() {
        return this.paymentPlan;
    }

    public Contract paymentPlan(PaymentPlan paymentPlan) {
        this.setPaymentPlan(paymentPlan);
        return this;
    }

    public void setPaymentPlan(PaymentPlan paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Contract vehicle(Vehicle vehicle) {
        this.setVehicle(vehicle);
        return this;
    }

    public Set<Document> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Document> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setContract(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setContract(this));
        }
        this.documents = documents;
    }

    public Contract documents(Set<Document> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Contract addDocuments(Document document) {
        this.documents.add(document);
        document.setContract(this);
        return this;
    }

    public Contract removeDocuments(Document document) {
        this.documents.remove(document);
        document.setContract(null);
        return this;
    }

    public Set<Claim> getClaims() {
        return this.claims;
    }

    public void setClaims(Set<Claim> claims) {
        if (this.claims != null) {
            this.claims.forEach(i -> i.setContract(null));
        }
        if (claims != null) {
            claims.forEach(i -> i.setContract(this));
        }
        this.claims = claims;
    }

    public Contract claims(Set<Claim> claims) {
        this.setClaims(claims);
        return this;
    }

    public Contract addClaims(Claim claim) {
        this.claims.add(claim);
        claim.setContract(this);
        return this;
    }

    public Contract removeClaims(Claim claim) {
        this.claims.remove(claim);
        claim.setContract(null);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Contract client(Client client) {
        this.setClient(client);
        return this;
    }

    public Agency getAgency() {
        return this.agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public Contract agency(Agency agency) {
        this.setAgency(agency);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return getId() != null && getId().equals(((Contract) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", contractNumber='" + getContractNumber() + "'" +
            ", duration='" + getDuration() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", netPremium=" + getNetPremium() +
            ", upfrontPremium=" + getUpfrontPremium() +
            ", cost=" + getCost() +
            ", taxes=" + getTaxes() +
            ", fSSR=" + getfSSR() +
            ", fPAC=" + getfPAC() +
            ", tFGA=" + gettFGA() +
            ", contractType='" + getContractType() + "'" +
            ", paymentPlan='" + getPaymentPlan() + "'" +
            "}";
    }
}
