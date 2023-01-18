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
import org.closure.laser.domain.Constants;
import org.closure.laser.repository.ConstantsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConstantsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConstantsResourceIT {

    private static final Double DEFAULT_WEIGHT_FACTOR = 1D;
    private static final Double UPDATED_WEIGHT_FACTOR = 2D;

    private static final Double DEFAULT_MAX_WEIGHT = 1D;
    private static final Double UPDATED_MAX_WEIGHT = 2D;

    private static final String ENTITY_API_URL = "/api/constants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConstantsRepository constantsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConstantsMockMvc;

    private Constants constants;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Constants createEntity(EntityManager em) {
        Constants constants = new Constants().weightFactor(DEFAULT_WEIGHT_FACTOR).maxWeight(DEFAULT_MAX_WEIGHT);
        return constants;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Constants createUpdatedEntity(EntityManager em) {
        Constants constants = new Constants().weightFactor(UPDATED_WEIGHT_FACTOR).maxWeight(UPDATED_MAX_WEIGHT);
        return constants;
    }

    @BeforeEach
    public void initTest() {
        constants = createEntity(em);
    }

    @Test
    @Transactional
    void createConstants() throws Exception {
        int databaseSizeBeforeCreate = constantsRepository.findAll().size();
        // Create the Constants
        restConstantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constants)))
            .andExpect(status().isCreated());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeCreate + 1);
        Constants testConstants = constantsList.get(constantsList.size() - 1);
        assertThat(testConstants.getWeightFactor()).isEqualTo(DEFAULT_WEIGHT_FACTOR);
        assertThat(testConstants.getMaxWeight()).isEqualTo(DEFAULT_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void createConstantsWithExistingId() throws Exception {
        // Create the Constants with an existing ID
        constants.setId(1L);

        int databaseSizeBeforeCreate = constantsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConstantsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constants)))
            .andExpect(status().isBadRequest());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConstants() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        // Get all the constantsList
        restConstantsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(constants.getId().intValue())))
            .andExpect(jsonPath("$.[*].weightFactor").value(hasItem(DEFAULT_WEIGHT_FACTOR.doubleValue())))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getConstants() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        // Get the constants
        restConstantsMockMvc
            .perform(get(ENTITY_API_URL_ID, constants.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(constants.getId().intValue()))
            .andExpect(jsonPath("$.weightFactor").value(DEFAULT_WEIGHT_FACTOR.doubleValue()))
            .andExpect(jsonPath("$.maxWeight").value(DEFAULT_MAX_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingConstants() throws Exception {
        // Get the constants
        restConstantsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConstants() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();

        // Update the constants
        Constants updatedConstants = constantsRepository.findById(constants.getId()).get();
        // Disconnect from session so that the updates on updatedConstants are not directly saved in db
        em.detach(updatedConstants);
        updatedConstants.weightFactor(UPDATED_WEIGHT_FACTOR).maxWeight(UPDATED_MAX_WEIGHT);

        restConstantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConstants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConstants))
            )
            .andExpect(status().isOk());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
        Constants testConstants = constantsList.get(constantsList.size() - 1);
        assertThat(testConstants.getWeightFactor()).isEqualTo(UPDATED_WEIGHT_FACTOR);
        assertThat(testConstants.getMaxWeight()).isEqualTo(UPDATED_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, constants.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(constants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(constants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(constants)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConstantsWithPatch() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();

        // Update the constants using partial update
        Constants partialUpdatedConstants = new Constants();
        partialUpdatedConstants.setId(constants.getId());

        restConstantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConstants))
            )
            .andExpect(status().isOk());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
        Constants testConstants = constantsList.get(constantsList.size() - 1);
        assertThat(testConstants.getWeightFactor()).isEqualTo(DEFAULT_WEIGHT_FACTOR);
        assertThat(testConstants.getMaxWeight()).isEqualTo(DEFAULT_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateConstantsWithPatch() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();

        // Update the constants using partial update
        Constants partialUpdatedConstants = new Constants();
        partialUpdatedConstants.setId(constants.getId());

        partialUpdatedConstants.weightFactor(UPDATED_WEIGHT_FACTOR).maxWeight(UPDATED_MAX_WEIGHT);

        restConstantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConstants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConstants))
            )
            .andExpect(status().isOk());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
        Constants testConstants = constantsList.get(constantsList.size() - 1);
        assertThat(testConstants.getWeightFactor()).isEqualTo(UPDATED_WEIGHT_FACTOR);
        assertThat(testConstants.getMaxWeight()).isEqualTo(UPDATED_MAX_WEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, constants.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(constants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(constants))
            )
            .andExpect(status().isBadRequest());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConstants() throws Exception {
        int databaseSizeBeforeUpdate = constantsRepository.findAll().size();
        constants.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConstantsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(constants))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Constants in the database
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConstants() throws Exception {
        // Initialize the database
        constantsRepository.saveAndFlush(constants);

        int databaseSizeBeforeDelete = constantsRepository.findAll().size();

        // Delete the constants
        restConstantsMockMvc
            .perform(delete(ENTITY_API_URL_ID, constants.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Constants> constantsList = constantsRepository.findAll();
        assertThat(constantsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
