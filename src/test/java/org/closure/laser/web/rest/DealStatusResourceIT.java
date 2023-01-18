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
import org.closure.laser.domain.DealStatus;
import org.closure.laser.repository.DealStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DealStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deal-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DealStatusRepository dealStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealStatusMockMvc;

    private DealStatus dealStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealStatus createEntity(EntityManager em) {
        DealStatus dealStatus = new DealStatus().name(DEFAULT_NAME);
        return dealStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealStatus createUpdatedEntity(EntityManager em) {
        DealStatus dealStatus = new DealStatus().name(UPDATED_NAME);
        return dealStatus;
    }

    @BeforeEach
    public void initTest() {
        dealStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createDealStatus() throws Exception {
        int databaseSizeBeforeCreate = dealStatusRepository.findAll().size();
        // Create the DealStatus
        restDealStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealStatus)))
            .andExpect(status().isCreated());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeCreate + 1);
        DealStatus testDealStatus = dealStatusList.get(dealStatusList.size() - 1);
        assertThat(testDealStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDealStatusWithExistingId() throws Exception {
        // Create the DealStatus with an existing ID
        dealStatus.setId(1L);

        int databaseSizeBeforeCreate = dealStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealStatus)))
            .andExpect(status().isBadRequest());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDealStatuses() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        // Get all the dealStatusList
        restDealStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDealStatus() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        // Get the dealStatus
        restDealStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, dealStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingDealStatus() throws Exception {
        // Get the dealStatus
        restDealStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDealStatus() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();

        // Update the dealStatus
        DealStatus updatedDealStatus = dealStatusRepository.findById(dealStatus.getId()).get();
        // Disconnect from session so that the updates on updatedDealStatus are not directly saved in db
        em.detach(updatedDealStatus);
        updatedDealStatus.name(UPDATED_NAME);

        restDealStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDealStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDealStatus))
            )
            .andExpect(status().isOk());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
        DealStatus testDealStatus = dealStatusList.get(dealStatusList.size() - 1);
        assertThat(testDealStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealStatus)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealStatusWithPatch() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();

        // Update the dealStatus using partial update
        DealStatus partialUpdatedDealStatus = new DealStatus();
        partialUpdatedDealStatus.setId(dealStatus.getId());

        partialUpdatedDealStatus.name(UPDATED_NAME);

        restDealStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealStatus))
            )
            .andExpect(status().isOk());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
        DealStatus testDealStatus = dealStatusList.get(dealStatusList.size() - 1);
        assertThat(testDealStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDealStatusWithPatch() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();

        // Update the dealStatus using partial update
        DealStatus partialUpdatedDealStatus = new DealStatus();
        partialUpdatedDealStatus.setId(dealStatus.getId());

        partialUpdatedDealStatus.name(UPDATED_NAME);

        restDealStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDealStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDealStatus))
            )
            .andExpect(status().isOk());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
        DealStatus testDealStatus = dealStatusList.get(dealStatusList.size() - 1);
        assertThat(testDealStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dealStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dealStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDealStatus() throws Exception {
        int databaseSizeBeforeUpdate = dealStatusRepository.findAll().size();
        dealStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(dealStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DealStatus in the database
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDealStatus() throws Exception {
        // Initialize the database
        dealStatusRepository.saveAndFlush(dealStatus);

        int databaseSizeBeforeDelete = dealStatusRepository.findAll().size();

        // Delete the dealStatus
        restDealStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DealStatus> dealStatusList = dealStatusRepository.findAll();
        assertThat(dealStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
