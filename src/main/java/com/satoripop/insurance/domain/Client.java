package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.Gender;
import com.satoripop.insurance.domain.enumeration.IdentityType;
import com.satoripop.insurance.domain.enumeration.MaritalStatus;
import com.satoripop.insurance.domain.enumeration.ProfessionalStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "last_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lastName;

    @Column(name = "first_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(name = "identity_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private IdentityType identityType;

    @Column(name = "identity_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String identityNumber;

    @Column(name = "identity_emission_date")
    private LocalDate identityEmissionDate;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "birth_place")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String birthPlace;

    @Column(name = "identity_issue_date")
    private LocalDate identityIssueDate;

    @Column(name = "identity_place_of_issue")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String identityPlaceOfIssue;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MaritalStatus maritalStatus;

    @Column(name = "nbr_ofchildren")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer nbrOfchildren;

    @Column(name = "professional_email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String professionalEmail;

    @Column(name = "personal_email")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String personalEmail;

    @Column(name = "primary_phone_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String primaryPhoneNumber;

    @Column(name = "secondary_phone_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String secondaryPhoneNumber;

    @Column(name = "fax_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String faxNumber;

    @Column(name = "nationality")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Gender gender;

    @Column(name = "job_title")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "professional_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private ProfessionalStatus professionalStatus;

    @Column(name = "bank")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bank;

    @Column(name = "agency")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String agency;

    @Column(name = "rib")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rib;

    @Column(name = "driving_license_number")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String drivingLicenseNumber;

    @Column(name = "driving_license_issue_date")
    private LocalDate drivingLicenseIssueDate;

    @Column(name = "driving_license_category")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String drivingLicenseCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "vehicle", "client" }, allowSetters = true)
    private Set<Quote> quotes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "vehicle", "documents", "claims", "client", "agency" }, allowSetters = true)
    private Set<Contract> contracts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "client", "contract" }, allowSetters = true)
    private Set<Claim> claims = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "clients", "city" }, allowSetters = true)
    private ClientAddress clientAddress;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Client id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Client lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Client firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public IdentityType getIdentityType() {
        return this.identityType;
    }

    public Client identityType(IdentityType identityType) {
        this.setIdentityType(identityType);
        return this;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNumber() {
        return this.identityNumber;
    }

    public Client identityNumber(String identityNumber) {
        this.setIdentityNumber(identityNumber);
        return this;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public LocalDate getIdentityEmissionDate() {
        return this.identityEmissionDate;
    }

    public Client identityEmissionDate(LocalDate identityEmissionDate) {
        this.setIdentityEmissionDate(identityEmissionDate);
        return this;
    }

    public void setIdentityEmissionDate(LocalDate identityEmissionDate) {
        this.identityEmissionDate = identityEmissionDate;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Client birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return this.birthPlace;
    }

    public Client birthPlace(String birthPlace) {
        this.setBirthPlace(birthPlace);
        return this;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getIdentityIssueDate() {
        return this.identityIssueDate;
    }

    public Client identityIssueDate(LocalDate identityIssueDate) {
        this.setIdentityIssueDate(identityIssueDate);
        return this;
    }

    public void setIdentityIssueDate(LocalDate identityIssueDate) {
        this.identityIssueDate = identityIssueDate;
    }

    public String getIdentityPlaceOfIssue() {
        return this.identityPlaceOfIssue;
    }

    public Client identityPlaceOfIssue(String identityPlaceOfIssue) {
        this.setIdentityPlaceOfIssue(identityPlaceOfIssue);
        return this;
    }

    public void setIdentityPlaceOfIssue(String identityPlaceOfIssue) {
        this.identityPlaceOfIssue = identityPlaceOfIssue;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Client maritalStatus(MaritalStatus maritalStatus) {
        this.setMaritalStatus(maritalStatus);
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getNbrOfchildren() {
        return this.nbrOfchildren;
    }

    public Client nbrOfchildren(Integer nbrOfchildren) {
        this.setNbrOfchildren(nbrOfchildren);
        return this;
    }

    public void setNbrOfchildren(Integer nbrOfchildren) {
        this.nbrOfchildren = nbrOfchildren;
    }

    public String getProfessionalEmail() {
        return this.professionalEmail;
    }

    public Client professionalEmail(String professionalEmail) {
        this.setProfessionalEmail(professionalEmail);
        return this;
    }

    public void setProfessionalEmail(String professionalEmail) {
        this.professionalEmail = professionalEmail;
    }

    public String getPersonalEmail() {
        return this.personalEmail;
    }

    public Client personalEmail(String personalEmail) {
        this.setPersonalEmail(personalEmail);
        return this;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPrimaryPhoneNumber() {
        return this.primaryPhoneNumber;
    }

    public Client primaryPhoneNumber(String primaryPhoneNumber) {
        this.setPrimaryPhoneNumber(primaryPhoneNumber);
        return this;
    }

    public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return this.secondaryPhoneNumber;
    }

    public Client secondaryPhoneNumber(String secondaryPhoneNumber) {
        this.setSecondaryPhoneNumber(secondaryPhoneNumber);
        return this;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public Client faxNumber(String faxNumber) {
        this.setFaxNumber(faxNumber);
        return this;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Client nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Client gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Client jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public ProfessionalStatus getProfessionalStatus() {
        return this.professionalStatus;
    }

    public Client professionalStatus(ProfessionalStatus professionalStatus) {
        this.setProfessionalStatus(professionalStatus);
        return this;
    }

    public void setProfessionalStatus(ProfessionalStatus professionalStatus) {
        this.professionalStatus = professionalStatus;
    }

    public String getBank() {
        return this.bank;
    }

    public Client bank(String bank) {
        this.setBank(bank);
        return this;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAgency() {
        return this.agency;
    }

    public Client agency(String agency) {
        this.setAgency(agency);
        return this;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getRib() {
        return this.rib;
    }

    public Client rib(String rib) {
        this.setRib(rib);
        return this;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getDrivingLicenseNumber() {
        return this.drivingLicenseNumber;
    }

    public Client drivingLicenseNumber(String drivingLicenseNumber) {
        this.setDrivingLicenseNumber(drivingLicenseNumber);
        return this;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public LocalDate getDrivingLicenseIssueDate() {
        return this.drivingLicenseIssueDate;
    }

    public Client drivingLicenseIssueDate(LocalDate drivingLicenseIssueDate) {
        this.setDrivingLicenseIssueDate(drivingLicenseIssueDate);
        return this;
    }

    public void setDrivingLicenseIssueDate(LocalDate drivingLicenseIssueDate) {
        this.drivingLicenseIssueDate = drivingLicenseIssueDate;
    }

    public String getDrivingLicenseCategory() {
        return this.drivingLicenseCategory;
    }

    public Client drivingLicenseCategory(String drivingLicenseCategory) {
        this.setDrivingLicenseCategory(drivingLicenseCategory);
        return this;
    }

    public void setDrivingLicenseCategory(String drivingLicenseCategory) {
        this.drivingLicenseCategory = drivingLicenseCategory;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Quote> getQuotes() {
        return this.quotes;
    }

    public void setQuotes(Set<Quote> quotes) {
        if (this.quotes != null) {
            this.quotes.forEach(i -> i.setClient(null));
        }
        if (quotes != null) {
            quotes.forEach(i -> i.setClient(this));
        }
        this.quotes = quotes;
    }

    public Client quotes(Set<Quote> quotes) {
        this.setQuotes(quotes);
        return this;
    }

    public Client addQuotes(Quote quote) {
        this.quotes.add(quote);
        quote.setClient(this);
        return this;
    }

    public Client removeQuotes(Quote quote) {
        this.quotes.remove(quote);
        quote.setClient(null);
        return this;
    }

    public Set<Contract> getContracts() {
        return this.contracts;
    }

    public void setContracts(Set<Contract> contracts) {
        if (this.contracts != null) {
            this.contracts.forEach(i -> i.setClient(null));
        }
        if (contracts != null) {
            contracts.forEach(i -> i.setClient(this));
        }
        this.contracts = contracts;
    }

    public Client contracts(Set<Contract> contracts) {
        this.setContracts(contracts);
        return this;
    }

    public Client addContracts(Contract contract) {
        this.contracts.add(contract);
        contract.setClient(this);
        return this;
    }

    public Client removeContracts(Contract contract) {
        this.contracts.remove(contract);
        contract.setClient(null);
        return this;
    }

    public Set<Claim> getClaims() {
        return this.claims;
    }

    public void setClaims(Set<Claim> claims) {
        if (this.claims != null) {
            this.claims.forEach(i -> i.setClient(null));
        }
        if (claims != null) {
            claims.forEach(i -> i.setClient(this));
        }
        this.claims = claims;
    }

    public Client claims(Set<Claim> claims) {
        this.setClaims(claims);
        return this;
    }

    public Client addClaims(Claim claim) {
        this.claims.add(claim);
        claim.setClient(this);
        return this;
    }

    public Client removeClaims(Claim claim) {
        this.claims.remove(claim);
        claim.setClient(null);
        return this;
    }

    public ClientAddress getClientAddress() {
        return this.clientAddress;
    }

    public void setClientAddress(ClientAddress clientAddress) {
        this.clientAddress = clientAddress;
    }

    public Client clientAddress(ClientAddress clientAddress) {
        this.setClientAddress(clientAddress);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return getId() != null && getId().equals(((Client) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", identityType='" + getIdentityType() + "'" +
            ", identityNumber='" + getIdentityNumber() + "'" +
            ", identityEmissionDate='" + getIdentityEmissionDate() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", birthPlace='" + getBirthPlace() + "'" +
            ", identityIssueDate='" + getIdentityIssueDate() + "'" +
            ", identityPlaceOfIssue='" + getIdentityPlaceOfIssue() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", nbrOfchildren=" + getNbrOfchildren() +
            ", professionalEmail='" + getProfessionalEmail() + "'" +
            ", personalEmail='" + getPersonalEmail() + "'" +
            ", primaryPhoneNumber='" + getPrimaryPhoneNumber() + "'" +
            ", secondaryPhoneNumber='" + getSecondaryPhoneNumber() + "'" +
            ", faxNumber='" + getFaxNumber() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", gender='" + getGender() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", professionalStatus='" + getProfessionalStatus() + "'" +
            ", bank='" + getBank() + "'" +
            ", agency='" + getAgency() + "'" +
            ", rib='" + getRib() + "'" +
            ", drivingLicenseNumber='" + getDrivingLicenseNumber() + "'" +
            ", drivingLicenseIssueDate='" + getDrivingLicenseIssueDate() + "'" +
            ", drivingLicenseCategory='" + getDrivingLicenseCategory() + "'" +
            "}";
    }
}
