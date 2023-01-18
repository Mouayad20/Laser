package org.closure.laser.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.closure.laser.IntegrationTest;
import org.closure.laser.domain.Trip;
import org.closure.laser.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FLY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FLY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ARRIVE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARRIVE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TRIP_IDENTIFIER = "AAAAAAAAAA";
    private static final String UPDATED_TRIP_IDENTIFIER = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_TICKET_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_TICKET_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TRIP_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TRIP_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_TRANSIT = "AAAAAAAAAA";
    private static final String UPDATED_TRANSIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity(EntityManager em) {
        Trip trip = new Trip()
            .tripIdentifier(DEFAULT_TRIP_IDENTIFIER)
            .details(DEFAULT_DETAILS)
            .ticketImage(DEFAULT_TICKET_IMAGE)
            .tripType(DEFAULT_TRIP_TYPE)
            .transit(DEFAULT_TRANSIT);
        return trip;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity(EntityManager em) {
        Trip trip = new Trip()
            .tripIdentifier(UPDATED_TRIP_IDENTIFIER)
            .details(UPDATED_DETAILS)
            .ticketImage(UPDATED_TICKET_IMAGE)
            .tripType(UPDATED_TRIP_TYPE)
            .transit(UPDATED_TRANSIT);
        return trip;
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity(em);
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        int databaseSizeBeforeCreate = tripRepository.findAll().size();
        // Create the Trip
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isCreated());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate + 1);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testTrip.getFlyTime()).isEqualTo(DEFAULT_FLY_TIME);
        assertThat(testTrip.getArriveTime()).isEqualTo(DEFAULT_ARRIVE_TIME);
        assertThat(testTrip.getTripIdentifier()).isEqualTo(DEFAULT_TRIP_IDENTIFIER);
        assertThat(testTrip.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testTrip.getTicketImage()).isEqualTo(DEFAULT_TICKET_IMAGE);
        assertThat(testTrip.getTripType()).isEqualTo(DEFAULT_TRIP_TYPE);
        assertThat(testTrip.getTransit()).isEqualTo(DEFAULT_TRANSIT);
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);

        int databaseSizeBeforeCreate = tripRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].flyTime").value(hasItem(DEFAULT_FLY_TIME.toString())))
            .andExpect(jsonPath("$.[*].arriveTime").value(hasItem(DEFAULT_ARRIVE_TIME.toString())))
            .andExpect(jsonPath("$.[*].tripIdentifier").value(hasItem(DEFAULT_TRIP_IDENTIFIER)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].ticketImage").value(hasItem(DEFAULT_TICKET_IMAGE)))
            .andExpect(jsonPath("$.[*].tripType").value(hasItem(DEFAULT_TRIP_TYPE)))
            .andExpect(jsonPath("$.[*].transit").value(hasItem(DEFAULT_TRANSIT)));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.flyTime").value(DEFAULT_FLY_TIME.toString()))
            .andExpect(jsonPath("$.arriveTime").value(DEFAULT_ARRIVE_TIME.toString()))
            .andExpect(jsonPath("$.tripIdentifier").value(DEFAULT_TRIP_IDENTIFIER))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.ticketImage").value(DEFAULT_TICKET_IMAGE))
            .andExpect(jsonPath("$.tripType").value(DEFAULT_TRIP_TYPE))
            .andExpect(jsonPath("$.transit").value(DEFAULT_TRANSIT));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).get();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .tripIdentifier(UPDATED_TRIP_IDENTIFIER)
            .details(UPDATED_DETAILS)
            .ticketImage(UPDATED_TICKET_IMAGE)
            .tripType(UPDATED_TRIP_TYPE)
            .transit(UPDATED_TRANSIT);

        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTrip.getFlyTime()).isEqualTo(UPDATED_FLY_TIME);
        assertThat(testTrip.getArriveTime()).isEqualTo(UPDATED_ARRIVE_TIME);
        assertThat(testTrip.getTripIdentifier()).isEqualTo(UPDATED_TRIP_IDENTIFIER);
        assertThat(testTrip.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testTrip.getTicketImage()).isEqualTo(UPDATED_TICKET_IMAGE);
        assertThat(testTrip.getTripType()).isEqualTo(UPDATED_TRIP_TYPE);
        assertThat(testTrip.getTransit()).isEqualTo(UPDATED_TRANSIT);
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trip.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTrip.getFlyTime()).isEqualTo(DEFAULT_FLY_TIME);
        assertThat(testTrip.getArriveTime()).isEqualTo(DEFAULT_ARRIVE_TIME);
        assertThat(testTrip.getTripIdentifier()).isEqualTo(DEFAULT_TRIP_IDENTIFIER);
        assertThat(testTrip.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testTrip.getTicketImage()).isEqualTo(DEFAULT_TICKET_IMAGE);
        assertThat(testTrip.getTripType()).isEqualTo(DEFAULT_TRIP_TYPE);
        assertThat(testTrip.getTransit()).isEqualTo(DEFAULT_TRANSIT);
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeUpdate = tripRepository.findAll().size();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .tripIdentifier(UPDATED_TRIP_IDENTIFIER)
            .details(UPDATED_DETAILS)
            .ticketImage(UPDATED_TICKET_IMAGE)
            .tripType(UPDATED_TRIP_TYPE)
            .transit(UPDATED_TRANSIT);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
        Trip testTrip = tripList.get(tripList.size() - 1);
        assertThat(testTrip.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testTrip.getFlyTime()).isEqualTo(UPDATED_FLY_TIME);
        assertThat(testTrip.getArriveTime()).isEqualTo(UPDATED_ARRIVE_TIME);
        assertThat(testTrip.getTripIdentifier()).isEqualTo(UPDATED_TRIP_IDENTIFIER);
        assertThat(testTrip.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testTrip.getTicketImage()).isEqualTo(UPDATED_TICKET_IMAGE);
        assertThat(testTrip.getTripType()).isEqualTo(UPDATED_TRIP_TYPE);
        assertThat(testTrip.getTransit()).isEqualTo(UPDATED_TRANSIT);
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trip.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trip))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        int databaseSizeBeforeUpdate = tripRepository.findAll().size();
        trip.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trip)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        tripRepository.saveAndFlush(trip);

        int databaseSizeBeforeDelete = tripRepository.findAll().size();

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trip> tripList = tripRepository.findAll();
        assertThat(tripList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
