package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.ContractAsserts.*;
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
import com.satoripop.insurance.domain.Client;
import com.satoripop.insurance.domain.Contract;
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.domain.enumeration.ContractType;
import com.satoripop.insurance.domain.enumeration.PaymentPlan;
import com.satoripop.insurance.repository.ContractRepository;
import com.satoripop.insurance.repository.search.ContractSearchRepository;
import com.satoripop.insurance.service.dto.ContractDTO;
import com.satoripop.insurance.service.mapper.ContractMapper;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DURATION = "AAAAAAAAAA";
    private static final String UPDATED_DURATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_NET_PREMIUM = 1F;
    private static final Float UPDATED_NET_PREMIUM = 2F;
    private static final Float SMALLER_NET_PREMIUM = 1F - 1F;

    private static final Float DEFAULT_UPFRONT_PREMIUM = 1F;
    private static final Float UPDATED_UPFRONT_PREMIUM = 2F;
    private static final Float SMALLER_UPFRONT_PREMIUM = 1F - 1F;

    private static final Float DEFAULT_COST = 1F;
    private static final Float UPDATED_COST = 2F;
    private static final Float SMALLER_COST = 1F - 1F;

    private static final Float DEFAULT_TAXES = 1F;
    private static final Float UPDATED_TAXES = 2F;
    private static final Float SMALLER_TAXES = 1F - 1F;

    private static final Float DEFAULT_F_SSR = 1F;
    private static final Float UPDATED_F_SSR = 2F;
    private static final Float SMALLER_F_SSR = 1F - 1F;

    private static final Float DEFAULT_F_PAC = 1F;
    private static final Float UPDATED_F_PAC = 2F;
    private static final Float SMALLER_F_PAC = 1F - 1F;

    private static final Float DEFAULT_T_FGA = 1F;
    private static final Float UPDATED_T_FGA = 2F;
    private static final Float SMALLER_T_FGA = 1F - 1F;

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.RENEWABLE;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.CLOSED;

    private static final PaymentPlan DEFAULT_PAYMENT_PLAN = PaymentPlan.ANNUAL;
    private static final PaymentPlan UPDATED_PAYMENT_PLAN = PaymentPlan.SEMI_ANNUAL;

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/contracts/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractSearchRepository contractSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    private Contract insertedContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity() {
        return new Contract()
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .duration(DEFAULT_DURATION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .netPremium(DEFAULT_NET_PREMIUM)
            .upfrontPremium(DEFAULT_UPFRONT_PREMIUM)
            .cost(DEFAULT_COST)
            .taxes(DEFAULT_TAXES)
            .fSSR(DEFAULT_F_SSR)
            .fPAC(DEFAULT_F_PAC)
            .tFGA(DEFAULT_T_FGA)
            .contractType(DEFAULT_CONTRACT_TYPE)
            .paymentPlan(DEFAULT_PAYMENT_PLAN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity() {
        return new Contract()
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .duration(UPDATED_DURATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .netPremium(UPDATED_NET_PREMIUM)
            .upfrontPremium(UPDATED_UPFRONT_PREMIUM)
            .cost(UPDATED_COST)
            .taxes(UPDATED_TAXES)
            .fSSR(UPDATED_F_SSR)
            .fPAC(UPDATED_F_PAC)
            .tFGA(UPDATED_T_FGA)
            .contractType(UPDATED_CONTRACT_TYPE)
            .paymentPlan(UPDATED_PAYMENT_PLAN);
    }

    @BeforeEach
    void initTest() {
        contract = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContract != null) {
            contractRepository.delete(insertedContract);
            contractSearchRepository.delete(insertedContract);
            insertedContract = null;
        }
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        var returnedContractDTO = om.readValue(
            restContractMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractDTO.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContract = contractMapper.toEntity(returnedContractDTO);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedContract = returnedContract;
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        insertedContract = contractRepository.saveAndFlush(contract);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().toString())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].netPremium").value(hasItem(DEFAULT_NET_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontPremium").value(hasItem(DEFAULT_UPFRONT_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.doubleValue())))
            .andExpect(jsonPath("$.[*].fSSR").value(hasItem(DEFAULT_F_SSR.doubleValue())))
            .andExpect(jsonPath("$.[*].fPAC").value(hasItem(DEFAULT_F_PAC.doubleValue())))
            .andExpect(jsonPath("$.[*].tFGA").value(hasItem(DEFAULT_T_FGA.doubleValue())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentPlan").value(hasItem(DEFAULT_PAYMENT_PLAN.toString())));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().toString()))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.netPremium").value(DEFAULT_NET_PREMIUM.doubleValue()))
            .andExpect(jsonPath("$.upfrontPremium").value(DEFAULT_UPFRONT_PREMIUM.doubleValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.taxes").value(DEFAULT_TAXES.doubleValue()))
            .andExpect(jsonPath("$.fSSR").value(DEFAULT_F_SSR.doubleValue()))
            .andExpect(jsonPath("$.fPAC").value(DEFAULT_F_PAC.doubleValue()))
            .andExpect(jsonPath("$.tFGA").value(DEFAULT_T_FGA.doubleValue()))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.paymentPlan").value(DEFAULT_PAYMENT_PLAN.toString()));
    }

    @Test
    @Transactional
    void getContractsByIdFiltering() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        UUID id = contract.getId();

        defaultContractFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber equals to
        defaultContractFiltering("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER, "contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber in
        defaultContractFiltering(
            "contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER,
            "contractNumber.in=" + UPDATED_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber is not null
        defaultContractFiltering("contractNumber.specified=true", "contractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber contains
        defaultContractFiltering(
            "contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER,
            "contractNumber.contains=" + UPDATED_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber does not contain
        defaultContractFiltering(
            "contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER,
            "contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where duration equals to
        defaultContractFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllContractsByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where duration in
        defaultContractFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllContractsByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where duration is not null
        defaultContractFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByDurationContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where duration contains
        defaultContractFiltering("duration.contains=" + DEFAULT_DURATION, "duration.contains=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllContractsByDurationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where duration does not contain
        defaultContractFiltering("duration.doesNotContain=" + UPDATED_DURATION, "duration.doesNotContain=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate equals to
        defaultContractFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate in
        defaultContractFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is not null
        defaultContractFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is greater than or equal to
        defaultContractFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is less than or equal to
        defaultContractFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is less than
        defaultContractFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is greater than
        defaultContractFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate equals to
        defaultContractFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate in
        defaultContractFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is not null
        defaultContractFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is greater than or equal to
        defaultContractFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is less than or equal to
        defaultContractFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is less than
        defaultContractFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is greater than
        defaultContractFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium equals to
        defaultContractFiltering("netPremium.equals=" + DEFAULT_NET_PREMIUM, "netPremium.equals=" + UPDATED_NET_PREMIUM);
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium in
        defaultContractFiltering(
            "netPremium.in=" + DEFAULT_NET_PREMIUM + "," + UPDATED_NET_PREMIUM,
            "netPremium.in=" + UPDATED_NET_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium is not null
        defaultContractFiltering("netPremium.specified=true", "netPremium.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium is greater than or equal to
        defaultContractFiltering(
            "netPremium.greaterThanOrEqual=" + DEFAULT_NET_PREMIUM,
            "netPremium.greaterThanOrEqual=" + UPDATED_NET_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium is less than or equal to
        defaultContractFiltering("netPremium.lessThanOrEqual=" + DEFAULT_NET_PREMIUM, "netPremium.lessThanOrEqual=" + SMALLER_NET_PREMIUM);
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium is less than
        defaultContractFiltering("netPremium.lessThan=" + UPDATED_NET_PREMIUM, "netPremium.lessThan=" + DEFAULT_NET_PREMIUM);
    }

    @Test
    @Transactional
    void getAllContractsByNetPremiumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where netPremium is greater than
        defaultContractFiltering("netPremium.greaterThan=" + SMALLER_NET_PREMIUM, "netPremium.greaterThan=" + DEFAULT_NET_PREMIUM);
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium equals to
        defaultContractFiltering("upfrontPremium.equals=" + DEFAULT_UPFRONT_PREMIUM, "upfrontPremium.equals=" + UPDATED_UPFRONT_PREMIUM);
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium in
        defaultContractFiltering(
            "upfrontPremium.in=" + DEFAULT_UPFRONT_PREMIUM + "," + UPDATED_UPFRONT_PREMIUM,
            "upfrontPremium.in=" + UPDATED_UPFRONT_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium is not null
        defaultContractFiltering("upfrontPremium.specified=true", "upfrontPremium.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium is greater than or equal to
        defaultContractFiltering(
            "upfrontPremium.greaterThanOrEqual=" + DEFAULT_UPFRONT_PREMIUM,
            "upfrontPremium.greaterThanOrEqual=" + UPDATED_UPFRONT_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium is less than or equal to
        defaultContractFiltering(
            "upfrontPremium.lessThanOrEqual=" + DEFAULT_UPFRONT_PREMIUM,
            "upfrontPremium.lessThanOrEqual=" + SMALLER_UPFRONT_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium is less than
        defaultContractFiltering(
            "upfrontPremium.lessThan=" + UPDATED_UPFRONT_PREMIUM,
            "upfrontPremium.lessThan=" + DEFAULT_UPFRONT_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByUpfrontPremiumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where upfrontPremium is greater than
        defaultContractFiltering(
            "upfrontPremium.greaterThan=" + SMALLER_UPFRONT_PREMIUM,
            "upfrontPremium.greaterThan=" + DEFAULT_UPFRONT_PREMIUM
        );
    }

    @Test
    @Transactional
    void getAllContractsByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost equals to
        defaultContractFiltering("cost.equals=" + DEFAULT_COST, "cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllContractsByCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost in
        defaultContractFiltering("cost.in=" + DEFAULT_COST + "," + UPDATED_COST, "cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllContractsByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost is not null
        defaultContractFiltering("cost.specified=true", "cost.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost is greater than or equal to
        defaultContractFiltering("cost.greaterThanOrEqual=" + DEFAULT_COST, "cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllContractsByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost is less than or equal to
        defaultContractFiltering("cost.lessThanOrEqual=" + DEFAULT_COST, "cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllContractsByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost is less than
        defaultContractFiltering("cost.lessThan=" + UPDATED_COST, "cost.lessThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllContractsByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where cost is greater than
        defaultContractFiltering("cost.greaterThan=" + SMALLER_COST, "cost.greaterThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes equals to
        defaultContractFiltering("taxes.equals=" + DEFAULT_TAXES, "taxes.equals=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes in
        defaultContractFiltering("taxes.in=" + DEFAULT_TAXES + "," + UPDATED_TAXES, "taxes.in=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes is not null
        defaultContractFiltering("taxes.specified=true", "taxes.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes is greater than or equal to
        defaultContractFiltering("taxes.greaterThanOrEqual=" + DEFAULT_TAXES, "taxes.greaterThanOrEqual=" + UPDATED_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes is less than or equal to
        defaultContractFiltering("taxes.lessThanOrEqual=" + DEFAULT_TAXES, "taxes.lessThanOrEqual=" + SMALLER_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes is less than
        defaultContractFiltering("taxes.lessThan=" + UPDATED_TAXES, "taxes.lessThan=" + DEFAULT_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByTaxesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where taxes is greater than
        defaultContractFiltering("taxes.greaterThan=" + SMALLER_TAXES, "taxes.greaterThan=" + DEFAULT_TAXES);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR equals to
        defaultContractFiltering("fSSR.equals=" + DEFAULT_F_SSR, "fSSR.equals=" + UPDATED_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR in
        defaultContractFiltering("fSSR.in=" + DEFAULT_F_SSR + "," + UPDATED_F_SSR, "fSSR.in=" + UPDATED_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR is not null
        defaultContractFiltering("fSSR.specified=true", "fSSR.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR is greater than or equal to
        defaultContractFiltering("fSSR.greaterThanOrEqual=" + DEFAULT_F_SSR, "fSSR.greaterThanOrEqual=" + UPDATED_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR is less than or equal to
        defaultContractFiltering("fSSR.lessThanOrEqual=" + DEFAULT_F_SSR, "fSSR.lessThanOrEqual=" + SMALLER_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR is less than
        defaultContractFiltering("fSSR.lessThan=" + UPDATED_F_SSR, "fSSR.lessThan=" + DEFAULT_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfSSRIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fSSR is greater than
        defaultContractFiltering("fSSR.greaterThan=" + SMALLER_F_SSR, "fSSR.greaterThan=" + DEFAULT_F_SSR);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC equals to
        defaultContractFiltering("fPAC.equals=" + DEFAULT_F_PAC, "fPAC.equals=" + UPDATED_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC in
        defaultContractFiltering("fPAC.in=" + DEFAULT_F_PAC + "," + UPDATED_F_PAC, "fPAC.in=" + UPDATED_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC is not null
        defaultContractFiltering("fPAC.specified=true", "fPAC.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC is greater than or equal to
        defaultContractFiltering("fPAC.greaterThanOrEqual=" + DEFAULT_F_PAC, "fPAC.greaterThanOrEqual=" + UPDATED_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC is less than or equal to
        defaultContractFiltering("fPAC.lessThanOrEqual=" + DEFAULT_F_PAC, "fPAC.lessThanOrEqual=" + SMALLER_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC is less than
        defaultContractFiltering("fPAC.lessThan=" + UPDATED_F_PAC, "fPAC.lessThan=" + DEFAULT_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsByfPACIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where fPAC is greater than
        defaultContractFiltering("fPAC.greaterThan=" + SMALLER_F_PAC, "fPAC.greaterThan=" + DEFAULT_F_PAC);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA equals to
        defaultContractFiltering("tFGA.equals=" + DEFAULT_T_FGA, "tFGA.equals=" + UPDATED_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA in
        defaultContractFiltering("tFGA.in=" + DEFAULT_T_FGA + "," + UPDATED_T_FGA, "tFGA.in=" + UPDATED_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA is not null
        defaultContractFiltering("tFGA.specified=true", "tFGA.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA is greater than or equal to
        defaultContractFiltering("tFGA.greaterThanOrEqual=" + DEFAULT_T_FGA, "tFGA.greaterThanOrEqual=" + UPDATED_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA is less than or equal to
        defaultContractFiltering("tFGA.lessThanOrEqual=" + DEFAULT_T_FGA, "tFGA.lessThanOrEqual=" + SMALLER_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA is less than
        defaultContractFiltering("tFGA.lessThan=" + UPDATED_T_FGA, "tFGA.lessThan=" + DEFAULT_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsBytFGAIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where tFGA is greater than
        defaultContractFiltering("tFGA.greaterThan=" + SMALLER_T_FGA, "tFGA.greaterThan=" + DEFAULT_T_FGA);
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType equals to
        defaultContractFiltering("contractType.equals=" + DEFAULT_CONTRACT_TYPE, "contractType.equals=" + UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType in
        defaultContractFiltering(
            "contractType.in=" + DEFAULT_CONTRACT_TYPE + "," + UPDATED_CONTRACT_TYPE,
            "contractType.in=" + UPDATED_CONTRACT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType is not null
        defaultContractFiltering("contractType.specified=true", "contractType.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByPaymentPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where paymentPlan equals to
        defaultContractFiltering("paymentPlan.equals=" + DEFAULT_PAYMENT_PLAN, "paymentPlan.equals=" + UPDATED_PAYMENT_PLAN);
    }

    @Test
    @Transactional
    void getAllContractsByPaymentPlanIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where paymentPlan in
        defaultContractFiltering(
            "paymentPlan.in=" + DEFAULT_PAYMENT_PLAN + "," + UPDATED_PAYMENT_PLAN,
            "paymentPlan.in=" + UPDATED_PAYMENT_PLAN
        );
    }

    @Test
    @Transactional
    void getAllContractsByPaymentPlanIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where paymentPlan is not null
        defaultContractFiltering("paymentPlan.specified=true", "paymentPlan.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByVehicleIsEqualToSomething() throws Exception {
        Vehicle vehicle;
        if (TestUtil.findAll(em, Vehicle.class).isEmpty()) {
            contractRepository.saveAndFlush(contract);
            vehicle = VehicleResourceIT.createEntity();
        } else {
            vehicle = TestUtil.findAll(em, Vehicle.class).get(0);
        }
        em.persist(vehicle);
        em.flush();
        contract.setVehicle(vehicle);
        contractRepository.saveAndFlush(contract);
        UUID vehicleId = vehicle.getId();
        // Get all the contractList where vehicle equals to vehicleId
        defaultContractShouldBeFound("vehicleId.equals=" + vehicleId);

        // Get all the contractList where vehicle equals to UUID.randomUUID()
        defaultContractShouldNotBeFound("vehicleId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllContractsByClientIsEqualToSomething() throws Exception {
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            contractRepository.saveAndFlush(contract);
            client = ClientResourceIT.createEntity();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        contract.setClient(client);
        contractRepository.saveAndFlush(contract);
        UUID clientId = client.getId();
        // Get all the contractList where client equals to clientId
        defaultContractShouldBeFound("clientId.equals=" + clientId);

        // Get all the contractList where client equals to UUID.randomUUID()
        defaultContractShouldNotBeFound("clientId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllContractsByAgencyIsEqualToSomething() throws Exception {
        Agency agency;
        if (TestUtil.findAll(em, Agency.class).isEmpty()) {
            contractRepository.saveAndFlush(contract);
            agency = AgencyResourceIT.createEntity();
        } else {
            agency = TestUtil.findAll(em, Agency.class).get(0);
        }
        em.persist(agency);
        em.flush();
        contract.setAgency(agency);
        contractRepository.saveAndFlush(contract);
        UUID agencyId = agency.getId();
        // Get all the contractList where agency equals to agencyId
        defaultContractShouldBeFound("agencyId.equals=" + agencyId);

        // Get all the contractList where agency equals to UUID.randomUUID()
        defaultContractShouldNotBeFound("agencyId.equals=" + UUID.randomUUID());
    }

    private void defaultContractFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContractShouldBeFound(shouldBeFound);
        defaultContractShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().toString())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].netPremium").value(hasItem(DEFAULT_NET_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontPremium").value(hasItem(DEFAULT_UPFRONT_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.doubleValue())))
            .andExpect(jsonPath("$.[*].fSSR").value(hasItem(DEFAULT_F_SSR.doubleValue())))
            .andExpect(jsonPath("$.[*].fPAC").value(hasItem(DEFAULT_F_PAC.doubleValue())))
            .andExpect(jsonPath("$.[*].tFGA").value(hasItem(DEFAULT_T_FGA.doubleValue())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentPlan").value(hasItem(DEFAULT_PAYMENT_PLAN.toString())));

        // Check, that the count call also returns 1
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractSearchRepository.save(contract);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .duration(UPDATED_DURATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .netPremium(UPDATED_NET_PREMIUM)
            .upfrontPremium(UPDATED_UPFRONT_PREMIUM)
            .cost(UPDATED_COST)
            .taxes(UPDATED_TAXES)
            .fSSR(UPDATED_F_SSR)
            .fPAC(UPDATED_F_PAC)
            .tFGA(UPDATED_T_FGA)
            .contractType(UPDATED_CONTRACT_TYPE)
            .paymentPlan(UPDATED_PAYMENT_PLAN);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Contract> contractSearchList = Streamable.of(contractSearchRepository.findAll()).toList();
                Contract testContractSearch = contractSearchList.get(searchDatabaseSizeAfter - 1);

                assertContractAllPropertiesEquals(testContractSearch, updatedContract);
            });
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .startDate(UPDATED_START_DATE)
            .netPremium(UPDATED_NET_PREMIUM)
            .upfrontPremium(UPDATED_UPFRONT_PREMIUM)
            .fSSR(UPDATED_F_SSR)
            .fPAC(UPDATED_F_PAC)
            .contractType(UPDATED_CONTRACT_TYPE)
            .paymentPlan(UPDATED_PAYMENT_PLAN);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .duration(UPDATED_DURATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .netPremium(UPDATED_NET_PREMIUM)
            .upfrontPremium(UPDATED_UPFRONT_PREMIUM)
            .cost(UPDATED_COST)
            .taxes(UPDATED_TAXES)
            .fSSR(UPDATED_F_SSR)
            .fPAC(UPDATED_F_PAC)
            .tFGA(UPDATED_T_FGA)
            .contractType(UPDATED_CONTRACT_TYPE)
            .paymentPlan(UPDATED_PAYMENT_PLAN);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(UUID.randomUUID());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);
        contractRepository.save(contract);
        contractSearchRepository.save(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);
        contractSearchRepository.save(contract);

        // Search the contract
        restContractMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().toString())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].netPremium").value(hasItem(DEFAULT_NET_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].upfrontPremium").value(hasItem(DEFAULT_UPFRONT_PREMIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].taxes").value(hasItem(DEFAULT_TAXES.doubleValue())))
            .andExpect(jsonPath("$.[*].fSSR").value(hasItem(DEFAULT_F_SSR.doubleValue())))
            .andExpect(jsonPath("$.[*].fPAC").value(hasItem(DEFAULT_F_PAC.doubleValue())))
            .andExpect(jsonPath("$.[*].tFGA").value(hasItem(DEFAULT_T_FGA.doubleValue())))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].paymentPlan").value(hasItem(DEFAULT_PAYMENT_PLAN.toString())));
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
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

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
