package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.DocumentAsserts.*;
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
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.domain.Document;
import com.satoripop.insurance.domain.enumeration.DocType;
import com.satoripop.insurance.domain.enumeration.RequestedDocType;
import com.satoripop.insurance.repository.DocumentRepository;
import com.satoripop.insurance.repository.search.DocumentSearchRepository;
import com.satoripop.insurance.service.dto.DocumentDTO;
import com.satoripop.insurance.service.mapper.DocumentMapper;
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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOC_PATH = "AAAAAAAAAA";
    private static final String UPDATED_DOC_PATH = "BBBBBBBBBB";

    private static final DocType DEFAULT_TYPE = DocType.GENERATED_FILE;
    private static final DocType UPDATED_TYPE = DocType.REQUESTED_FILE;

    private static final RequestedDocType DEFAULT_REQUESTED_DOC_TYPE = RequestedDocType.CIN;
    private static final RequestedDocType UPDATED_REQUESTED_DOC_TYPE = RequestedDocType.VEHICLE_REGISTRATION;

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_CREATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/documents/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentSearchRepository documentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    private Document insertedDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity() {
        return new Document()
            .name(DEFAULT_NAME)
            .docPath(DEFAULT_DOC_PATH)
            .type(DEFAULT_TYPE)
            .requestedDocType(DEFAULT_REQUESTED_DOC_TYPE)
            .creationDate(DEFAULT_CREATION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity() {
        return new Document()
            .name(UPDATED_NAME)
            .docPath(UPDATED_DOC_PATH)
            .type(UPDATED_TYPE)
            .requestedDocType(UPDATED_REQUESTED_DOC_TYPE)
            .creationDate(UPDATED_CREATION_DATE);
    }

    @BeforeEach
    void initTest() {
        document = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocument != null) {
            documentRepository.delete(insertedDocument);
            documentSearchRepository.delete(insertedDocument);
            insertedDocument = null;
        }
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        var returnedDocumentDTO = om.readValue(
            restDocumentMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentDTO.class
        );

        // Validate the Document in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocument = documentMapper.toEntity(returnedDocumentDTO);
        assertDocumentUpdatableFieldsEquals(returnedDocument, getPersistedDocument(returnedDocument));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocument = returnedDocument;
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        insertedDocument = documentRepository.saveAndFlush(document);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].docPath").value(hasItem(DEFAULT_DOC_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requestedDocType").value(hasItem(DEFAULT_REQUESTED_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.docPath").value(DEFAULT_DOC_PATH))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.requestedDocType").value(DEFAULT_REQUESTED_DOC_TYPE.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        UUID id = document.getId();

        defaultDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where name equals to
        defaultDocumentFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where name in
        defaultDocumentFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where name is not null
        defaultDocumentFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where name contains
        defaultDocumentFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where name does not contain
        defaultDocumentFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where docPath equals to
        defaultDocumentFiltering("docPath.equals=" + DEFAULT_DOC_PATH, "docPath.equals=" + UPDATED_DOC_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where docPath in
        defaultDocumentFiltering("docPath.in=" + DEFAULT_DOC_PATH + "," + UPDATED_DOC_PATH, "docPath.in=" + UPDATED_DOC_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where docPath is not null
        defaultDocumentFiltering("docPath.specified=true", "docPath.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByDocPathContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where docPath contains
        defaultDocumentFiltering("docPath.contains=" + DEFAULT_DOC_PATH, "docPath.contains=" + UPDATED_DOC_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where docPath does not contain
        defaultDocumentFiltering("docPath.doesNotContain=" + UPDATED_DOC_PATH, "docPath.doesNotContain=" + DEFAULT_DOC_PATH);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where type equals to
        defaultDocumentFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where type in
        defaultDocumentFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where type is not null
        defaultDocumentFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByRequestedDocTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where requestedDocType equals to
        defaultDocumentFiltering(
            "requestedDocType.equals=" + DEFAULT_REQUESTED_DOC_TYPE,
            "requestedDocType.equals=" + UPDATED_REQUESTED_DOC_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByRequestedDocTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where requestedDocType in
        defaultDocumentFiltering(
            "requestedDocType.in=" + DEFAULT_REQUESTED_DOC_TYPE + "," + UPDATED_REQUESTED_DOC_TYPE,
            "requestedDocType.in=" + UPDATED_REQUESTED_DOC_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByRequestedDocTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where requestedDocType is not null
        defaultDocumentFiltering("requestedDocType.specified=true", "requestedDocType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate equals to
        defaultDocumentFiltering("creationDate.equals=" + DEFAULT_CREATION_DATE, "creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate in
        defaultDocumentFiltering(
            "creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE,
            "creationDate.in=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate is not null
        defaultDocumentFiltering("creationDate.specified=true", "creationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate is greater than or equal to
        defaultDocumentFiltering(
            "creationDate.greaterThanOrEqual=" + DEFAULT_CREATION_DATE,
            "creationDate.greaterThanOrEqual=" + UPDATED_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate is less than or equal to
        defaultDocumentFiltering(
            "creationDate.lessThanOrEqual=" + DEFAULT_CREATION_DATE,
            "creationDate.lessThanOrEqual=" + SMALLER_CREATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate is less than
        defaultDocumentFiltering("creationDate.lessThan=" + UPDATED_CREATION_DATE, "creationDate.lessThan=" + DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where creationDate is greater than
        defaultDocumentFiltering("creationDate.greaterThan=" + SMALLER_CREATION_DATE, "creationDate.greaterThan=" + DEFAULT_CREATION_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByContractIsEqualToSomething() throws Exception {
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            documentRepository.saveAndFlush(document);
            contract = ContractResourceIT.createEntity();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        em.persist(contract);
        em.flush();
        document.setContract(contract);
        documentRepository.saveAndFlush(document);
        UUID contractId = contract.getId();
        // Get all the documentList where contract equals to contractId
        defaultDocumentShouldBeFound("contractId.equals=" + contractId);

        // Get all the documentList where contract equals to UUID.randomUUID()
        defaultDocumentShouldNotBeFound("contractId.equals=" + UUID.randomUUID());
    }

    private void defaultDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentShouldBeFound(shouldBeFound);
        defaultDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].docPath").value(hasItem(DEFAULT_DOC_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requestedDocType").value(hasItem(DEFAULT_REQUESTED_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSearchRepository.save(document);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .name(UPDATED_NAME)
            .docPath(UPDATED_DOC_PATH)
            .type(UPDATED_TYPE)
            .requestedDocType(UPDATED_REQUESTED_DOC_TYPE)
            .creationDate(UPDATED_CREATION_DATE);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentToMatchAllProperties(updatedDocument);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Document> documentSearchList = Streamable.of(documentSearchRepository.findAll()).toList();
                Document testDocumentSearch = documentSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentAllPropertiesEquals(testDocumentSearch, updatedDocument);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .name(UPDATED_NAME)
            .docPath(UPDATED_DOC_PATH)
            .type(UPDATED_TYPE)
            .requestedDocType(UPDATED_REQUESTED_DOC_TYPE)
            .creationDate(UPDATED_CREATION_DATE);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDocument, document), getPersistedDocument(document));
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .name(UPDATED_NAME)
            .docPath(UPDATED_DOC_PATH)
            .type(UPDATED_TYPE)
            .requestedDocType(UPDATED_REQUESTED_DOC_TYPE)
            .creationDate(UPDATED_CREATION_DATE);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentUpdatableFieldsEquals(partialUpdatedDocument, getPersistedDocument(partialUpdatedDocument));
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(UUID.randomUUID());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);
        documentRepository.save(document);
        documentSearchRepository.save(document);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, document.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);
        documentSearchRepository.save(document);

        // Search the document
        restDocumentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].docPath").value(hasItem(DEFAULT_DOC_PATH)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].requestedDocType").value(hasItem(DEFAULT_REQUESTED_DOC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentRepository.count();
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

    protected Document getPersistedDocument(Document document) {
        return documentRepository.findById(document.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentToMatchAllProperties(Document expectedDocument) {
        assertDocumentAllPropertiesEquals(expectedDocument, getPersistedDocument(expectedDocument));
    }

    protected void assertPersistedDocumentToMatchUpdatableProperties(Document expectedDocument) {
        assertDocumentAllUpdatablePropertiesEquals(expectedDocument, getPersistedDocument(expectedDocument));
    }
}
