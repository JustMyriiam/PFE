package com.satoripop.insurance.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.satoripop.insurance.domain.enumeration.DocType;
import com.satoripop.insurance.domain.enumeration.RequestedDocType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Column(name = "doc_path")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String docPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private DocType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "requested_doc_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private RequestedDocType requestedDocType;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "vehicle", "documents", "claims", "client", "agency" }, allowSetters = true)
    private Contract contract;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getId() {
        return this.id;
    }

    public Document id(UUID id) {
        this.setId(id);
        return this;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Document name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocPath() {
        return this.docPath;
    }

    public Document docPath(String docPath) {
        this.setDocPath(docPath);
        return this;
    }

    public void setDocPath(String docPath) {
        this.docPath = docPath;
    }

    public DocType getType() {
        return this.type;
    }

    public Document type(DocType type) {
        this.setType(type);
        return this;
    }

    public void setType(DocType type) {
        this.type = type;
    }

    public RequestedDocType getRequestedDocType() {
        return this.requestedDocType;
    }

    public Document requestedDocType(RequestedDocType requestedDocType) {
        this.setRequestedDocType(requestedDocType);
        return this;
    }

    public void setRequestedDocType(RequestedDocType requestedDocType) {
        this.requestedDocType = requestedDocType;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Document creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Document contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return getId() != null && getId().equals(((Document) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", docPath='" + getDocPath() + "'" +
            ", type='" + getType() + "'" +
            ", requestedDocType='" + getRequestedDocType() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
