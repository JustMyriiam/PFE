package com.satoripop.insurance.web.rest;

import com.satoripop.insurance.repository.InsurancePackRepository;
import com.satoripop.insurance.service.InsurancePackQueryService;
import com.satoripop.insurance.service.InsurancePackService;
import com.satoripop.insurance.service.criteria.InsurancePackCriteria;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
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
 * REST controller for managing {@link com.satoripop.insurance.domain.InsurancePack}.
 */
@RestController
@RequestMapping("/api/insurance-packs")
public class InsurancePackResource {

    private static final Logger LOG = LoggerFactory.getLogger(InsurancePackResource.class);

    private static final String ENTITY_NAME = "insuranceInsurancePack";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsurancePackService insurancePackService;

    private final InsurancePackRepository insurancePackRepository;

    private final InsurancePackQueryService insurancePackQueryService;

    public InsurancePackResource(
        InsurancePackService insurancePackService,
        InsurancePackRepository insurancePackRepository,
        InsurancePackQueryService insurancePackQueryService
    ) {
        this.insurancePackService = insurancePackService;
        this.insurancePackRepository = insurancePackRepository;
        this.insurancePackQueryService = insurancePackQueryService;
    }

    /**
     * {@code POST  /insurance-packs} : Create a new insurancePack.
     *
     * @param insurancePackDTO the insurancePackDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insurancePackDTO, or with status {@code 400 (Bad Request)} if the insurancePack has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InsurancePackDTO> createInsurancePack(@RequestBody InsurancePackDTO insurancePackDTO) throws URISyntaxException {
        LOG.debug("REST request to save InsurancePack : {}", insurancePackDTO);
        if (insurancePackDTO.getId() != null) {
            throw new BadRequestAlertException("A new insurancePack cannot already have an ID", ENTITY_NAME, "idexists");
        }
        insurancePackDTO = insurancePackService.save(insurancePackDTO);
        return ResponseEntity.created(new URI("/api/insurance-packs/" + insurancePackDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, insurancePackDTO.getId().toString()))
            .body(insurancePackDTO);
    }

    /**
     * {@code PUT  /insurance-packs/:id} : Updates an existing insurancePack.
     *
     * @param id the id of the insurancePackDTO to save.
     * @param insurancePackDTO the insurancePackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePackDTO,
     * or with status {@code 400 (Bad Request)} if the insurancePackDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insurancePackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InsurancePackDTO> updateInsurancePack(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody InsurancePackDTO insurancePackDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InsurancePack : {}, {}", id, insurancePackDTO);
        if (insurancePackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        insurancePackDTO = insurancePackService.update(insurancePackDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insurancePackDTO.getId().toString()))
            .body(insurancePackDTO);
    }

    /**
     * {@code PATCH  /insurance-packs/:id} : Partial updates given fields of an existing insurancePack, field will ignore if it is null
     *
     * @param id the id of the insurancePackDTO to save.
     * @param insurancePackDTO the insurancePackDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insurancePackDTO,
     * or with status {@code 400 (Bad Request)} if the insurancePackDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insurancePackDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insurancePackDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsurancePackDTO> partialUpdateInsurancePack(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody InsurancePackDTO insurancePackDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InsurancePack partially : {}, {}", id, insurancePackDTO);
        if (insurancePackDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insurancePackDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insurancePackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsurancePackDTO> result = insurancePackService.partialUpdate(insurancePackDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insurancePackDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insurance-packs} : get all the insurancePacks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insurancePacks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InsurancePackDTO>> getAllInsurancePacks(
        InsurancePackCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InsurancePacks by criteria: {}", criteria);

        Page<InsurancePackDTO> page = insurancePackQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insurance-packs/count} : count all the insurancePacks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInsurancePacks(InsurancePackCriteria criteria) {
        LOG.debug("REST request to count InsurancePacks by criteria: {}", criteria);
        return ResponseEntity.ok().body(insurancePackQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insurance-packs/:id} : get the "id" insurancePack.
     *
     * @param id the id of the insurancePackDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insurancePackDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InsurancePackDTO> getInsurancePack(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get InsurancePack : {}", id);
        Optional<InsurancePackDTO> insurancePackDTO = insurancePackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insurancePackDTO);
    }

    /**
     * {@code DELETE  /insurance-packs/:id} : delete the "id" insurancePack.
     *
     * @param id the id of the insurancePackDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurancePack(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete InsurancePack : {}", id);
        insurancePackService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /insurance-packs/_search?query=:query} : search for the insurancePack corresponding
     * to the query.
     *
     * @param query the query of the insurancePack search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<InsurancePackDTO>> searchInsurancePacks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of InsurancePacks for query {}", query);
        try {
            Page<InsurancePackDTO> page = insurancePackService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
