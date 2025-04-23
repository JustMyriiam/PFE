package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.ClaimAsserts.*;
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
import com.satoripop.insurance.domain.Claim;
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.domain.enumeration.ClaimStatus;
import com.satoripop.insurance.repository.ClaimRepository;
import com.satoripop.insurance.repository.search.ClaimSearchRepository;
import com.satoripop.insurance.service.dto.ClaimDTO;
import com.satoripop.insurance.service.mapper.ClaimMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClaimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClaimResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final ClaimStatus DEFAULT_STATUS = ClaimStatus.IN_PROGRESS;
    private static final ClaimStatus UPDATED_STATUS = ClaimStatus.RESOLVED;

    private static final String ENTITY_API_URL = "/api/claims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/claims/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ClaimMapper claimMapper;

    @Autowired
    private ClaimSearchRepository claimSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClaimMockMvc;

    private Claim claim;

    private Claim insertedClaim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createEntity() {
        return new Claim().type(DEFAULT_TYPE).description(DEFAULT_DESCRIPTION).date(DEFAULT_DATE).status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createUpdatedEntity() {
        return new Claim().type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE).status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        claim = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClaim != null) {
            claimRepository.delete(insertedClaim);
            claimSearchRepository.delete(insertedClaim);
            insertedClaim = null;
        }
    }

    @Test
    @Transactional
    void createClaim() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);
        var returnedClaimDTO = om.readValue(
            restClaimMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(claimDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClaimDTO.class
        );

        // Validate the Claim in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClaim = claimMapper.toEntity(returnedClaimDTO);
        assertClaimUpdatableFieldsEquals(returnedClaim, getPersistedClaim(returnedClaim));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedClaim = returnedClaim;
    }

    @Test
    @Transactional
    void createClaimWithExistingId() throws Exception {
        // Create the Claim with an existing ID
        insertedClaim = claimRepository.saveAndFlush(claim);
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(claimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllClaims() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getClaim() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get the claim
        restClaimMockMvc
            .perform(get(ENTITY_API_URL_ID, claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(claim.getId().toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getClaimsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        UUID id = claim.getId();

        defaultClaimFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllClaimsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where type equals to
        defaultClaimFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllClaimsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where type in
        defaultClaimFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllClaimsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where type is not null
        defaultClaimFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllClaimsByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where type contains
        defaultClaimFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllClaimsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where type does not contain
        defaultClaimFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllClaimsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where description equals to
        defaultClaimFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClaimsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where description in
        defaultClaimFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClaimsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where description is not null
        defaultClaimFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllClaimsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where description contains
        defaultClaimFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClaimsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where description does not contain
        defaultClaimFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date equals to
        defaultClaimFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date in
        defaultClaimFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date is not null
        defaultClaimFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date is greater than or equal to
        defaultClaimFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date is less than or equal to
        defaultClaimFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date is less than
        defaultClaimFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where date is greater than
        defaultClaimFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllClaimsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where status equals to
        defaultClaimFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllClaimsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where status in
        defaultClaimFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllClaimsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        // Get all the claimList where status is not null
        defaultClaimFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllClaimsByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            claimRepository.saveAndFlush(claim);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        claim.setClient(client);
        claimRepository.saveAndFlush(claim);
        UUID clientId = client.getId();
        // Get all the claimList where client equals to clientId
        defaultClaimShouldBeFound("clientId.equals=" + clientId);

        // Get all the claimList where client equals to UUID.randomUUID()
        defaultClaimShouldNotBeFound("clientId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllClaimsByContractIsEqualToSomething() throws Exception {
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            claimRepository.saveAndFlush(claim);
            contract = ContractResourceIT.createEntity();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        em.persist(contract);
        em.flush();
        claim.setContract(contract);
        claimRepository.saveAndFlush(claim);
        UUID contractId = contract.getId();
        // Get all the claimList where contract equals to contractId
        defaultClaimShouldBeFound("contractId.equals=" + contractId);

        // Get all the claimList where contract equals to UUID.randomUUID()
        defaultClaimShouldNotBeFound("contractId.equals=" + UUID.randomUUID());
    }

    private void defaultClaimFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClaimShouldBeFound(shouldBeFound);
        defaultClaimShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClaimShouldBeFound(String filter) throws Exception {
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClaimShouldNotBeFound(String filter) throws Exception {
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClaimMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClaim() throws Exception {
        // Get the claim
        restClaimMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClaim() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        claimSearchRepository.save(claim);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());

        // Update the claim
        Claim updatedClaim = claimRepository.findById(claim.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClaim are not directly saved in db
        em.detach(updatedClaim);
        updatedClaim.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE).status(UPDATED_STATUS);
        ClaimDTO claimDTO = claimMapper.toDto(updatedClaim);

        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, claimDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(claimDTO))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClaimToMatchAllProperties(updatedClaim);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Claim> claimSearchList = Streamable.of(claimSearchRepository.findAll()).toList();
                Claim testClaimSearch = claimSearchList.get(searchDatabaseSizeAfter - 1);

                assertClaimAllPropertiesEquals(testClaimSearch, updatedClaim);
            });
    }

    @Test
    @Transactional
    void putNonExistingClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, claimDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(claimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(claimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(claimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateClaimWithPatch() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the claim using partial update
        Claim partialUpdatedClaim = new Claim();
        partialUpdatedClaim.setId(claim.getId());

        partialUpdatedClaim.description(UPDATED_DESCRIPTION).status(UPDATED_STATUS);

        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClaim.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClaim))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClaimUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClaim, claim), getPersistedClaim(claim));
    }

    @Test
    @Transactional
    void fullUpdateClaimWithPatch() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the claim using partial update
        Claim partialUpdatedClaim = new Claim();
        partialUpdatedClaim.setId(claim.getId());

        partialUpdatedClaim.type(UPDATED_TYPE).description(UPDATED_DESCRIPTION).date(UPDATED_DATE).status(UPDATED_STATUS);

        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClaim.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClaim))
            )
            .andExpect(status().isOk());

        // Validate the Claim in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClaimUpdatableFieldsEquals(partialUpdatedClaim, getPersistedClaim(partialUpdatedClaim));
    }

    @Test
    @Transactional
    void patchNonExistingClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, claimDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(claimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(claimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClaim() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        claim.setId(UUID.randomUUID());

        // Create the Claim
        ClaimDTO claimDTO = claimMapper.toDto(claim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClaimMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(claimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Claim in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteClaim() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);
        claimRepository.save(claim);
        claimSearchRepository.save(claim);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the claim
        restClaimMockMvc
            .perform(delete(ENTITY_API_URL_ID, claim.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(claimSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchClaim() throws Exception {
        // Initialize the database
        insertedClaim = claimRepository.saveAndFlush(claim);
        claimSearchRepository.save(claim);

        // Search the claim
        restClaimMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    protected long getRepositoryCount() {
        return claimRepository.count();
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

    protected Claim getPersistedClaim(Claim claim) {
        return claimRepository.findById(claim.getId()).orElseThrow();
    }

    protected void assertPersistedClaimToMatchAllProperties(Claim expectedClaim) {
        assertClaimAllPropertiesEquals(expectedClaim, getPersistedClaim(expectedClaim));
    }

    protected void assertPersistedClaimToMatchUpdatableProperties(Claim expectedClaim) {
        assertClaimAllUpdatablePropertiesEquals(expectedClaim, getPersistedClaim(expectedClaim));
    }
}
