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
import org.closure.laser.domain.Deal;
import org.closure.laser.repository.DealRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DealResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DealResourceIT {

    private static final Double DEFAULT_TOTAL_PRICE = 1D;
    private static final Double UPDATED_TOTAL_PRICE = 2D;

    private static final Boolean DEFAULT_IS_CASHED = false;
    private static final Boolean UPDATED_IS_CASHED = true;

    private static final String DEFAULT_FROM_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_FROM_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_TO_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_TO_ACCOUNT = "BBBBBBBBBB";

    private static final Double DEFAULT_FULL_WEIGHT = 1D;
    private static final Double UPDATED_FULL_WEIGHT = 2D;

    private static final Double DEFAULT_AVAILABLE_WEIGHT = 1D;
    private static final Double UPDATED_AVAILABLE_WEIGHT = 2D;

    private static final String ENTITY_API_URL = "/api/deals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealMockMvc;

    private Deal deal;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createEntity(EntityManager em) {
        Deal deal = new Deal()
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .isCashed(DEFAULT_IS_CASHED)
            .fromAccount(DEFAULT_FROM_ACCOUNT)
            .toAccount(DEFAULT_TO_ACCOUNT)
            .fullWeight(DEFAULT_FULL_WEIGHT)
            .availableWeight(DEFAULT_AVAILABLE_WEIGHT);
        return deal;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deal createUpdatedEntity(EntityManager em) {
        Deal deal = new Deal()
            .totalPrice(UPDATED_TOTAL_PRICE)
            .isCashed(UPDATED_IS_CASHED)
            .fromAccount(UPDATED_FROM_ACCOUNT)
            .toAccount(UPDATED_TO_ACCOUNT)
            .fullWeight(UPDATED_FULL_WEIGHT)
            .availableWeight(UPDATED_AVAILABLE_WEIGHT);
        return deal;
    }

    @BeforeEach
    public void initTest() {
        deal = createEntity(em);
    }

    @Test
    @Transactional
    void createDeal() throws Exception {
        int databaseSizeBeforeCreate = dealRepository.findAll().size();
        // Create the Deal
        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isCreated());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeCreate + 1);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testDeal.getIsCashed()).isEqualTo(DEFAULT_IS_CASHED);
        assertThat(testDeal.getFromAccount()).isEqualTo(DEFAULT_FROM_ACCOUNT);
        assertThat(testDeal.getToAccount()).isEqualTo(DEFAULT_TO_ACCOUNT);
        assertThat(testDeal.getFullWeight()).isEqualTo(DEFAULT_FULL_WEIGHT);
        assertThat(testDeal.getAvailableWeight()).isEqualTo(DEFAULT_AVAILABLE_WEIGHT);
    }

    @Test
    @Transactional
    void createDealWithExistingId() throws Exception {
        // Create the Deal with an existing ID
        deal.setId(1L);

        int databaseSizeBeforeCreate = dealRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDealMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDeals() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the dealList
        restDealMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deal.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].isCashed").value(hasItem(DEFAULT_IS_CASHED.booleanValue())))
            .andExpect(jsonPath("$.[*].fromAccount").value(hasItem(DEFAULT_FROM_ACCOUNT)))
            .andExpect(jsonPath("$.[*].toAccount").value(hasItem(DEFAULT_TO_ACCOUNT)))
            .andExpect(jsonPath("$.[*].fullWeight").value(hasItem(DEFAULT_FULL_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].availableWeight").value(hasItem(DEFAULT_AVAILABLE_WEIGHT.doubleValue())));
    }

    @Test
    @Transactional
    void getDeal() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get the deal
        restDealMockMvc
            .perform(get(ENTITY_API_URL_ID, deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deal.getId().intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.isCashed").value(DEFAULT_IS_CASHED.booleanValue()))
            .andExpect(jsonPath("$.fromAccount").value(DEFAULT_FROM_ACCOUNT))
            .andExpect(jsonPath("$.toAccount").value(DEFAULT_TO_ACCOUNT))
            .andExpect(jsonPath("$.fullWeight").value(DEFAULT_FULL_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.availableWeight").value(DEFAULT_AVAILABLE_WEIGHT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDeal() throws Exception {
        // Get the deal
        restDealMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeal() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Update the deal
        Deal updatedDeal = dealRepository.findById(deal.getId()).get();
        // Disconnect from session so that the updates on updatedDeal are not directly saved in db
        em.detach(updatedDeal);
        updatedDeal
            .totalPrice(UPDATED_TOTAL_PRICE)
            .isCashed(UPDATED_IS_CASHED)
            .fromAccount(UPDATED_FROM_ACCOUNT)
            .toAccount(UPDATED_TO_ACCOUNT)
            .fullWeight(UPDATED_FULL_WEIGHT)
            .availableWeight(UPDATED_AVAILABLE_WEIGHT);

        restDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testDeal.getIsCashed()).isEqualTo(UPDATED_IS_CASHED);
        assertThat(testDeal.getFromAccount()).isEqualTo(UPDATED_FROM_ACCOUNT);
        assertThat(testDeal.getToAccount()).isEqualTo(UPDATED_TO_ACCOUNT);
        assertThat(testDeal.getFullWeight()).isEqualTo(UPDATED_FULL_WEIGHT);
        assertThat(testDeal.getAvailableWeight()).isEqualTo(UPDATED_AVAILABLE_WEIGHT);
    }

    @Test
    @Transactional
    void putNonExistingDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDealWithPatch() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal.totalPrice(UPDATED_TOTAL_PRICE).isCashed(UPDATED_IS_CASHED).fromAccount(UPDATED_FROM_ACCOUNT);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testDeal.getIsCashed()).isEqualTo(UPDATED_IS_CASHED);
        assertThat(testDeal.getFromAccount()).isEqualTo(UPDATED_FROM_ACCOUNT);
        assertThat(testDeal.getToAccount()).isEqualTo(DEFAULT_TO_ACCOUNT);
        assertThat(testDeal.getFullWeight()).isEqualTo(DEFAULT_FULL_WEIGHT);
        assertThat(testDeal.getAvailableWeight()).isEqualTo(DEFAULT_AVAILABLE_WEIGHT);
    }

    @Test
    @Transactional
    void fullUpdateDealWithPatch() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        int databaseSizeBeforeUpdate = dealRepository.findAll().size();

        // Update the deal using partial update
        Deal partialUpdatedDeal = new Deal();
        partialUpdatedDeal.setId(deal.getId());

        partialUpdatedDeal
            .totalPrice(UPDATED_TOTAL_PRICE)
            .isCashed(UPDATED_IS_CASHED)
            .fromAccount(UPDATED_FROM_ACCOUNT)
            .toAccount(UPDATED_TO_ACCOUNT)
            .fullWeight(UPDATED_FULL_WEIGHT)
            .availableWeight(UPDATED_AVAILABLE_WEIGHT);

        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeal))
            )
            .andExpect(status().isOk());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testDeal.getIsCashed()).isEqualTo(UPDATED_IS_CASHED);
        assertThat(testDeal.getFromAccount()).isEqualTo(UPDATED_FROM_ACCOUNT);
        assertThat(testDeal.getToAccount()).isEqualTo(UPDATED_TO_ACCOUNT);
        assertThat(testDeal.getFullWeight()).isEqualTo(UPDATED_FULL_WEIGHT);
        assertThat(testDeal.getAvailableWeight()).isEqualTo(UPDATED_AVAILABLE_WEIGHT);
    }

    @Test
    @Transactional
    void patchNonExistingDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deal))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeal() throws Exception {
        int databaseSizeBeforeUpdate = dealRepository.findAll().size();
        deal.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDealMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(deal)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeal() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        int databaseSizeBeforeDelete = dealRepository.findAll().size();

        // Delete the deal
        restDealMockMvc
            .perform(delete(ENTITY_API_URL_ID, deal.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
