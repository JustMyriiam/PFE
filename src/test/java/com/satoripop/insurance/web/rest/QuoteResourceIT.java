package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.QuoteAsserts.*;
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
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Quote;
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.repository.QuoteRepository;
import com.satoripop.insurance.repository.search.QuoteSearchRepository;
import com.satoripop.insurance.service.dto.QuoteDTO;
import com.satoripop.insurance.service.mapper.QuoteMapper;
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
 * Integration tests for the {@link QuoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuoteResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_ESTIMATED_AMOUNT = 1F;
    private static final Float UPDATED_ESTIMATED_AMOUNT = 2F;
    private static final Float SMALLER_ESTIMATED_AMOUNT = 1F - 1F;

    private static final String ENTITY_API_URL = "/api/quotes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/quotes/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteMapper quoteMapper;

    @Autowired
    private QuoteSearchRepository quoteSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuoteMockMvc;

    private Quote quote;

    private Quote insertedQuote;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createEntity() {
        return new Quote().date(DEFAULT_DATE).estimatedAmount(DEFAULT_ESTIMATED_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Quote createUpdatedEntity() {
        return new Quote().date(UPDATED_DATE).estimatedAmount(UPDATED_ESTIMATED_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        quote = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedQuote != null) {
            quoteRepository.delete(insertedQuote);
            quoteSearchRepository.delete(insertedQuote);
            insertedQuote = null;
        }
    }

    @Test
    @Transactional
    void createQuote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);
        var returnedQuoteDTO = om.readValue(
            restQuoteMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            QuoteDTO.class
        );

        // Validate the Quote in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedQuote = quoteMapper.toEntity(returnedQuoteDTO);
        assertQuoteUpdatableFieldsEquals(returnedQuote, getPersistedQuote(returnedQuote));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedQuote = returnedQuote;
    }

    @Test
    @Transactional
    void createQuoteWithExistingId() throws Exception {
        // Create the Quote with an existing ID
        insertedQuote = quoteRepository.saveAndFlush(quote);
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuoteMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllQuotes() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedAmount").value(hasItem(DEFAULT_ESTIMATED_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get the quote
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL_ID, quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quote.getId().toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.estimatedAmount").value(DEFAULT_ESTIMATED_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getQuotesByIdFiltering() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        UUID id = quote.getId();

        defaultQuoteFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date equals to
        defaultQuoteFiltering("date.equals=" + DEFAULT_DATE, "date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date in
        defaultQuoteFiltering("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE, "date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date is not null
        defaultQuoteFiltering("date.specified=true", "date.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date is greater than or equal to
        defaultQuoteFiltering("date.greaterThanOrEqual=" + DEFAULT_DATE, "date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date is less than or equal to
        defaultQuoteFiltering("date.lessThanOrEqual=" + DEFAULT_DATE, "date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date is less than
        defaultQuoteFiltering("date.lessThan=" + UPDATED_DATE, "date.lessThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where date is greater than
        defaultQuoteFiltering("date.greaterThan=" + SMALLER_DATE, "date.greaterThan=" + DEFAULT_DATE);
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount equals to
        defaultQuoteFiltering("estimatedAmount.equals=" + DEFAULT_ESTIMATED_AMOUNT, "estimatedAmount.equals=" + UPDATED_ESTIMATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount in
        defaultQuoteFiltering(
            "estimatedAmount.in=" + DEFAULT_ESTIMATED_AMOUNT + "," + UPDATED_ESTIMATED_AMOUNT,
            "estimatedAmount.in=" + UPDATED_ESTIMATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount is not null
        defaultQuoteFiltering("estimatedAmount.specified=true", "estimatedAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount is greater than or equal to
        defaultQuoteFiltering(
            "estimatedAmount.greaterThanOrEqual=" + DEFAULT_ESTIMATED_AMOUNT,
            "estimatedAmount.greaterThanOrEqual=" + UPDATED_ESTIMATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount is less than or equal to
        defaultQuoteFiltering(
            "estimatedAmount.lessThanOrEqual=" + DEFAULT_ESTIMATED_AMOUNT,
            "estimatedAmount.lessThanOrEqual=" + SMALLER_ESTIMATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount is less than
        defaultQuoteFiltering(
            "estimatedAmount.lessThan=" + UPDATED_ESTIMATED_AMOUNT,
            "estimatedAmount.lessThan=" + DEFAULT_ESTIMATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotesByEstimatedAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        // Get all the quoteList where estimatedAmount is greater than
        defaultQuoteFiltering(
            "estimatedAmount.greaterThan=" + SMALLER_ESTIMATED_AMOUNT,
            "estimatedAmount.greaterThan=" + DEFAULT_ESTIMATED_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllQuotesByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            vehicle = VehicleResourceIT.createEntity();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        quote.setVehicle(vehicle);
        quoteRepository.saveAndFlush(quote);
        UUID vehicleId = vehicle.getId();
        // Get all the quoteList where vehicle equals to vehicleId
        defaultQuoteShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the quoteList where vehicle equals to UUID.randomUUID()
        defaultQuoteShouldNotBeFound("vehicleId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllQuotesByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            quoteRepository.saveAndFlush(quote);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        quote.setClient(client);
        quoteRepository.saveAndFlush(quote);
        UUID clientId = client.getId();
        // Get all the quoteList where client equals to clientId
        defaultQuoteShouldBeFound("clientId.equals=" + clientId);

        // Get all the quoteList where client equals to UUID.randomUUID()
        defaultQuoteShouldNotBeFound("clientId.equals=" + UUID.randomUUID());
    }

    private void defaultQuoteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultQuoteShouldBeFound(shouldBeFound);
        defaultQuoteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuoteShouldBeFound(String filter) throws Exception {
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedAmount").value(hasItem(DEFAULT_ESTIMATED_AMOUNT.doubleValue())));

        // Check, that the count call also returns 1
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuoteShouldNotBeFound(String filter) throws Exception {
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuoteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingQuote() throws Exception {
        // Get the quote
        restQuoteMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        quoteSearchRepository.save(quote);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());

        // Update the quote
        Quote updatedQuote = quoteRepository.findById(quote.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedQuote are not directly saved in db
        em.detach(updatedQuote);
        updatedQuote.date(UPDATED_DATE).estimatedAmount(UPDATED_ESTIMATED_AMOUNT);
        QuoteDTO quoteDTO = quoteMapper.toDto(updatedQuote);

        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedQuoteToMatchAllProperties(updatedQuote);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Quote> quoteSearchList = Streamable.of(quoteSearchRepository.findAll()).toList();
                Quote testQuoteSearch = quoteSearchList.get(searchDatabaseSizeAfter - 1);

                assertQuoteAllPropertiesEquals(testQuoteSearch, updatedQuote);
            });
    }

    @Test
    @Transactional
    void putNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quoteDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(quoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(quoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote.date(UPDATED_DATE).estimatedAmount(UPDATED_ESTIMATED_AMOUNT);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedQuote, quote), getPersistedQuote(quote));
    }

    @Test
    @Transactional
    void fullUpdateQuoteWithPatch() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the quote using partial update
        Quote partialUpdatedQuote = new Quote();
        partialUpdatedQuote.setId(quote.getId());

        partialUpdatedQuote.date(UPDATED_DATE).estimatedAmount(UPDATED_ESTIMATED_AMOUNT);

        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuote.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedQuote))
            )
            .andExpect(status().isOk());

        // Validate the Quote in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertQuoteUpdatableFieldsEquals(partialUpdatedQuote, getPersistedQuote(partialUpdatedQuote));
    }

    @Test
    @Transactional
    void patchNonExistingQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quoteDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(quoteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        quote.setId(UUID.randomUUID());

        // Create the Quote
        QuoteDTO quoteDTO = quoteMapper.toDto(quote);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuoteMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(quoteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quote in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);
        quoteRepository.save(quote);
        quoteSearchRepository.save(quote);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the quote
        restQuoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, quote.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(quoteSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchQuote() throws Exception {
        // Initialize the database
        insertedQuote = quoteRepository.saveAndFlush(quote);
        quoteSearchRepository.save(quote);

        // Search the quote
        restQuoteMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + quote.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quote.getId().toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedAmount").value(hasItem(DEFAULT_ESTIMATED_AMOUNT.doubleValue())));
    }

    protected long getRepositoryCount() {
        return quoteRepository.count();
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

    protected Quote getPersistedQuote(Quote quote) {
        return quoteRepository.findById(quote.getId()).orElseThrow();
    }

    protected void assertPersistedQuoteToMatchAllProperties(Quote expectedQuote) {
        assertQuoteAllPropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }

    protected void assertPersistedQuoteToMatchUpdatableProperties(Quote expectedQuote) {
        assertQuoteAllUpdatablePropertiesEquals(expectedQuote, getPersistedQuote(expectedQuote));
    }
}
