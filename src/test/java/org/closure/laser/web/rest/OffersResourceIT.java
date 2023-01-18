package org.closure.laser.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.closure.laser.IntegrationTest;
import org.closure.laser.domain.Offers;
import org.closure.laser.repository.OffersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OffersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OffersResourceIT {

    private static final Long DEFAULT_SHIPMENT_DEAL_ID = 1L;
    private static final Long UPDATED_SHIPMENT_DEAL_ID = 2L;

    private static final Long DEFAULT_TRIP_DEAL_ID = 1L;
    private static final Long UPDATED_TRIP_DEAL_ID = 2L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/offers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOffersMockMvc;

    private Offers offers;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offers createEntity(EntityManager em) {
        Offers offers = new Offers().shipmentDealId(DEFAULT_SHIPMENT_DEAL_ID).tripDealId(DEFAULT_TRIP_DEAL_ID).status(DEFAULT_STATUS);
        return offers;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offers createUpdatedEntity(EntityManager em) {
        Offers offers = new Offers().shipmentDealId(UPDATED_SHIPMENT_DEAL_ID).tripDealId(UPDATED_TRIP_DEAL_ID).status(UPDATED_STATUS);
        return offers;
    }

    @BeforeEach
    public void initTest() {
        offers = createEntity(em);
    }

    @Test
    @Transactional
    void createOffers() throws Exception {
        int databaseSizeBeforeCreate = offersRepository.findAll().size();
        // Create the Offers
        restOffersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offers)))
            .andExpect(status().isCreated());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeCreate + 1);
        Offers testOffers = offersList.get(offersList.size() - 1);
        assertThat(testOffers.getShipmentDealId()).isEqualTo(DEFAULT_SHIPMENT_DEAL_ID);
        assertThat(testOffers.getTripDealId()).isEqualTo(DEFAULT_TRIP_DEAL_ID);
        assertThat(testOffers.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createOffersWithExistingId() throws Exception {
        // Create the Offers with an existing ID
        offers.setId(1L);

        int databaseSizeBeforeCreate = offersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOffersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offers)))
            .andExpect(status().isBadRequest());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOffers() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        // Get all the offersList
        restOffersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offers.getId().intValue())))
            .andExpect(jsonPath("$.[*].shipmentDealId").value(hasItem(DEFAULT_SHIPMENT_DEAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].tripDealId").value(hasItem(DEFAULT_TRIP_DEAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getOffers() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        // Get the offers
        restOffersMockMvc
            .perform(get(ENTITY_API_URL_ID, offers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(offers.getId().intValue()))
            .andExpect(jsonPath("$.shipmentDealId").value(DEFAULT_SHIPMENT_DEAL_ID.intValue()))
            .andExpect(jsonPath("$.tripDealId").value(DEFAULT_TRIP_DEAL_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingOffers() throws Exception {
        // Get the offers
        restOffersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOffers() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        int databaseSizeBeforeUpdate = offersRepository.findAll().size();

        // Update the offers
        Offers updatedOffers = offersRepository.findById(offers.getId()).get();
        // Disconnect from session so that the updates on updatedOffers are not directly saved in db
        em.detach(updatedOffers);
        updatedOffers.shipmentDealId(UPDATED_SHIPMENT_DEAL_ID).tripDealId(UPDATED_TRIP_DEAL_ID).status(UPDATED_STATUS);

        restOffersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOffers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOffers))
            )
            .andExpect(status().isOk());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
        Offers testOffers = offersList.get(offersList.size() - 1);
        assertThat(testOffers.getShipmentDealId()).isEqualTo(UPDATED_SHIPMENT_DEAL_ID);
        assertThat(testOffers.getTripDealId()).isEqualTo(UPDATED_TRIP_DEAL_ID);
        assertThat(testOffers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, offers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(offers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(offers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOffersWithPatch() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        int databaseSizeBeforeUpdate = offersRepository.findAll().size();

        // Update the offers using partial update
        Offers partialUpdatedOffers = new Offers();
        partialUpdatedOffers.setId(offers.getId());

        partialUpdatedOffers.shipmentDealId(UPDATED_SHIPMENT_DEAL_ID).status(UPDATED_STATUS);

        restOffersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffers))
            )
            .andExpect(status().isOk());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
        Offers testOffers = offersList.get(offersList.size() - 1);
        assertThat(testOffers.getShipmentDealId()).isEqualTo(UPDATED_SHIPMENT_DEAL_ID);
        assertThat(testOffers.getTripDealId()).isEqualTo(DEFAULT_TRIP_DEAL_ID);
        assertThat(testOffers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateOffersWithPatch() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        int databaseSizeBeforeUpdate = offersRepository.findAll().size();

        // Update the offers using partial update
        Offers partialUpdatedOffers = new Offers();
        partialUpdatedOffers.setId(offers.getId());

        partialUpdatedOffers.shipmentDealId(UPDATED_SHIPMENT_DEAL_ID).tripDealId(UPDATED_TRIP_DEAL_ID).status(UPDATED_STATUS);

        restOffersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOffers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOffers))
            )
            .andExpect(status().isOk());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
        Offers testOffers = offersList.get(offersList.size() - 1);
        assertThat(testOffers.getShipmentDealId()).isEqualTo(UPDATED_SHIPMENT_DEAL_ID);
        assertThat(testOffers.getTripDealId()).isEqualTo(UPDATED_TRIP_DEAL_ID);
        assertThat(testOffers.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, offers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(offers))
            )
            .andExpect(status().isBadRequest());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOffers() throws Exception {
        int databaseSizeBeforeUpdate = offersRepository.findAll().size();
        offers.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOffersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(offers)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Offers in the database
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOffers() throws Exception {
        // Initialize the database
        offersRepository.saveAndFlush(offers);

        int databaseSizeBeforeDelete = offersRepository.findAll().size();

        // Delete the offers
        restOffersMockMvc
            .perform(delete(ENTITY_API_URL_ID, offers.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Offers> offersList = offersRepository.findAll();
        assertThat(offersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
