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
import org.closure.laser.domain.AccountProvider;
import org.closure.laser.repository.AccountProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AccountProviderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountProviderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-providers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountProviderRepository accountProviderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountProviderMockMvc;

    private AccountProvider accountProvider;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountProvider createEntity(EntityManager em) {
        AccountProvider accountProvider = new AccountProvider().name(DEFAULT_NAME);
        return accountProvider;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountProvider createUpdatedEntity(EntityManager em) {
        AccountProvider accountProvider = new AccountProvider().name(UPDATED_NAME);
        return accountProvider;
    }

    @BeforeEach
    public void initTest() {
        accountProvider = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountProvider() throws Exception {
        int databaseSizeBeforeCreate = accountProviderRepository.findAll().size();
        // Create the AccountProvider
        restAccountProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isCreated());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeCreate + 1);
        AccountProvider testAccountProvider = accountProviderList.get(accountProviderList.size() - 1);
        assertThat(testAccountProvider.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createAccountProviderWithExistingId() throws Exception {
        // Create the AccountProvider with an existing ID
        accountProvider.setId(1L);

        int databaseSizeBeforeCreate = accountProviderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountProviderMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccountProviders() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        // Get all the accountProviderList
        restAccountProviderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountProvider.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getAccountProvider() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        // Get the accountProvider
        restAccountProviderMockMvc
            .perform(get(ENTITY_API_URL_ID, accountProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountProvider.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingAccountProvider() throws Exception {
        // Get the accountProvider
        restAccountProviderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAccountProvider() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();

        // Update the accountProvider
        AccountProvider updatedAccountProvider = accountProviderRepository.findById(accountProvider.getId()).get();
        // Disconnect from session so that the updates on updatedAccountProvider are not directly saved in db
        em.detach(updatedAccountProvider);
        updatedAccountProvider.name(UPDATED_NAME);

        restAccountProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountProvider))
            )
            .andExpect(status().isOk());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
        AccountProvider testAccountProvider = accountProviderList.get(accountProviderList.size() - 1);
        assertThat(testAccountProvider.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountProvider.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountProviderWithPatch() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();

        // Update the accountProvider using partial update
        AccountProvider partialUpdatedAccountProvider = new AccountProvider();
        partialUpdatedAccountProvider.setId(accountProvider.getId());

        restAccountProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountProvider))
            )
            .andExpect(status().isOk());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
        AccountProvider testAccountProvider = accountProviderList.get(accountProviderList.size() - 1);
        assertThat(testAccountProvider.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateAccountProviderWithPatch() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();

        // Update the accountProvider using partial update
        AccountProvider partialUpdatedAccountProvider = new AccountProvider();
        partialUpdatedAccountProvider.setId(accountProvider.getId());

        partialUpdatedAccountProvider.name(UPDATED_NAME);

        restAccountProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountProvider))
            )
            .andExpect(status().isOk());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
        AccountProvider testAccountProvider = accountProviderList.get(accountProviderList.size() - 1);
        assertThat(testAccountProvider.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountProvider.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountProvider() throws Exception {
        int databaseSizeBeforeUpdate = accountProviderRepository.findAll().size();
        accountProvider.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountProviderMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountProvider))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountProvider in the database
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountProvider() throws Exception {
        // Initialize the database
        accountProviderRepository.saveAndFlush(accountProvider);

        int databaseSizeBeforeDelete = accountProviderRepository.findAll().size();

        // Delete the accountProvider
        restAccountProviderMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountProvider.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountProvider> accountProviderList = accountProviderRepository.findAll();
        assertThat(accountProviderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
