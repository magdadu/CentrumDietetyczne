package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Result;
import wat.edu.pl.repository.ResultRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ResultResource REST controller.
 *
 * @see ResultResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class ResultResourceIntTest {

    private static final byte[] DEFAULT_RESLUTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RESLUTS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_RESLUTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RESLUTS_CONTENT_TYPE = "image/png";

    @Inject
    private ResultRepository resultRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restResultMockMvc;

    private Result result;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResultResource resultResource = new ResultResource();
        ReflectionTestUtils.setField(resultResource, "resultRepository", resultRepository);
        this.restResultMockMvc = MockMvcBuilders.standaloneSetup(resultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Result createEntity(EntityManager em) {
        Result result = new Result()
                .resluts(DEFAULT_RESLUTS)
                .reslutsContentType(DEFAULT_RESLUTS_CONTENT_TYPE);
        return result;
    }

    @Before
    public void initTest() {
        result = createEntity(em);
    }

    @Test
    @Transactional
    public void createResult() throws Exception {
        int databaseSizeBeforeCreate = resultRepository.findAll().size();

        // Create the Result

        restResultMockMvc.perform(post("/api/results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(result)))
                .andExpect(status().isCreated());

        // Validate the Result in the database
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeCreate + 1);
        Result testResult = results.get(results.size() - 1);
        assertThat(testResult.getResluts()).isEqualTo(DEFAULT_RESLUTS);
        assertThat(testResult.getReslutsContentType()).isEqualTo(DEFAULT_RESLUTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllResults() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);

        // Get all the results
        restResultMockMvc.perform(get("/api/results?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(result.getId().intValue())))
                .andExpect(jsonPath("$.[*].reslutsContentType").value(hasItem(DEFAULT_RESLUTS_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].resluts").value(hasItem(Base64Utils.encodeToString(DEFAULT_RESLUTS))));
    }

    @Test
    @Transactional
    public void getResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);

        // Get the result
        restResultMockMvc.perform(get("/api/results/{id}", result.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(result.getId().intValue()))
            .andExpect(jsonPath("$.reslutsContentType").value(DEFAULT_RESLUTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.resluts").value(Base64Utils.encodeToString(DEFAULT_RESLUTS)));
    }

    @Test
    @Transactional
    public void getNonExistingResult() throws Exception {
        // Get the result
        restResultMockMvc.perform(get("/api/results/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);
        int databaseSizeBeforeUpdate = resultRepository.findAll().size();

        // Update the result
        Result updatedResult = resultRepository.findOne(result.getId());
        updatedResult
                .resluts(UPDATED_RESLUTS)
                .reslutsContentType(UPDATED_RESLUTS_CONTENT_TYPE);

        restResultMockMvc.perform(put("/api/results")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResult)))
                .andExpect(status().isOk());

        // Validate the Result in the database
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeUpdate);
        Result testResult = results.get(results.size() - 1);
        assertThat(testResult.getResluts()).isEqualTo(UPDATED_RESLUTS);
        assertThat(testResult.getReslutsContentType()).isEqualTo(UPDATED_RESLUTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteResult() throws Exception {
        // Initialize the database
        resultRepository.saveAndFlush(result);
        int databaseSizeBeforeDelete = resultRepository.findAll().size();

        // Get the result
        restResultMockMvc.perform(delete("/api/results/{id}", result.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Result> results = resultRepository.findAll();
        assertThat(results).hasSize(databaseSizeBeforeDelete - 1);
    }
}
