package com.satoripop.insurance.web.rest;

import com.satoripop.insurance.repository.WarrantyRepository;
import com.satoripop.insurance.service.WarrantyQueryService;
import com.satoripop.insurance.service.WarrantyService;
import com.satoripop.insurance.service.criteria.WarrantyCriteria;
import com.satoripop.insurance.service.dto.WarrantyDTO;
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
 * REST controller for managing {@link com.satoripop.insurance.domain.Warranty}.
 */
@RestController
@RequestMapping("/api/warranties")
public class WarrantyResource {

    private static final Logger LOG = LoggerFactory.getLogger(WarrantyResource.class);

    private static final String ENTITY_NAME = "insuranceWarranty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarrantyService warrantyService;

    private final WarrantyRepository warrantyRepository;

    private final WarrantyQueryService warrantyQueryService;

    public WarrantyResource(
        WarrantyService warrantyService,
        WarrantyRepository warrantyRepository,
        WarrantyQueryService warrantyQueryService
    ) {
        this.warrantyService = warrantyService;
        this.warrantyRepository = warrantyRepository;
        this.warrantyQueryService = warrantyQueryService;
    }

    /**
     * {@code POST  /warranties} : Create a new warranty.
     *
     * @param warrantyDTO the warrantyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warrantyDTO, or with status {@code 400 (Bad Request)} if the warranty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WarrantyDTO> createWarranty(@RequestBody WarrantyDTO warrantyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Warranty : {}", warrantyDTO);
        if (warrantyDTO.getId() != null) {
            throw new BadRequestAlertException("A new warranty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        warrantyDTO = warrantyService.save(warrantyDTO);
        return ResponseEntity.created(new URI("/api/warranties/" + warrantyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, warrantyDTO.getId().toString()))
            .body(warrantyDTO);
    }

    /**
     * {@code PUT  /warranties/:id} : Updates an existing warranty.
     *
     * @param id the id of the warrantyDTO to save.
     * @param warrantyDTO the warrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WarrantyDTO> updateWarranty(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WarrantyDTO warrantyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Warranty : {}, {}", id, warrantyDTO);
        if (warrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warrantyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        warrantyDTO = warrantyService.update(warrantyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warrantyDTO.getId().toString()))
            .body(warrantyDTO);
    }

    /**
     * {@code PATCH  /warranties/:id} : Partial updates given fields of an existing warranty, field will ignore if it is null
     *
     * @param id the id of the warrantyDTO to save.
     * @param warrantyDTO the warrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warrantyDTO,
     * or with status {@code 400 (Bad Request)} if the warrantyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the warrantyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the warrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WarrantyDTO> partialUpdateWarranty(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WarrantyDTO warrantyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Warranty partially : {}, {}", id, warrantyDTO);
        if (warrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warrantyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WarrantyDTO> result = warrantyService.partialUpdate(warrantyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warrantyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /warranties} : get all the warranties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warranties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WarrantyDTO>> getAllWarranties(
        WarrantyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Warranties by criteria: {}", criteria);

        Page<WarrantyDTO> page = warrantyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warranties/count} : count all the warranties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countWarranties(WarrantyCriteria criteria) {
        LOG.debug("REST request to count Warranties by criteria: {}", criteria);
        return ResponseEntity.ok().body(warrantyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /warranties/:id} : get the "id" warranty.
     *
     * @param id the id of the warrantyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warrantyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarrantyDTO> getWarranty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Warranty : {}", id);
        Optional<WarrantyDTO> warrantyDTO = warrantyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(warrantyDTO);
    }

    /**
     * {@code DELETE  /warranties/:id} : delete the "id" warranty.
     *
     * @param id the id of the warrantyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarranty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Warranty : {}", id);
        warrantyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /warranties/_search?query=:query} : search for the warranty corresponding
     * to the query.
     *
     * @param query the query of the warranty search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<WarrantyDTO>> searchWarranties(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Warranties for query {}", query);
        try {
            Page<WarrantyDTO> page = warrantyService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
