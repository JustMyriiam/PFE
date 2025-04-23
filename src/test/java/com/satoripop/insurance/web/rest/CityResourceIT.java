package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.CityAsserts.*;
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
import com.satoripop.insurance.domain.Governorate;
import com.satoripop.insurance.repository.CityRepository;
import com.satoripop.insurance.repository.search.CitySearchRepository;
import com.satoripop.insurance.service.dto.CityDTO;
import com.satoripop.insurance.service.mapper.CityMapper;
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
 * Integration tests for the {@link CityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cities/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private CitySearchRepository citySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCityMockMvc;

    private City city;

    private City insertedCity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createEntity() {
        return new City().name(DEFAULT_NAME).postalCode(DEFAULT_POSTAL_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static City createUpdatedEntity() {
        return new City().name(UPDATED_NAME).postalCode(UPDATED_POSTAL_CODE);
    }

    @BeforeEach
    void initTest() {
        city = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCity != null) {
            cityRepository.delete(insertedCity);
            citySearchRepository.delete(insertedCity);
            insertedCity = null;
        }
    }

    @Test
    @Transactional
    void createCity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);
        var returnedCityDTO = om.readValue(
            restCityMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CityDTO.class
        );

        // Validate the City in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCity = cityMapper.toEntity(returnedCityDTO);
        assertCityUpdatableFieldsEquals(returnedCity, getPersistedCity(returnedCity));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCity = returnedCity;
    }

    @Test
    @Transactional
    void createCityWithExistingId() throws Exception {
        // Create the City with an existing ID
        insertedCity = cityRepository.saveAndFlush(city);
        CityDTO cityDTO = cityMapper.toDto(city);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCityMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllCities() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));
    }

    @Test
    @Transactional
    void getCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get the city
        restCityMockMvc
            .perform(get(ENTITY_API_URL_ID, city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(city.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE));
    }

    @Test
    @Transactional
    void getCitiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        UUID id = city.getId();

        defaultCityFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name equals to
        defaultCityFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name in
        defaultCityFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name is not null
        defaultCityFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name contains
        defaultCityFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where name does not contain
        defaultCityFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCitiesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where postalCode equals to
        defaultCityFiltering("postalCode.equals=" + DEFAULT_POSTAL_CODE, "postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where postalCode in
        defaultCityFiltering("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE, "postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where postalCode is not null
        defaultCityFiltering("postalCode.specified=true", "postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCitiesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where postalCode contains
        defaultCityFiltering("postalCode.contains=" + DEFAULT_POSTAL_CODE, "postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        // Get all the cityList where postalCode does not contain
        defaultCityFiltering("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE, "postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCitiesByGovernorateIsEqualToSomething() throws Exception {
        Governorate governorate;
        if (TestUtil.findAll(em, Governorate.class).isEmpty()) {
            cityRepository.saveAndFlush(city);
            governorate = GovernorateResourceIT.createEntity();
        } else {
            governorate = TestUtil.findAll(em, Governorate.class).get(0);
        }
        em.persist(governorate);
        em.flush();
        city.setGovernorate(governorate);
        cityRepository.saveAndFlush(city);
        UUID governorateId = governorate.getId();
        // Get all the cityList where governorate equals to governorateId
        defaultCityShouldBeFound("governorateId.equals=" + governorateId);

        // Get all the cityList where governorate equals to UUID.randomUUID()
        defaultCityShouldNotBeFound("governorateId.equals=" + UUID.randomUUID());
    }

    private void defaultCityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCityShouldBeFound(shouldBeFound);
        defaultCityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCityShouldBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));

        // Check, that the count call also returns 1
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCityShouldNotBeFound(String filter) throws Exception {
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCity() throws Exception {
        // Get the city
        restCityMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        citySearchRepository.save(city);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());

        // Update the city
        City updatedCity = cityRepository.findById(city.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCity are not directly saved in db
        em.detach(updatedCity);
        updatedCity.name(UPDATED_NAME).postalCode(UPDATED_POSTAL_CODE);
        CityDTO cityDTO = cityMapper.toDto(updatedCity);

        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isOk());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCityToMatchAllProperties(updatedCity);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<City> citySearchList = Streamable.of(citySearchRepository.findAll()).toList();
                City testCitySearch = citySearchList.get(searchDatabaseSizeAfter - 1);

                assertCityAllPropertiesEquals(testCitySearch, updatedCity);
            });
    }

    @Test
    @Transactional
    void putNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cityDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCity, city), getPersistedCity(city));
    }

    @Test
    @Transactional
    void fullUpdateCityWithPatch() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the city using partial update
        City partialUpdatedCity = new City();
        partialUpdatedCity.setId(city.getId());

        partialUpdatedCity.name(UPDATED_NAME).postalCode(UPDATED_POSTAL_CODE);

        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCity))
            )
            .andExpect(status().isOk());

        // Validate the City in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCityUpdatableFieldsEquals(partialUpdatedCity, getPersistedCity(partialUpdatedCity));
    }

    @Test
    @Transactional
    void patchNonExistingCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cityDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        city.setId(UUID.randomUUID());

        // Create the City
        CityDTO cityDTO = cityMapper.toDto(city);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCityMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the City in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);
        cityRepository.save(city);
        citySearchRepository.save(city);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the city
        restCityMockMvc
            .perform(delete(ENTITY_API_URL_ID, city.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(citySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchCity() throws Exception {
        // Initialize the database
        insertedCity = cityRepository.saveAndFlush(city);
        citySearchRepository.save(city);

        // Search the city
        restCityMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + city.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(city.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));
    }

    protected long getRepositoryCount() {
        return cityRepository.count();
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

    protected City getPersistedCity(City city) {
        return cityRepository.findById(city.getId()).orElseThrow();
    }

    protected void assertPersistedCityToMatchAllProperties(City expectedCity) {
        assertCityAllPropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }

    protected void assertPersistedCityToMatchUpdatableProperties(City expectedCity) {
        assertCityAllUpdatablePropertiesEquals(expectedCity, getPersistedCity(expectedCity));
    }
}
