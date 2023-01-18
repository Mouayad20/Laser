package org.closure.laser.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.closure.laser.IntegrationTest;
import org.closure.laser.domain.UserApplication;
import org.closure.laser.repository.UserApplicationRepository;
import org.closure.laser.service.UserApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserApplicationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserApplicationResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PASSPORT = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT = "BBBBBBBBBB";

    private static final Date DEFAULT_CREATED_AT = Date.from(Instant.ofEpochMilli(0L));
    private static final Date UPDATED_CREATED_AT = Date.from(Instant.now().truncatedTo(ChronoUnit.MILLIS));

    private static final Boolean DEFAULT_IS_GOOGLE_ACCOUNT = false;
    private static final Boolean UPDATED_IS_GOOGLE_ACCOUNT = true;

    private static final Boolean DEFAULT_IS_FACEBOOK_ACCOUNT = false;
    private static final Boolean UPDATED_IS_FACEBOOK_ACCOUNT = true;

    private static final Boolean DEFAULT_IS_TWITTER_ACCOUNT = false;
    private static final Boolean UPDATED_IS_TWITTER_ACCOUNT = true;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-applications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Mock
    private UserApplicationRepository userApplicationRepositoryMock;

    @Mock
    private UserApplicationService userApplicationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserApplicationMockMvc;

    private UserApplication userApplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserApplication createEntity(EntityManager em) {
        UserApplication userApplication = new UserApplication()
            .phone(DEFAULT_PHONE)
            .passport(DEFAULT_PASSPORT)
            .createdAt(DEFAULT_CREATED_AT)
            .isGoogleAccount(DEFAULT_IS_GOOGLE_ACCOUNT)
            .isFacebookAccount(DEFAULT_IS_FACEBOOK_ACCOUNT)
            .isTwitterAccount(DEFAULT_IS_TWITTER_ACCOUNT)
            .image(DEFAULT_IMAGE);
        return userApplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserApplication createUpdatedEntity(EntityManager em) {
        UserApplication userApplication = new UserApplication()
            .phone(UPDATED_PHONE)
            .passport(UPDATED_PASSPORT)
            .createdAt(UPDATED_CREATED_AT)
            .isGoogleAccount(UPDATED_IS_GOOGLE_ACCOUNT)
            .isFacebookAccount(UPDATED_IS_FACEBOOK_ACCOUNT)
            .isTwitterAccount(UPDATED_IS_TWITTER_ACCOUNT)
            .image(UPDATED_IMAGE);
        return userApplication;
    }

    @BeforeEach
    public void initTest() {
        userApplication = createEntity(em);
    }

    @Test
    @Transactional
    void createUserApplication() throws Exception {
        int databaseSizeBeforeCreate = userApplicationRepository.findAll().size();
        // Create the UserApplication
        restUserApplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isCreated());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeCreate + 1);
        UserApplication testUserApplication = userApplicationList.get(userApplicationList.size() - 1);
        assertThat(testUserApplication.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserApplication.getPassport()).isEqualTo(DEFAULT_PASSPORT);
        assertThat(testUserApplication.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserApplication.getIsGoogleAccount()).isEqualTo(DEFAULT_IS_GOOGLE_ACCOUNT);
        assertThat(testUserApplication.getIsFacebookAccount()).isEqualTo(DEFAULT_IS_FACEBOOK_ACCOUNT);
        assertThat(testUserApplication.getIsTwitterAccount()).isEqualTo(DEFAULT_IS_TWITTER_ACCOUNT);
        assertThat(testUserApplication.getImage()).isEqualTo(DEFAULT_IMAGE);
    }

    @Test
    @Transactional
    void createUserApplicationWithExistingId() throws Exception {
        // Create the UserApplication with an existing ID
        userApplication.setId(1L);

        int databaseSizeBeforeCreate = userApplicationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserApplicationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserApplications() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        // Get all the userApplicationList
        restUserApplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userApplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].passport").value(hasItem(DEFAULT_PASSPORT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].isGoogleAccount").value(hasItem(DEFAULT_IS_GOOGLE_ACCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].isFacebookAccount").value(hasItem(DEFAULT_IS_FACEBOOK_ACCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].isTwitterAccount").value(hasItem(DEFAULT_IS_TWITTER_ACCOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserApplicationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserApplicationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userApplicationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserApplicationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userApplicationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUserApplication() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        // Get the userApplication
        restUserApplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, userApplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userApplication.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.passport").value(DEFAULT_PASSPORT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.isGoogleAccount").value(DEFAULT_IS_GOOGLE_ACCOUNT.booleanValue()))
            .andExpect(jsonPath("$.isFacebookAccount").value(DEFAULT_IS_FACEBOOK_ACCOUNT.booleanValue()))
            .andExpect(jsonPath("$.isTwitterAccount").value(DEFAULT_IS_TWITTER_ACCOUNT.booleanValue()))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingUserApplication() throws Exception {
        // Get the userApplication
        restUserApplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserApplication() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();

        // Update the userApplication
        UserApplication updatedUserApplication = userApplicationRepository.findById(userApplication.getId()).get();
        // Disconnect from session so that the updates on updatedUserApplication are not
        // directly saved in db
        em.detach(updatedUserApplication);
        updatedUserApplication
            .phone(UPDATED_PHONE)
            .passport(UPDATED_PASSPORT)
            .createdAt(UPDATED_CREATED_AT)
            .isGoogleAccount(UPDATED_IS_GOOGLE_ACCOUNT)
            .isFacebookAccount(UPDATED_IS_FACEBOOK_ACCOUNT)
            .isTwitterAccount(UPDATED_IS_TWITTER_ACCOUNT)
            .image(UPDATED_IMAGE);

        restUserApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserApplication))
            )
            .andExpect(status().isOk());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
        UserApplication testUserApplication = userApplicationList.get(userApplicationList.size() - 1);
        assertThat(testUserApplication.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserApplication.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testUserApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserApplication.getIsGoogleAccount()).isEqualTo(UPDATED_IS_GOOGLE_ACCOUNT);
        assertThat(testUserApplication.getIsFacebookAccount()).isEqualTo(UPDATED_IS_FACEBOOK_ACCOUNT);
        assertThat(testUserApplication.getIsTwitterAccount()).isEqualTo(UPDATED_IS_TWITTER_ACCOUNT);
        assertThat(testUserApplication.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userApplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserApplicationWithPatch() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();

        // Update the userApplication using partial update
        UserApplication partialUpdatedUserApplication = new UserApplication();
        partialUpdatedUserApplication.setId(userApplication.getId());

        partialUpdatedUserApplication.isFacebookAccount(UPDATED_IS_FACEBOOK_ACCOUNT).image(UPDATED_IMAGE);

        restUserApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserApplication))
            )
            .andExpect(status().isOk());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
        UserApplication testUserApplication = userApplicationList.get(userApplicationList.size() - 1);
        assertThat(testUserApplication.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testUserApplication.getPassport()).isEqualTo(DEFAULT_PASSPORT);
        assertThat(testUserApplication.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testUserApplication.getIsGoogleAccount()).isEqualTo(DEFAULT_IS_GOOGLE_ACCOUNT);
        assertThat(testUserApplication.getIsFacebookAccount()).isEqualTo(UPDATED_IS_FACEBOOK_ACCOUNT);
        assertThat(testUserApplication.getIsTwitterAccount()).isEqualTo(DEFAULT_IS_TWITTER_ACCOUNT);
        assertThat(testUserApplication.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateUserApplicationWithPatch() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();

        // Update the userApplication using partial update
        UserApplication partialUpdatedUserApplication = new UserApplication();
        partialUpdatedUserApplication.setId(userApplication.getId());

        partialUpdatedUserApplication
            .phone(UPDATED_PHONE)
            .passport(UPDATED_PASSPORT)
            .createdAt(UPDATED_CREATED_AT)
            .isGoogleAccount(UPDATED_IS_GOOGLE_ACCOUNT)
            .isFacebookAccount(UPDATED_IS_FACEBOOK_ACCOUNT)
            .isTwitterAccount(UPDATED_IS_TWITTER_ACCOUNT)
            .image(UPDATED_IMAGE);

        restUserApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserApplication))
            )
            .andExpect(status().isOk());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
        UserApplication testUserApplication = userApplicationList.get(userApplicationList.size() - 1);
        assertThat(testUserApplication.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testUserApplication.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testUserApplication.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testUserApplication.getIsGoogleAccount()).isEqualTo(UPDATED_IS_GOOGLE_ACCOUNT);
        assertThat(testUserApplication.getIsFacebookAccount()).isEqualTo(UPDATED_IS_FACEBOOK_ACCOUNT);
        assertThat(testUserApplication.getIsTwitterAccount()).isEqualTo(UPDATED_IS_TWITTER_ACCOUNT);
        assertThat(testUserApplication.getImage()).isEqualTo(UPDATED_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userApplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserApplication() throws Exception {
        int databaseSizeBeforeUpdate = userApplicationRepository.findAll().size();
        userApplication.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserApplicationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userApplication))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserApplication in the database
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserApplication() throws Exception {
        // Initialize the database
        userApplicationRepository.saveAndFlush(userApplication);

        int databaseSizeBeforeDelete = userApplicationRepository.findAll().size();

        // Delete the userApplication
        restUserApplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userApplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserApplication> userApplicationList = userApplicationRepository.findAll();
        assertThat(userApplicationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
