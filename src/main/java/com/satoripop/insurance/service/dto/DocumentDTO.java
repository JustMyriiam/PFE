package com.satoripop.insurance.service.dto;

import com.satoripop.insurance.domain.enumeration.DocType;
import com.satoripop.insurance.domain.enumeration.RequestedDocType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link com.satoripop.insurance.domain.Document} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDTO implements Serializable {

    private UUID id;

    private String name;

    private String docPath;

    private DocType type;

    private RequestedDocType requestedDocType;

    private LocalDate creationDate;

    private ContractDTO contract;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocPath() {
        return docPath;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public DocType getType() {
        return type;
    }

    public void setType(DocType type) {
        this.type = type;
    }

    public RequestedDocType getRequestedDocType() {
        return requestedDocType;
    }

    public void setRequestedDocType(RequestedDocType requestedDocType) {
        this.requestedDocType = requestedDocType;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", docPath='" + getDocPath() + "'" +
            ", type='" + getType() + "'" +
            ", requestedDocType='" + getRequestedDocType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", contract=" + getContract() +
            "}";
    }
}
