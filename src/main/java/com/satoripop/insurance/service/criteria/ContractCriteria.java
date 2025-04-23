package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.ContractType;
import com.satoripop.insurance.domain.enumeration.PaymentPlan;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Contract} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.ContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContractType
     */
    public static class ContractTypeFilter extends Filter<ContractType> {

        public ContractTypeFilter() {}

        public ContractTypeFilter(ContractTypeFilter filter) {
            super(filter);
        }

        @Override
        public ContractTypeFilter copy() {
            return new ContractTypeFilter(this);
        }
    }

    /**
     * Class for filtering PaymentPlan
     */
    public static class PaymentPlanFilter extends Filter<PaymentPlan> {

        public PaymentPlanFilter() {}

        public PaymentPlanFilter(PaymentPlanFilter filter) {
            super(filter);
        }

        @Override
        public PaymentPlanFilter copy() {
            return new PaymentPlanFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter contractNumber;

    private StringFilter duration;

    private LocalDateFilter startDate;

    private LocalDateFilter endDate;

    private FloatFilter netPremium;

    private FloatFilter upfrontPremium;

    private FloatFilter cost;

    private FloatFilter taxes;

    private FloatFilter fSSR;

    private FloatFilter fPAC;

    private FloatFilter tFGA;

    private ContractTypeFilter contractType;

    private PaymentPlanFilter paymentPlan;

    private UUIDFilter vehicleId;

    private UUIDFilter documentsId;

    private UUIDFilter claimsId;

    private UUIDFilter clientId;

    private UUIDFilter agencyId;

    private Boolean distinct;

    public ContractCriteria() {}

    public ContractCriteria(ContractCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.contractNumber = other.optionalContractNumber().map(StringFilter::copy).orElse(null);
        this.duration = other.optionalDuration().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(LocalDateFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(LocalDateFilter::copy).orElse(null);
        this.netPremium = other.optionalNetPremium().map(FloatFilter::copy).orElse(null);
        this.upfrontPremium = other.optionalUpfrontPremium().map(FloatFilter::copy).orElse(null);
        this.cost = other.optionalCost().map(FloatFilter::copy).orElse(null);
        this.taxes = other.optionalTaxes().map(FloatFilter::copy).orElse(null);
        this.fSSR = other.optionalfSSR().map(FloatFilter::copy).orElse(null);
        this.fPAC = other.optionalfPAC().map(FloatFilter::copy).orElse(null);
        this.tFGA = other.optionaltFGA().map(FloatFilter::copy).orElse(null);
        this.contractType = other.optionalContractType().map(ContractTypeFilter::copy).orElse(null);
        this.paymentPlan = other.optionalPaymentPlan().map(PaymentPlanFilter::copy).orElse(null);
        this.vehicleId = other.optionalVehicleId().map(UUIDFilter::copy).orElse(null);
        this.documentsId = other.optionalDocumentsId().map(UUIDFilter::copy).orElse(null);
        this.claimsId = other.optionalClaimsId().map(UUIDFilter::copy).orElse(null);
        this.clientId = other.optionalClientId().map(UUIDFilter::copy).orElse(null);
        this.agencyId = other.optionalAgencyId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ContractCriteria copy() {
        return new ContractCriteria(this);
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

    public StringFilter getContractNumber() {
        return contractNumber;
    }

    public Optional<StringFilter> optionalContractNumber() {
        return Optional.ofNullable(contractNumber);
    }

    public StringFilter contractNumber() {
        if (contractNumber == null) {
            setContractNumber(new StringFilter());
        }
        return contractNumber;
    }

    public void setContractNumber(StringFilter contractNumber) {
        this.contractNumber = contractNumber;
    }

    public StringFilter getDuration() {
        return duration;
    }

    public Optional<StringFilter> optionalDuration() {
        return Optional.ofNullable(duration);
    }

    public StringFilter duration() {
        if (duration == null) {
            setDuration(new StringFilter());
        }
        return duration;
    }

    public void setDuration(StringFilter duration) {
        this.duration = duration;
    }

    public LocalDateFilter getStartDate() {
        return startDate;
    }

    public Optional<LocalDateFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public LocalDateFilter startDate() {
        if (startDate == null) {
            setStartDate(new LocalDateFilter());
        }
        return startDate;
    }

    public void setStartDate(LocalDateFilter startDate) {
        this.startDate = startDate;
    }

    public LocalDateFilter getEndDate() {
        return endDate;
    }

    public Optional<LocalDateFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public LocalDateFilter endDate() {
        if (endDate == null) {
            setEndDate(new LocalDateFilter());
        }
        return endDate;
    }

    public void setEndDate(LocalDateFilter endDate) {
        this.endDate = endDate;
    }

    public FloatFilter getNetPremium() {
        return netPremium;
    }

    public Optional<FloatFilter> optionalNetPremium() {
        return Optional.ofNullable(netPremium);
    }

    public FloatFilter netPremium() {
        if (netPremium == null) {
            setNetPremium(new FloatFilter());
        }
        return netPremium;
    }

    public void setNetPremium(FloatFilter netPremium) {
        this.netPremium = netPremium;
    }

    public FloatFilter getUpfrontPremium() {
        return upfrontPremium;
    }

    public Optional<FloatFilter> optionalUpfrontPremium() {
        return Optional.ofNullable(upfrontPremium);
    }

    public FloatFilter upfrontPremium() {
        if (upfrontPremium == null) {
            setUpfrontPremium(new FloatFilter());
        }
        return upfrontPremium;
    }

    public void setUpfrontPremium(FloatFilter upfrontPremium) {
        this.upfrontPremium = upfrontPremium;
    }

    public FloatFilter getCost() {
        return cost;
    }

    public Optional<FloatFilter> optionalCost() {
        return Optional.ofNullable(cost);
    }

    public FloatFilter cost() {
        if (cost == null) {
            setCost(new FloatFilter());
        }
        return cost;
    }

    public void setCost(FloatFilter cost) {
        this.cost = cost;
    }

    public FloatFilter getTaxes() {
        return taxes;
    }

    public Optional<FloatFilter> optionalTaxes() {
        return Optional.ofNullable(taxes);
    }

    public FloatFilter taxes() {
        if (taxes == null) {
            setTaxes(new FloatFilter());
        }
        return taxes;
    }

    public void setTaxes(FloatFilter taxes) {
        this.taxes = taxes;
    }

    public FloatFilter getfSSR() {
        return fSSR;
    }

    public Optional<FloatFilter> optionalfSSR() {
        return Optional.ofNullable(fSSR);
    }

    public FloatFilter fSSR() {
        if (fSSR == null) {
            setfSSR(new FloatFilter());
        }
        return fSSR;
    }

    public void setfSSR(FloatFilter fSSR) {
        this.fSSR = fSSR;
    }

    public FloatFilter getfPAC() {
        return fPAC;
    }

    public Optional<FloatFilter> optionalfPAC() {
        return Optional.ofNullable(fPAC);
    }

    public FloatFilter fPAC() {
        if (fPAC == null) {
            setfPAC(new FloatFilter());
        }
        return fPAC;
    }

    public void setfPAC(FloatFilter fPAC) {
        this.fPAC = fPAC;
    }

    public FloatFilter gettFGA() {
        return tFGA;
    }

    public Optional<FloatFilter> optionaltFGA() {
        return Optional.ofNullable(tFGA);
    }

    public FloatFilter tFGA() {
        if (tFGA == null) {
            settFGA(new FloatFilter());
        }
        return tFGA;
    }

    public void settFGA(FloatFilter tFGA) {
        this.tFGA = tFGA;
    }

    public ContractTypeFilter getContractType() {
        return contractType;
    }

    public Optional<ContractTypeFilter> optionalContractType() {
        return Optional.ofNullable(contractType);
    }

    public ContractTypeFilter contractType() {
        if (contractType == null) {
            setContractType(new ContractTypeFilter());
        }
        return contractType;
    }

    public void setContractType(ContractTypeFilter contractType) {
        this.contractType = contractType;
    }

    public PaymentPlanFilter getPaymentPlan() {
        return paymentPlan;
    }

    public Optional<PaymentPlanFilter> optionalPaymentPlan() {
        return Optional.ofNullable(paymentPlan);
    }

    public PaymentPlanFilter paymentPlan() {
        if (paymentPlan == null) {
            setPaymentPlan(new PaymentPlanFilter());
        }
        return paymentPlan;
    }

    public void setPaymentPlan(PaymentPlanFilter paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public UUIDFilter getVehicleId() {
        return vehicleId;
    }

    public Optional<UUIDFilter> optionalVehicleId() {
        return Optional.ofNullable(vehicleId);
    }

    public UUIDFilter vehicleId() {
        if (vehicleId == null) {
            setVehicleId(new UUIDFilter());
        }
        return vehicleId;
    }

    public void setVehicleId(UUIDFilter vehicleId) {
        this.vehicleId = vehicleId;
    }

    public UUIDFilter getDocumentsId() {
        return documentsId;
    }

    public Optional<UUIDFilter> optionalDocumentsId() {
        return Optional.ofNullable(documentsId);
    }

    public UUIDFilter documentsId() {
        if (documentsId == null) {
            setDocumentsId(new UUIDFilter());
        }
        return documentsId;
    }

    public void setDocumentsId(UUIDFilter documentsId) {
        this.documentsId = documentsId;
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

    public UUIDFilter getClientId() {
        return clientId;
    }

    public Optional<UUIDFilter> optionalClientId() {
        return Optional.ofNullable(clientId);
    }

    public UUIDFilter clientId() {
        if (clientId == null) {
            setClientId(new UUIDFilter());
        }
        return clientId;
    }

    public void setClientId(UUIDFilter clientId) {
        this.clientId = clientId;
    }

    public UUIDFilter getAgencyId() {
        return agencyId;
    }

    public Optional<UUIDFilter> optionalAgencyId() {
        return Optional.ofNullable(agencyId);
    }

    public UUIDFilter agencyId() {
        if (agencyId == null) {
            setAgencyId(new UUIDFilter());
        }
        return agencyId;
    }

    public void setAgencyId(UUIDFilter agencyId) {
        this.agencyId = agencyId;
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
        final ContractCriteria that = (ContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(contractNumber, that.contractNumber) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(netPremium, that.netPremium) &&
            Objects.equals(upfrontPremium, that.upfrontPremium) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(taxes, that.taxes) &&
            Objects.equals(fSSR, that.fSSR) &&
            Objects.equals(fPAC, that.fPAC) &&
            Objects.equals(tFGA, that.tFGA) &&
            Objects.equals(contractType, that.contractType) &&
            Objects.equals(paymentPlan, that.paymentPlan) &&
            Objects.equals(vehicleId, that.vehicleId) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(claimsId, that.claimsId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(agencyId, that.agencyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            contractNumber,
            duration,
            startDate,
            endDate,
            netPremium,
            upfrontPremium,
            cost,
            taxes,
            fSSR,
            fPAC,
            tFGA,
            contractType,
            paymentPlan,
            vehicleId,
            documentsId,
            claimsId,
            clientId,
            agencyId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalContractNumber().map(f -> "contractNumber=" + f + ", ").orElse("") +
            optionalDuration().map(f -> "duration=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalNetPremium().map(f -> "netPremium=" + f + ", ").orElse("") +
            optionalUpfrontPremium().map(f -> "upfrontPremium=" + f + ", ").orElse("") +
            optionalCost().map(f -> "cost=" + f + ", ").orElse("") +
            optionalTaxes().map(f -> "taxes=" + f + ", ").orElse("") +
            optionalfSSR().map(f -> "fSSR=" + f + ", ").orElse("") +
            optionalfPAC().map(f -> "fPAC=" + f + ", ").orElse("") +
            optionaltFGA().map(f -> "tFGA=" + f + ", ").orElse("") +
            optionalContractType().map(f -> "contractType=" + f + ", ").orElse("") +
            optionalPaymentPlan().map(f -> "paymentPlan=" + f + ", ").orElse("") +
            optionalVehicleId().map(f -> "vehicleId=" + f + ", ").orElse("") +
            optionalDocumentsId().map(f -> "documentsId=" + f + ", ").orElse("") +
            optionalClaimsId().map(f -> "claimsId=" + f + ", ").orElse("") +
            optionalClientId().map(f -> "clientId=" + f + ", ").orElse("") +
            optionalAgencyId().map(f -> "agencyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
