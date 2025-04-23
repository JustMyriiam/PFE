package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.ContractType;
import com.satoripop.insurance.domain.enumeration.PaymentPlan;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Contract} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContractDTO implements Serializable {

    private UUID id;

    private String contractNumber;

    private String duration;

    private LocalDate startDate;

    private LocalDate endDate;

    private Float netPremium;

    private Float upfrontPremium;

    private Float cost;

    private Float taxes;

    private Float fSSR;

    private Float fPAC;

    private Float tFGA;

    private ContractType contractType;

    private PaymentPlan paymentPlan;

    private VehicleDTO vehicle;

    private ClientDTO client;

    private AgencyDTO agency;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Float getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(Float netPremium) {
        this.netPremium = netPremium;
    }

    public Float getUpfrontPremium() {
        return upfrontPremium;
    }

    public void setUpfrontPremium(Float upfrontPremium) {
        this.upfrontPremium = upfrontPremium;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getTaxes() {
        return taxes;
    }

    public void setTaxes(Float taxes) {
        this.taxes = taxes;
    }

    public Float getfSSR() {
        return fSSR;
    }

    public void setfSSR(Float fSSR) {
        this.fSSR = fSSR;
    }

    public Float getfPAC() {
        return fPAC;
    }

    public void setfPAC(Float fPAC) {
        this.fPAC = fPAC;
    }

    public Float gettFGA() {
        return tFGA;
    }

    public void settFGA(Float tFGA) {
        this.tFGA = tFGA;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public PaymentPlan getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(PaymentPlan paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDTO vehicle) {
        this.vehicle = vehicle;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public AgencyDTO getAgency() {
        return agency;
    }

    public void setAgency(AgencyDTO agency) {
        this.agency = agency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContractDTO)) {
            return false;
        }

        ContractDTO contractDTO = (ContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContractDTO{" +
            "id='" + getId() + "'" +
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
            ", vehicle=" + getVehicle() +
            ", client=" + getClient() +
            ", agency=" + getAgency() +
            "}";
    }
}
