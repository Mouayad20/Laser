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
import org.closure.laser.domain.ShipmentType;
import org.closure.laser.repository.ShipmentTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShipmentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_FACTOR = 1D;
    private static final Double UPDATED_FACTOR = 2D;

    private static final String ENTITY_API_URL = "/api/shipment-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShipmentTypeRepository shipmentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentTypeMockMvc;

    private ShipmentType shipmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentType createEntity(EntityManager em) {
        ShipmentType shipmentType = new ShipmentType().name(DEFAULT_NAME).factor(DEFAULT_FACTOR);
        return shipmentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentType createUpdatedEntity(EntityManager em) {
        ShipmentType shipmentType = new ShipmentType().name(UPDATED_NAME).factor(UPDATED_FACTOR);
        return shipmentType;
    }

    @BeforeEach
    public void initTest() {
        shipmentType = createEntity(em);
    }

    @Test
    @Transactional
    void createShipmentType() throws Exception {
        int databaseSizeBeforeCreate = shipmentTypeRepository.findAll().size();
        // Create the ShipmentType
        restShipmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentType)))
            .andExpect(status().isCreated());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ShipmentType testShipmentType = shipmentTypeList.get(shipmentTypeList.size() - 1);
        assertThat(testShipmentType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShipmentType.getFactor()).isEqualTo(DEFAULT_FACTOR);
    }

    @Test
    @Transactional
    void createShipmentTypeWithExistingId() throws Exception {
        // Create the ShipmentType with an existing ID
        shipmentType.setId(1L);

        int databaseSizeBeforeCreate = shipmentTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentType)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllShipmentTypes() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get all the shipmentTypeList
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].factor").value(hasItem(DEFAULT_FACTOR.doubleValue())));
    }

    @Test
    @Transactional
    void getShipmentType() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        // Get the shipmentType
        restShipmentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.factor").value(DEFAULT_FACTOR.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingShipmentType() throws Exception {
        // Get the shipmentType
        restShipmentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShipmentType() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();

        // Update the shipmentType
        ShipmentType updatedShipmentType = shipmentTypeRepository.findById(shipmentType.getId()).get();
        // Disconnect from session so that the updates on updatedShipmentType are not directly saved in db
        em.detach(updatedShipmentType);
        updatedShipmentType.name(UPDATED_NAME).factor(UPDATED_FACTOR);

        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedShipmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
        ShipmentType testShipmentType = shipmentTypeList.get(shipmentTypeList.size() - 1);
        assertThat(testShipmentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShipmentType.getFactor()).isEqualTo(UPDATED_FACTOR);
    }

    @Test
    @Transactional
    void putNonExistingShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(shipmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentTypeWithPatch() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();

        // Update the shipmentType using partial update
        ShipmentType partialUpdatedShipmentType = new ShipmentType();
        partialUpdatedShipmentType.setId(shipmentType.getId());

        partialUpdatedShipmentType.name(UPDATED_NAME).factor(UPDATED_FACTOR);

        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
        ShipmentType testShipmentType = shipmentTypeList.get(shipmentTypeList.size() - 1);
        assertThat(testShipmentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShipmentType.getFactor()).isEqualTo(UPDATED_FACTOR);
    }

    @Test
    @Transactional
    void fullUpdateShipmentTypeWithPatch() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();

        // Update the shipmentType using partial update
        ShipmentType partialUpdatedShipmentType = new ShipmentType();
        partialUpdatedShipmentType.setId(shipmentType.getId());

        partialUpdatedShipmentType.name(UPDATED_NAME).factor(UPDATED_FACTOR);

        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShipmentType))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
        ShipmentType testShipmentType = shipmentTypeList.get(shipmentTypeList.size() - 1);
        assertThat(testShipmentType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShipmentType.getFactor()).isEqualTo(UPDATED_FACTOR);
    }

    @Test
    @Transactional
    void patchNonExistingShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(shipmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentType() throws Exception {
        int databaseSizeBeforeUpdate = shipmentTypeRepository.findAll().size();
        shipmentType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(shipmentType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentType in the database
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentType() throws Exception {
        // Initialize the database
        shipmentTypeRepository.saveAndFlush(shipmentType);

        int databaseSizeBeforeDelete = shipmentTypeRepository.findAll().size();

        // Delete the shipmentType
        restShipmentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShipmentType> shipmentTypeList = shipmentTypeRepository.findAll();
        assertThat(shipmentTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
