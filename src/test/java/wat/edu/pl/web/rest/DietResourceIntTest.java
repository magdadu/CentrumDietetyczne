package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Diet;
import wat.edu.pl.repository.DietRepository;

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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DietResource REST controller.
 *
 * @see DietResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class DietResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Inject
    private DietRepository dietRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDietMockMvc;

    private Diet diet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DietResource dietResource = new DietResource();
        ReflectionTestUtils.setField(dietResource, "dietRepository", dietRepository);
        this.restDietMockMvc = MockMvcBuilders.standaloneSetup(dietResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diet createEntity(EntityManager em) {
        Diet diet = new Diet()
                .name(DEFAULT_NAME);
        return diet;
    }

    @Before
    public void initTest() {
        diet = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiet() throws Exception {
        int databaseSizeBeforeCreate = dietRepository.findAll().size();

        // Create the Diet

        restDietMockMvc.perform(post("/api/diets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diet)))
                .andExpect(status().isCreated());

        // Validate the Diet in the database
        List<Diet> diets = dietRepository.findAll();
        assertThat(diets).hasSize(databaseSizeBeforeCreate + 1);
        Diet testDiet = diets.get(diets.size() - 1);
        assertThat(testDiet.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllDiets() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get all the diets
        restDietMockMvc.perform(get("/api/diets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diet.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getDiet() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);

        // Get the diet
        restDietMockMvc.perform(get("/api/diets/{id}", diet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDiet() throws Exception {
        // Get the diet
        restDietMockMvc.perform(get("/api/diets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiet() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);
        int databaseSizeBeforeUpdate = dietRepository.findAll().size();

        // Update the diet
        Diet updatedDiet = dietRepository.findOne(diet.getId());
        updatedDiet
                .name(UPDATED_NAME);

        restDietMockMvc.perform(put("/api/diets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiet)))
                .andExpect(status().isOk());

        // Validate the Diet in the database
        List<Diet> diets = dietRepository.findAll();
        assertThat(diets).hasSize(databaseSizeBeforeUpdate);
        Diet testDiet = diets.get(diets.size() - 1);
        assertThat(testDiet.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteDiet() throws Exception {
        // Initialize the database
        dietRepository.saveAndFlush(diet);
        int databaseSizeBeforeDelete = dietRepository.findAll().size();

        // Get the diet
        restDietMockMvc.perform(delete("/api/diets/{id}", diet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Diet> diets = dietRepository.findAll();
        assertThat(diets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
