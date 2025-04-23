package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.Gender;
import com.satoripop.insurance.domain.enumeration.IdentityType;
import com.satoripop.insurance.domain.enumeration.MaritalStatus;
import com.satoripop.insurance.domain.enumeration.ProfessionalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Client} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientDTO implements Serializable {

    private UUID id;

    private String lastName;

    private String firstName;

    private IdentityType identityType;

    private String identityNumber;

    private LocalDate identityEmissionDate;

    private LocalDate birthDate;

    private String birthPlace;

    private LocalDate identityIssueDate;

    private String identityPlaceOfIssue;

    private MaritalStatus maritalStatus;

    private Integer nbrOfchildren;

    private String professionalEmail;

    private String personalEmail;

    private String primaryPhoneNumber;

    private String secondaryPhoneNumber;

    private String faxNumber;

    private String nationality;

    private Gender gender;

    private String jobTitle;

    private ProfessionalStatus professionalStatus;

    private String bank;

    private String agency;

    private String rib;

    private String drivingLicenseNumber;

    private LocalDate drivingLicenseIssueDate;

    private String drivingLicenseCategory;

    private UserDTO user;

    private ClientAddressDTO clientAddress;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public LocalDate getIdentityEmissionDate() {
        return identityEmissionDate;
    }

    public void setIdentityEmissionDate(LocalDate identityEmissionDate) {
        this.identityEmissionDate = identityEmissionDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDate getIdentityIssueDate() {
        return identityIssueDate;
    }

    public void setIdentityIssueDate(LocalDate identityIssueDate) {
        this.identityIssueDate = identityIssueDate;
    }

    public String getIdentityPlaceOfIssue() {
        return identityPlaceOfIssue;
    }

    public void setIdentityPlaceOfIssue(String identityPlaceOfIssue) {
        this.identityPlaceOfIssue = identityPlaceOfIssue;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Integer getNbrOfchildren() {
        return nbrOfchildren;
    }

    public void setNbrOfchildren(Integer nbrOfchildren) {
        this.nbrOfchildren = nbrOfchildren;
    }

    public String getProfessionalEmail() {
        return professionalEmail;
    }

    public void setProfessionalEmail(String professionalEmail) {
        this.professionalEmail = professionalEmail;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public void setPrimaryPhoneNumber(String primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public String getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(String secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public ProfessionalStatus getProfessionalStatus() {
        return professionalStatus;
    }

    public void setProfessionalStatus(ProfessionalStatus professionalStatus) {
        this.professionalStatus = professionalStatus;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(String drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public LocalDate getDrivingLicenseIssueDate() {
        return drivingLicenseIssueDate;
    }

    public void setDrivingLicenseIssueDate(LocalDate drivingLicenseIssueDate) {
        this.drivingLicenseIssueDate = drivingLicenseIssueDate;
    }

    public String getDrivingLicenseCategory() {
        return drivingLicenseCategory;
    }

    public void setDrivingLicenseCategory(String drivingLicenseCategory) {
        this.drivingLicenseCategory = drivingLicenseCategory;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ClientAddressDTO getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(ClientAddressDTO clientAddress) {
        this.clientAddress = clientAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id='" + getId() + "'" +
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
            ", user=" + getUser() +
            ", clientAddress=" + getClientAddress() +
            "}";
    }
}
