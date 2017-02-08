package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Meal;
import wat.edu.pl.repository.MealRepository;

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
 * Test class for the MealResource REST controller.
 *
 * @see MealResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class MealResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Inject
    private MealRepository mealRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMealMockMvc;

    private Meal meal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MealResource mealResource = new MealResource();
        ReflectionTestUtils.setField(mealResource, "mealRepository", mealRepository);
        this.restMealMockMvc = MockMvcBuilders.standaloneSetup(mealResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Meal createEntity(EntityManager em) {
        Meal meal = new Meal()
                .name(DEFAULT_NAME)
                .amount(DEFAULT_AMOUNT)
                .unit(DEFAULT_UNIT);
        return meal;
    }

    @Before
    public void initTest() {
        meal = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeal() throws Exception {
        int databaseSizeBeforeCreate = mealRepository.findAll().size();

        // Create the Meal

        restMealMockMvc.perform(post("/api/meals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meal)))
                .andExpect(status().isCreated());

        // Validate the Meal in the database
        List<Meal> meals = mealRepository.findAll();
        assertThat(meals).hasSize(databaseSizeBeforeCreate + 1);
        Meal testMeal = meals.get(meals.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMeal.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMeal.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mealRepository.findAll().size();
        // set the field null
        meal.setName(null);

        // Create the Meal, which fails.

        restMealMockMvc.perform(post("/api/meals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(meal)))
                .andExpect(status().isBadRequest());

        List<Meal> meals = mealRepository.findAll();
        assertThat(meals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMeals() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get all the meals
        restMealMockMvc.perform(get("/api/meals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(meal.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }

    @Test
    @Transactional
    public void getMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);

        // Get the meal
        restMealMockMvc.perform(get("/api/meals/{id}", meal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeal() throws Exception {
        // Get the meal
        restMealMockMvc.perform(get("/api/meals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);
        int databaseSizeBeforeUpdate = mealRepository.findAll().size();

        // Update the meal
        Meal updatedMeal = mealRepository.findOne(meal.getId());
        updatedMeal
                .name(UPDATED_NAME)
                .amount(UPDATED_AMOUNT)
                .unit(UPDATED_UNIT);

        restMealMockMvc.perform(put("/api/meals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMeal)))
                .andExpect(status().isOk());

        // Validate the Meal in the database
        List<Meal> meals = mealRepository.findAll();
        assertThat(meals).hasSize(databaseSizeBeforeUpdate);
        Meal testMeal = meals.get(meals.size() - 1);
        assertThat(testMeal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMeal.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMeal.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void deleteMeal() throws Exception {
        // Initialize the database
        mealRepository.saveAndFlush(meal);
        int databaseSizeBeforeDelete = mealRepository.findAll().size();

        // Get the meal
        restMealMockMvc.perform(delete("/api/meals/{id}", meal.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Meal> meals = mealRepository.findAll();
        assertThat(meals).hasSize(databaseSizeBeforeDelete - 1);
    }
}
