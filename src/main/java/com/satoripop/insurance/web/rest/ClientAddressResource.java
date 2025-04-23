package com.satoripop.insurance.web.rest;

import com.satoripop.insurance.repository.ClientAddressRepository;
import com.satoripop.insurance.service.ClientAddressQueryService;
import com.satoripop.insurance.service.ClientAddressService;
import com.satoripop.insurance.service.criteria.ClientAddressCriteria;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
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
 * REST controller for managing {@link com.satoripop.insurance.domain.ClientAddress}.
 */
@RestController
@RequestMapping("/api/client-addresses")
public class ClientAddressResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientAddressResource.class);

    private static final String ENTITY_NAME = "insuranceClientAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientAddressService clientAddressService;

    private final ClientAddressRepository clientAddressRepository;

    private final ClientAddressQueryService clientAddressQueryService;

    public ClientAddressResource(
        ClientAddressService clientAddressService,
        ClientAddressRepository clientAddressRepository,
        ClientAddressQueryService clientAddressQueryService
    ) {
        this.clientAddressService = clientAddressService;
        this.clientAddressRepository = clientAddressRepository;
        this.clientAddressQueryService = clientAddressQueryService;
    }

    /**
     * {@code POST  /client-addresses} : Create a new clientAddress.
     *
     * @param clientAddressDTO the clientAddressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientAddressDTO, or with status {@code 400 (Bad Request)} if the clientAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientAddressDTO> createClientAddress(@RequestBody ClientAddressDTO clientAddressDTO) throws URISyntaxException {
        LOG.debug("REST request to save ClientAddress : {}", clientAddressDTO);
        if (clientAddressDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clientAddressDTO = clientAddressService.save(clientAddressDTO);
        return ResponseEntity.created(new URI("/api/client-addresses/" + clientAddressDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clientAddressDTO.getId().toString()))
            .body(clientAddressDTO);
    }

    /**
     * {@code PUT  /client-addresses/:id} : Updates an existing clientAddress.
     *
     * @param id the id of the clientAddressDTO to save.
     * @param clientAddressDTO the clientAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientAddressDTO,
     * or with status {@code 400 (Bad Request)} if the clientAddressDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientAddressDTO> updateClientAddress(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ClientAddressDTO clientAddressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClientAddress : {}, {}", id, clientAddressDTO);
        if (clientAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientAddressDTO = clientAddressService.update(clientAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientAddressDTO.getId().toString()))
            .body(clientAddressDTO);
    }

    /**
     * {@code PATCH  /client-addresses/:id} : Partial updates given fields of an existing clientAddress, field will ignore if it is null
     *
     * @param id the id of the clientAddressDTO to save.
     * @param clientAddressDTO the clientAddressDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientAddressDTO,
     * or with status {@code 400 (Bad Request)} if the clientAddressDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientAddressDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientAddressDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientAddressDTO> partialUpdateClientAddress(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ClientAddressDTO clientAddressDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClientAddress partially : {}, {}", id, clientAddressDTO);
        if (clientAddressDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientAddressDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientAddressDTO> result = clientAddressService.partialUpdate(clientAddressDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientAddressDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /client-addresses} : get all the clientAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientAddresses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ClientAddressDTO>> getAllClientAddresses(
        ClientAddressCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ClientAddresses by criteria: {}", criteria);

        Page<ClientAddressDTO> page = clientAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /client-addresses/count} : count all the clientAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countClientAddresses(ClientAddressCriteria criteria) {
        LOG.debug("REST request to count ClientAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(clientAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /client-addresses/:id} : get the "id" clientAddress.
     *
     * @param id the id of the clientAddressDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientAddressDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientAddressDTO> getClientAddress(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get ClientAddress : {}", id);
        Optional<ClientAddressDTO> clientAddressDTO = clientAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientAddressDTO);
    }

    /**
     * {@code DELETE  /client-addresses/:id} : delete the "id" clientAddress.
     *
     * @param id the id of the clientAddressDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientAddress(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete ClientAddress : {}", id);
        clientAddressService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /client-addresses/_search?query=:query} : search for the clientAddress corresponding
     * to the query.
     *
     * @param query the query of the clientAddress search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ClientAddressDTO>> searchClientAddresses(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ClientAddresses for query {}", query);
        try {
            Page<ClientAddressDTO> page = clientAddressService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
