package com.satoripop.insurance.web.rest;

import static com.satoripop.insurance.domain.VehicleAsserts.*;
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
import com.satoripop.insurance.domain.Vehicle;
import com.satoripop.insurance.domain.enumeration.Brand;
import com.satoripop.insurance.domain.enumeration.Energy;
import com.satoripop.insurance.domain.enumeration.Gearbox;
import com.satoripop.insurance.domain.enumeration.RegistrationType;
import com.satoripop.insurance.domain.enumeration.TechnicalInspectionStatus;
import com.satoripop.insurance.domain.enumeration.VehicleUsage;
import com.satoripop.insurance.repository.VehicleRepository;
import com.satoripop.insurance.repository.search.VehicleSearchRepository;
import com.satoripop.insurance.service.dto.VehicleDTO;
import com.satoripop.insurance.service.mapper.VehicleMapper;
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
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleResourceIT {

    private static final String DEFAULT_REGISTRATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_NUMBER = "BBBBBBBBBB";

    private static final RegistrationType DEFAULT_REGISTRATION_TYPE = RegistrationType.STANDARD_PRIVATE_VEHICLE;
    private static final RegistrationType UPDATED_REGISTRATION_TYPE = RegistrationType.SUSPENSIVE_REGIME;

    private static final LocalDate DEFAULT_FIRST_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FIRST_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final TechnicalInspectionStatus DEFAULT_TECHNICAL_INSPECTION_STATUS = TechnicalInspectionStatus.VALID;
    private static final TechnicalInspectionStatus UPDATED_TECHNICAL_INSPECTION_STATUS = TechnicalInspectionStatus.EXEMPTED;

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Float DEFAULT_NEW_VALUE = 1F;
    private static final Float UPDATED_NEW_VALUE = 2F;
    private static final Float SMALLER_NEW_VALUE = 1F - 1F;

    private static final Float DEFAULT_MARKET_VALUE = 1F;
    private static final Float UPDATED_MARKET_VALUE = 2F;
    private static final Float SMALLER_MARKET_VALUE = 1F - 1F;

    private static final Brand DEFAULT_BRAND = Brand.AUDI;
    private static final Brand UPDATED_BRAND = Brand.BMW;

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_FISCAL_POWER = 1;
    private static final Integer UPDATED_FISCAL_POWER = 2;
    private static final Integer SMALLER_FISCAL_POWER = 1 - 1;

    private static final String DEFAULT_CHASSIS_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CHASSIS_NUMBER = "BBBBBBBBBB";

    private static final Energy DEFAULT_ENERGY = Energy.DIESEL;
    private static final Energy UPDATED_ENERGY = Energy.PETROL;

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBR_OF_SEATS = 1;
    private static final Integer UPDATED_NBR_OF_SEATS = 2;
    private static final Integer SMALLER_NBR_OF_SEATS = 1 - 1;

    private static final Integer DEFAULT_NBR_OF_STANDING_PLACES = 1;
    private static final Integer UPDATED_NBR_OF_STANDING_PLACES = 2;
    private static final Integer SMALLER_NBR_OF_STANDING_PLACES = 1 - 1;

    private static final Integer DEFAULT_EMPTY_WEIGHT = 1;
    private static final Integer UPDATED_EMPTY_WEIGHT = 2;
    private static final Integer SMALLER_EMPTY_WEIGHT = 1 - 1;

    private static final Integer DEFAULT_PAYLOAD = 1;
    private static final Integer UPDATED_PAYLOAD = 2;
    private static final Integer SMALLER_PAYLOAD = 1 - 1;

    private static final Integer DEFAULT_BONUS_MALUS = 1;
    private static final Integer UPDATED_BONUS_MALUS = 2;
    private static final Integer SMALLER_BONUS_MALUS = 1 - 1;

    private static final String DEFAULT_VEHICLE_AGE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_AGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MILEAGE = 1;
    private static final Integer UPDATED_MILEAGE = 2;
    private static final Integer SMALLER_MILEAGE = 1 - 1;

    private static final Long DEFAULT_NUMBER_OF_DOORS = 1L;
    private static final Long UPDATED_NUMBER_OF_DOORS = 2L;
    private static final Long SMALLER_NUMBER_OF_DOORS = 1L - 1L;

    private static final Gearbox DEFAULT_GEARBOX = Gearbox.AUTOMATIC;
    private static final Gearbox UPDATED_GEARBOX = Gearbox.MANUAL;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final VehicleUsage DEFAULT_USAGE = VehicleUsage.PERSONAL;
    private static final VehicleUsage UPDATED_USAGE = VehicleUsage.COMMERCIAL;

    private static final Boolean DEFAULT_IS_NEW = false;
    private static final Boolean UPDATED_IS_NEW = true;

    private static final Boolean DEFAULT_HAS_GARAGE = false;
    private static final Boolean UPDATED_HAS_GARAGE = true;

    private static final Boolean DEFAULT_HAS_PARKING = false;
    private static final Boolean UPDATED_HAS_PARKING = true;

    private static final Boolean DEFAULT_HAS_ALARM_SYSTEM = false;
    private static final Boolean UPDATED_HAS_ALARM_SYSTEM = true;

    private static final Boolean DEFAULT_HAS_SEATBELT_ALARM = false;
    private static final Boolean UPDATED_HAS_SEATBELT_ALARM = true;

    private static final Boolean DEFAULT_HAS_REAR_CAMERA = false;
    private static final Boolean UPDATED_HAS_REAR_CAMERA = true;

    private static final Boolean DEFAULT_HAS_REAR_RADAR = false;
    private static final Boolean UPDATED_HAS_REAR_RADAR = true;

    private static final Boolean DEFAULT_HAS_ABS_SYSTEM = false;
    private static final Boolean UPDATED_HAS_ABS_SYSTEM = true;

    private static final Boolean DEFAULT_HAS_GPS = false;
    private static final Boolean UPDATED_HAS_GPS = true;

    private static final Boolean DEFAULT_HAS_AIRBAG = false;
    private static final Boolean UPDATED_HAS_AIRBAG = true;

    private static final Boolean DEFAULT_NAVETTE = false;
    private static final Boolean UPDATED_NAVETTE = true;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/vehicles/_search";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private VehicleSearchRepository vehicleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    private Vehicle insertedVehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity() {
        return new Vehicle()
            .registrationNumber(DEFAULT_REGISTRATION_NUMBER)
            .registrationType(DEFAULT_REGISTRATION_TYPE)
            .firstRegistrationDate(DEFAULT_FIRST_REGISTRATION_DATE)
            .technicalInspectionStatus(DEFAULT_TECHNICAL_INSPECTION_STATUS)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .newValue(DEFAULT_NEW_VALUE)
            .marketValue(DEFAULT_MARKET_VALUE)
            .brand(DEFAULT_BRAND)
            .model(DEFAULT_MODEL)
            .fiscalPower(DEFAULT_FISCAL_POWER)
            .chassisNumber(DEFAULT_CHASSIS_NUMBER)
            .energy(DEFAULT_ENERGY)
            .genre(DEFAULT_GENRE)
            .nbrOfSeats(DEFAULT_NBR_OF_SEATS)
            .nbrOfStandingPlaces(DEFAULT_NBR_OF_STANDING_PLACES)
            .emptyWeight(DEFAULT_EMPTY_WEIGHT)
            .payload(DEFAULT_PAYLOAD)
            .bonusMalus(DEFAULT_BONUS_MALUS)
            .vehicleAge(DEFAULT_VEHICLE_AGE)
            .mileage(DEFAULT_MILEAGE)
            .numberOfDoors(DEFAULT_NUMBER_OF_DOORS)
            .gearbox(DEFAULT_GEARBOX)
            .color(DEFAULT_COLOR)
            .usage(DEFAULT_USAGE)
            .isNew(DEFAULT_IS_NEW)
            .hasGarage(DEFAULT_HAS_GARAGE)
            .hasParking(DEFAULT_HAS_PARKING)
            .hasAlarmSystem(DEFAULT_HAS_ALARM_SYSTEM)
            .hasSeatbeltAlarm(DEFAULT_HAS_SEATBELT_ALARM)
            .hasRearCamera(DEFAULT_HAS_REAR_CAMERA)
            .hasRearRadar(DEFAULT_HAS_REAR_RADAR)
            .hasAbsSystem(DEFAULT_HAS_ABS_SYSTEM)
            .hasGPS(DEFAULT_HAS_GPS)
            .hasAirbag(DEFAULT_HAS_AIRBAG)
            .navette(DEFAULT_NAVETTE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity() {
        return new Vehicle()
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationType(UPDATED_REGISTRATION_TYPE)
            .firstRegistrationDate(UPDATED_FIRST_REGISTRATION_DATE)
            .technicalInspectionStatus(UPDATED_TECHNICAL_INSPECTION_STATUS)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .newValue(UPDATED_NEW_VALUE)
            .marketValue(UPDATED_MARKET_VALUE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .fiscalPower(UPDATED_FISCAL_POWER)
            .chassisNumber(UPDATED_CHASSIS_NUMBER)
            .energy(UPDATED_ENERGY)
            .genre(UPDATED_GENRE)
            .nbrOfSeats(UPDATED_NBR_OF_SEATS)
            .nbrOfStandingPlaces(UPDATED_NBR_OF_STANDING_PLACES)
            .emptyWeight(UPDATED_EMPTY_WEIGHT)
            .payload(UPDATED_PAYLOAD)
            .bonusMalus(UPDATED_BONUS_MALUS)
            .vehicleAge(UPDATED_VEHICLE_AGE)
            .mileage(UPDATED_MILEAGE)
            .numberOfDoors(UPDATED_NUMBER_OF_DOORS)
            .gearbox(UPDATED_GEARBOX)
            .color(UPDATED_COLOR)
            .usage(UPDATED_USAGE)
            .isNew(UPDATED_IS_NEW)
            .hasGarage(UPDATED_HAS_GARAGE)
            .hasParking(UPDATED_HAS_PARKING)
            .hasAlarmSystem(UPDATED_HAS_ALARM_SYSTEM)
            .hasSeatbeltAlarm(UPDATED_HAS_SEATBELT_ALARM)
            .hasRearCamera(UPDATED_HAS_REAR_CAMERA)
            .hasRearRadar(UPDATED_HAS_REAR_RADAR)
            .hasAbsSystem(UPDATED_HAS_ABS_SYSTEM)
            .hasGPS(UPDATED_HAS_GPS)
            .hasAirbag(UPDATED_HAS_AIRBAG)
            .navette(UPDATED_NAVETTE);
    }

    @BeforeEach
    void initTest() {
        vehicle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVehicle != null) {
            vehicleRepository.delete(insertedVehicle);
            vehicleSearchRepository.delete(insertedVehicle);
            insertedVehicle = null;
        }
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);
        var returnedVehicleDTO = om.readValue(
            restVehicleMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleDTO.class
        );

        // Validate the Vehicle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicle = vehicleMapper.toEntity(returnedVehicleDTO);
        assertVehicleUpdatableFieldsEquals(returnedVehicle, getPersistedVehicle(returnedVehicle));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedVehicle = returnedVehicle;
    }

    @Test
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().toString())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationType").value(hasItem(DEFAULT_REGISTRATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].firstRegistrationDate").value(hasItem(DEFAULT_FIRST_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].technicalInspectionStatus").value(hasItem(DEFAULT_TECHNICAL_INSPECTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketValue").value(hasItem(DEFAULT_MARKET_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].fiscalPower").value(hasItem(DEFAULT_FISCAL_POWER)))
            .andExpect(jsonPath("$.[*].chassisNumber").value(hasItem(DEFAULT_CHASSIS_NUMBER)))
            .andExpect(jsonPath("$.[*].energy").value(hasItem(DEFAULT_ENERGY.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].nbrOfSeats").value(hasItem(DEFAULT_NBR_OF_SEATS)))
            .andExpect(jsonPath("$.[*].nbrOfStandingPlaces").value(hasItem(DEFAULT_NBR_OF_STANDING_PLACES)))
            .andExpect(jsonPath("$.[*].emptyWeight").value(hasItem(DEFAULT_EMPTY_WEIGHT)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].bonusMalus").value(hasItem(DEFAULT_BONUS_MALUS)))
            .andExpect(jsonPath("$.[*].vehicleAge").value(hasItem(DEFAULT_VEHICLE_AGE)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].numberOfDoors").value(hasItem(DEFAULT_NUMBER_OF_DOORS.intValue())))
            .andExpect(jsonPath("$.[*].gearbox").value(hasItem(DEFAULT_GEARBOX.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE.toString())))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW)))
            .andExpect(jsonPath("$.[*].hasGarage").value(hasItem(DEFAULT_HAS_GARAGE)))
            .andExpect(jsonPath("$.[*].hasParking").value(hasItem(DEFAULT_HAS_PARKING)))
            .andExpect(jsonPath("$.[*].hasAlarmSystem").value(hasItem(DEFAULT_HAS_ALARM_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasSeatbeltAlarm").value(hasItem(DEFAULT_HAS_SEATBELT_ALARM)))
            .andExpect(jsonPath("$.[*].hasRearCamera").value(hasItem(DEFAULT_HAS_REAR_CAMERA)))
            .andExpect(jsonPath("$.[*].hasRearRadar").value(hasItem(DEFAULT_HAS_REAR_RADAR)))
            .andExpect(jsonPath("$.[*].hasAbsSystem").value(hasItem(DEFAULT_HAS_ABS_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasGPS").value(hasItem(DEFAULT_HAS_GPS)))
            .andExpect(jsonPath("$.[*].hasAirbag").value(hasItem(DEFAULT_HAS_AIRBAG)))
            .andExpect(jsonPath("$.[*].navette").value(hasItem(DEFAULT_NAVETTE)));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().toString()))
            .andExpect(jsonPath("$.registrationNumber").value(DEFAULT_REGISTRATION_NUMBER))
            .andExpect(jsonPath("$.registrationType").value(DEFAULT_REGISTRATION_TYPE.toString()))
            .andExpect(jsonPath("$.firstRegistrationDate").value(DEFAULT_FIRST_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.technicalInspectionStatus").value(DEFAULT_TECHNICAL_INSPECTION_STATUS.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.newValue").value(DEFAULT_NEW_VALUE.doubleValue()))
            .andExpect(jsonPath("$.marketValue").value(DEFAULT_MARKET_VALUE.doubleValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.fiscalPower").value(DEFAULT_FISCAL_POWER))
            .andExpect(jsonPath("$.chassisNumber").value(DEFAULT_CHASSIS_NUMBER))
            .andExpect(jsonPath("$.energy").value(DEFAULT_ENERGY.toString()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE))
            .andExpect(jsonPath("$.nbrOfSeats").value(DEFAULT_NBR_OF_SEATS))
            .andExpect(jsonPath("$.nbrOfStandingPlaces").value(DEFAULT_NBR_OF_STANDING_PLACES))
            .andExpect(jsonPath("$.emptyWeight").value(DEFAULT_EMPTY_WEIGHT))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD))
            .andExpect(jsonPath("$.bonusMalus").value(DEFAULT_BONUS_MALUS))
            .andExpect(jsonPath("$.vehicleAge").value(DEFAULT_VEHICLE_AGE))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE))
            .andExpect(jsonPath("$.numberOfDoors").value(DEFAULT_NUMBER_OF_DOORS.intValue()))
            .andExpect(jsonPath("$.gearbox").value(DEFAULT_GEARBOX.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE.toString()))
            .andExpect(jsonPath("$.isNew").value(DEFAULT_IS_NEW))
            .andExpect(jsonPath("$.hasGarage").value(DEFAULT_HAS_GARAGE))
            .andExpect(jsonPath("$.hasParking").value(DEFAULT_HAS_PARKING))
            .andExpect(jsonPath("$.hasAlarmSystem").value(DEFAULT_HAS_ALARM_SYSTEM))
            .andExpect(jsonPath("$.hasSeatbeltAlarm").value(DEFAULT_HAS_SEATBELT_ALARM))
            .andExpect(jsonPath("$.hasRearCamera").value(DEFAULT_HAS_REAR_CAMERA))
            .andExpect(jsonPath("$.hasRearRadar").value(DEFAULT_HAS_REAR_RADAR))
            .andExpect(jsonPath("$.hasAbsSystem").value(DEFAULT_HAS_ABS_SYSTEM))
            .andExpect(jsonPath("$.hasGPS").value(DEFAULT_HAS_GPS))
            .andExpect(jsonPath("$.hasAirbag").value(DEFAULT_HAS_AIRBAG))
            .andExpect(jsonPath("$.navette").value(DEFAULT_NAVETTE));
    }

    @Test
    @Transactional
    void getVehiclesByIdFiltering() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        UUID id = vehicle.getId();

        defaultVehicleFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationNumber equals to
        defaultVehicleFiltering(
            "registrationNumber.equals=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.equals=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationNumber in
        defaultVehicleFiltering(
            "registrationNumber.in=" + DEFAULT_REGISTRATION_NUMBER + "," + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.in=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationNumber is not null
        defaultVehicleFiltering("registrationNumber.specified=true", "registrationNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationNumber contains
        defaultVehicleFiltering(
            "registrationNumber.contains=" + DEFAULT_REGISTRATION_NUMBER,
            "registrationNumber.contains=" + UPDATED_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationNumber does not contain
        defaultVehicleFiltering(
            "registrationNumber.doesNotContain=" + UPDATED_REGISTRATION_NUMBER,
            "registrationNumber.doesNotContain=" + DEFAULT_REGISTRATION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationType equals to
        defaultVehicleFiltering(
            "registrationType.equals=" + DEFAULT_REGISTRATION_TYPE,
            "registrationType.equals=" + UPDATED_REGISTRATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationType in
        defaultVehicleFiltering(
            "registrationType.in=" + DEFAULT_REGISTRATION_TYPE + "," + UPDATED_REGISTRATION_TYPE,
            "registrationType.in=" + UPDATED_REGISTRATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByRegistrationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where registrationType is not null
        defaultVehicleFiltering("registrationType.specified=true", "registrationType.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate equals to
        defaultVehicleFiltering(
            "firstRegistrationDate.equals=" + DEFAULT_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.equals=" + UPDATED_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate in
        defaultVehicleFiltering(
            "firstRegistrationDate.in=" + DEFAULT_FIRST_REGISTRATION_DATE + "," + UPDATED_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.in=" + UPDATED_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate is not null
        defaultVehicleFiltering("firstRegistrationDate.specified=true", "firstRegistrationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate is greater than or equal to
        defaultVehicleFiltering(
            "firstRegistrationDate.greaterThanOrEqual=" + DEFAULT_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.greaterThanOrEqual=" + UPDATED_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate is less than or equal to
        defaultVehicleFiltering(
            "firstRegistrationDate.lessThanOrEqual=" + DEFAULT_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.lessThanOrEqual=" + SMALLER_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate is less than
        defaultVehicleFiltering(
            "firstRegistrationDate.lessThan=" + UPDATED_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.lessThan=" + DEFAULT_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFirstRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where firstRegistrationDate is greater than
        defaultVehicleFiltering(
            "firstRegistrationDate.greaterThan=" + SMALLER_FIRST_REGISTRATION_DATE,
            "firstRegistrationDate.greaterThan=" + DEFAULT_FIRST_REGISTRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByTechnicalInspectionStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where technicalInspectionStatus equals to
        defaultVehicleFiltering(
            "technicalInspectionStatus.equals=" + DEFAULT_TECHNICAL_INSPECTION_STATUS,
            "technicalInspectionStatus.equals=" + UPDATED_TECHNICAL_INSPECTION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByTechnicalInspectionStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where technicalInspectionStatus in
        defaultVehicleFiltering(
            "technicalInspectionStatus.in=" + DEFAULT_TECHNICAL_INSPECTION_STATUS + "," + UPDATED_TECHNICAL_INSPECTION_STATUS,
            "technicalInspectionStatus.in=" + UPDATED_TECHNICAL_INSPECTION_STATUS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByTechnicalInspectionStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where technicalInspectionStatus is not null
        defaultVehicleFiltering("technicalInspectionStatus.specified=true", "technicalInspectionStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate equals to
        defaultVehicleFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate in
        defaultVehicleFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate is not null
        defaultVehicleFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate is greater than or equal to
        defaultVehicleFiltering(
            "expirationDate.greaterThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.greaterThanOrEqual=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate is less than or equal to
        defaultVehicleFiltering(
            "expirationDate.lessThanOrEqual=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.lessThanOrEqual=" + SMALLER_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate is less than
        defaultVehicleFiltering("expirationDate.lessThan=" + UPDATED_EXPIRATION_DATE, "expirationDate.lessThan=" + DEFAULT_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllVehiclesByExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where expirationDate is greater than
        defaultVehicleFiltering(
            "expirationDate.greaterThan=" + SMALLER_EXPIRATION_DATE,
            "expirationDate.greaterThan=" + DEFAULT_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue equals to
        defaultVehicleFiltering("newValue.equals=" + DEFAULT_NEW_VALUE, "newValue.equals=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue in
        defaultVehicleFiltering("newValue.in=" + DEFAULT_NEW_VALUE + "," + UPDATED_NEW_VALUE, "newValue.in=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue is not null
        defaultVehicleFiltering("newValue.specified=true", "newValue.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue is greater than or equal to
        defaultVehicleFiltering("newValue.greaterThanOrEqual=" + DEFAULT_NEW_VALUE, "newValue.greaterThanOrEqual=" + UPDATED_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue is less than or equal to
        defaultVehicleFiltering("newValue.lessThanOrEqual=" + DEFAULT_NEW_VALUE, "newValue.lessThanOrEqual=" + SMALLER_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue is less than
        defaultVehicleFiltering("newValue.lessThan=" + UPDATED_NEW_VALUE, "newValue.lessThan=" + DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNewValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where newValue is greater than
        defaultVehicleFiltering("newValue.greaterThan=" + SMALLER_NEW_VALUE, "newValue.greaterThan=" + DEFAULT_NEW_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue equals to
        defaultVehicleFiltering("marketValue.equals=" + DEFAULT_MARKET_VALUE, "marketValue.equals=" + UPDATED_MARKET_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue in
        defaultVehicleFiltering(
            "marketValue.in=" + DEFAULT_MARKET_VALUE + "," + UPDATED_MARKET_VALUE,
            "marketValue.in=" + UPDATED_MARKET_VALUE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue is not null
        defaultVehicleFiltering("marketValue.specified=true", "marketValue.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue is greater than or equal to
        defaultVehicleFiltering(
            "marketValue.greaterThanOrEqual=" + DEFAULT_MARKET_VALUE,
            "marketValue.greaterThanOrEqual=" + UPDATED_MARKET_VALUE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue is less than or equal to
        defaultVehicleFiltering(
            "marketValue.lessThanOrEqual=" + DEFAULT_MARKET_VALUE,
            "marketValue.lessThanOrEqual=" + SMALLER_MARKET_VALUE
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue is less than
        defaultVehicleFiltering("marketValue.lessThan=" + UPDATED_MARKET_VALUE, "marketValue.lessThan=" + DEFAULT_MARKET_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMarketValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where marketValue is greater than
        defaultVehicleFiltering("marketValue.greaterThan=" + SMALLER_MARKET_VALUE, "marketValue.greaterThan=" + DEFAULT_MARKET_VALUE);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand equals to
        defaultVehicleFiltering("brand.equals=" + DEFAULT_BRAND, "brand.equals=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand in
        defaultVehicleFiltering("brand.in=" + DEFAULT_BRAND + "," + UPDATED_BRAND, "brand.in=" + UPDATED_BRAND);
    }

    @Test
    @Transactional
    void getAllVehiclesByBrandIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where brand is not null
        defaultVehicleFiltering("brand.specified=true", "brand.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model equals to
        defaultVehicleFiltering("model.equals=" + DEFAULT_MODEL, "model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model in
        defaultVehicleFiltering("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL, "model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model is not null
        defaultVehicleFiltering("model.specified=true", "model.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByModelContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model contains
        defaultVehicleFiltering("model.contains=" + DEFAULT_MODEL, "model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where model does not contain
        defaultVehicleFiltering("model.doesNotContain=" + UPDATED_MODEL, "model.doesNotContain=" + DEFAULT_MODEL);
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower equals to
        defaultVehicleFiltering("fiscalPower.equals=" + DEFAULT_FISCAL_POWER, "fiscalPower.equals=" + UPDATED_FISCAL_POWER);
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower in
        defaultVehicleFiltering(
            "fiscalPower.in=" + DEFAULT_FISCAL_POWER + "," + UPDATED_FISCAL_POWER,
            "fiscalPower.in=" + UPDATED_FISCAL_POWER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower is not null
        defaultVehicleFiltering("fiscalPower.specified=true", "fiscalPower.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower is greater than or equal to
        defaultVehicleFiltering(
            "fiscalPower.greaterThanOrEqual=" + DEFAULT_FISCAL_POWER,
            "fiscalPower.greaterThanOrEqual=" + UPDATED_FISCAL_POWER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower is less than or equal to
        defaultVehicleFiltering(
            "fiscalPower.lessThanOrEqual=" + DEFAULT_FISCAL_POWER,
            "fiscalPower.lessThanOrEqual=" + SMALLER_FISCAL_POWER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower is less than
        defaultVehicleFiltering("fiscalPower.lessThan=" + UPDATED_FISCAL_POWER, "fiscalPower.lessThan=" + DEFAULT_FISCAL_POWER);
    }

    @Test
    @Transactional
    void getAllVehiclesByFiscalPowerIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where fiscalPower is greater than
        defaultVehicleFiltering("fiscalPower.greaterThan=" + SMALLER_FISCAL_POWER, "fiscalPower.greaterThan=" + DEFAULT_FISCAL_POWER);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassisNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassisNumber equals to
        defaultVehicleFiltering("chassisNumber.equals=" + DEFAULT_CHASSIS_NUMBER, "chassisNumber.equals=" + UPDATED_CHASSIS_NUMBER);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassisNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassisNumber in
        defaultVehicleFiltering(
            "chassisNumber.in=" + DEFAULT_CHASSIS_NUMBER + "," + UPDATED_CHASSIS_NUMBER,
            "chassisNumber.in=" + UPDATED_CHASSIS_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByChassisNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassisNumber is not null
        defaultVehicleFiltering("chassisNumber.specified=true", "chassisNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByChassisNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassisNumber contains
        defaultVehicleFiltering("chassisNumber.contains=" + DEFAULT_CHASSIS_NUMBER, "chassisNumber.contains=" + UPDATED_CHASSIS_NUMBER);
    }

    @Test
    @Transactional
    void getAllVehiclesByChassisNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where chassisNumber does not contain
        defaultVehicleFiltering(
            "chassisNumber.doesNotContain=" + UPDATED_CHASSIS_NUMBER,
            "chassisNumber.doesNotContain=" + DEFAULT_CHASSIS_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByEnergyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where energy equals to
        defaultVehicleFiltering("energy.equals=" + DEFAULT_ENERGY, "energy.equals=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllVehiclesByEnergyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where energy in
        defaultVehicleFiltering("energy.in=" + DEFAULT_ENERGY + "," + UPDATED_ENERGY, "energy.in=" + UPDATED_ENERGY);
    }

    @Test
    @Transactional
    void getAllVehiclesByEnergyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where energy is not null
        defaultVehicleFiltering("energy.specified=true", "energy.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByGenreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where genre equals to
        defaultVehicleFiltering("genre.equals=" + DEFAULT_GENRE, "genre.equals=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllVehiclesByGenreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where genre in
        defaultVehicleFiltering("genre.in=" + DEFAULT_GENRE + "," + UPDATED_GENRE, "genre.in=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllVehiclesByGenreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where genre is not null
        defaultVehicleFiltering("genre.specified=true", "genre.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByGenreContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where genre contains
        defaultVehicleFiltering("genre.contains=" + DEFAULT_GENRE, "genre.contains=" + UPDATED_GENRE);
    }

    @Test
    @Transactional
    void getAllVehiclesByGenreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where genre does not contain
        defaultVehicleFiltering("genre.doesNotContain=" + UPDATED_GENRE, "genre.doesNotContain=" + DEFAULT_GENRE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats equals to
        defaultVehicleFiltering("nbrOfSeats.equals=" + DEFAULT_NBR_OF_SEATS, "nbrOfSeats.equals=" + UPDATED_NBR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats in
        defaultVehicleFiltering(
            "nbrOfSeats.in=" + DEFAULT_NBR_OF_SEATS + "," + UPDATED_NBR_OF_SEATS,
            "nbrOfSeats.in=" + UPDATED_NBR_OF_SEATS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats is not null
        defaultVehicleFiltering("nbrOfSeats.specified=true", "nbrOfSeats.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats is greater than or equal to
        defaultVehicleFiltering(
            "nbrOfSeats.greaterThanOrEqual=" + DEFAULT_NBR_OF_SEATS,
            "nbrOfSeats.greaterThanOrEqual=" + UPDATED_NBR_OF_SEATS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats is less than or equal to
        defaultVehicleFiltering("nbrOfSeats.lessThanOrEqual=" + DEFAULT_NBR_OF_SEATS, "nbrOfSeats.lessThanOrEqual=" + SMALLER_NBR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats is less than
        defaultVehicleFiltering("nbrOfSeats.lessThan=" + UPDATED_NBR_OF_SEATS, "nbrOfSeats.lessThan=" + DEFAULT_NBR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfSeatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfSeats is greater than
        defaultVehicleFiltering("nbrOfSeats.greaterThan=" + SMALLER_NBR_OF_SEATS, "nbrOfSeats.greaterThan=" + DEFAULT_NBR_OF_SEATS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces equals to
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.equals=" + DEFAULT_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.equals=" + UPDATED_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces in
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.in=" + DEFAULT_NBR_OF_STANDING_PLACES + "," + UPDATED_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.in=" + UPDATED_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces is not null
        defaultVehicleFiltering("nbrOfStandingPlaces.specified=true", "nbrOfStandingPlaces.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces is greater than or equal to
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.greaterThanOrEqual=" + DEFAULT_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.greaterThanOrEqual=" + UPDATED_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces is less than or equal to
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.lessThanOrEqual=" + DEFAULT_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.lessThanOrEqual=" + SMALLER_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces is less than
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.lessThan=" + UPDATED_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.lessThan=" + DEFAULT_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNbrOfStandingPlacesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where nbrOfStandingPlaces is greater than
        defaultVehicleFiltering(
            "nbrOfStandingPlaces.greaterThan=" + SMALLER_NBR_OF_STANDING_PLACES,
            "nbrOfStandingPlaces.greaterThan=" + DEFAULT_NBR_OF_STANDING_PLACES
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight equals to
        defaultVehicleFiltering("emptyWeight.equals=" + DEFAULT_EMPTY_WEIGHT, "emptyWeight.equals=" + UPDATED_EMPTY_WEIGHT);
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight in
        defaultVehicleFiltering(
            "emptyWeight.in=" + DEFAULT_EMPTY_WEIGHT + "," + UPDATED_EMPTY_WEIGHT,
            "emptyWeight.in=" + UPDATED_EMPTY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight is not null
        defaultVehicleFiltering("emptyWeight.specified=true", "emptyWeight.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight is greater than or equal to
        defaultVehicleFiltering(
            "emptyWeight.greaterThanOrEqual=" + DEFAULT_EMPTY_WEIGHT,
            "emptyWeight.greaterThanOrEqual=" + UPDATED_EMPTY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight is less than or equal to
        defaultVehicleFiltering(
            "emptyWeight.lessThanOrEqual=" + DEFAULT_EMPTY_WEIGHT,
            "emptyWeight.lessThanOrEqual=" + SMALLER_EMPTY_WEIGHT
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight is less than
        defaultVehicleFiltering("emptyWeight.lessThan=" + UPDATED_EMPTY_WEIGHT, "emptyWeight.lessThan=" + DEFAULT_EMPTY_WEIGHT);
    }

    @Test
    @Transactional
    void getAllVehiclesByEmptyWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where emptyWeight is greater than
        defaultVehicleFiltering("emptyWeight.greaterThan=" + SMALLER_EMPTY_WEIGHT, "emptyWeight.greaterThan=" + DEFAULT_EMPTY_WEIGHT);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload equals to
        defaultVehicleFiltering("payload.equals=" + DEFAULT_PAYLOAD, "payload.equals=" + UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload in
        defaultVehicleFiltering("payload.in=" + DEFAULT_PAYLOAD + "," + UPDATED_PAYLOAD, "payload.in=" + UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload is not null
        defaultVehicleFiltering("payload.specified=true", "payload.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload is greater than or equal to
        defaultVehicleFiltering("payload.greaterThanOrEqual=" + DEFAULT_PAYLOAD, "payload.greaterThanOrEqual=" + UPDATED_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload is less than or equal to
        defaultVehicleFiltering("payload.lessThanOrEqual=" + DEFAULT_PAYLOAD, "payload.lessThanOrEqual=" + SMALLER_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload is less than
        defaultVehicleFiltering("payload.lessThan=" + UPDATED_PAYLOAD, "payload.lessThan=" + DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByPayloadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where payload is greater than
        defaultVehicleFiltering("payload.greaterThan=" + SMALLER_PAYLOAD, "payload.greaterThan=" + DEFAULT_PAYLOAD);
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus equals to
        defaultVehicleFiltering("bonusMalus.equals=" + DEFAULT_BONUS_MALUS, "bonusMalus.equals=" + UPDATED_BONUS_MALUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus in
        defaultVehicleFiltering("bonusMalus.in=" + DEFAULT_BONUS_MALUS + "," + UPDATED_BONUS_MALUS, "bonusMalus.in=" + UPDATED_BONUS_MALUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus is not null
        defaultVehicleFiltering("bonusMalus.specified=true", "bonusMalus.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus is greater than or equal to
        defaultVehicleFiltering(
            "bonusMalus.greaterThanOrEqual=" + DEFAULT_BONUS_MALUS,
            "bonusMalus.greaterThanOrEqual=" + UPDATED_BONUS_MALUS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus is less than or equal to
        defaultVehicleFiltering("bonusMalus.lessThanOrEqual=" + DEFAULT_BONUS_MALUS, "bonusMalus.lessThanOrEqual=" + SMALLER_BONUS_MALUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus is less than
        defaultVehicleFiltering("bonusMalus.lessThan=" + UPDATED_BONUS_MALUS, "bonusMalus.lessThan=" + DEFAULT_BONUS_MALUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByBonusMalusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where bonusMalus is greater than
        defaultVehicleFiltering("bonusMalus.greaterThan=" + SMALLER_BONUS_MALUS, "bonusMalus.greaterThan=" + DEFAULT_BONUS_MALUS);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleAgeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleAge equals to
        defaultVehicleFiltering("vehicleAge.equals=" + DEFAULT_VEHICLE_AGE, "vehicleAge.equals=" + UPDATED_VEHICLE_AGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleAgeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleAge in
        defaultVehicleFiltering("vehicleAge.in=" + DEFAULT_VEHICLE_AGE + "," + UPDATED_VEHICLE_AGE, "vehicleAge.in=" + UPDATED_VEHICLE_AGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleAgeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleAge is not null
        defaultVehicleFiltering("vehicleAge.specified=true", "vehicleAge.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleAgeContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleAge contains
        defaultVehicleFiltering("vehicleAge.contains=" + DEFAULT_VEHICLE_AGE, "vehicleAge.contains=" + UPDATED_VEHICLE_AGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByVehicleAgeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where vehicleAge does not contain
        defaultVehicleFiltering("vehicleAge.doesNotContain=" + UPDATED_VEHICLE_AGE, "vehicleAge.doesNotContain=" + DEFAULT_VEHICLE_AGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage equals to
        defaultVehicleFiltering("mileage.equals=" + DEFAULT_MILEAGE, "mileage.equals=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage in
        defaultVehicleFiltering("mileage.in=" + DEFAULT_MILEAGE + "," + UPDATED_MILEAGE, "mileage.in=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage is not null
        defaultVehicleFiltering("mileage.specified=true", "mileage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage is greater than or equal to
        defaultVehicleFiltering("mileage.greaterThanOrEqual=" + DEFAULT_MILEAGE, "mileage.greaterThanOrEqual=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage is less than or equal to
        defaultVehicleFiltering("mileage.lessThanOrEqual=" + DEFAULT_MILEAGE, "mileage.lessThanOrEqual=" + SMALLER_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage is less than
        defaultVehicleFiltering("mileage.lessThan=" + UPDATED_MILEAGE, "mileage.lessThan=" + DEFAULT_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByMileageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where mileage is greater than
        defaultVehicleFiltering("mileage.greaterThan=" + SMALLER_MILEAGE, "mileage.greaterThan=" + DEFAULT_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors equals to
        defaultVehicleFiltering("numberOfDoors.equals=" + DEFAULT_NUMBER_OF_DOORS, "numberOfDoors.equals=" + UPDATED_NUMBER_OF_DOORS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors in
        defaultVehicleFiltering(
            "numberOfDoors.in=" + DEFAULT_NUMBER_OF_DOORS + "," + UPDATED_NUMBER_OF_DOORS,
            "numberOfDoors.in=" + UPDATED_NUMBER_OF_DOORS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors is not null
        defaultVehicleFiltering("numberOfDoors.specified=true", "numberOfDoors.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors is greater than or equal to
        defaultVehicleFiltering(
            "numberOfDoors.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_DOORS,
            "numberOfDoors.greaterThanOrEqual=" + UPDATED_NUMBER_OF_DOORS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors is less than or equal to
        defaultVehicleFiltering(
            "numberOfDoors.lessThanOrEqual=" + DEFAULT_NUMBER_OF_DOORS,
            "numberOfDoors.lessThanOrEqual=" + SMALLER_NUMBER_OF_DOORS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors is less than
        defaultVehicleFiltering("numberOfDoors.lessThan=" + UPDATED_NUMBER_OF_DOORS, "numberOfDoors.lessThan=" + DEFAULT_NUMBER_OF_DOORS);
    }

    @Test
    @Transactional
    void getAllVehiclesByNumberOfDoorsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where numberOfDoors is greater than
        defaultVehicleFiltering(
            "numberOfDoors.greaterThan=" + SMALLER_NUMBER_OF_DOORS,
            "numberOfDoors.greaterThan=" + DEFAULT_NUMBER_OF_DOORS
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByGearboxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where gearbox equals to
        defaultVehicleFiltering("gearbox.equals=" + DEFAULT_GEARBOX, "gearbox.equals=" + UPDATED_GEARBOX);
    }

    @Test
    @Transactional
    void getAllVehiclesByGearboxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where gearbox in
        defaultVehicleFiltering("gearbox.in=" + DEFAULT_GEARBOX + "," + UPDATED_GEARBOX, "gearbox.in=" + UPDATED_GEARBOX);
    }

    @Test
    @Transactional
    void getAllVehiclesByGearboxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where gearbox is not null
        defaultVehicleFiltering("gearbox.specified=true", "gearbox.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color equals to
        defaultVehicleFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color in
        defaultVehicleFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color is not null
        defaultVehicleFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color contains
        defaultVehicleFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where color does not contain
        defaultVehicleFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllVehiclesByUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where usage equals to
        defaultVehicleFiltering("usage.equals=" + DEFAULT_USAGE, "usage.equals=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where usage in
        defaultVehicleFiltering("usage.in=" + DEFAULT_USAGE + "," + UPDATED_USAGE, "usage.in=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where usage is not null
        defaultVehicleFiltering("usage.specified=true", "usage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByIsNewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isNew equals to
        defaultVehicleFiltering("isNew.equals=" + DEFAULT_IS_NEW, "isNew.equals=" + UPDATED_IS_NEW);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsNewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isNew in
        defaultVehicleFiltering("isNew.in=" + DEFAULT_IS_NEW + "," + UPDATED_IS_NEW, "isNew.in=" + UPDATED_IS_NEW);
    }

    @Test
    @Transactional
    void getAllVehiclesByIsNewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where isNew is not null
        defaultVehicleFiltering("isNew.specified=true", "isNew.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGarageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGarage equals to
        defaultVehicleFiltering("hasGarage.equals=" + DEFAULT_HAS_GARAGE, "hasGarage.equals=" + UPDATED_HAS_GARAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGarageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGarage in
        defaultVehicleFiltering("hasGarage.in=" + DEFAULT_HAS_GARAGE + "," + UPDATED_HAS_GARAGE, "hasGarage.in=" + UPDATED_HAS_GARAGE);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGarageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGarage is not null
        defaultVehicleFiltering("hasGarage.specified=true", "hasGarage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasParkingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasParking equals to
        defaultVehicleFiltering("hasParking.equals=" + DEFAULT_HAS_PARKING, "hasParking.equals=" + UPDATED_HAS_PARKING);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasParkingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasParking in
        defaultVehicleFiltering("hasParking.in=" + DEFAULT_HAS_PARKING + "," + UPDATED_HAS_PARKING, "hasParking.in=" + UPDATED_HAS_PARKING);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasParkingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasParking is not null
        defaultVehicleFiltering("hasParking.specified=true", "hasParking.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAlarmSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAlarmSystem equals to
        defaultVehicleFiltering("hasAlarmSystem.equals=" + DEFAULT_HAS_ALARM_SYSTEM, "hasAlarmSystem.equals=" + UPDATED_HAS_ALARM_SYSTEM);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAlarmSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAlarmSystem in
        defaultVehicleFiltering(
            "hasAlarmSystem.in=" + DEFAULT_HAS_ALARM_SYSTEM + "," + UPDATED_HAS_ALARM_SYSTEM,
            "hasAlarmSystem.in=" + UPDATED_HAS_ALARM_SYSTEM
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAlarmSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAlarmSystem is not null
        defaultVehicleFiltering("hasAlarmSystem.specified=true", "hasAlarmSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasSeatbeltAlarmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasSeatbeltAlarm equals to
        defaultVehicleFiltering(
            "hasSeatbeltAlarm.equals=" + DEFAULT_HAS_SEATBELT_ALARM,
            "hasSeatbeltAlarm.equals=" + UPDATED_HAS_SEATBELT_ALARM
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasSeatbeltAlarmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasSeatbeltAlarm in
        defaultVehicleFiltering(
            "hasSeatbeltAlarm.in=" + DEFAULT_HAS_SEATBELT_ALARM + "," + UPDATED_HAS_SEATBELT_ALARM,
            "hasSeatbeltAlarm.in=" + UPDATED_HAS_SEATBELT_ALARM
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasSeatbeltAlarmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasSeatbeltAlarm is not null
        defaultVehicleFiltering("hasSeatbeltAlarm.specified=true", "hasSeatbeltAlarm.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearCameraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearCamera equals to
        defaultVehicleFiltering("hasRearCamera.equals=" + DEFAULT_HAS_REAR_CAMERA, "hasRearCamera.equals=" + UPDATED_HAS_REAR_CAMERA);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearCameraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearCamera in
        defaultVehicleFiltering(
            "hasRearCamera.in=" + DEFAULT_HAS_REAR_CAMERA + "," + UPDATED_HAS_REAR_CAMERA,
            "hasRearCamera.in=" + UPDATED_HAS_REAR_CAMERA
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearCameraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearCamera is not null
        defaultVehicleFiltering("hasRearCamera.specified=true", "hasRearCamera.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearRadarIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearRadar equals to
        defaultVehicleFiltering("hasRearRadar.equals=" + DEFAULT_HAS_REAR_RADAR, "hasRearRadar.equals=" + UPDATED_HAS_REAR_RADAR);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearRadarIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearRadar in
        defaultVehicleFiltering(
            "hasRearRadar.in=" + DEFAULT_HAS_REAR_RADAR + "," + UPDATED_HAS_REAR_RADAR,
            "hasRearRadar.in=" + UPDATED_HAS_REAR_RADAR
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasRearRadarIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasRearRadar is not null
        defaultVehicleFiltering("hasRearRadar.specified=true", "hasRearRadar.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAbsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAbsSystem equals to
        defaultVehicleFiltering("hasAbsSystem.equals=" + DEFAULT_HAS_ABS_SYSTEM, "hasAbsSystem.equals=" + UPDATED_HAS_ABS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAbsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAbsSystem in
        defaultVehicleFiltering(
            "hasAbsSystem.in=" + DEFAULT_HAS_ABS_SYSTEM + "," + UPDATED_HAS_ABS_SYSTEM,
            "hasAbsSystem.in=" + UPDATED_HAS_ABS_SYSTEM
        );
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAbsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAbsSystem is not null
        defaultVehicleFiltering("hasAbsSystem.specified=true", "hasAbsSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGPSIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGPS equals to
        defaultVehicleFiltering("hasGPS.equals=" + DEFAULT_HAS_GPS, "hasGPS.equals=" + UPDATED_HAS_GPS);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGPSIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGPS in
        defaultVehicleFiltering("hasGPS.in=" + DEFAULT_HAS_GPS + "," + UPDATED_HAS_GPS, "hasGPS.in=" + UPDATED_HAS_GPS);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasGPSIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasGPS is not null
        defaultVehicleFiltering("hasGPS.specified=true", "hasGPS.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAirbagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAirbag equals to
        defaultVehicleFiltering("hasAirbag.equals=" + DEFAULT_HAS_AIRBAG, "hasAirbag.equals=" + UPDATED_HAS_AIRBAG);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAirbagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAirbag in
        defaultVehicleFiltering("hasAirbag.in=" + DEFAULT_HAS_AIRBAG + "," + UPDATED_HAS_AIRBAG, "hasAirbag.in=" + UPDATED_HAS_AIRBAG);
    }

    @Test
    @Transactional
    void getAllVehiclesByHasAirbagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where hasAirbag is not null
        defaultVehicleFiltering("hasAirbag.specified=true", "hasAirbag.specified=false");
    }

    @Test
    @Transactional
    void getAllVehiclesByNavetteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where navette equals to
        defaultVehicleFiltering("navette.equals=" + DEFAULT_NAVETTE, "navette.equals=" + UPDATED_NAVETTE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNavetteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where navette in
        defaultVehicleFiltering("navette.in=" + DEFAULT_NAVETTE + "," + UPDATED_NAVETTE, "navette.in=" + UPDATED_NAVETTE);
    }

    @Test
    @Transactional
    void getAllVehiclesByNavetteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList where navette is not null
        defaultVehicleFiltering("navette.specified=true", "navette.specified=false");
    }

    private void defaultVehicleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVehicleShouldBeFound(shouldBeFound);
        defaultVehicleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleShouldBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().toString())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationType").value(hasItem(DEFAULT_REGISTRATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].firstRegistrationDate").value(hasItem(DEFAULT_FIRST_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].technicalInspectionStatus").value(hasItem(DEFAULT_TECHNICAL_INSPECTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketValue").value(hasItem(DEFAULT_MARKET_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].fiscalPower").value(hasItem(DEFAULT_FISCAL_POWER)))
            .andExpect(jsonPath("$.[*].chassisNumber").value(hasItem(DEFAULT_CHASSIS_NUMBER)))
            .andExpect(jsonPath("$.[*].energy").value(hasItem(DEFAULT_ENERGY.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].nbrOfSeats").value(hasItem(DEFAULT_NBR_OF_SEATS)))
            .andExpect(jsonPath("$.[*].nbrOfStandingPlaces").value(hasItem(DEFAULT_NBR_OF_STANDING_PLACES)))
            .andExpect(jsonPath("$.[*].emptyWeight").value(hasItem(DEFAULT_EMPTY_WEIGHT)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].bonusMalus").value(hasItem(DEFAULT_BONUS_MALUS)))
            .andExpect(jsonPath("$.[*].vehicleAge").value(hasItem(DEFAULT_VEHICLE_AGE)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].numberOfDoors").value(hasItem(DEFAULT_NUMBER_OF_DOORS.intValue())))
            .andExpect(jsonPath("$.[*].gearbox").value(hasItem(DEFAULT_GEARBOX.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE.toString())))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW)))
            .andExpect(jsonPath("$.[*].hasGarage").value(hasItem(DEFAULT_HAS_GARAGE)))
            .andExpect(jsonPath("$.[*].hasParking").value(hasItem(DEFAULT_HAS_PARKING)))
            .andExpect(jsonPath("$.[*].hasAlarmSystem").value(hasItem(DEFAULT_HAS_ALARM_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasSeatbeltAlarm").value(hasItem(DEFAULT_HAS_SEATBELT_ALARM)))
            .andExpect(jsonPath("$.[*].hasRearCamera").value(hasItem(DEFAULT_HAS_REAR_CAMERA)))
            .andExpect(jsonPath("$.[*].hasRearRadar").value(hasItem(DEFAULT_HAS_REAR_RADAR)))
            .andExpect(jsonPath("$.[*].hasAbsSystem").value(hasItem(DEFAULT_HAS_ABS_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasGPS").value(hasItem(DEFAULT_HAS_GPS)))
            .andExpect(jsonPath("$.[*].hasAirbag").value(hasItem(DEFAULT_HAS_AIRBAG)))
            .andExpect(jsonPath("$.[*].navette").value(hasItem(DEFAULT_NAVETTE)));

        // Check, that the count call also returns 1
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleShouldNotBeFound(String filter) throws Exception {
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicleSearchRepository.save(vehicle);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationType(UPDATED_REGISTRATION_TYPE)
            .firstRegistrationDate(UPDATED_FIRST_REGISTRATION_DATE)
            .technicalInspectionStatus(UPDATED_TECHNICAL_INSPECTION_STATUS)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .newValue(UPDATED_NEW_VALUE)
            .marketValue(UPDATED_MARKET_VALUE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .fiscalPower(UPDATED_FISCAL_POWER)
            .chassisNumber(UPDATED_CHASSIS_NUMBER)
            .energy(UPDATED_ENERGY)
            .genre(UPDATED_GENRE)
            .nbrOfSeats(UPDATED_NBR_OF_SEATS)
            .nbrOfStandingPlaces(UPDATED_NBR_OF_STANDING_PLACES)
            .emptyWeight(UPDATED_EMPTY_WEIGHT)
            .payload(UPDATED_PAYLOAD)
            .bonusMalus(UPDATED_BONUS_MALUS)
            .vehicleAge(UPDATED_VEHICLE_AGE)
            .mileage(UPDATED_MILEAGE)
            .numberOfDoors(UPDATED_NUMBER_OF_DOORS)
            .gearbox(UPDATED_GEARBOX)
            .color(UPDATED_COLOR)
            .usage(UPDATED_USAGE)
            .isNew(UPDATED_IS_NEW)
            .hasGarage(UPDATED_HAS_GARAGE)
            .hasParking(UPDATED_HAS_PARKING)
            .hasAlarmSystem(UPDATED_HAS_ALARM_SYSTEM)
            .hasSeatbeltAlarm(UPDATED_HAS_SEATBELT_ALARM)
            .hasRearCamera(UPDATED_HAS_REAR_CAMERA)
            .hasRearRadar(UPDATED_HAS_REAR_RADAR)
            .hasAbsSystem(UPDATED_HAS_ABS_SYSTEM)
            .hasGPS(UPDATED_HAS_GPS)
            .hasAirbag(UPDATED_HAS_AIRBAG)
            .navette(UPDATED_NAVETTE);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(updatedVehicle);

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleToMatchAllProperties(updatedVehicle);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Vehicle> vehicleSearchList = Streamable.of(vehicleSearchRepository.findAll()).toList();
                Vehicle testVehicleSearch = vehicleSearchList.get(searchDatabaseSizeAfter - 1);

                assertVehicleAllPropertiesEquals(testVehicleSearch, updatedVehicle);
            });
    }

    @Test
    @Transactional
    void putNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .registrationType(UPDATED_REGISTRATION_TYPE)
            .firstRegistrationDate(UPDATED_FIRST_REGISTRATION_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .newValue(UPDATED_NEW_VALUE)
            .marketValue(UPDATED_MARKET_VALUE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .fiscalPower(UPDATED_FISCAL_POWER)
            .chassisNumber(UPDATED_CHASSIS_NUMBER)
            .energy(UPDATED_ENERGY)
            .genre(UPDATED_GENRE)
            .nbrOfStandingPlaces(UPDATED_NBR_OF_STANDING_PLACES)
            .vehicleAge(UPDATED_VEHICLE_AGE)
            .gearbox(UPDATED_GEARBOX)
            .color(UPDATED_COLOR)
            .usage(UPDATED_USAGE)
            .isNew(UPDATED_IS_NEW)
            .hasGarage(UPDATED_HAS_GARAGE)
            .hasParking(UPDATED_HAS_PARKING)
            .navette(UPDATED_NAVETTE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVehicle, vehicle), getPersistedVehicle(vehicle));
    }

    @Test
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .registrationNumber(UPDATED_REGISTRATION_NUMBER)
            .registrationType(UPDATED_REGISTRATION_TYPE)
            .firstRegistrationDate(UPDATED_FIRST_REGISTRATION_DATE)
            .technicalInspectionStatus(UPDATED_TECHNICAL_INSPECTION_STATUS)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .newValue(UPDATED_NEW_VALUE)
            .marketValue(UPDATED_MARKET_VALUE)
            .brand(UPDATED_BRAND)
            .model(UPDATED_MODEL)
            .fiscalPower(UPDATED_FISCAL_POWER)
            .chassisNumber(UPDATED_CHASSIS_NUMBER)
            .energy(UPDATED_ENERGY)
            .genre(UPDATED_GENRE)
            .nbrOfSeats(UPDATED_NBR_OF_SEATS)
            .nbrOfStandingPlaces(UPDATED_NBR_OF_STANDING_PLACES)
            .emptyWeight(UPDATED_EMPTY_WEIGHT)
            .payload(UPDATED_PAYLOAD)
            .bonusMalus(UPDATED_BONUS_MALUS)
            .vehicleAge(UPDATED_VEHICLE_AGE)
            .mileage(UPDATED_MILEAGE)
            .numberOfDoors(UPDATED_NUMBER_OF_DOORS)
            .gearbox(UPDATED_GEARBOX)
            .color(UPDATED_COLOR)
            .usage(UPDATED_USAGE)
            .isNew(UPDATED_IS_NEW)
            .hasGarage(UPDATED_HAS_GARAGE)
            .hasParking(UPDATED_HAS_PARKING)
            .hasAlarmSystem(UPDATED_HAS_ALARM_SYSTEM)
            .hasSeatbeltAlarm(UPDATED_HAS_SEATBELT_ALARM)
            .hasRearCamera(UPDATED_HAS_REAR_CAMERA)
            .hasRearRadar(UPDATED_HAS_REAR_RADAR)
            .hasAbsSystem(UPDATED_HAS_ABS_SYSTEM)
            .hasGPS(UPDATED_HAS_GPS)
            .hasAirbag(UPDATED_HAS_AIRBAG)
            .navette(UPDATED_NAVETTE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(partialUpdatedVehicle, getPersistedVehicle(partialUpdatedVehicle));
    }

    @Test
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        vehicle.setId(UUID.randomUUID());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);
        vehicleRepository.save(vehicle);
        vehicleSearchRepository.save(vehicle);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId().toString()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(vehicleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);
        vehicleSearchRepository.save(vehicle);

        // Search the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().toString())))
            .andExpect(jsonPath("$.[*].registrationNumber").value(hasItem(DEFAULT_REGISTRATION_NUMBER)))
            .andExpect(jsonPath("$.[*].registrationType").value(hasItem(DEFAULT_REGISTRATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].firstRegistrationDate").value(hasItem(DEFAULT_FIRST_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].technicalInspectionStatus").value(hasItem(DEFAULT_TECHNICAL_INSPECTION_STATUS.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].newValue").value(hasItem(DEFAULT_NEW_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].marketValue").value(hasItem(DEFAULT_MARKET_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].fiscalPower").value(hasItem(DEFAULT_FISCAL_POWER)))
            .andExpect(jsonPath("$.[*].chassisNumber").value(hasItem(DEFAULT_CHASSIS_NUMBER)))
            .andExpect(jsonPath("$.[*].energy").value(hasItem(DEFAULT_ENERGY.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].nbrOfSeats").value(hasItem(DEFAULT_NBR_OF_SEATS)))
            .andExpect(jsonPath("$.[*].nbrOfStandingPlaces").value(hasItem(DEFAULT_NBR_OF_STANDING_PLACES)))
            .andExpect(jsonPath("$.[*].emptyWeight").value(hasItem(DEFAULT_EMPTY_WEIGHT)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].bonusMalus").value(hasItem(DEFAULT_BONUS_MALUS)))
            .andExpect(jsonPath("$.[*].vehicleAge").value(hasItem(DEFAULT_VEHICLE_AGE)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].numberOfDoors").value(hasItem(DEFAULT_NUMBER_OF_DOORS.intValue())))
            .andExpect(jsonPath("$.[*].gearbox").value(hasItem(DEFAULT_GEARBOX.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE.toString())))
            .andExpect(jsonPath("$.[*].isNew").value(hasItem(DEFAULT_IS_NEW)))
            .andExpect(jsonPath("$.[*].hasGarage").value(hasItem(DEFAULT_HAS_GARAGE)))
            .andExpect(jsonPath("$.[*].hasParking").value(hasItem(DEFAULT_HAS_PARKING)))
            .andExpect(jsonPath("$.[*].hasAlarmSystem").value(hasItem(DEFAULT_HAS_ALARM_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasSeatbeltAlarm").value(hasItem(DEFAULT_HAS_SEATBELT_ALARM)))
            .andExpect(jsonPath("$.[*].hasRearCamera").value(hasItem(DEFAULT_HAS_REAR_CAMERA)))
            .andExpect(jsonPath("$.[*].hasRearRadar").value(hasItem(DEFAULT_HAS_REAR_RADAR)))
            .andExpect(jsonPath("$.[*].hasAbsSystem").value(hasItem(DEFAULT_HAS_ABS_SYSTEM)))
            .andExpect(jsonPath("$.[*].hasGPS").value(hasItem(DEFAULT_HAS_GPS)))
            .andExpect(jsonPath("$.[*].hasAirbag").value(hasItem(DEFAULT_HAS_AIRBAG)))
            .andExpect(jsonPath("$.[*].navette").value(hasItem(DEFAULT_NAVETTE)));
    }

    protected long getRepositoryCount() {
        return vehicleRepository.count();
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

    protected Vehicle getPersistedVehicle(Vehicle vehicle) {
        return vehicleRepository.findById(vehicle.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleToMatchAllProperties(Vehicle expectedVehicle) {
        assertVehicleAllPropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }

    protected void assertPersistedVehicleToMatchUpdatableProperties(Vehicle expectedVehicle) {
        assertVehicleAllUpdatablePropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }
}
