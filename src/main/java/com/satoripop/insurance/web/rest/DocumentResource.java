package com.satoripop.insurance.web.rest;

import com.satoripop.insurance.repository.DocumentRepository;
import com.satoripop.insurance.service.DocumentQueryService;
import com.satoripop.insurance.service.DocumentService;
import com.satoripop.insurance.service.criteria.DocumentCriteria;
import com.satoripop.insurance.service.dto.DocumentDTO;
import com.satoripop.insurance.web.rest.errors.BadRequestAlertException;
import com.satoripop.insurance.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.satoripop.insurance.domain.Document}.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentResource.class);

    private static final String ENTITY_NAME = "insuranceDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentService documentService;

    private final DocumentRepository documentRepository;

    private final DocumentQueryService documentQueryService;

    public DocumentResource(
        DocumentService documentService,
        DocumentRepository documentRepository,
        DocumentQueryService documentQueryService
    ) {
        this.documentService = documentService;
        this.documentRepository = documentRepository;
        this.documentQueryService = documentQueryService;
    }

    /**
     * {@code POST  /documents} : Create a new document.
     *
     * @param documentDTO the documentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentDTO, or with status {@code 400 (Bad Request)} if the document has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO documentDTO) throws URISyntaxException {
        LOG.debug("REST request to save Document : {}", documentDTO);
        if (documentDTO.getId() != null) {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentDTO = documentService.save(documentDTO);
        return ResponseEntity.created(new URI("/api/documents/" + documentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, documentDTO.getId().toString()))
            .body(documentDTO);
    }

    /**
     * {@code PUT  /documents/:id} : Updates an existing document.
     *
     * @param id the id of the documentDTO to save.
     * @param documentDTO the documentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDTO,
     * or with status {@code 400 (Bad Request)} if the documentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> updateDocument(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DocumentDTO documentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Document : {}, {}", id, documentDTO);
        if (documentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentDTO = documentService.update(documentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDTO.getId().toString()))
            .body(documentDTO);
    }

    /**
     * {@code PATCH  /documents/:id} : Partial updates given fields of an existing document, field will ignore if it is null
     *
     * @param id the id of the documentDTO to save.
     * @param documentDTO the documentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentDTO,
     * or with status {@code 400 (Bad Request)} if the documentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentDTO> partialUpdateDocument(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DocumentDTO documentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Document partially : {}, {}", id, documentDTO);
        if (documentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentDTO> result = documentService.partialUpdate(documentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments(
        DocumentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Documents by criteria: {}", criteria);

        Page<DocumentDTO> page = documentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /documents/count} : count all the documents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDocuments(DocumentCriteria criteria) {
        LOG.debug("REST request to count Documents by criteria: {}", criteria);
        return ResponseEntity.ok().body(documentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the documentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Document : {}", id);
        Optional<DocumentDTO> documentDTO = documentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentDTO);
    }

    /**
     * {@code DELETE  /documents/:id} : delete the "id" document.
     *
     * @param id the id of the documentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Document : {}", id);
        documentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /documents/_search?query=:query} : search for the document corresponding
     * to the query.
     *
     * @param query the query of the document search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<DocumentDTO>> searchDocuments(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Documents for query {}", query);
        try {
            Page<DocumentDTO> page = documentService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
