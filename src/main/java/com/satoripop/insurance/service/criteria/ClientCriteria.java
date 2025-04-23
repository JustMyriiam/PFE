package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.Gender;
import com.satoripop.insurance.domain.enumeration.IdentityType;
import com.satoripop.insurance.domain.enumeration.MaritalStatus;
import com.satoripop.insurance.domain.enumeration.ProfessionalStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Client} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientCriteria implements Serializable, Criteria {

    /**
     * Class for filtering IdentityType
     */
    public static class IdentityTypeFilter extends Filter<IdentityType> {

        public IdentityTypeFilter() {}

        public IdentityTypeFilter(IdentityTypeFilter filter) {
            super(filter);
        }

        @Override
        public IdentityTypeFilter copy() {
            return new IdentityTypeFilter(this);
        }
    }

    /**
     * Class for filtering MaritalStatus
     */
    public static class MaritalStatusFilter extends Filter<MaritalStatus> {

        public MaritalStatusFilter() {}

        public MaritalStatusFilter(MaritalStatusFilter filter) {
            super(filter);
        }

        @Override
        public MaritalStatusFilter copy() {
            return new MaritalStatusFilter(this);
        }
    }

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    /**
     * Class for filtering ProfessionalStatus
     */
    public static class ProfessionalStatusFilter extends Filter<ProfessionalStatus> {

        public ProfessionalStatusFilter() {}

        public ProfessionalStatusFilter(ProfessionalStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProfessionalStatusFilter copy() {
            return new ProfessionalStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter lastName;

    private StringFilter firstName;

    private IdentityTypeFilter identityType;

    private StringFilter identityNumber;

    private LocalDateFilter identityEmissionDate;

    private LocalDateFilter birthDate;

    private StringFilter birthPlace;

    private LocalDateFilter identityIssueDate;

    private StringFilter identityPlaceOfIssue;

    private MaritalStatusFilter maritalStatus;

    private IntegerFilter nbrOfchildren;

    private StringFilter professionalEmail;

    private StringFilter personalEmail;

    private StringFilter primaryPhoneNumber;

    private StringFilter secondaryPhoneNumber;

    private StringFilter faxNumber;

    private StringFilter nationality;

    private GenderFilter gender;

    private StringFilter jobTitle;

    private ProfessionalStatusFilter professionalStatus;

    private StringFilter bank;

    private StringFilter agency;

    private StringFilter rib;

    private StringFilter drivingLicenseNumber;

    private LocalDateFilter drivingLicenseIssueDate;

    private StringFilter drivingLicenseCategory;

    private StringFilter userId;

    private UUIDFilter quotesId;

    private UUIDFilter contractsId;

    private UUIDFilter claimsId;

    private UUIDFilter clientAddressId;

    private Boolean distinct;

    public ClientCriteria() {}

    public ClientCriteria(ClientCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.lastName = other.optionalLastName().map(StringFilter::copy).orElse(null);
        this.firstName = other.optionalFirstName().map(StringFilter::copy).orElse(null);
        this.identityType = other.optionalIdentityType().map(IdentityTypeFilter::copy).orElse(null);
        this.identityNumber = other.optionalIdentityNumber().map(StringFilter::copy).orElse(null);
        this.identityEmissionDate = other.optionalIdentityEmissionDate().map(LocalDateFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(LocalDateFilter::copy).orElse(null);
        this.birthPlace = other.optionalBirthPlace().map(StringFilter::copy).orElse(null);
        this.identityIssueDate = other.optionalIdentityIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.identityPlaceOfIssue = other.optionalIdentityPlaceOfIssue().map(StringFilter::copy).orElse(null);
        this.maritalStatus = other.optionalMaritalStatus().map(MaritalStatusFilter::copy).orElse(null);
        this.nbrOfchildren = other.optionalNbrOfchildren().map(IntegerFilter::copy).orElse(null);
        this.professionalEmail = other.optionalProfessionalEmail().map(StringFilter::copy).orElse(null);
        this.personalEmail = other.optionalPersonalEmail().map(StringFilter::copy).orElse(null);
        this.primaryPhoneNumber = other.optionalPrimaryPhoneNumber().map(StringFilter::copy).orElse(null);
        this.secondaryPhoneNumber = other.optionalSecondaryPhoneNumber().map(StringFilter::copy).orElse(null);
        this.faxNumber = other.optionalFaxNumber().map(StringFilter::copy).orElse(null);
        this.nationality = other.optionalNationality().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderFilter::copy).orElse(null);
        this.jobTitle = other.optionalJobTitle().map(StringFilter::copy).orElse(null);
        this.professionalStatus = other.optionalProfessionalStatus().map(ProfessionalStatusFilter::copy).orElse(null);
        this.bank = other.optionalBank().map(StringFilter::copy).orElse(null);
        this.agency = other.optionalAgency().map(StringFilter::copy).orElse(null);
        this.rib = other.optionalRib().map(StringFilter::copy).orElse(null);
        this.drivingLicenseNumber = other.optionalDrivingLicenseNumber().map(StringFilter::copy).orElse(null);
        this.drivingLicenseIssueDate = other.optionalDrivingLicenseIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.drivingLicenseCategory = other.optionalDrivingLicenseCategory().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(StringFilter::copy).orElse(null);
        this.quotesId = other.optionalQuotesId().map(UUIDFilter::copy).orElse(null);
        this.contractsId = other.optionalContractsId().map(UUIDFilter::copy).orElse(null);
        this.claimsId = other.optionalClaimsId().map(UUIDFilter::copy).orElse(null);
        this.clientAddressId = other.optionalClientAddressId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public Optional<UUIDFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public UUIDFilter id() {
        if (id == null) {
            setId(new UUIDFilter());
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public Optional<StringFilter> optionalLastName() {
        return Optional.ofNullable(lastName);
    }

    public StringFilter lastName() {
        if (lastName == null) {
            setLastName(new StringFilter());
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public Optional<StringFilter> optionalFirstName() {
        return Optional.ofNullable(firstName);
    }

    public StringFilter firstName() {
        if (firstName == null) {
            setFirstName(new StringFilter());
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public IdentityTypeFilter getIdentityType() {
        return identityType;
    }

    public Optional<IdentityTypeFilter> optionalIdentityType() {
        return Optional.ofNullable(identityType);
    }

    public IdentityTypeFilter identityType() {
        if (identityType == null) {
            setIdentityType(new IdentityTypeFilter());
        }
        return identityType;
    }

    public void setIdentityType(IdentityTypeFilter identityType) {
        this.identityType = identityType;
    }

    public StringFilter getIdentityNumber() {
        return identityNumber;
    }

    public Optional<StringFilter> optionalIdentityNumber() {
        return Optional.ofNullable(identityNumber);
    }

    public StringFilter identityNumber() {
        if (identityNumber == null) {
            setIdentityNumber(new StringFilter());
        }
        return identityNumber;
    }

    public void setIdentityNumber(StringFilter identityNumber) {
        this.identityNumber = identityNumber;
    }

    public LocalDateFilter getIdentityEmissionDate() {
        return identityEmissionDate;
    }

    public Optional<LocalDateFilter> optionalIdentityEmissionDate() {
        return Optional.ofNullable(identityEmissionDate);
    }

    public LocalDateFilter identityEmissionDate() {
        if (identityEmissionDate == null) {
            setIdentityEmissionDate(new LocalDateFilter());
        }
        return identityEmissionDate;
    }

    public void setIdentityEmissionDate(LocalDateFilter identityEmissionDate) {
        this.identityEmissionDate = identityEmissionDate;
    }

    public LocalDateFilter getBirthDate() {
        return birthDate;
    }

    public Optional<LocalDateFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public LocalDateFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new LocalDateFilter());
        }
        return birthDate;
    }

    public void setBirthDate(LocalDateFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getBirthPlace() {
        return birthPlace;
    }

    public Optional<StringFilter> optionalBirthPlace() {
        return Optional.ofNullable(birthPlace);
    }

    public StringFilter birthPlace() {
        if (birthPlace == null) {
            setBirthPlace(new StringFilter());
        }
        return birthPlace;
    }

    public void setBirthPlace(StringFilter birthPlace) {
        this.birthPlace = birthPlace;
    }

    public LocalDateFilter getIdentityIssueDate() {
        return identityIssueDate;
    }

    public Optional<LocalDateFilter> optionalIdentityIssueDate() {
        return Optional.ofNullable(identityIssueDate);
    }

    public LocalDateFilter identityIssueDate() {
        if (identityIssueDate == null) {
            setIdentityIssueDate(new LocalDateFilter());
        }
        return identityIssueDate;
    }

    public void setIdentityIssueDate(LocalDateFilter identityIssueDate) {
        this.identityIssueDate = identityIssueDate;
    }

    public StringFilter getIdentityPlaceOfIssue() {
        return identityPlaceOfIssue;
    }

    public Optional<StringFilter> optionalIdentityPlaceOfIssue() {
        return Optional.ofNullable(identityPlaceOfIssue);
    }

    public StringFilter identityPlaceOfIssue() {
        if (identityPlaceOfIssue == null) {
            setIdentityPlaceOfIssue(new StringFilter());
        }
        return identityPlaceOfIssue;
    }

    public void setIdentityPlaceOfIssue(StringFilter identityPlaceOfIssue) {
        this.identityPlaceOfIssue = identityPlaceOfIssue;
    }

    public MaritalStatusFilter getMaritalStatus() {
        return maritalStatus;
    }

    public Optional<MaritalStatusFilter> optionalMaritalStatus() {
        return Optional.ofNullable(maritalStatus);
    }

    public MaritalStatusFilter maritalStatus() {
        if (maritalStatus == null) {
            setMaritalStatus(new MaritalStatusFilter());
        }
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusFilter maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public IntegerFilter getNbrOfchildren() {
        return nbrOfchildren;
    }

    public Optional<IntegerFilter> optionalNbrOfchildren() {
        return Optional.ofNullable(nbrOfchildren);
    }

    public IntegerFilter nbrOfchildren() {
        if (nbrOfchildren == null) {
            setNbrOfchildren(new IntegerFilter());
        }
        return nbrOfchildren;
    }

    public void setNbrOfchildren(IntegerFilter nbrOfchildren) {
        this.nbrOfchildren = nbrOfchildren;
    }

    public StringFilter getProfessionalEmail() {
        return professionalEmail;
    }

    public Optional<StringFilter> optionalProfessionalEmail() {
        return Optional.ofNullable(professionalEmail);
    }

    public StringFilter professionalEmail() {
        if (professionalEmail == null) {
            setProfessionalEmail(new StringFilter());
        }
        return professionalEmail;
    }

    public void setProfessionalEmail(StringFilter professionalEmail) {
        this.professionalEmail = professionalEmail;
    }

    public StringFilter getPersonalEmail() {
        return personalEmail;
    }

    public Optional<StringFilter> optionalPersonalEmail() {
        return Optional.ofNullable(personalEmail);
    }

    public StringFilter personalEmail() {
        if (personalEmail == null) {
            setPersonalEmail(new StringFilter());
        }
        return personalEmail;
    }

    public void setPersonalEmail(StringFilter personalEmail) {
        this.personalEmail = personalEmail;
    }

    public StringFilter getPrimaryPhoneNumber() {
        return primaryPhoneNumber;
    }

    public Optional<StringFilter> optionalPrimaryPhoneNumber() {
        return Optional.ofNullable(primaryPhoneNumber);
    }

    public StringFilter primaryPhoneNumber() {
        if (primaryPhoneNumber == null) {
            setPrimaryPhoneNumber(new StringFilter());
        }
        return primaryPhoneNumber;
    }

    public void setPrimaryPhoneNumber(StringFilter primaryPhoneNumber) {
        this.primaryPhoneNumber = primaryPhoneNumber;
    }

    public StringFilter getSecondaryPhoneNumber() {
        return secondaryPhoneNumber;
    }

    public Optional<StringFilter> optionalSecondaryPhoneNumber() {
        return Optional.ofNullable(secondaryPhoneNumber);
    }

    public StringFilter secondaryPhoneNumber() {
        if (secondaryPhoneNumber == null) {
            setSecondaryPhoneNumber(new StringFilter());
        }
        return secondaryPhoneNumber;
    }

    public void setSecondaryPhoneNumber(StringFilter secondaryPhoneNumber) {
        this.secondaryPhoneNumber = secondaryPhoneNumber;
    }

    public StringFilter getFaxNumber() {
        return faxNumber;
    }

    public Optional<StringFilter> optionalFaxNumber() {
        return Optional.ofNullable(faxNumber);
    }

    public StringFilter faxNumber() {
        if (faxNumber == null) {
            setFaxNumber(new StringFilter());
        }
        return faxNumber;
    }

    public void setFaxNumber(StringFilter faxNumber) {
        this.faxNumber = faxNumber;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public Optional<StringFilter> optionalNationality() {
        return Optional.ofNullable(nationality);
    }

    public StringFilter nationality() {
        if (nationality == null) {
            setNationality(new StringFilter());
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public Optional<GenderFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderFilter gender() {
        if (gender == null) {
            setGender(new GenderFilter());
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public Optional<StringFilter> optionalJobTitle() {
        return Optional.ofNullable(jobTitle);
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            setJobTitle(new StringFilter());
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public ProfessionalStatusFilter getProfessionalStatus() {
        return professionalStatus;
    }

    public Optional<ProfessionalStatusFilter> optionalProfessionalStatus() {
        return Optional.ofNullable(professionalStatus);
    }

    public ProfessionalStatusFilter professionalStatus() {
        if (professionalStatus == null) {
            setProfessionalStatus(new ProfessionalStatusFilter());
        }
        return professionalStatus;
    }

    public void setProfessionalStatus(ProfessionalStatusFilter professionalStatus) {
        this.professionalStatus = professionalStatus;
    }

    public StringFilter getBank() {
        return bank;
    }

    public Optional<StringFilter> optionalBank() {
        return Optional.ofNullable(bank);
    }

    public StringFilter bank() {
        if (bank == null) {
            setBank(new StringFilter());
        }
        return bank;
    }

    public void setBank(StringFilter bank) {
        this.bank = bank;
    }

    public StringFilter getAgency() {
        return agency;
    }

    public Optional<StringFilter> optionalAgency() {
        return Optional.ofNullable(agency);
    }

    public StringFilter agency() {
        if (agency == null) {
            setAgency(new StringFilter());
        }
        return agency;
    }

    public void setAgency(StringFilter agency) {
        this.agency = agency;
    }

    public StringFilter getRib() {
        return rib;
    }

    public Optional<StringFilter> optionalRib() {
        return Optional.ofNullable(rib);
    }

    public StringFilter rib() {
        if (rib == null) {
            setRib(new StringFilter());
        }
        return rib;
    }

    public void setRib(StringFilter rib) {
        this.rib = rib;
    }

    public StringFilter getDrivingLicenseNumber() {
        return drivingLicenseNumber;
    }

    public Optional<StringFilter> optionalDrivingLicenseNumber() {
        return Optional.ofNullable(drivingLicenseNumber);
    }

    public StringFilter drivingLicenseNumber() {
        if (drivingLicenseNumber == null) {
            setDrivingLicenseNumber(new StringFilter());
        }
        return drivingLicenseNumber;
    }

    public void setDrivingLicenseNumber(StringFilter drivingLicenseNumber) {
        this.drivingLicenseNumber = drivingLicenseNumber;
    }

    public LocalDateFilter getDrivingLicenseIssueDate() {
        return drivingLicenseIssueDate;
    }

    public Optional<LocalDateFilter> optionalDrivingLicenseIssueDate() {
        return Optional.ofNullable(drivingLicenseIssueDate);
    }

    public LocalDateFilter drivingLicenseIssueDate() {
        if (drivingLicenseIssueDate == null) {
            setDrivingLicenseIssueDate(new LocalDateFilter());
        }
        return drivingLicenseIssueDate;
    }

    public void setDrivingLicenseIssueDate(LocalDateFilter drivingLicenseIssueDate) {
        this.drivingLicenseIssueDate = drivingLicenseIssueDate;
    }

    public StringFilter getDrivingLicenseCategory() {
        return drivingLicenseCategory;
    }

    public Optional<StringFilter> optionalDrivingLicenseCategory() {
        return Optional.ofNullable(drivingLicenseCategory);
    }

    public StringFilter drivingLicenseCategory() {
        if (drivingLicenseCategory == null) {
            setDrivingLicenseCategory(new StringFilter());
        }
        return drivingLicenseCategory;
    }

    public void setDrivingLicenseCategory(StringFilter drivingLicenseCategory) {
        this.drivingLicenseCategory = drivingLicenseCategory;
    }

    public StringFilter getUserId() {
        return userId;
    }

    public Optional<StringFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public StringFilter userId() {
        if (userId == null) {
            setUserId(new StringFilter());
        }
        return userId;
    }

    public void setUserId(StringFilter userId) {
        this.userId = userId;
    }

    public UUIDFilter getQuotesId() {
        return quotesId;
    }

    public Optional<UUIDFilter> optionalQuotesId() {
        return Optional.ofNullable(quotesId);
    }

    public UUIDFilter quotesId() {
        if (quotesId == null) {
            setQuotesId(new UUIDFilter());
        }
        return quotesId;
    }

    public void setQuotesId(UUIDFilter quotesId) {
        this.quotesId = quotesId;
    }

    public UUIDFilter getContractsId() {
        return contractsId;
    }

    public Optional<UUIDFilter> optionalContractsId() {
        return Optional.ofNullable(contractsId);
    }

    public UUIDFilter contractsId() {
        if (contractsId == null) {
            setContractsId(new UUIDFilter());
        }
        return contractsId;
    }

    public void setContractsId(UUIDFilter contractsId) {
        this.contractsId = contractsId;
    }

    public UUIDFilter getClaimsId() {
        return claimsId;
    }

    public Optional<UUIDFilter> optionalClaimsId() {
        return Optional.ofNullable(claimsId);
    }

    public UUIDFilter claimsId() {
        if (claimsId == null) {
            setClaimsId(new UUIDFilter());
        }
        return claimsId;
    }

    public void setClaimsId(UUIDFilter claimsId) {
        this.claimsId = claimsId;
    }

    public UUIDFilter getClientAddressId() {
        return clientAddressId;
    }

    public Optional<UUIDFilter> optionalClientAddressId() {
        return Optional.ofNullable(clientAddressId);
    }

    public UUIDFilter clientAddressId() {
        if (clientAddressId == null) {
            setClientAddressId(new UUIDFilter());
        }
        return clientAddressId;
    }

    public void setClientAddressId(UUIDFilter clientAddressId) {
        this.clientAddressId = clientAddressId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ClientCriteria that = (ClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(identityType, that.identityType) &&
            Objects.equals(identityNumber, that.identityNumber) &&
            Objects.equals(identityEmissionDate, that.identityEmissionDate) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(birthPlace, that.birthPlace) &&
            Objects.equals(identityIssueDate, that.identityIssueDate) &&
            Objects.equals(identityPlaceOfIssue, that.identityPlaceOfIssue) &&
            Objects.equals(maritalStatus, that.maritalStatus) &&
            Objects.equals(nbrOfchildren, that.nbrOfchildren) &&
            Objects.equals(professionalEmail, that.professionalEmail) &&
            Objects.equals(personalEmail, that.personalEmail) &&
            Objects.equals(primaryPhoneNumber, that.primaryPhoneNumber) &&
            Objects.equals(secondaryPhoneNumber, that.secondaryPhoneNumber) &&
            Objects.equals(faxNumber, that.faxNumber) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(professionalStatus, that.professionalStatus) &&
            Objects.equals(bank, that.bank) &&
            Objects.equals(agency, that.agency) &&
            Objects.equals(rib, that.rib) &&
            Objects.equals(drivingLicenseNumber, that.drivingLicenseNumber) &&
            Objects.equals(drivingLicenseIssueDate, that.drivingLicenseIssueDate) &&
            Objects.equals(drivingLicenseCategory, that.drivingLicenseCategory) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(quotesId, that.quotesId) &&
            Objects.equals(contractsId, that.contractsId) &&
            Objects.equals(claimsId, that.claimsId) &&
            Objects.equals(clientAddressId, that.clientAddressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            lastName,
            firstName,
            identityType,
            identityNumber,
            identityEmissionDate,
            birthDate,
            birthPlace,
            identityIssueDate,
            identityPlaceOfIssue,
            maritalStatus,
            nbrOfchildren,
            professionalEmail,
            personalEmail,
            primaryPhoneNumber,
            secondaryPhoneNumber,
            faxNumber,
            nationality,
            gender,
            jobTitle,
            professionalStatus,
            bank,
            agency,
            rib,
            drivingLicenseNumber,
            drivingLicenseIssueDate,
            drivingLicenseCategory,
            userId,
            quotesId,
            contractsId,
            claimsId,
            clientAddressId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLastName().map(f -> "lastName=" + f + ", ").orElse("") +
            optionalFirstName().map(f -> "firstName=" + f + ", ").orElse("") +
            optionalIdentityType().map(f -> "identityType=" + f + ", ").orElse("") +
            optionalIdentityNumber().map(f -> "identityNumber=" + f + ", ").orElse("") +
            optionalIdentityEmissionDate().map(f -> "identityEmissionDate=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalBirthPlace().map(f -> "birthPlace=" + f + ", ").orElse("") +
            optionalIdentityIssueDate().map(f -> "identityIssueDate=" + f + ", ").orElse("") +
            optionalIdentityPlaceOfIssue().map(f -> "identityPlaceOfIssue=" + f + ", ").orElse("") +
            optionalMaritalStatus().map(f -> "maritalStatus=" + f + ", ").orElse("") +
            optionalNbrOfchildren().map(f -> "nbrOfchildren=" + f + ", ").orElse("") +
            optionalProfessionalEmail().map(f -> "professionalEmail=" + f + ", ").orElse("") +
            optionalPersonalEmail().map(f -> "personalEmail=" + f + ", ").orElse("") +
            optionalPrimaryPhoneNumber().map(f -> "primaryPhoneNumber=" + f + ", ").orElse("") +
            optionalSecondaryPhoneNumber().map(f -> "secondaryPhoneNumber=" + f + ", ").orElse("") +
            optionalFaxNumber().map(f -> "faxNumber=" + f + ", ").orElse("") +
            optionalNationality().map(f -> "nationality=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalJobTitle().map(f -> "jobTitle=" + f + ", ").orElse("") +
            optionalProfessionalStatus().map(f -> "professionalStatus=" + f + ", ").orElse("") +
            optionalBank().map(f -> "bank=" + f + ", ").orElse("") +
            optionalAgency().map(f -> "agency=" + f + ", ").orElse("") +
            optionalRib().map(f -> "rib=" + f + ", ").orElse("") +
            optionalDrivingLicenseNumber().map(f -> "drivingLicenseNumber=" + f + ", ").orElse("") +
            optionalDrivingLicenseIssueDate().map(f -> "drivingLicenseIssueDate=" + f + ", ").orElse("") +
            optionalDrivingLicenseCategory().map(f -> "drivingLicenseCategory=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalQuotesId().map(f -> "quotesId=" + f + ", ").orElse("") +
            optionalContractsId().map(f -> "contractsId=" + f + ", ").orElse("") +
            optionalClaimsId().map(f -> "claimsId=" + f + ", ").orElse("") +
            optionalClientAddressId().map(f -> "clientAddressId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
