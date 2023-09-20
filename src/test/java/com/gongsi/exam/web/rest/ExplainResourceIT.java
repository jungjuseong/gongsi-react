package com.gongsi.exam.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gongsi.exam.IntegrationTest;
import com.gongsi.exam.domain.Explain;
import com.gongsi.exam.domain.enumeration.AnswerType;
import com.gongsi.exam.repository.ExplainRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ExplainResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ExplainResourceIT {

    private static final AnswerType DEFAULT_ANSWER = AnswerType.Q1;
    private static final AnswerType UPDATED_ANSWER = AnswerType.Q2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/explains";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExplainRepository explainRepository;

    @Mock
    private ExplainRepository explainRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExplainMockMvc;

    private Explain explain;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Explain createEntity(EntityManager em) {
        Explain explain = new Explain().answer(DEFAULT_ANSWER).description(DEFAULT_DESCRIPTION);
        return explain;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Explain createUpdatedEntity(EntityManager em) {
        Explain explain = new Explain().answer(UPDATED_ANSWER).description(UPDATED_DESCRIPTION);
        return explain;
    }

    @BeforeEach
    public void initTest() {
        explain = createEntity(em);
    }

    @Test
    @Transactional
    void createExplain() throws Exception {
        int databaseSizeBeforeCreate = explainRepository.findAll().size();
        // Create the Explain
        restExplainMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isCreated());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeCreate + 1);
        Explain testExplain = explainList.get(explainList.size() - 1);
        assertThat(testExplain.getAnswer()).isEqualTo(DEFAULT_ANSWER);
        assertThat(testExplain.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createExplainWithExistingId() throws Exception {
        // Create the Explain with an existing ID
        explain.setId(1L);

        int databaseSizeBeforeCreate = explainRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExplainMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnswerIsRequired() throws Exception {
        int databaseSizeBeforeTest = explainRepository.findAll().size();
        // set the field null
        explain.setAnswer(null);

        // Create the Explain, which fails.

        restExplainMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExplains() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        // Get all the explainList
        restExplainMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(explain.getId().intValue())))
            .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExplainsWithEagerRelationshipsIsEnabled() throws Exception {
        when(explainRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExplainMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(explainRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllExplainsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(explainRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restExplainMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(explainRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getExplain() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        // Get the explain
        restExplainMockMvc
            .perform(get(ENTITY_API_URL_ID, explain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(explain.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExplain() throws Exception {
        // Get the explain
        restExplainMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExplain() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        int databaseSizeBeforeUpdate = explainRepository.findAll().size();

        // Update the explain
        Explain updatedExplain = explainRepository.findById(explain.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExplain are not directly saved in db
        em.detach(updatedExplain);
        updatedExplain.answer(UPDATED_ANSWER).description(UPDATED_DESCRIPTION);

        restExplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExplain.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedExplain))
            )
            .andExpect(status().isOk());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
        Explain testExplain = explainList.get(explainList.size() - 1);
        assertThat(testExplain.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExplain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, explain.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExplainWithPatch() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        int databaseSizeBeforeUpdate = explainRepository.findAll().size();

        // Update the explain using partial update
        Explain partialUpdatedExplain = new Explain();
        partialUpdatedExplain.setId(explain.getId());

        partialUpdatedExplain.answer(UPDATED_ANSWER).description(UPDATED_DESCRIPTION);

        restExplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExplain.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExplain))
            )
            .andExpect(status().isOk());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
        Explain testExplain = explainList.get(explainList.size() - 1);
        assertThat(testExplain.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExplain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateExplainWithPatch() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        int databaseSizeBeforeUpdate = explainRepository.findAll().size();

        // Update the explain using partial update
        Explain partialUpdatedExplain = new Explain();
        partialUpdatedExplain.setId(explain.getId());

        partialUpdatedExplain.answer(UPDATED_ANSWER).description(UPDATED_DESCRIPTION);

        restExplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExplain.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExplain))
            )
            .andExpect(status().isOk());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
        Explain testExplain = explainList.get(explainList.size() - 1);
        assertThat(testExplain.getAnswer()).isEqualTo(UPDATED_ANSWER);
        assertThat(testExplain.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, explain.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isBadRequest());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExplain() throws Exception {
        int databaseSizeBeforeUpdate = explainRepository.findAll().size();
        explain.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExplainMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(explain))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Explain in the database
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExplain() throws Exception {
        // Initialize the database
        explainRepository.saveAndFlush(explain);

        int databaseSizeBeforeDelete = explainRepository.findAll().size();

        // Delete the explain
        restExplainMockMvc
            .perform(delete(ENTITY_API_URL_ID, explain.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Explain> explainList = explainRepository.findAll();
        assertThat(explainList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
