package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.InsurancePackAsserts.*;
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
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.domain.Warranty;
import com.satoripop.insurance.domain.enumeration.InsurancePackName;
import com.satoripop.insurance.repository.InsurancePackRepository;
import com.satoripop.insurance.repository.search.InsurancePackSearchRepository;
import com.satoripop.insurance.service.InsurancePackService;
import com.satoripop.insurance.service.dto.InsurancePackDTO;
import com.satoripop.insurance.service.mapper.InsurancePackMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InsurancePackResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InsurancePackResourceIT {

    private static final InsurancePackName DEFAULT_NAME = InsurancePackName.BASIC;
    private static final InsurancePackName UPDATED_NAME = InsurancePackName.COMFORT;

    private static final String DEFAULT_DESCIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCIPTION = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/insurance-packs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/insurance-packs/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InsurancePackRepository insurancePackRepository;

    @Mock
    private InsurancePackRepository insurancePackRepositoryMock;

    @Autowired
    private InsurancePackMapper insurancePackMapper;

    @Mock
    private InsurancePackService insurancePackServiceMock;

    @Autowired
    private InsurancePackSearchRepository insurancePackSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInsurancePackMockMvc;

    private InsurancePack insurancePack;

    private InsurancePack insertedInsurancePack;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePack createEntity() {
        return new InsurancePack().name(DEFAULT_NAME).desciption(DEFAULT_DESCIPTION).price(DEFAULT_PRICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InsurancePack createUpdatedEntity() {
        return new InsurancePack().name(UPDATED_NAME).desciption(UPDATED_DESCIPTION).price(UPDATED_PRICE);
    }

    @BeforeEach
    void initTest() {
        insurancePack = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInsurancePack != null) {
            insurancePackRepository.delete(insertedInsurancePack);
            insurancePackSearchRepository.delete(insertedInsurancePack);
            insertedInsurancePack = null;
        }
    }

    @Test
    @Transactional
    void createInsurancePack() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);
        var returnedInsurancePackDTO = om.readValue(
            restInsurancePackMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(insurancePackDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InsurancePackDTO.class
        );

        // Validate the InsurancePack in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInsurancePack = insurancePackMapper.toEntity(returnedInsurancePackDTO);
        assertInsurancePackUpdatableFieldsEquals(returnedInsurancePack, getPersistedInsurancePack(returnedInsurancePack));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedInsurancePack = returnedInsurancePack;
    }

    @Test
    @Transactional
    void createInsurancePackWithExistingId() throws Exception {
        // Create the InsurancePack with an existing ID
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restInsurancePackMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllInsurancePacks() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePack.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desciption").value(hasItem(DEFAULT_DESCIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsurancePacksWithEagerRelationshipsIsEnabled() throws Exception {
        when(insurancePackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsurancePackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(insurancePackServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInsurancePacksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(insurancePackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInsurancePackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(insurancePackRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInsurancePack() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get the insurancePack
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL_ID, insurancePack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(insurancePack.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.desciption").value(DEFAULT_DESCIPTION))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getInsurancePacksByIdFiltering() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        UUID id = insurancePack.getId();

        defaultInsurancePackFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where name equals to
        defaultInsurancePackFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where name in
        defaultInsurancePackFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where name is not null
        defaultInsurancePackFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePacksByDesciptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where desciption equals to
        defaultInsurancePackFiltering("desciption.equals=" + DEFAULT_DESCIPTION, "desciption.equals=" + UPDATED_DESCIPTION);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByDesciptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where desciption in
        defaultInsurancePackFiltering(
            "desciption.in=" + DEFAULT_DESCIPTION + "," + UPDATED_DESCIPTION,
            "desciption.in=" + UPDATED_DESCIPTION
        );
    }

    @Test
    @Transactional
    void getAllInsurancePacksByDesciptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where desciption is not null
        defaultInsurancePackFiltering("desciption.specified=true", "desciption.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePacksByDesciptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where desciption contains
        defaultInsurancePackFiltering("desciption.contains=" + DEFAULT_DESCIPTION, "desciption.contains=" + UPDATED_DESCIPTION);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByDesciptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where desciption does not contain
        defaultInsurancePackFiltering("desciption.doesNotContain=" + UPDATED_DESCIPTION, "desciption.doesNotContain=" + DEFAULT_DESCIPTION);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price equals to
        defaultInsurancePackFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price in
        defaultInsurancePackFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price is not null
        defaultInsurancePackFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price is greater than or equal to
        defaultInsurancePackFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price is less than or equal to
        defaultInsurancePackFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price is less than
        defaultInsurancePackFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        // Get all the insurancePackList where price is greater than
        defaultInsurancePackFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllInsurancePacksByWarrantiesIsEqualToSomething() throws Exception {
        Warranty warranties;
        if (TestUtil.findAll(em, Warranty.class).isEmpty()) {
            insurancePackRepository.saveAndFlush(insurancePack);
            warranties = WarrantyResourceIT.createEntity();
        } else {
            warranties = TestUtil.findAll(em, Warranty.class).get(0);
        }
        em.persist(warranties);
        em.flush();
        insurancePack.addWarranties(warranties);
        insurancePackRepository.saveAndFlush(insurancePack);
        UUID warrantiesId = warranties.getId();
        // Get all the insurancePackList where warranties equals to warrantiesId
        defaultInsurancePackShouldBeFound("warrantiesId.equals=" + warrantiesId);

        // Get all the insurancePackList where warranties equals to UUID.randomUUID()
        defaultInsurancePackShouldNotBeFound("warrantiesId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllInsurancePacksByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            insurancePackRepository.saveAndFlush(insurancePack);
            vehicle = VehicleResourceIT.createEntity();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        insurancePack.setVehicle(vehicle);
        insurancePackRepository.saveAndFlush(insurancePack);
        UUID vehicleId = vehicle.getId();
        // Get all the insurancePackList where vehicle equals to vehicleId
        defaultInsurancePackShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the insurancePackList where vehicle equals to UUID.randomUUID()
        defaultInsurancePackShouldNotBeFound("vehicleId.equals=" + UUID.randomUUID());
    }

    private void defaultInsurancePackFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInsurancePackShouldBeFound(shouldBeFound);
        defaultInsurancePackShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInsurancePackShouldBeFound(String filter) throws Exception {
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePack.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desciption").value(hasItem(DEFAULT_DESCIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInsurancePackShouldNotBeFound(String filter) throws Exception {
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInsurancePackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInsurancePack() throws Exception {
        // Get the insurancePack
        restInsurancePackMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInsurancePack() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        insurancePackSearchRepository.save(insurancePack);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());

        // Update the insurancePack
        InsurancePack updatedInsurancePack = insurancePackRepository.findById(insurancePack.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInsurancePack are not directly saved in db
        em.detach(updatedInsurancePack);
        updatedInsurancePack.name(UPDATED_NAME).desciption(UPDATED_DESCIPTION).price(UPDATED_PRICE);
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(updatedInsurancePack);

        restInsurancePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancePackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInsurancePackToMatchAllProperties(updatedInsurancePack);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<InsurancePack> insurancePackSearchList = Streamable.of(insurancePackSearchRepository.findAll()).toList();
                InsurancePack testInsurancePackSearch = insurancePackSearchList.get(searchDatabaseSizeAfter - 1);

                assertInsurancePackAllPropertiesEquals(testInsurancePackSearch, updatedInsurancePack);
            });
    }

    @Test
    @Transactional
    void putNonExistingInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, insurancePackDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateInsurancePackWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePack using partial update
        InsurancePack partialUpdatedInsurancePack = new InsurancePack();
        partialUpdatedInsurancePack.setId(insurancePack.getId());

        partialUpdatedInsurancePack.name(UPDATED_NAME).price(UPDATED_PRICE);

        restInsurancePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePack.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePack))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePack in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePackUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInsurancePack, insurancePack),
            getPersistedInsurancePack(insurancePack)
        );
    }

    @Test
    @Transactional
    void fullUpdateInsurancePackWithPatch() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the insurancePack using partial update
        InsurancePack partialUpdatedInsurancePack = new InsurancePack();
        partialUpdatedInsurancePack.setId(insurancePack.getId());

        partialUpdatedInsurancePack.name(UPDATED_NAME).desciption(UPDATED_DESCIPTION).price(UPDATED_PRICE);

        restInsurancePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInsurancePack.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInsurancePack))
            )
            .andExpect(status().isOk());

        // Validate the InsurancePack in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInsurancePackUpdatableFieldsEquals(partialUpdatedInsurancePack, getPersistedInsurancePack(partialUpdatedInsurancePack));
    }

    @Test
    @Transactional
    void patchNonExistingInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, insurancePackDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInsurancePack() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        insurancePack.setId(UUID.randomUUID());

        // Create the InsurancePack
        InsurancePackDTO insurancePackDTO = insurancePackMapper.toDto(insurancePack);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInsurancePackMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(insurancePackDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InsurancePack in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteInsurancePack() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);
        insurancePackRepository.save(insurancePack);
        insurancePackSearchRepository.save(insurancePack);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the insurancePack
        restInsurancePackMockMvc
            .perform(delete(ENTITY_API_URL_ID, insurancePack.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(insurancePackSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchInsurancePack() throws Exception {
        // Initialize the database
        insertedInsurancePack = insurancePackRepository.saveAndFlush(insurancePack);
        insurancePackSearchRepository.save(insurancePack);

        // Search the insurancePack
        restInsurancePackMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + insurancePack.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(insurancePack.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].desciption").value(hasItem(DEFAULT_DESCIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    protected long getRepositoryCount() {
        return insurancePackRepository.count();
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

    protected InsurancePack getPersistedInsurancePack(InsurancePack insurancePack) {
        return insurancePackRepository.findById(insurancePack.getId()).orElseThrow();
    }

    protected void assertPersistedInsurancePackToMatchAllProperties(InsurancePack expectedInsurancePack) {
        assertInsurancePackAllPropertiesEquals(expectedInsurancePack, getPersistedInsurancePack(expectedInsurancePack));
    }

    protected void assertPersistedInsurancePackToMatchUpdatableProperties(InsurancePack expectedInsurancePack) {
        assertInsurancePackAllUpdatablePropertiesEquals(expectedInsurancePack, getPersistedInsurancePack(expectedInsurancePack));
    }
}
