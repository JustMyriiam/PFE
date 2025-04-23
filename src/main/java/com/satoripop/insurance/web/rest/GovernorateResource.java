package com.satoripop.insurance.web.rest;

import com.satoripop.insurance.repository.GovernorateRepository;
import com.satoripop.insurance.service.GovernorateQueryService;
import com.satoripop.insurance.service.GovernorateService;
import com.satoripop.insurance.service.criteria.GovernorateCriteria;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import com.satoripop.insurance.web.rest.errors.BadRequestAlertException;
import com.satoripop.insurance.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.satoripop.insurance.domain.Governorate}.
 */
@RestController
@RequestMapping("/api/governorates")
public class GovernorateResource {

    private static final Logger LOG = LoggerFactory.getLogger(GovernorateResource.class);

    private static final String ENTITY_NAME = "insuranceGovernorate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GovernorateService governorateService;

    private final GovernorateRepository governorateRepository;

    private final GovernorateQueryService governorateQueryService;

    public GovernorateResource(
        GovernorateService governorateService,
        GovernorateRepository governorateRepository,
        GovernorateQueryService governorateQueryService
    ) {
        this.governorateService = governorateService;
        this.governorateRepository = governorateRepository;
        this.governorateQueryService = governorateQueryService;
    }

    /**
     * {@code POST  /governorates} : Create a new governorate.
     *
     * @param governorateDTO the governorateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new governorateDTO, or with status {@code 400 (Bad Request)} if the governorate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<GovernorateDTO> createGovernorate(@Valid @RequestBody GovernorateDTO governorateDTO) throws URISyntaxException {
        LOG.debug("REST request to save Governorate : {}", governorateDTO);
        if (governorateDTO.getId() != null) {
            throw new BadRequestAlertException("A new governorate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        governorateDTO = governorateService.save(governorateDTO);
        return ResponseEntity.created(new URI("/api/governorates/" + governorateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, governorateDTO.getId().toString()))
            .body(governorateDTO);
    }

    /**
     * {@code PUT  /governorates/:id} : Updates an existing governorate.
     *
     * @param id the id of the governorateDTO to save.
     * @param governorateDTO the governorateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated governorateDTO,
     * or with status {@code 400 (Bad Request)} if the governorateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the governorateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GovernorateDTO> updateGovernorate(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody GovernorateDTO governorateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Governorate : {}, {}", id, governorateDTO);
        if (governorateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, governorateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!governorateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        governorateDTO = governorateService.update(governorateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, governorateDTO.getId().toString()))
            .body(governorateDTO);
    }

    /**
     * {@code PATCH  /governorates/:id} : Partial updates given fields of an existing governorate, field will ignore if it is null
     *
     * @param id the id of the governorateDTO to save.
     * @param governorateDTO the governorateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated governorateDTO,
     * or with status {@code 400 (Bad Request)} if the governorateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the governorateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the governorateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GovernorateDTO> partialUpdateGovernorate(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody GovernorateDTO governorateDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Governorate partially : {}, {}", id, governorateDTO);
        if (governorateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, governorateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!governorateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GovernorateDTO> result = governorateService.partialUpdate(governorateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, governorateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /governorates} : get all the governorates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of governorates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<GovernorateDTO>> getAllGovernorates(
        GovernorateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Governorates by criteria: {}", criteria);

        Page<GovernorateDTO> page = governorateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /governorates/count} : count all the governorates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countGovernorates(GovernorateCriteria criteria) {
        LOG.debug("REST request to count Governorates by criteria: {}", criteria);
        return ResponseEntity.ok().body(governorateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /governorates/:id} : get the "id" governorate.
     *
     * @param id the id of the governorateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the governorateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GovernorateDTO> getGovernorate(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get Governorate : {}", id);
        Optional<GovernorateDTO> governorateDTO = governorateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(governorateDTO);
    }

    /**
     * {@code DELETE  /governorates/:id} : delete the "id" governorate.
     *
     * @param id the id of the governorateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGovernorate(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete Governorate : {}", id);
        governorateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /governorates/_search?query=:query} : search for the governorate corresponding
     * to the query.
     *
     * @param query the query of the governorate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<GovernorateDTO>> searchGovernorates(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Governorates for query {}", query);
        try {
            Page<GovernorateDTO> page = governorateService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
