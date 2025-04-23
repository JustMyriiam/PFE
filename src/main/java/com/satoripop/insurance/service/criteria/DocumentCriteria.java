package com.satoripop.insurance.service.criteria;

import com.satoripop.insurance.domain.enumeration.DocType;
import com.satoripop.insurance.domain.enumeration.RequestedDocType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.satoripop.insurance.domain.Document} entity. This class is used
 * in {@link com.satoripop.insurance.web.rest.DocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocType
     */
    public static class DocTypeFilter extends Filter<DocType> {

        public DocTypeFilter() {}

        public DocTypeFilter(DocTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocTypeFilter copy() {
            return new DocTypeFilter(this);
        }
    }

    /**
     * Class for filtering RequestedDocType
     */
    public static class RequestedDocTypeFilter extends Filter<RequestedDocType> {

        public RequestedDocTypeFilter() {}

        public RequestedDocTypeFilter(RequestedDocTypeFilter filter) {
            super(filter);
        }

        @Override
        public RequestedDocTypeFilter copy() {
            return new RequestedDocTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter name;

    private StringFilter docPath;

    private DocTypeFilter type;

    private RequestedDocTypeFilter requestedDocType;

    private LocalDateFilter creationDate;

    private UUIDFilter contractId;

    private Boolean distinct;

    public DocumentCriteria() {}

    public DocumentCriteria(DocumentCriteria other) {
        this.id = other.optionalId().map(UUIDFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.docPath = other.optionalDocPath().map(StringFilter::copy).orElse(null);
        this.type = other.optionalType().map(DocTypeFilter::copy).orElse(null);
        this.requestedDocType = other.optionalRequestedDocType().map(RequestedDocTypeFilter::copy).orElse(null);
        this.creationDate = other.optionalCreationDate().map(LocalDateFilter::copy).orElse(null);
        this.contractId = other.optionalContractId().map(UUIDFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentCriteria copy() {
        return new DocumentCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDocPath() {
        return docPath;
    }

    public Optional<StringFilter> optionalDocPath() {
        return Optional.ofNullable(docPath);
    }

    public StringFilter docPath() {
        if (docPath == null) {
            setDocPath(new StringFilter());
        }
        return docPath;
    }

    public void setDocPath(StringFilter docPath) {
        this.docPath = docPath;
    }

    public DocTypeFilter getType() {
        return type;
    }

    public Optional<DocTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public DocTypeFilter type() {
        if (type == null) {
            setType(new DocTypeFilter());
        }
        return type;
    }

    public void setType(DocTypeFilter type) {
        this.type = type;
    }

    public RequestedDocTypeFilter getRequestedDocType() {
        return requestedDocType;
    }

    public Optional<RequestedDocTypeFilter> optionalRequestedDocType() {
        return Optional.ofNullable(requestedDocType);
    }

    public RequestedDocTypeFilter requestedDocType() {
        if (requestedDocType == null) {
            setRequestedDocType(new RequestedDocTypeFilter());
        }
        return requestedDocType;
    }

    public void setRequestedDocType(RequestedDocTypeFilter requestedDocType) {
        this.requestedDocType = requestedDocType;
    }

    public LocalDateFilter getCreationDate() {
        return creationDate;
    }

    public Optional<LocalDateFilter> optionalCreationDate() {
        return Optional.ofNullable(creationDate);
    }

    public LocalDateFilter creationDate() {
        if (creationDate == null) {
            setCreationDate(new LocalDateFilter());
        }
        return creationDate;
    }

    public void setCreationDate(LocalDateFilter creationDate) {
        this.creationDate = creationDate;
    }

    public UUIDFilter getContractId() {
        return contractId;
    }

    public Optional<UUIDFilter> optionalContractId() {
        return Optional.ofNullable(contractId);
    }

    public UUIDFilter contractId() {
        if (contractId == null) {
            setContractId(new UUIDFilter());
        }
        return contractId;
    }

    public void setContractId(UUIDFilter contractId) {
        this.contractId = contractId;
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
        final DocumentCriteria that = (DocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(docPath, that.docPath) &&
            Objects.equals(type, that.type) &&
            Objects.equals(requestedDocType, that.requestedDocType) &&
            Objects.equals(creationDate, that.creationDate) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, docPath, type, requestedDocType, creationDate, contractId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDocPath().map(f -> "docPath=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalRequestedDocType().map(f -> "requestedDocType=" + f + ", ").orElse("") +
            optionalCreationDate().map(f -> "creationDate=" + f + ", ").orElse("") +
            optionalContractId().map(f -> "contractId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
