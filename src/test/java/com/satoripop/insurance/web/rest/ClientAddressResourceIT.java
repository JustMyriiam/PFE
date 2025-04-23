package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.ClientAddressAsserts.*;
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
import com.satoripop.insurance.domain.City;
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.repository.ClientAddressRepository;
import com.satoripop.insurance.repository.search.ClientAddressSearchRepository;
import com.satoripop.insurance.service.dto.ClientAddressDTO;
import com.satoripop.insurance.service.mapper.ClientAddressMapper;
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
 * Integration tests for the {@link ClientAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientAddressResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_REGION = "AAAAAAAAAA";
    private static final String UPDATED_REGION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/client-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/client-addresses/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientAddressRepository clientAddressRepository;

    @Autowired
    private ClientAddressMapper clientAddressMapper;

    @Autowired
    private ClientAddressSearchRepository clientAddressSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientAddressMockMvc;

    private ClientAddress clientAddress;

    private ClientAddress insertedClientAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientAddress createEntity() {
        return new ClientAddress().address(DEFAULT_ADDRESS).region(DEFAULT_REGION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientAddress createUpdatedEntity() {
        return new ClientAddress().address(UPDATED_ADDRESS).region(UPDATED_REGION);
    }

    @BeforeEach
    void initTest() {
        clientAddress = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClientAddress != null) {
            clientAddressRepository.delete(insertedClientAddress);
            clientAddressSearchRepository.delete(insertedClientAddress);
            insertedClientAddress = null;
        }
    }

    @Test
    @Transactional
    void createClientAddress() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);
        var returnedClientAddressDTO = om.readValue(
            restClientAddressMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(clientAddressDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientAddressDTO.class
        );

        // Validate the ClientAddress in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClientAddress = clientAddressMapper.toEntity(returnedClientAddressDTO);
        assertClientAddressUpdatableFieldsEquals(returnedClientAddress, getPersistedClientAddress(returnedClientAddress));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedClientAddress = returnedClientAddress;
    }

    @Test
    @Transactional
    void createClientAddressWithExistingId() throws Exception {
        // Create the ClientAddress with an existing ID
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientAddressMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllClientAddresses() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAddress.getId().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)));
    }

    @Test
    @Transactional
    void getClientAddress() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get the clientAddress
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, clientAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientAddress.getId().toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION));
    }

    @Test
    @Transactional
    void getClientAddressesByIdFiltering() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        UUID id = clientAddress.getId();

        defaultClientAddressFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllClientAddressesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where address equals to
        defaultClientAddressFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientAddressesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where address in
        defaultClientAddressFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientAddressesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where address is not null
        defaultClientAddressFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllClientAddressesByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where address contains
        defaultClientAddressFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientAddressesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where address does not contain
        defaultClientAddressFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllClientAddressesByRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where region equals to
        defaultClientAddressFiltering("region.equals=" + DEFAULT_REGION, "region.equals=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllClientAddressesByRegionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where region in
        defaultClientAddressFiltering("region.in=" + DEFAULT_REGION + "," + UPDATED_REGION, "region.in=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllClientAddressesByRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where region is not null
        defaultClientAddressFiltering("region.specified=true", "region.specified=false");
    }

    @Test
    @Transactional
    void getAllClientAddressesByRegionContainsSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where region contains
        defaultClientAddressFiltering("region.contains=" + DEFAULT_REGION, "region.contains=" + UPDATED_REGION);
    }

    @Test
    @Transactional
    void getAllClientAddressesByRegionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        // Get all the clientAddressList where region does not contain
        defaultClientAddressFiltering("region.doesNotContain=" + UPDATED_REGION, "region.doesNotContain=" + DEFAULT_REGION);
    }

    @Test
    @Transactional
    void getAllClientAddressesByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            clientAddressRepository.saveAndFlush(clientAddress);
            city = CityResourceIT.createEntity();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        clientAddress.setCity(city);
        clientAddressRepository.saveAndFlush(clientAddress);
        UUID cityId = city.getId();
        // Get all the clientAddressList where city equals to cityId
        defaultClientAddressShouldBeFound("cityId.equals=" + cityId);

        // Get all the clientAddressList where city equals to UUID.randomUUID()
        defaultClientAddressShouldNotBeFound("cityId.equals=" + UUID.randomUUID());
    }

    private void defaultClientAddressFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientAddressShouldBeFound(shouldBeFound);
        defaultClientAddressShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientAddressShouldBeFound(String filter) throws Exception {
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAddress.getId().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)));

        // Check, that the count call also returns 1
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientAddressShouldNotBeFound(String filter) throws Exception {
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClientAddress() throws Exception {
        // Get the clientAddress
        restClientAddressMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClientAddress() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientAddressSearchRepository.save(clientAddress);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());

        // Update the clientAddress
        ClientAddress updatedClientAddress = clientAddressRepository.findById(clientAddress.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClientAddress are not directly saved in db
        em.detach(updatedClientAddress);
        updatedClientAddress.address(UPDATED_ADDRESS).region(UPDATED_REGION);
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(updatedClientAddress);

        restClientAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isOk());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientAddressToMatchAllProperties(updatedClientAddress);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ClientAddress> clientAddressSearchList = Streamable.of(clientAddressSearchRepository.findAll()).toList();
                ClientAddress testClientAddressSearch = clientAddressSearchList.get(searchDatabaseSizeAfter - 1);

                assertClientAddressAllPropertiesEquals(testClientAddressSearch, updatedClientAddress);
            });
    }

    @Test
    @Transactional
    void putNonExistingClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientAddressDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateClientAddressWithPatch() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientAddress using partial update
        ClientAddress partialUpdatedClientAddress = new ClientAddress();
        partialUpdatedClientAddress.setId(clientAddress.getId());

        partialUpdatedClientAddress.address(UPDATED_ADDRESS).region(UPDATED_REGION);

        restClientAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientAddress))
            )
            .andExpect(status().isOk());

        // Validate the ClientAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientAddressUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedClientAddress, clientAddress),
            getPersistedClientAddress(clientAddress)
        );
    }

    @Test
    @Transactional
    void fullUpdateClientAddressWithPatch() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the clientAddress using partial update
        ClientAddress partialUpdatedClientAddress = new ClientAddress();
        partialUpdatedClientAddress.setId(clientAddress.getId());

        partialUpdatedClientAddress.address(UPDATED_ADDRESS).region(UPDATED_REGION);

        restClientAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientAddress.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClientAddress))
            )
            .andExpect(status().isOk());

        // Validate the ClientAddress in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientAddressUpdatableFieldsEquals(partialUpdatedClientAddress, getPersistedClientAddress(partialUpdatedClientAddress));
    }

    @Test
    @Transactional
    void patchNonExistingClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientAddressDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientAddress() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        clientAddress.setId(UUID.randomUUID());

        // Create the ClientAddress
        ClientAddressDTO clientAddressDTO = clientAddressMapper.toDto(clientAddress);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientAddressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientAddress in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteClientAddress() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);
        clientAddressRepository.save(clientAddress);
        clientAddressSearchRepository.save(clientAddress);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the clientAddress
        restClientAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientAddress.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientAddressSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchClientAddress() throws Exception {
        // Initialize the database
        insertedClientAddress = clientAddressRepository.saveAndFlush(clientAddress);
        clientAddressSearchRepository.save(clientAddress);

        // Search the clientAddress
        restClientAddressMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + clientAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientAddress.getId().toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION)));
    }

    protected long getRepositoryCount() {
        return clientAddressRepository.count();
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

    protected ClientAddress getPersistedClientAddress(ClientAddress clientAddress) {
        return clientAddressRepository.findById(clientAddress.getId()).orElseThrow();
    }

    protected void assertPersistedClientAddressToMatchAllProperties(ClientAddress expectedClientAddress) {
        assertClientAddressAllPropertiesEquals(expectedClientAddress, getPersistedClientAddress(expectedClientAddress));
    }

    protected void assertPersistedClientAddressToMatchUpdatableProperties(ClientAddress expectedClientAddress) {
        assertClientAddressAllUpdatablePropertiesEquals(expectedClientAddress, getPersistedClientAddress(expectedClientAddress));
    }
}
