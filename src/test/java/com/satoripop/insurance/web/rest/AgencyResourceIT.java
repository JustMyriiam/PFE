package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.AgencyAsserts.*;
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
import com.satoripop.insurance.domain.Agency;
import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.repository.AgencyRepository;
import com.satoripop.insurance.repository.search.AgencySearchRepository;
import com.satoripop.insurance.service.dto.AgencyDTO;
import com.satoripop.insurance.service.mapper.AgencyMapper;
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
 * Integration tests for the {@link AgencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgencyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MANAGER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANAGER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/agencies/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private AgencySearchRepository agencySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgencyMockMvc;

    private Agency agency;

    private Agency insertedAgency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createEntity() {
        return new Agency()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .region(DEFAULT_REGION)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .managerName(DEFAULT_MANAGER_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agency createUpdatedEntity() {
        return new Agency()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .region(UPDATED_REGION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .managerName(UPDATED_MANAGER_NAME);
    }

    @BeforeEach
    void initTest() {
        agency = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAgency != null) {
            agencyRepository.delete(insertedAgency);
            agencySearchRepository.delete(insertedAgency);
            insertedAgency = null;
        }
    }

    @Test
    @Transactional
    void createAgency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);
        var returnedAgencyDTO = om.readValue(
            restAgencyMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AgencyDTO.class
        );

        // Validate the Agency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAgency = agencyMapper.toEntity(returnedAgencyDTO);
        assertAgencyUpdatableFieldsEquals(returnedAgency, getPersistedAgency(returnedAgency));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAgency = returnedAgency;
    }

    @Test
    @Transactional
    void createAgencyWithExistingId() throws Exception {
        // Create the Agency with an existing ID
        insertedAgency = agencyRepository.saveAndFlush(agency);
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgencyMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAgencies() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)));
    }

    @Test
    @Transactional
    void getAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get the agency
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL_ID, agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agency.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.managerName").value(DEFAULT_MANAGER_NAME));
    }

    @Test
    @Transactional
    void getAgenciesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        UUID id = agency.getId();

        defaultAgencyFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name equals to
        defaultAgencyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name in
        defaultAgencyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name is not null
        defaultAgencyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name contains
        defaultAgencyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where name does not contain
        defaultAgencyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address equals to
        defaultAgencyFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address in
        defaultAgencyFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address is not null
        defaultAgencyFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address contains
        defaultAgencyFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where address does not contain
        defaultAgencyFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAgenciesByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where region equals to
        defaultAgencyFiltering("region.equals=" + DEFAULT_REGION, "region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllAgenciesByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where region in
        defaultAgencyFiltering("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION, "region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllAgenciesByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where region is not null
        defaultAgencyFiltering("region.specified=true", "region.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByRegionContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where region contains
        defaultAgencyFiltering("region.contains=" + DEFAULT_REGION, "region.contains=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllAgenciesByRegionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where region does not contain
        defaultAgencyFiltering("region.doesNotContain=" + UPDATED_REGION, "region.doesNotContain=" + DEFAULT_REGION);
    }

    @Test
    @Transactional
    void getAllAgenciesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where phoneNumber equals to
        defaultAgencyFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgenciesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where phoneNumber in
        defaultAgencyFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAgenciesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where phoneNumber is not null
        defaultAgencyFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where phoneNumber contains
        defaultAgencyFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgenciesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where phoneNumber does not contain
        defaultAgencyFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAgenciesByManagerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where managerName equals to
        defaultAgencyFiltering("managerName.equals=" + DEFAULT_MANAGER_NAME, "managerName.equals=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByManagerNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where managerName in
        defaultAgencyFiltering(
            "managerName.in=" + DEFAULT_MANAGER_NAME + "," + UPDATED_MANAGER_NAME,
            "managerName.in=" + UPDATED_MANAGER_NAME
        );
    }

    @Test
    @Transactional
    void getAllAgenciesByManagerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where managerName is not null
        defaultAgencyFiltering("managerName.specified=true", "managerName.specified=false");
    }

    @Test
    @Transactional
    void getAllAgenciesByManagerNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where managerName contains
        defaultAgencyFiltering("managerName.contains=" + DEFAULT_MANAGER_NAME, "managerName.contains=" + UPDATED_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByManagerNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        // Get all the agencyList where managerName does not contain
        defaultAgencyFiltering("managerName.doesNotContain=" + UPDATED_MANAGER_NAME, "managerName.doesNotContain=" + DEFAULT_MANAGER_NAME);
    }

    @Test
    @Transactional
    void getAllAgenciesByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            agencyRepository.saveAndFlush(agency);
            city = CityResourceIT.createEntity();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        agency.setCity(city);
        agencyRepository.saveAndFlush(agency);
        UUID cityId = city.getId();
        // Get all the agencyList where city equals to cityId
        defaultAgencyShouldBeFound("cityId.equals=" + cityId);

        // Get all the agencyList where city equals to UUID.randomUUID()
        defaultAgencyShouldNotBeFound("cityId.equals=" + UUID.randomUUID());
    }

    private void defaultAgencyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAgencyShouldBeFound(shouldBeFound);
        defaultAgencyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAgencyShouldBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)));

        // Check, that the count call also returns 1
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAgencyShouldNotBeFound(String filter) throws Exception {
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAgencyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAgency() throws Exception {
        // Get the agency
        restAgencyMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        agencySearchRepository.save(agency);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());

        // Update the agency
        Agency updatedAgency = agencyRepository.findById(agency.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAgency are not directly saved in db
        em.detach(updatedAgency);
        updatedAgency
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .region(UPDATED_REGION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .managerName(UPDATED_MANAGER_NAME);
        AgencyDTO agencyDTO = agencyMapper.toDto(updatedAgency);

        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAgencyToMatchAllProperties(updatedAgency);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Agency> agencySearchList = Streamable.of(agencySearchRepository.findAll()).toList();
                Agency testAgencySearch = agencySearchList.get(searchDatabaseSizeAfter - 1);

                assertAgencyAllPropertiesEquals(testAgencySearch, updatedAgency);
            });
    }

    @Test
    @Transactional
    void putNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agencyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(agencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency.name(UPDATED_NAME).phoneNumber(UPDATED_PHONE_NUMBER);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAgency, agency), getPersistedAgency(agency));
    }

    @Test
    @Transactional
    void fullUpdateAgencyWithPatch() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the agency using partial update
        Agency partialUpdatedAgency = new Agency();
        partialUpdatedAgency.setId(agency.getId());

        partialUpdatedAgency
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .region(UPDATED_REGION)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .managerName(UPDATED_MANAGER_NAME);

        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAgency))
            )
            .andExpect(status().isOk());

        // Validate the Agency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAgencyUpdatableFieldsEquals(partialUpdatedAgency, getPersistedAgency(partialUpdatedAgency));
    }

    @Test
    @Transactional
    void patchNonExistingAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agencyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        agency.setId(UUID.randomUUID());

        // Create the Agency
        AgencyDTO agencyDTO = agencyMapper.toDto(agency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgencyMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(agencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);
        agencyRepository.save(agency);
        agencySearchRepository.save(agency);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the agency
        restAgencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, agency.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(agencySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAgency() throws Exception {
        // Initialize the database
        insertedAgency = agencyRepository.saveAndFlush(agency);
        agencySearchRepository.save(agency);

        // Search the agency
        restAgencyMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + agency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agency.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].managerName").value(hasItem(DEFAULT_MANAGER_NAME)));
    }

    protected long getRepositoryCount() {
        return agencyRepository.count();
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

    protected Agency getPersistedAgency(Agency agency) {
        return agencyRepository.findById(agency.getId()).orElseThrow();
    }

    protected void assertPersistedAgencyToMatchAllProperties(Agency expectedAgency) {
        assertAgencyAllPropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }

    protected void assertPersistedAgencyToMatchUpdatableProperties(Agency expectedAgency) {
        assertAgencyAllUpdatablePropertiesEquals(expectedAgency, getPersistedAgency(expectedAgency));
    }
}
