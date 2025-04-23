package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.GovernorateAsserts.*;
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
import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.repository.GovernorateRepository;
import com.satoripop.insurance.repository.search.GovernorateSearchRepository;
import com.satoripop.insurance.service.dto.GovernorateDTO;
import com.satoripop.insurance.service.mapper.GovernorateMapper;
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
 * Integration tests for the {@link GovernorateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GovernorateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/governorates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/governorates/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private GovernorateRepository governorateRepository;

    @Autowired
    private GovernorateMapper governorateMapper;

    @Autowired
    private GovernorateSearchRepository governorateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGovernorateMockMvc;

    private Governorate governorate;

    private Governorate insertedGovernorate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Governorate createEntity() {
        return new Governorate().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Governorate createUpdatedEntity() {
        return new Governorate().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        governorate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedGovernorate != null) {
            governorateRepository.delete(insertedGovernorate);
            governorateSearchRepository.delete(insertedGovernorate);
            insertedGovernorate = null;
        }
    }

    @Test
    @Transactional
    void createGovernorate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);
        var returnedGovernorateDTO = om.readValue(
            restGovernorateMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            GovernorateDTO.class
        );

        // Validate the Governorate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedGovernorate = governorateMapper.toEntity(returnedGovernorateDTO);
        assertGovernorateUpdatableFieldsEquals(returnedGovernorate, getPersistedGovernorate(returnedGovernorate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedGovernorate = returnedGovernorate;
    }

    @Test
    @Transactional
    void createGovernorateWithExistingId() throws Exception {
        // Create the Governorate with an existing ID
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restGovernorateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        // set the field null
        governorate.setName(null);

        // Create the Governorate, which fails.
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        restGovernorateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllGovernorates() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(governorate.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get the governorate
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL_ID, governorate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(governorate.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getGovernoratesByIdFiltering() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        UUID id = governorate.getId();

        defaultGovernorateFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllGovernoratesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList where name equals to
        defaultGovernorateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGovernoratesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList where name in
        defaultGovernorateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGovernoratesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList where name is not null
        defaultGovernorateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllGovernoratesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList where name contains
        defaultGovernorateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGovernoratesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        // Get all the governorateList where name does not contain
        defaultGovernorateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultGovernorateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultGovernorateShouldBeFound(shouldBeFound);
        defaultGovernorateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGovernorateShouldBeFound(String filter) throws Exception {
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(governorate.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGovernorateShouldNotBeFound(String filter) throws Exception {
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGovernorateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGovernorate() throws Exception {
        // Get the governorate
        restGovernorateMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        governorateSearchRepository.save(governorate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());

        // Update the governorate
        Governorate updatedGovernorate = governorateRepository.findById(governorate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedGovernorate are not directly saved in db
        em.detach(updatedGovernorate);
        updatedGovernorate.name(UPDATED_NAME);
        GovernorateDTO governorateDTO = governorateMapper.toDto(updatedGovernorate);

        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, governorateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedGovernorateToMatchAllProperties(updatedGovernorate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Governorate> governorateSearchList = Streamable.of(governorateSearchRepository.findAll()).toList();
                Governorate testGovernorateSearch = governorateSearchList.get(searchDatabaseSizeAfter - 1);

                assertGovernorateAllPropertiesEquals(testGovernorateSearch, updatedGovernorate);
            });
    }

    @Test
    @Transactional
    void putNonExistingGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, governorateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(governorateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateGovernorateWithPatch() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the governorate using partial update
        Governorate partialUpdatedGovernorate = new Governorate();
        partialUpdatedGovernorate.setId(governorate.getId());

        partialUpdatedGovernorate.name(UPDATED_NAME);

        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGovernorate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGovernorate))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGovernorateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedGovernorate, governorate),
            getPersistedGovernorate(governorate)
        );
    }

    @Test
    @Transactional
    void fullUpdateGovernorateWithPatch() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the governorate using partial update
        Governorate partialUpdatedGovernorate = new Governorate();
        partialUpdatedGovernorate.setId(governorate.getId());

        partialUpdatedGovernorate.name(UPDATED_NAME);

        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGovernorate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedGovernorate))
            )
            .andExpect(status().isOk());

        // Validate the Governorate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertGovernorateUpdatableFieldsEquals(partialUpdatedGovernorate, getPersistedGovernorate(partialUpdatedGovernorate));
    }

    @Test
    @Transactional
    void patchNonExistingGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, governorateDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGovernorate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        governorate.setId(UUID.randomUUID());

        // Create the Governorate
        GovernorateDTO governorateDTO = governorateMapper.toDto(governorate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGovernorateMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(governorateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Governorate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);
        governorateRepository.save(governorate);
        governorateSearchRepository.save(governorate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the governorate
        restGovernorateMockMvc
            .perform(delete(ENTITY_API_URL_ID, governorate.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(governorateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchGovernorate() throws Exception {
        // Initialize the database
        insertedGovernorate = governorateRepository.saveAndFlush(governorate);
        governorateSearchRepository.save(governorate);

        // Search the governorate
        restGovernorateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + governorate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(governorate.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    protected long getRepositoryCount() {
        return governorateRepository.count();
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

    protected Governorate getPersistedGovernorate(Governorate governorate) {
        return governorateRepository.findById(governorate.getId()).orElseThrow();
    }

    protected void assertPersistedGovernorateToMatchAllProperties(Governorate expectedGovernorate) {
        assertGovernorateAllPropertiesEquals(expectedGovernorate, getPersistedGovernorate(expectedGovernorate));
    }

    protected void assertPersistedGovernorateToMatchUpdatableProperties(Governorate expectedGovernorate) {
        assertGovernorateAllUpdatablePropertiesEquals(expectedGovernorate, getPersistedGovernorate(expectedGovernorate));
    }
}
