package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.ClientAsserts.*;
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
import com.satoripop.insurance.domain.ClientAddress;
import com.satoripop.insurance.domain.User;
import com.satoripop.insurance.domain.enumeration.Gender;
import com.satoripop.insurance.domain.enumeration.IdentityType;
import com.satoripop.insurance.domain.enumeration.MaritalStatus;
import com.satoripop.insurance.domain.enumeration.ProfessionalStatus;
import com.satoripop.insurance.repository.ClientRepository;
import com.satoripop.insurance.repository.UserRepository;
import com.satoripop.insurance.repository.search.ClientSearchRepository;
import com.satoripop.insurance.service.ClientService;
import com.satoripop.insurance.service.dto.ClientDTO;
import com.satoripop.insurance.service.mapper.ClientMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final IdentityType DEFAULT_IDENTITY_TYPE = IdentityType.CIN;
    private static final IdentityType UPDATED_IDENTITY_TYPE = IdentityType.PASSPORT;

    private static final String DEFAULT_IDENTITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_IDENTITY_EMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_IDENTITY_EMISSION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_IDENTITY_EMISSION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BIRTH_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_IDENTITY_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_IDENTITY_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_IDENTITY_ISSUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_IDENTITY_PLACE_OF_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_PLACE_OF_ISSUE = "BBBBBBBBBB";

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.SINGLE;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.MARRIED;

    private static final Integer DEFAULT_NBR_OFCHILDREN = 1;
    private static final Integer UPDATED_NBR_OFCHILDREN = 2;
    private static final Integer SMALLER_NBR_OFCHILDREN = 1 - 1;

    private static final String DEFAULT_PROFESSIONAL_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSIONAL_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PERSONAL_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PRIMARY_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final ProfessionalStatus DEFAULT_PROFESSIONAL_STATUS = ProfessionalStatus.EMPLOYEE;
    private static final ProfessionalStatus UPDATED_PROFESSIONAL_STATUS = ProfessionalStatus.SELF_EMPLOYED;

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCY = "AAAAAAAAAA";
    private static final String UPDATED_AGENCY = "BBBBBBBBBB";

    private static final String DEFAULT_RIB = "AAAAAAAAAA";
    private static final String UPDATED_RIB = "BBBBBBBBBB";

    private static final String DEFAULT_DRIVING_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DRIVING_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DRIVING_LICENSE_ISSUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DRIVING_LICENSE_ISSUE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DRIVING_LICENSE_ISSUE_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_DRIVING_LICENSE_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_DRIVING_LICENSE_CATEGORY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/clients/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepositoryMock;

    @Autowired
    private ClientMapper clientMapper;

    @Mock
    private ClientService clientServiceMock;

    @Autowired
    private ClientSearchRepository clientSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    private Client insertedClient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity() {
        return new Client()
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .identityType(DEFAULT_IDENTITY_TYPE)
            .identityNumber(DEFAULT_IDENTITY_NUMBER)
            .identityEmissionDate(DEFAULT_IDENTITY_EMISSION_DATE)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .identityIssueDate(DEFAULT_IDENTITY_ISSUE_DATE)
            .identityPlaceOfIssue(DEFAULT_IDENTITY_PLACE_OF_ISSUE)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .nbrOfchildren(DEFAULT_NBR_OFCHILDREN)
            .professionalEmail(DEFAULT_PROFESSIONAL_EMAIL)
            .personalEmail(DEFAULT_PERSONAL_EMAIL)
            .primaryPhoneNumber(DEFAULT_PRIMARY_PHONE_NUMBER)
            .secondaryPhoneNumber(DEFAULT_SECONDARY_PHONE_NUMBER)
            .faxNumber(DEFAULT_FAX_NUMBER)
            .nationality(DEFAULT_NATIONALITY)
            .gender(DEFAULT_GENDER)
            .jobTitle(DEFAULT_JOB_TITLE)
            .professionalStatus(DEFAULT_PROFESSIONAL_STATUS)
            .bank(DEFAULT_BANK)
            .agency(DEFAULT_AGENCY)
            .rib(DEFAULT_RIB)
            .drivingLicenseNumber(DEFAULT_DRIVING_LICENSE_NUMBER)
            .drivingLicenseIssueDate(DEFAULT_DRIVING_LICENSE_ISSUE_DATE)
            .drivingLicenseCategory(DEFAULT_DRIVING_LICENSE_CATEGORY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity() {
        return new Client()
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .identityType(UPDATED_IDENTITY_TYPE)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .identityEmissionDate(UPDATED_IDENTITY_EMISSION_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .identityIssueDate(UPDATED_IDENTITY_ISSUE_DATE)
            .identityPlaceOfIssue(UPDATED_IDENTITY_PLACE_OF_ISSUE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .nbrOfchildren(UPDATED_NBR_OFCHILDREN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .primaryPhoneNumber(UPDATED_PRIMARY_PHONE_NUMBER)
            .secondaryPhoneNumber(UPDATED_SECONDARY_PHONE_NUMBER)
            .faxNumber(UPDATED_FAX_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .gender(UPDATED_GENDER)
            .jobTitle(UPDATED_JOB_TITLE)
            .professionalStatus(UPDATED_PROFESSIONAL_STATUS)
            .bank(UPDATED_BANK)
            .agency(UPDATED_AGENCY)
            .rib(UPDATED_RIB)
            .drivingLicenseNumber(UPDATED_DRIVING_LICENSE_NUMBER)
            .drivingLicenseIssueDate(UPDATED_DRIVING_LICENSE_ISSUE_DATE)
            .drivingLicenseCategory(UPDATED_DRIVING_LICENSE_CATEGORY);
    }

    @BeforeEach
    void initTest() {
        client = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedClient != null) {
            clientRepository.delete(insertedClient);
            clientSearchRepository.delete(insertedClient);
            insertedClient = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        var returnedClientDTO = om.readValue(
            restClientMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ClientDTO.class
        );

        // Validate the Client in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedClient = clientMapper.toEntity(returnedClientDTO);
        assertClientUpdatableFieldsEquals(returnedClient, getPersistedClient(returnedClient));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedClient = returnedClient;
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        insertedClient = clientRepository.saveAndFlush(client);
        ClientDTO clientDTO = clientMapper.toDto(client);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identityNumber").value(hasItem(DEFAULT_IDENTITY_NUMBER)))
            .andExpect(jsonPath("$.[*].identityEmissionDate").value(hasItem(DEFAULT_IDENTITY_EMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].identityIssueDate").value(hasItem(DEFAULT_IDENTITY_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].identityPlaceOfIssue").value(hasItem(DEFAULT_IDENTITY_PLACE_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nbrOfchildren").value(hasItem(DEFAULT_NBR_OFCHILDREN)))
            .andExpect(jsonPath("$.[*].professionalEmail").value(hasItem(DEFAULT_PROFESSIONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].personalEmail").value(hasItem(DEFAULT_PERSONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].primaryPhoneNumber").value(hasItem(DEFAULT_PRIMARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].secondaryPhoneNumber").value(hasItem(DEFAULT_SECONDARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].professionalStatus").value(hasItem(DEFAULT_PROFESSIONAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].agency").value(hasItem(DEFAULT_AGENCY)))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB)))
            .andExpect(jsonPath("$.[*].drivingLicenseNumber").value(hasItem(DEFAULT_DRIVING_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].drivingLicenseIssueDate").value(hasItem(DEFAULT_DRIVING_LICENSE_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].drivingLicenseCategory").value(hasItem(DEFAULT_DRIVING_LICENSE_CATEGORY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientsWithEagerRelationshipsIsEnabled() throws Exception {
        when(clientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(clientServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllClientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(clientServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restClientMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(clientRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.identityType").value(DEFAULT_IDENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.identityNumber").value(DEFAULT_IDENTITY_NUMBER))
            .andExpect(jsonPath("$.identityEmissionDate").value(DEFAULT_IDENTITY_EMISSION_DATE.toString()))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.identityIssueDate").value(DEFAULT_IDENTITY_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.identityPlaceOfIssue").value(DEFAULT_IDENTITY_PLACE_OF_ISSUE))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.nbrOfchildren").value(DEFAULT_NBR_OFCHILDREN))
            .andExpect(jsonPath("$.professionalEmail").value(DEFAULT_PROFESSIONAL_EMAIL))
            .andExpect(jsonPath("$.personalEmail").value(DEFAULT_PERSONAL_EMAIL))
            .andExpect(jsonPath("$.primaryPhoneNumber").value(DEFAULT_PRIMARY_PHONE_NUMBER))
            .andExpect(jsonPath("$.secondaryPhoneNumber").value(DEFAULT_SECONDARY_PHONE_NUMBER))
            .andExpect(jsonPath("$.faxNumber").value(DEFAULT_FAX_NUMBER))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.professionalStatus").value(DEFAULT_PROFESSIONAL_STATUS.toString()))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK))
            .andExpect(jsonPath("$.agency").value(DEFAULT_AGENCY))
            .andExpect(jsonPath("$.rib").value(DEFAULT_RIB))
            .andExpect(jsonPath("$.drivingLicenseNumber").value(DEFAULT_DRIVING_LICENSE_NUMBER))
            .andExpect(jsonPath("$.drivingLicenseIssueDate").value(DEFAULT_DRIVING_LICENSE_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.drivingLicenseCategory").value(DEFAULT_DRIVING_LICENSE_CATEGORY));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        UUID id = client.getId();

        defaultClientFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName equals to
        defaultClientFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName in
        defaultClientFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName is not null
        defaultClientFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName contains
        defaultClientFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName does not contain
        defaultClientFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName equals to
        defaultClientFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName in
        defaultClientFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName is not null
        defaultClientFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName contains
        defaultClientFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName does not contain
        defaultClientFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByIdentityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityType equals to
        defaultClientFiltering("identityType.equals=" + DEFAULT_IDENTITY_TYPE, "identityType.equals=" + UPDATED_IDENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllClientsByIdentityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityType in
        defaultClientFiltering(
            "identityType.in=" + DEFAULT_IDENTITY_TYPE + "," + UPDATED_IDENTITY_TYPE,
            "identityType.in=" + UPDATED_IDENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityType is not null
        defaultClientFiltering("identityType.specified=true", "identityType.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByIdentityNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityNumber equals to
        defaultClientFiltering("identityNumber.equals=" + DEFAULT_IDENTITY_NUMBER, "identityNumber.equals=" + UPDATED_IDENTITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByIdentityNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityNumber in
        defaultClientFiltering(
            "identityNumber.in=" + DEFAULT_IDENTITY_NUMBER + "," + UPDATED_IDENTITY_NUMBER,
            "identityNumber.in=" + UPDATED_IDENTITY_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityNumber is not null
        defaultClientFiltering("identityNumber.specified=true", "identityNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByIdentityNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityNumber contains
        defaultClientFiltering("identityNumber.contains=" + DEFAULT_IDENTITY_NUMBER, "identityNumber.contains=" + UPDATED_IDENTITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByIdentityNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityNumber does not contain
        defaultClientFiltering(
            "identityNumber.doesNotContain=" + UPDATED_IDENTITY_NUMBER,
            "identityNumber.doesNotContain=" + DEFAULT_IDENTITY_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate equals to
        defaultClientFiltering(
            "identityEmissionDate.equals=" + DEFAULT_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.equals=" + UPDATED_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate in
        defaultClientFiltering(
            "identityEmissionDate.in=" + DEFAULT_IDENTITY_EMISSION_DATE + "," + UPDATED_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.in=" + UPDATED_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate is not null
        defaultClientFiltering("identityEmissionDate.specified=true", "identityEmissionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate is greater than or equal to
        defaultClientFiltering(
            "identityEmissionDate.greaterThanOrEqual=" + DEFAULT_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.greaterThanOrEqual=" + UPDATED_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate is less than or equal to
        defaultClientFiltering(
            "identityEmissionDate.lessThanOrEqual=" + DEFAULT_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.lessThanOrEqual=" + SMALLER_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate is less than
        defaultClientFiltering(
            "identityEmissionDate.lessThan=" + UPDATED_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.lessThan=" + DEFAULT_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityEmissionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityEmissionDate is greater than
        defaultClientFiltering(
            "identityEmissionDate.greaterThan=" + SMALLER_IDENTITY_EMISSION_DATE,
            "identityEmissionDate.greaterThan=" + DEFAULT_IDENTITY_EMISSION_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate equals to
        defaultClientFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate in
        defaultClientFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate is not null
        defaultClientFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate is greater than or equal to
        defaultClientFiltering("birthDate.greaterThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.greaterThanOrEqual=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate is less than or equal to
        defaultClientFiltering("birthDate.lessThanOrEqual=" + DEFAULT_BIRTH_DATE, "birthDate.lessThanOrEqual=" + SMALLER_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate is less than
        defaultClientFiltering("birthDate.lessThan=" + UPDATED_BIRTH_DATE, "birthDate.lessThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthDate is greater than
        defaultClientFiltering("birthDate.greaterThan=" + SMALLER_BIRTH_DATE, "birthDate.greaterThan=" + DEFAULT_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthPlace equals to
        defaultClientFiltering("birthPlace.equals=" + DEFAULT_BIRTH_PLACE, "birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthPlace in
        defaultClientFiltering("birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE, "birthPlace.in=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthPlace is not null
        defaultClientFiltering("birthPlace.specified=true", "birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthPlace contains
        defaultClientFiltering("birthPlace.contains=" + DEFAULT_BIRTH_PLACE, "birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllClientsByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where birthPlace does not contain
        defaultClientFiltering("birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE, "birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate equals to
        defaultClientFiltering(
            "identityIssueDate.equals=" + DEFAULT_IDENTITY_ISSUE_DATE,
            "identityIssueDate.equals=" + UPDATED_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate in
        defaultClientFiltering(
            "identityIssueDate.in=" + DEFAULT_IDENTITY_ISSUE_DATE + "," + UPDATED_IDENTITY_ISSUE_DATE,
            "identityIssueDate.in=" + UPDATED_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate is not null
        defaultClientFiltering("identityIssueDate.specified=true", "identityIssueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate is greater than or equal to
        defaultClientFiltering(
            "identityIssueDate.greaterThanOrEqual=" + DEFAULT_IDENTITY_ISSUE_DATE,
            "identityIssueDate.greaterThanOrEqual=" + UPDATED_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate is less than or equal to
        defaultClientFiltering(
            "identityIssueDate.lessThanOrEqual=" + DEFAULT_IDENTITY_ISSUE_DATE,
            "identityIssueDate.lessThanOrEqual=" + SMALLER_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate is less than
        defaultClientFiltering(
            "identityIssueDate.lessThan=" + UPDATED_IDENTITY_ISSUE_DATE,
            "identityIssueDate.lessThan=" + DEFAULT_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityIssueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityIssueDate is greater than
        defaultClientFiltering(
            "identityIssueDate.greaterThan=" + SMALLER_IDENTITY_ISSUE_DATE,
            "identityIssueDate.greaterThan=" + DEFAULT_IDENTITY_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityPlaceOfIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityPlaceOfIssue equals to
        defaultClientFiltering(
            "identityPlaceOfIssue.equals=" + DEFAULT_IDENTITY_PLACE_OF_ISSUE,
            "identityPlaceOfIssue.equals=" + UPDATED_IDENTITY_PLACE_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityPlaceOfIssueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityPlaceOfIssue in
        defaultClientFiltering(
            "identityPlaceOfIssue.in=" + DEFAULT_IDENTITY_PLACE_OF_ISSUE + "," + UPDATED_IDENTITY_PLACE_OF_ISSUE,
            "identityPlaceOfIssue.in=" + UPDATED_IDENTITY_PLACE_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityPlaceOfIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityPlaceOfIssue is not null
        defaultClientFiltering("identityPlaceOfIssue.specified=true", "identityPlaceOfIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByIdentityPlaceOfIssueContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityPlaceOfIssue contains
        defaultClientFiltering(
            "identityPlaceOfIssue.contains=" + DEFAULT_IDENTITY_PLACE_OF_ISSUE,
            "identityPlaceOfIssue.contains=" + UPDATED_IDENTITY_PLACE_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllClientsByIdentityPlaceOfIssueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where identityPlaceOfIssue does not contain
        defaultClientFiltering(
            "identityPlaceOfIssue.doesNotContain=" + UPDATED_IDENTITY_PLACE_OF_ISSUE,
            "identityPlaceOfIssue.doesNotContain=" + DEFAULT_IDENTITY_PLACE_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllClientsByMaritalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where maritalStatus equals to
        defaultClientFiltering("maritalStatus.equals=" + DEFAULT_MARITAL_STATUS, "maritalStatus.equals=" + UPDATED_MARITAL_STATUS);
    }

    @Test
    @Transactional
    void getAllClientsByMaritalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where maritalStatus in
        defaultClientFiltering(
            "maritalStatus.in=" + DEFAULT_MARITAL_STATUS + "," + UPDATED_MARITAL_STATUS,
            "maritalStatus.in=" + UPDATED_MARITAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllClientsByMaritalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where maritalStatus is not null
        defaultClientFiltering("maritalStatus.specified=true", "maritalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren equals to
        defaultClientFiltering("nbrOfchildren.equals=" + DEFAULT_NBR_OFCHILDREN, "nbrOfchildren.equals=" + UPDATED_NBR_OFCHILDREN);
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren in
        defaultClientFiltering(
            "nbrOfchildren.in=" + DEFAULT_NBR_OFCHILDREN + "," + UPDATED_NBR_OFCHILDREN,
            "nbrOfchildren.in=" + UPDATED_NBR_OFCHILDREN
        );
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren is not null
        defaultClientFiltering("nbrOfchildren.specified=true", "nbrOfchildren.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren is greater than or equal to
        defaultClientFiltering(
            "nbrOfchildren.greaterThanOrEqual=" + DEFAULT_NBR_OFCHILDREN,
            "nbrOfchildren.greaterThanOrEqual=" + UPDATED_NBR_OFCHILDREN
        );
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren is less than or equal to
        defaultClientFiltering(
            "nbrOfchildren.lessThanOrEqual=" + DEFAULT_NBR_OFCHILDREN,
            "nbrOfchildren.lessThanOrEqual=" + SMALLER_NBR_OFCHILDREN
        );
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren is less than
        defaultClientFiltering("nbrOfchildren.lessThan=" + UPDATED_NBR_OFCHILDREN, "nbrOfchildren.lessThan=" + DEFAULT_NBR_OFCHILDREN);
    }

    @Test
    @Transactional
    void getAllClientsByNbrOfchildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nbrOfchildren is greater than
        defaultClientFiltering(
            "nbrOfchildren.greaterThan=" + SMALLER_NBR_OFCHILDREN,
            "nbrOfchildren.greaterThan=" + DEFAULT_NBR_OFCHILDREN
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalEmail equals to
        defaultClientFiltering(
            "professionalEmail.equals=" + DEFAULT_PROFESSIONAL_EMAIL,
            "professionalEmail.equals=" + UPDATED_PROFESSIONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalEmail in
        defaultClientFiltering(
            "professionalEmail.in=" + DEFAULT_PROFESSIONAL_EMAIL + "," + UPDATED_PROFESSIONAL_EMAIL,
            "professionalEmail.in=" + UPDATED_PROFESSIONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalEmail is not null
        defaultClientFiltering("professionalEmail.specified=true", "professionalEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalEmail contains
        defaultClientFiltering(
            "professionalEmail.contains=" + DEFAULT_PROFESSIONAL_EMAIL,
            "professionalEmail.contains=" + UPDATED_PROFESSIONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalEmail does not contain
        defaultClientFiltering(
            "professionalEmail.doesNotContain=" + UPDATED_PROFESSIONAL_EMAIL,
            "professionalEmail.doesNotContain=" + DEFAULT_PROFESSIONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByPersonalEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where personalEmail equals to
        defaultClientFiltering("personalEmail.equals=" + DEFAULT_PERSONAL_EMAIL, "personalEmail.equals=" + UPDATED_PERSONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByPersonalEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where personalEmail in
        defaultClientFiltering(
            "personalEmail.in=" + DEFAULT_PERSONAL_EMAIL + "," + UPDATED_PERSONAL_EMAIL,
            "personalEmail.in=" + UPDATED_PERSONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByPersonalEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where personalEmail is not null
        defaultClientFiltering("personalEmail.specified=true", "personalEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPersonalEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where personalEmail contains
        defaultClientFiltering("personalEmail.contains=" + DEFAULT_PERSONAL_EMAIL, "personalEmail.contains=" + UPDATED_PERSONAL_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByPersonalEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where personalEmail does not contain
        defaultClientFiltering(
            "personalEmail.doesNotContain=" + UPDATED_PERSONAL_EMAIL,
            "personalEmail.doesNotContain=" + DEFAULT_PERSONAL_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllClientsByPrimaryPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where primaryPhoneNumber equals to
        defaultClientFiltering(
            "primaryPhoneNumber.equals=" + DEFAULT_PRIMARY_PHONE_NUMBER,
            "primaryPhoneNumber.equals=" + UPDATED_PRIMARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByPrimaryPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where primaryPhoneNumber in
        defaultClientFiltering(
            "primaryPhoneNumber.in=" + DEFAULT_PRIMARY_PHONE_NUMBER + "," + UPDATED_PRIMARY_PHONE_NUMBER,
            "primaryPhoneNumber.in=" + UPDATED_PRIMARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByPrimaryPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where primaryPhoneNumber is not null
        defaultClientFiltering("primaryPhoneNumber.specified=true", "primaryPhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPrimaryPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where primaryPhoneNumber contains
        defaultClientFiltering(
            "primaryPhoneNumber.contains=" + DEFAULT_PRIMARY_PHONE_NUMBER,
            "primaryPhoneNumber.contains=" + UPDATED_PRIMARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByPrimaryPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where primaryPhoneNumber does not contain
        defaultClientFiltering(
            "primaryPhoneNumber.doesNotContain=" + UPDATED_PRIMARY_PHONE_NUMBER,
            "primaryPhoneNumber.doesNotContain=" + DEFAULT_PRIMARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsBySecondaryPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where secondaryPhoneNumber equals to
        defaultClientFiltering(
            "secondaryPhoneNumber.equals=" + DEFAULT_SECONDARY_PHONE_NUMBER,
            "secondaryPhoneNumber.equals=" + UPDATED_SECONDARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsBySecondaryPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where secondaryPhoneNumber in
        defaultClientFiltering(
            "secondaryPhoneNumber.in=" + DEFAULT_SECONDARY_PHONE_NUMBER + "," + UPDATED_SECONDARY_PHONE_NUMBER,
            "secondaryPhoneNumber.in=" + UPDATED_SECONDARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsBySecondaryPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where secondaryPhoneNumber is not null
        defaultClientFiltering("secondaryPhoneNumber.specified=true", "secondaryPhoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsBySecondaryPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where secondaryPhoneNumber contains
        defaultClientFiltering(
            "secondaryPhoneNumber.contains=" + DEFAULT_SECONDARY_PHONE_NUMBER,
            "secondaryPhoneNumber.contains=" + UPDATED_SECONDARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsBySecondaryPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where secondaryPhoneNumber does not contain
        defaultClientFiltering(
            "secondaryPhoneNumber.doesNotContain=" + UPDATED_SECONDARY_PHONE_NUMBER,
            "secondaryPhoneNumber.doesNotContain=" + DEFAULT_SECONDARY_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByFaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where faxNumber equals to
        defaultClientFiltering("faxNumber.equals=" + DEFAULT_FAX_NUMBER, "faxNumber.equals=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByFaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where faxNumber in
        defaultClientFiltering("faxNumber.in=" + DEFAULT_FAX_NUMBER + "," + UPDATED_FAX_NUMBER, "faxNumber.in=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByFaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where faxNumber is not null
        defaultClientFiltering("faxNumber.specified=true", "faxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByFaxNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where faxNumber contains
        defaultClientFiltering("faxNumber.contains=" + DEFAULT_FAX_NUMBER, "faxNumber.contains=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByFaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where faxNumber does not contain
        defaultClientFiltering("faxNumber.doesNotContain=" + UPDATED_FAX_NUMBER, "faxNumber.doesNotContain=" + DEFAULT_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllClientsByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationality equals to
        defaultClientFiltering("nationality.equals=" + DEFAULT_NATIONALITY, "nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllClientsByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationality in
        defaultClientFiltering(
            "nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY,
            "nationality.in=" + UPDATED_NATIONALITY
        );
    }

    @Test
    @Transactional
    void getAllClientsByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationality is not null
        defaultClientFiltering("nationality.specified=true", "nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByNationalityContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationality contains
        defaultClientFiltering("nationality.contains=" + DEFAULT_NATIONALITY, "nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllClientsByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where nationality does not contain
        defaultClientFiltering("nationality.doesNotContain=" + UPDATED_NATIONALITY, "nationality.doesNotContain=" + DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllClientsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where gender equals to
        defaultClientFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllClientsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where gender in
        defaultClientFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllClientsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where gender is not null
        defaultClientFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where jobTitle equals to
        defaultClientFiltering("jobTitle.equals=" + DEFAULT_JOB_TITLE, "jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllClientsByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where jobTitle in
        defaultClientFiltering("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE, "jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllClientsByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where jobTitle is not null
        defaultClientFiltering("jobTitle.specified=true", "jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where jobTitle contains
        defaultClientFiltering("jobTitle.contains=" + DEFAULT_JOB_TITLE, "jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllClientsByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where jobTitle does not contain
        defaultClientFiltering("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE, "jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalStatus equals to
        defaultClientFiltering(
            "professionalStatus.equals=" + DEFAULT_PROFESSIONAL_STATUS,
            "professionalStatus.equals=" + UPDATED_PROFESSIONAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalStatus in
        defaultClientFiltering(
            "professionalStatus.in=" + DEFAULT_PROFESSIONAL_STATUS + "," + UPDATED_PROFESSIONAL_STATUS,
            "professionalStatus.in=" + UPDATED_PROFESSIONAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllClientsByProfessionalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where professionalStatus is not null
        defaultClientFiltering("professionalStatus.specified=true", "professionalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByBankIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where bank equals to
        defaultClientFiltering("bank.equals=" + DEFAULT_BANK, "bank.equals=" + UPDATED_BANK);
    }

    @Test
    @Transactional
    void getAllClientsByBankIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where bank in
        defaultClientFiltering("bank.in=" + DEFAULT_BANK + "," + UPDATED_BANK, "bank.in=" + UPDATED_BANK);
    }

    @Test
    @Transactional
    void getAllClientsByBankIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where bank is not null
        defaultClientFiltering("bank.specified=true", "bank.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByBankContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where bank contains
        defaultClientFiltering("bank.contains=" + DEFAULT_BANK, "bank.contains=" + UPDATED_BANK);
    }

    @Test
    @Transactional
    void getAllClientsByBankNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where bank does not contain
        defaultClientFiltering("bank.doesNotContain=" + UPDATED_BANK, "bank.doesNotContain=" + DEFAULT_BANK);
    }

    @Test
    @Transactional
    void getAllClientsByAgencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where agency equals to
        defaultClientFiltering("agency.equals=" + DEFAULT_AGENCY, "agency.equals=" + UPDATED_AGENCY);
    }

    @Test
    @Transactional
    void getAllClientsByAgencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where agency in
        defaultClientFiltering("agency.in=" + DEFAULT_AGENCY + "," + UPDATED_AGENCY, "agency.in=" + UPDATED_AGENCY);
    }

    @Test
    @Transactional
    void getAllClientsByAgencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where agency is not null
        defaultClientFiltering("agency.specified=true", "agency.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAgencyContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where agency contains
        defaultClientFiltering("agency.contains=" + DEFAULT_AGENCY, "agency.contains=" + UPDATED_AGENCY);
    }

    @Test
    @Transactional
    void getAllClientsByAgencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where agency does not contain
        defaultClientFiltering("agency.doesNotContain=" + UPDATED_AGENCY, "agency.doesNotContain=" + DEFAULT_AGENCY);
    }

    @Test
    @Transactional
    void getAllClientsByRibIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where rib equals to
        defaultClientFiltering("rib.equals=" + DEFAULT_RIB, "rib.equals=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllClientsByRibIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where rib in
        defaultClientFiltering("rib.in=" + DEFAULT_RIB + "," + UPDATED_RIB, "rib.in=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllClientsByRibIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where rib is not null
        defaultClientFiltering("rib.specified=true", "rib.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByRibContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where rib contains
        defaultClientFiltering("rib.contains=" + DEFAULT_RIB, "rib.contains=" + UPDATED_RIB);
    }

    @Test
    @Transactional
    void getAllClientsByRibNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where rib does not contain
        defaultClientFiltering("rib.doesNotContain=" + UPDATED_RIB, "rib.doesNotContain=" + DEFAULT_RIB);
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseNumber equals to
        defaultClientFiltering(
            "drivingLicenseNumber.equals=" + DEFAULT_DRIVING_LICENSE_NUMBER,
            "drivingLicenseNumber.equals=" + UPDATED_DRIVING_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseNumber in
        defaultClientFiltering(
            "drivingLicenseNumber.in=" + DEFAULT_DRIVING_LICENSE_NUMBER + "," + UPDATED_DRIVING_LICENSE_NUMBER,
            "drivingLicenseNumber.in=" + UPDATED_DRIVING_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseNumber is not null
        defaultClientFiltering("drivingLicenseNumber.specified=true", "drivingLicenseNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseNumber contains
        defaultClientFiltering(
            "drivingLicenseNumber.contains=" + DEFAULT_DRIVING_LICENSE_NUMBER,
            "drivingLicenseNumber.contains=" + UPDATED_DRIVING_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseNumber does not contain
        defaultClientFiltering(
            "drivingLicenseNumber.doesNotContain=" + UPDATED_DRIVING_LICENSE_NUMBER,
            "drivingLicenseNumber.doesNotContain=" + DEFAULT_DRIVING_LICENSE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate equals to
        defaultClientFiltering(
            "drivingLicenseIssueDate.equals=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.equals=" + UPDATED_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate in
        defaultClientFiltering(
            "drivingLicenseIssueDate.in=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE + "," + UPDATED_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.in=" + UPDATED_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate is not null
        defaultClientFiltering("drivingLicenseIssueDate.specified=true", "drivingLicenseIssueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate is greater than or equal to
        defaultClientFiltering(
            "drivingLicenseIssueDate.greaterThanOrEqual=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.greaterThanOrEqual=" + UPDATED_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate is less than or equal to
        defaultClientFiltering(
            "drivingLicenseIssueDate.lessThanOrEqual=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.lessThanOrEqual=" + SMALLER_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate is less than
        defaultClientFiltering(
            "drivingLicenseIssueDate.lessThan=" + UPDATED_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.lessThan=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseIssueDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseIssueDate is greater than
        defaultClientFiltering(
            "drivingLicenseIssueDate.greaterThan=" + SMALLER_DRIVING_LICENSE_ISSUE_DATE,
            "drivingLicenseIssueDate.greaterThan=" + DEFAULT_DRIVING_LICENSE_ISSUE_DATE
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseCategory equals to
        defaultClientFiltering(
            "drivingLicenseCategory.equals=" + DEFAULT_DRIVING_LICENSE_CATEGORY,
            "drivingLicenseCategory.equals=" + UPDATED_DRIVING_LICENSE_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseCategory in
        defaultClientFiltering(
            "drivingLicenseCategory.in=" + DEFAULT_DRIVING_LICENSE_CATEGORY + "," + UPDATED_DRIVING_LICENSE_CATEGORY,
            "drivingLicenseCategory.in=" + UPDATED_DRIVING_LICENSE_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseCategory is not null
        defaultClientFiltering("drivingLicenseCategory.specified=true", "drivingLicenseCategory.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseCategoryContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseCategory contains
        defaultClientFiltering(
            "drivingLicenseCategory.contains=" + DEFAULT_DRIVING_LICENSE_CATEGORY,
            "drivingLicenseCategory.contains=" + UPDATED_DRIVING_LICENSE_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllClientsByDrivingLicenseCategoryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        // Get all the clientList where drivingLicenseCategory does not contain
        defaultClientFiltering(
            "drivingLicenseCategory.doesNotContain=" + UPDATED_DRIVING_LICENSE_CATEGORY,
            "drivingLicenseCategory.doesNotContain=" + DEFAULT_DRIVING_LICENSE_CATEGORY
        );
    }

    @Test
    @Transactional
    void getAllClientsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            clientRepository.saveAndFlush(client);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        client.setUser(user);
        clientRepository.saveAndFlush(client);
        String userId = user.getId();
        // Get all the clientList where user equals to userId
        defaultClientShouldBeFound("userId.equals=" + userId);

        // Get all the clientList where user equals to "invalid-id"
        defaultClientShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllClientsByClientAddressIsEqualToSomething() throws Exception {
        ClientAddress clientAddress;
        if (TestUtil.findAll(em, ClientAddress.class).isEmpty()) {
            clientRepository.saveAndFlush(client);
            clientAddress = ClientAddressResourceIT.createEntity();
        } else {
            clientAddress = TestUtil.findAll(em, ClientAddress.class).get(0);
        }
        em.persist(clientAddress);
        em.flush();
        client.setClientAddress(clientAddress);
        clientRepository.saveAndFlush(client);
        UUID clientAddressId = clientAddress.getId();
        // Get all the clientList where clientAddress equals to clientAddressId
        defaultClientShouldBeFound("clientAddressId.equals=" + clientAddressId);

        // Get all the clientList where clientAddress equals to UUID.randomUUID()
        defaultClientShouldNotBeFound("clientAddressId.equals=" + UUID.randomUUID());
    }

    private void defaultClientFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultClientShouldBeFound(shouldBeFound);
        defaultClientShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identityNumber").value(hasItem(DEFAULT_IDENTITY_NUMBER)))
            .andExpect(jsonPath("$.[*].identityEmissionDate").value(hasItem(DEFAULT_IDENTITY_EMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].identityIssueDate").value(hasItem(DEFAULT_IDENTITY_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].identityPlaceOfIssue").value(hasItem(DEFAULT_IDENTITY_PLACE_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nbrOfchildren").value(hasItem(DEFAULT_NBR_OFCHILDREN)))
            .andExpect(jsonPath("$.[*].professionalEmail").value(hasItem(DEFAULT_PROFESSIONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].personalEmail").value(hasItem(DEFAULT_PERSONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].primaryPhoneNumber").value(hasItem(DEFAULT_PRIMARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].secondaryPhoneNumber").value(hasItem(DEFAULT_SECONDARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].professionalStatus").value(hasItem(DEFAULT_PROFESSIONAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].agency").value(hasItem(DEFAULT_AGENCY)))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB)))
            .andExpect(jsonPath("$.[*].drivingLicenseNumber").value(hasItem(DEFAULT_DRIVING_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].drivingLicenseIssueDate").value(hasItem(DEFAULT_DRIVING_LICENSE_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].drivingLicenseCategory").value(hasItem(DEFAULT_DRIVING_LICENSE_CATEGORY)));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        clientSearchRepository.save(client);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .identityType(UPDATED_IDENTITY_TYPE)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .identityEmissionDate(UPDATED_IDENTITY_EMISSION_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .identityIssueDate(UPDATED_IDENTITY_ISSUE_DATE)
            .identityPlaceOfIssue(UPDATED_IDENTITY_PLACE_OF_ISSUE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .nbrOfchildren(UPDATED_NBR_OFCHILDREN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .primaryPhoneNumber(UPDATED_PRIMARY_PHONE_NUMBER)
            .secondaryPhoneNumber(UPDATED_SECONDARY_PHONE_NUMBER)
            .faxNumber(UPDATED_FAX_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .gender(UPDATED_GENDER)
            .jobTitle(UPDATED_JOB_TITLE)
            .professionalStatus(UPDATED_PROFESSIONAL_STATUS)
            .bank(UPDATED_BANK)
            .agency(UPDATED_AGENCY)
            .rib(UPDATED_RIB)
            .drivingLicenseNumber(UPDATED_DRIVING_LICENSE_NUMBER)
            .drivingLicenseIssueDate(UPDATED_DRIVING_LICENSE_ISSUE_DATE)
            .drivingLicenseCategory(UPDATED_DRIVING_LICENSE_CATEGORY);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedClientToMatchAllProperties(updatedClient);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Client> clientSearchList = Streamable.of(clientSearchRepository.findAll()).toList();
                Client testClientSearch = clientSearchList.get(searchDatabaseSizeAfter - 1);

                assertClientAllPropertiesEquals(testClientSearch, updatedClient);
            });
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(clientDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .firstName(UPDATED_FIRST_NAME)
            .identityIssueDate(UPDATED_IDENTITY_ISSUE_DATE)
            .identityPlaceOfIssue(UPDATED_IDENTITY_PLACE_OF_ISSUE)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .primaryPhoneNumber(UPDATED_PRIMARY_PHONE_NUMBER)
            .secondaryPhoneNumber(UPDATED_SECONDARY_PHONE_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .bank(UPDATED_BANK)
            .rib(UPDATED_RIB)
            .drivingLicenseNumber(UPDATED_DRIVING_LICENSE_NUMBER);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedClient, client), getPersistedClient(client));
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .identityType(UPDATED_IDENTITY_TYPE)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .identityEmissionDate(UPDATED_IDENTITY_EMISSION_DATE)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .identityIssueDate(UPDATED_IDENTITY_ISSUE_DATE)
            .identityPlaceOfIssue(UPDATED_IDENTITY_PLACE_OF_ISSUE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .nbrOfchildren(UPDATED_NBR_OFCHILDREN)
            .professionalEmail(UPDATED_PROFESSIONAL_EMAIL)
            .personalEmail(UPDATED_PERSONAL_EMAIL)
            .primaryPhoneNumber(UPDATED_PRIMARY_PHONE_NUMBER)
            .secondaryPhoneNumber(UPDATED_SECONDARY_PHONE_NUMBER)
            .faxNumber(UPDATED_FAX_NUMBER)
            .nationality(UPDATED_NATIONALITY)
            .gender(UPDATED_GENDER)
            .jobTitle(UPDATED_JOB_TITLE)
            .professionalStatus(UPDATED_PROFESSIONAL_STATUS)
            .bank(UPDATED_BANK)
            .agency(UPDATED_AGENCY)
            .rib(UPDATED_RIB)
            .drivingLicenseNumber(UPDATED_DRIVING_LICENSE_NUMBER)
            .drivingLicenseIssueDate(UPDATED_DRIVING_LICENSE_ISSUE_DATE)
            .drivingLicenseCategory(UPDATED_DRIVING_LICENSE_CATEGORY);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertClientUpdatableFieldsEquals(partialUpdatedClient, getPersistedClient(partialUpdatedClient));
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        client.setId(UUID.randomUUID());

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(clientDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);
        clientRepository.save(client);
        clientSearchRepository.save(client);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(clientSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchClient() throws Exception {
        // Initialize the database
        insertedClient = clientRepository.saveAndFlush(client);
        clientSearchRepository.save(client);

        // Search the client
        restClientMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].identityType").value(hasItem(DEFAULT_IDENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].identityNumber").value(hasItem(DEFAULT_IDENTITY_NUMBER)))
            .andExpect(jsonPath("$.[*].identityEmissionDate").value(hasItem(DEFAULT_IDENTITY_EMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].identityIssueDate").value(hasItem(DEFAULT_IDENTITY_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].identityPlaceOfIssue").value(hasItem(DEFAULT_IDENTITY_PLACE_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nbrOfchildren").value(hasItem(DEFAULT_NBR_OFCHILDREN)))
            .andExpect(jsonPath("$.[*].professionalEmail").value(hasItem(DEFAULT_PROFESSIONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].personalEmail").value(hasItem(DEFAULT_PERSONAL_EMAIL)))
            .andExpect(jsonPath("$.[*].primaryPhoneNumber").value(hasItem(DEFAULT_PRIMARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].secondaryPhoneNumber").value(hasItem(DEFAULT_SECONDARY_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].professionalStatus").value(hasItem(DEFAULT_PROFESSIONAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].agency").value(hasItem(DEFAULT_AGENCY)))
            .andExpect(jsonPath("$.[*].rib").value(hasItem(DEFAULT_RIB)))
            .andExpect(jsonPath("$.[*].drivingLicenseNumber").value(hasItem(DEFAULT_DRIVING_LICENSE_NUMBER)))
            .andExpect(jsonPath("$.[*].drivingLicenseIssueDate").value(hasItem(DEFAULT_DRIVING_LICENSE_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].drivingLicenseCategory").value(hasItem(DEFAULT_DRIVING_LICENSE_CATEGORY)));
    }

    protected long getRepositoryCount() {
        return clientRepository.count();
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

    protected Client getPersistedClient(Client client) {
        return clientRepository.findById(client.getId()).orElseThrow();
    }

    protected void assertPersistedClientToMatchAllProperties(Client expectedClient) {
        assertClientAllPropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }

    protected void assertPersistedClientToMatchUpdatableProperties(Client expectedClient) {
        assertClientAllUpdatablePropertiesEquals(expectedClient, getPersistedClient(expectedClient));
    }
}
