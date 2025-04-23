package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.WarrantyAsserts.*;
import static com.satoripop.insurance.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.satoripop.insurance.IntegrationTest;
import com.satoripop.insurance.domain.InsurancePack;
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.repository.WarrantyRepository;
import com.satoripop.insurance.repository.search.WarrantySearchRepository;
import com.satoripop.insurance.service.dto.WarrantyDTO;
import com.satoripop.insurance.service.mapper.WarrantyMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WarrantyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarrantyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_LIMIT = 1F;
    private static final Float UPDATED_LIMIT = 2F;
    private static final Float SMALLER_LIMIT = 1F - 1F;

    private static final Float DEFAULT_FRANCHISE = 1F;
    private static final Float UPDATED_FRANCHISE = 2F;
    private static final Float SMALLER_FRANCHISE = 1F - 1F;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Boolean DEFAULT_MANDATORY = false;
    private static final Boolean UPDATED_MANDATORY = true;

    private static final String ENTITY_API_URL = "/api/warranties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/warranties/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WarrantyRepository warrantyRepository;

    @Autowired
    private WarrantyMapper warrantyMapper;

    @Autowired
    private WarrantySearchRepository warrantySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarrantyMockMvc;

    private Warranty warranty;

    private Warranty insertedWarranty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createEntity() {
        return new Warranty()
            .name(DEFAULT_NAME)
            .limit(DEFAULT_LIMIT)
            .franchise(DEFAULT_FRANCHISE)
            .price(DEFAULT_PRICE)
            .mandatory(DEFAULT_MANDATORY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Warranty createUpdatedEntity() {
        return new Warranty()
            .name(UPDATED_NAME)
            .limit(UPDATED_LIMIT)
            .franchise(UPDATED_FRANCHISE)
            .price(UPDATED_PRICE)
            .mandatory(UPDATED_MANDATORY);
    }

    @BeforeEach
    void initTest() {
        warranty = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWarranty != null) {
            warrantyRepository.delete(insertedWarranty);
            warrantySearchRepository.delete(insertedWarranty);
            insertedWarranty = null;
        }
    }

    @Test
    @Transactional
    void createWarranty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);
        var returnedWarrantyDTO = om.readValue(
            restWarrantyMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WarrantyDTO.class
        );

        // Validate the Warranty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWarranty = warrantyMapper.toEntity(returnedWarrantyDTO);
        assertWarrantyUpdatableFieldsEquals(returnedWarranty, getPersistedWarranty(returnedWarranty));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWarranty = returnedWarranty;
    }

    @Test
    @Transactional
    void createWarrantyWithExistingId() throws Exception {
        // Create the Warranty with an existing ID
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarrantyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWarranties() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warranty.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].franchise").value(hasItem(DEFAULT_FRANCHISE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY)));
    }

    @Test
    @Transactional
    void getWarranty() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get the warranty
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL_ID, warranty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warranty.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.limit").value(DEFAULT_LIMIT.doubleValue()))
            .andExpect(jsonPath("$.franchise").value(DEFAULT_FRANCHISE.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.mandatory").value(DEFAULT_MANDATORY));
    }

    @Test
    @Transactional
    void getWarrantiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        UUID id = warranty.getId();

        defaultWarrantyFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllWarrantiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where name equals to
        defaultWarrantyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarrantiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where name in
        defaultWarrantyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarrantiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where name is not null
        defaultWarrantyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllWarrantiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where name contains
        defaultWarrantyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWarrantiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where name does not contain
        defaultWarrantyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit equals to
        defaultWarrantyFiltering("limit.equals=" + DEFAULT_LIMIT, "limit.equals=" + UPDATED_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit in
        defaultWarrantyFiltering("limit.in=" + DEFAULT_LIMIT + "," + UPDATED_LIMIT, "limit.in=" + UPDATED_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit is not null
        defaultWarrantyFiltering("limit.specified=true", "limit.specified=false");
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit is greater than or equal to
        defaultWarrantyFiltering("limit.greaterThanOrEqual=" + DEFAULT_LIMIT, "limit.greaterThanOrEqual=" + UPDATED_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit is less than or equal to
        defaultWarrantyFiltering("limit.lessThanOrEqual=" + DEFAULT_LIMIT, "limit.lessThanOrEqual=" + SMALLER_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit is less than
        defaultWarrantyFiltering("limit.lessThan=" + UPDATED_LIMIT, "limit.lessThan=" + DEFAULT_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByLimitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where limit is greater than
        defaultWarrantyFiltering("limit.greaterThan=" + SMALLER_LIMIT, "limit.greaterThan=" + DEFAULT_LIMIT);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise equals to
        defaultWarrantyFiltering("franchise.equals=" + DEFAULT_FRANCHISE, "franchise.equals=" + UPDATED_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise in
        defaultWarrantyFiltering("franchise.in=" + DEFAULT_FRANCHISE + "," + UPDATED_FRANCHISE, "franchise.in=" + UPDATED_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise is not null
        defaultWarrantyFiltering("franchise.specified=true", "franchise.specified=false");
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise is greater than or equal to
        defaultWarrantyFiltering("franchise.greaterThanOrEqual=" + DEFAULT_FRANCHISE, "franchise.greaterThanOrEqual=" + UPDATED_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise is less than or equal to
        defaultWarrantyFiltering("franchise.lessThanOrEqual=" + DEFAULT_FRANCHISE, "franchise.lessThanOrEqual=" + SMALLER_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise is less than
        defaultWarrantyFiltering("franchise.lessThan=" + UPDATED_FRANCHISE, "franchise.lessThan=" + DEFAULT_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByFranchiseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where franchise is greater than
        defaultWarrantyFiltering("franchise.greaterThan=" + SMALLER_FRANCHISE, "franchise.greaterThan=" + DEFAULT_FRANCHISE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price equals to
        defaultWarrantyFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price in
        defaultWarrantyFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price is not null
        defaultWarrantyFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price is greater than or equal to
        defaultWarrantyFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price is less than or equal to
        defaultWarrantyFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price is less than
        defaultWarrantyFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where price is greater than
        defaultWarrantyFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllWarrantiesByMandatoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where mandatory equals to
        defaultWarrantyFiltering("mandatory.equals=" + DEFAULT_MANDATORY, "mandatory.equals=" + UPDATED_MANDATORY);
    }

    @Test
    @Transactional
    void getAllWarrantiesByMandatoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where mandatory in
        defaultWarrantyFiltering("mandatory.in=" + DEFAULT_MANDATORY + "," + UPDATED_MANDATORY, "mandatory.in=" + UPDATED_MANDATORY);
    }

    @Test
    @Transactional
    void getAllWarrantiesByMandatoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        // Get all the warrantyList where mandatory is not null
        defaultWarrantyFiltering("mandatory.specified=true", "mandatory.specified=false");
    }

    @Test
    @Transactional
    void getAllWarrantiesByInsurancePacksIsEqualToSomething() throws Exception {
        InsurancePack insurancePacks;
        if (TestUtil.findAll(em, InsurancePack.class).isEmpty()) {
            warrantyRepository.saveAndFlush(warranty);
            insurancePacks = InsurancePackResourceIT.createEntity();
        } else {
            insurancePacks = TestUtil.findAll(em, InsurancePack.class).get(0);
        }
        em.persist(insurancePacks);
        em.flush();
        warranty.addInsurancePacks(insurancePacks);
        warrantyRepository.saveAndFlush(warranty);
        UUID insurancePacksId = insurancePacks.getId();
        // Get all the warrantyList where insurancePacks equals to insurancePacksId
        defaultWarrantyShouldBeFound("insurancePacksId.equals=" + insurancePacksId);

        // Get all the warrantyList where insurancePacks equals to UUID.randomUUID()
        defaultWarrantyShouldNotBeFound("insurancePacksId.equals=" + UUID.randomUUID());
    }

    private void defaultWarrantyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWarrantyShouldBeFound(shouldBeFound);
        defaultWarrantyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWarrantyShouldBeFound(String filter) throws Exception {
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warranty.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].franchise").value(hasItem(DEFAULT_FRANCHISE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY)));

        // Check, that the count call also returns 1
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWarrantyShouldNotBeFound(String filter) throws Exception {
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWarrantyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWarranty() throws Exception {
        // Get the warranty
        restWarrantyMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWarranty() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        warrantySearchRepository.save(warranty);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());

        // Update the warranty
        Warranty updatedWarranty = warrantyRepository.findById(warranty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWarranty are not directly saved in db
        em.detach(updatedWarranty);
        updatedWarranty
            .name(UPDATED_NAME)
            .limit(UPDATED_LIMIT)
            .franchise(UPDATED_FRANCHISE)
            .price(UPDATED_PRICE)
            .mandatory(UPDATED_MANDATORY);
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(updatedWarranty);

        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warrantyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWarrantyToMatchAllProperties(updatedWarranty);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Warranty> warrantySearchList = Streamable.of(warrantySearchRepository.findAll()).toList();
                Warranty testWarrantySearch = warrantySearchList.get(searchDatabaseSizeAfter - 1);

                assertWarrantyAllPropertiesEquals(testWarrantySearch, updatedWarranty);
            });
    }

    @Test
    @Transactional
    void putNonExistingWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warrantyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(warrantyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty.name(UPDATED_NAME);

        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarranty))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWarranty, warranty), getPersistedWarranty(warranty));
    }

    @Test
    @Transactional
    void fullUpdateWarrantyWithPatch() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the warranty using partial update
        Warranty partialUpdatedWarranty = new Warranty();
        partialUpdatedWarranty.setId(warranty.getId());

        partialUpdatedWarranty
            .name(UPDATED_NAME)
            .limit(UPDATED_LIMIT)
            .franchise(UPDATED_FRANCHISE)
            .price(UPDATED_PRICE)
            .mandatory(UPDATED_MANDATORY);

        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarranty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWarranty))
            )
            .andExpect(status().isOk());

        // Validate the Warranty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWarrantyUpdatableFieldsEquals(partialUpdatedWarranty, getPersistedWarranty(partialUpdatedWarranty));
    }

    @Test
    @Transactional
    void patchNonExistingWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warrantyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarranty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        warranty.setId(UUID.randomUUID());

        // Create the Warranty
        WarrantyDTO warrantyDTO = warrantyMapper.toDto(warranty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarrantyMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(warrantyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Warranty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWarranty() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);
        warrantyRepository.save(warranty);
        warrantySearchRepository.save(warranty);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the warranty
        restWarrantyMockMvc
            .perform(delete(ENTITY_API_URL_ID, warranty.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(warrantySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWarranty() throws Exception {
        // Initialize the database
        insertedWarranty = warrantyRepository.saveAndFlush(warranty);
        warrantySearchRepository.save(warranty);

        // Search the warranty
        restWarrantyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + warranty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warranty.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].limit").value(hasItem(DEFAULT_LIMIT.doubleValue())))
            .andExpect(jsonPath("$.[*].franchise").value(hasItem(DEFAULT_FRANCHISE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].mandatory").value(hasItem(DEFAULT_MANDATORY)));
    }

    protected long getRepositoryCount() {
        return warrantyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Warranty getPersistedWarranty(Warranty warranty) {
        return warrantyRepository.findById(warranty.getId()).orElseThrow();
    }

    protected void assertPersistedWarrantyToMatchAllProperties(Warranty expectedWarranty) {
        assertWarrantyAllPropertiesEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
    }

    protected void assertPersistedWarrantyToMatchUpdatableProperties(Warranty expectedWarranty) {
        assertWarrantyAllUpdatablePropertiesEquals(expectedWarranty, getPersistedWarranty(expectedWarranty));
    }
}
