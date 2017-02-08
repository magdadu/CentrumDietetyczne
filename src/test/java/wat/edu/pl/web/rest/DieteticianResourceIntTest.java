package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Dietetician;
import wat.edu.pl.repository.DieteticianRepository;
import wat.edu.pl.service.DieteticianService;

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
 * Test class for the DieteticianResource REST controller.
 *
 * @see DieteticianResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class DieteticianResourceIntTest {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    @Inject
    private DieteticianRepository dieteticianRepository;

    @Inject
    private DieteticianService dieteticianService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDieteticianMockMvc;

    private Dietetician dietetician;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DieteticianResource dieteticianResource = new DieteticianResource();
        ReflectionTestUtils.setField(dieteticianResource, "dieteticianService", dieteticianService);
        this.restDieteticianMockMvc = MockMvcBuilders.standaloneSetup(dieteticianResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dietetician createEntity(EntityManager em) {
        Dietetician dietetician = new Dietetician()
                .firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME);
        return dietetician;
    }

    @Before
    public void initTest() {
        dietetician = createEntity(em);
    }

    @Test
    @Transactional
    public void createDietetician() throws Exception {
        int databaseSizeBeforeCreate = dieteticianRepository.findAll().size();

        // Create the Dietetician

        restDieteticianMockMvc.perform(post("/api/dieteticians")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietetician)))
                .andExpect(status().isCreated());

        // Validate the Dietetician in the database
        List<Dietetician> dieteticians = dieteticianRepository.findAll();
        assertThat(dieteticians).hasSize(databaseSizeBeforeCreate + 1);
        Dietetician testDietetician = dieteticians.get(dieteticians.size() - 1);
        assertThat(testDietetician.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testDietetician.getLastname()).isEqualTo(DEFAULT_LASTNAME);
    }

    @Test
    @Transactional
    public void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dieteticianRepository.findAll().size();
        // set the field null
        dietetician.setFirstname(null);

        // Create the Dietetician, which fails.

        restDieteticianMockMvc.perform(post("/api/dieteticians")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietetician)))
                .andExpect(status().isBadRequest());

        List<Dietetician> dieteticians = dieteticianRepository.findAll();
        assertThat(dieteticians).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dieteticianRepository.findAll().size();
        // set the field null
        dietetician.setLastname(null);

        // Create the Dietetician, which fails.

        restDieteticianMockMvc.perform(post("/api/dieteticians")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietetician)))
                .andExpect(status().isBadRequest());

        List<Dietetician> dieteticians = dieteticianRepository.findAll();
        assertThat(dieteticians).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDieteticians() throws Exception {
        // Initialize the database
        dieteticianRepository.saveAndFlush(dietetician);

        // Get all the dieteticians
        restDieteticianMockMvc.perform(get("/api/dieteticians?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dietetician.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
                .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())));
    }

    @Test
    @Transactional
    public void getDietetician() throws Exception {
        // Initialize the database
        dieteticianRepository.saveAndFlush(dietetician);

        // Get the dietetician
        restDieteticianMockMvc.perform(get("/api/dieteticians/{id}", dietetician.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dietetician.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDietetician() throws Exception {
        // Get the dietetician
        restDieteticianMockMvc.perform(get("/api/dieteticians/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDietetician() throws Exception {
        // Initialize the database
        dieteticianService.save(dietetician);

        int databaseSizeBeforeUpdate = dieteticianRepository.findAll().size();

        // Update the dietetician
        Dietetician updatedDietetician = dieteticianRepository.findOne(dietetician.getId());
        updatedDietetician
                .firstname(UPDATED_FIRSTNAME)
                .lastname(UPDATED_LASTNAME);

        restDieteticianMockMvc.perform(put("/api/dieteticians")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDietetician)))
                .andExpect(status().isOk());

        // Validate the Dietetician in the database
        List<Dietetician> dieteticians = dieteticianRepository.findAll();
        assertThat(dieteticians).hasSize(databaseSizeBeforeUpdate);
        Dietetician testDietetician = dieteticians.get(dieteticians.size() - 1);
        assertThat(testDietetician.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testDietetician.getLastname()).isEqualTo(UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    public void deleteDietetician() throws Exception {
        // Initialize the database
        dieteticianService.save(dietetician);

        int databaseSizeBeforeDelete = dieteticianRepository.findAll().size();

        // Get the dietetician
        restDieteticianMockMvc.perform(delete("/api/dieteticians/{id}", dietetician.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dietetician> dieteticians = dieteticianRepository.findAll();
        assertThat(dieteticians).hasSize(databaseSizeBeforeDelete - 1);
    }
}
