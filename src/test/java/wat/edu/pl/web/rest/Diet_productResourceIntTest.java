package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Diet_product;
import wat.edu.pl.repository.Diet_productRepository;

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
 * Test class for the Diet_productResource REST controller.
 *
 * @see Diet_productResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class Diet_productResourceIntTest {

    private static final Integer DEFAULT_IS_RECOMMENDED = 1;
    private static final Integer UPDATED_IS_RECOMMENDED = 2;

    @Inject
    private Diet_productRepository diet_productRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDiet_productMockMvc;

    private Diet_product diet_product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Diet_productResource diet_productResource = new Diet_productResource();
        ReflectionTestUtils.setField(diet_productResource, "diet_productRepository", diet_productRepository);
        this.restDiet_productMockMvc = MockMvcBuilders.standaloneSetup(diet_productResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Diet_product createEntity(EntityManager em) {
        Diet_product diet_product = new Diet_product()
                .isRecommended(DEFAULT_IS_RECOMMENDED);
        return diet_product;
    }

    @Before
    public void initTest() {
        diet_product = createEntity(em);
    }

    @Test
    @Transactional
    public void createDiet_product() throws Exception {
        int databaseSizeBeforeCreate = diet_productRepository.findAll().size();

        // Create the Diet_product

        restDiet_productMockMvc.perform(post("/api/diet-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diet_product)))
                .andExpect(status().isCreated());

        // Validate the Diet_product in the database
        List<Diet_product> diet_products = diet_productRepository.findAll();
        assertThat(diet_products).hasSize(databaseSizeBeforeCreate + 1);
        Diet_product testDiet_product = diet_products.get(diet_products.size() - 1);
        assertThat(testDiet_product.getIsRecommended()).isEqualTo(DEFAULT_IS_RECOMMENDED);
    }

    @Test
    @Transactional
    public void getAllDiet_products() throws Exception {
        // Initialize the database
        diet_productRepository.saveAndFlush(diet_product);

        // Get all the diet_products
        restDiet_productMockMvc.perform(get("/api/diet-products?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diet_product.getId().intValue())))
                .andExpect(jsonPath("$.[*].isRecommended").value(hasItem(DEFAULT_IS_RECOMMENDED)));
    }

    @Test
    @Transactional
    public void getDiet_product() throws Exception {
        // Initialize the database
        diet_productRepository.saveAndFlush(diet_product);

        // Get the diet_product
        restDiet_productMockMvc.perform(get("/api/diet-products/{id}", diet_product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(diet_product.getId().intValue()))
            .andExpect(jsonPath("$.isRecommended").value(DEFAULT_IS_RECOMMENDED));
    }

    @Test
    @Transactional
    public void getNonExistingDiet_product() throws Exception {
        // Get the diet_product
        restDiet_productMockMvc.perform(get("/api/diet-products/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiet_product() throws Exception {
        // Initialize the database
        diet_productRepository.saveAndFlush(diet_product);
        int databaseSizeBeforeUpdate = diet_productRepository.findAll().size();

        // Update the diet_product
        Diet_product updatedDiet_product = diet_productRepository.findOne(diet_product.getId());
        updatedDiet_product
                .isRecommended(UPDATED_IS_RECOMMENDED);

        restDiet_productMockMvc.perform(put("/api/diet-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiet_product)))
                .andExpect(status().isOk());

        // Validate the Diet_product in the database
        List<Diet_product> diet_products = diet_productRepository.findAll();
        assertThat(diet_products).hasSize(databaseSizeBeforeUpdate);
        Diet_product testDiet_product = diet_products.get(diet_products.size() - 1);
        assertThat(testDiet_product.getIsRecommended()).isEqualTo(UPDATED_IS_RECOMMENDED);
    }

    @Test
    @Transactional
    public void deleteDiet_product() throws Exception {
        // Initialize the database
        diet_productRepository.saveAndFlush(diet_product);
        int databaseSizeBeforeDelete = diet_productRepository.findAll().size();

        // Get the diet_product
        restDiet_productMockMvc.perform(delete("/api/diet-products/{id}", diet_product.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Diet_product> diet_products = diet_productRepository.findAll();
        assertThat(diet_products).hasSize(databaseSizeBeforeDelete - 1);
    }
}
