package wat.edu.pl.web.rest;

import wat.edu.pl.InzynierkaApp;

import wat.edu.pl.domain.Client;
import wat.edu.pl.domain.Dietappointment;
import wat.edu.pl.domain.Dietetician;
import wat.edu.pl.repository.DietappointmentRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DietappointmentResource REST controller.
 *
 * @see DietappointmentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InzynierkaApp.class)
public class DietappointmentResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HOUR = "AAAAAAAAAA";
    private static final String UPDATED_HOUR = "BBBBBBBBBB";

    private static final Client DEFAULT_CLIENT = new Client();
    private static final Client UPDATED_CLIENT = new Client();

    private static final Dietetician DEFAULT_DIETETICIAN = new Dietetician();
    private static final Dietetician UPDATED_DIETETICIAN = new Dietetician();
    @Inject
    private DietappointmentRepository dietappointmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDietappointmentMockMvc;

    private Dietappointment dietappointment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DietappointmentResource dietappointmentResource = new DietappointmentResource();
        ReflectionTestUtils.setField(dietappointmentResource, "dietappointmentRepository", dietappointmentRepository);
        this.restDietappointmentMockMvc = MockMvcBuilders.standaloneSetup(dietappointmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dietappointment createEntity(EntityManager em) {
        Dietappointment dietappointment = new Dietappointment(DEFAULT_DATE,DEFAULT_HOUR,DEFAULT_CLIENT,DEFAULT_DIETETICIAN);

        return dietappointment;
    }

    @Before
    public void initTest() {
        dietappointment = createEntity(em);
    }

    @Test
    @Transactional
    public void createDietappointment() throws Exception {
        int databaseSizeBeforeCreate = dietappointmentRepository.findAll().size();

        // Create the Dietappointment

        restDietappointmentMockMvc.perform(post("/api/dietappointments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dietappointment)))
                .andExpect(status().isCreated());

        // Validate the Dietappointment in the database
        List<Dietappointment> dietappointments = dietappointmentRepository.findAll();
        assertThat(dietappointments).hasSize(databaseSizeBeforeCreate + 1);
        Dietappointment testDietappointment = dietappointments.get(dietappointments.size() - 1);
        assertThat(testDietappointment.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testDietappointment.getHour()).isEqualTo(DEFAULT_HOUR);
    }

    @Test
    @Transactional
    public void getAllDietappointments() throws Exception {
        // Initialize the database
        dietappointmentRepository.saveAndFlush(dietappointment);

        // Get all the dietappointments
        restDietappointmentMockMvc.perform(get("/api/dietappointments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dietappointment.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].hour").value(hasItem(DEFAULT_HOUR.toString())));
    }

    @Test
    @Transactional
    public void getDietappointment() throws Exception {
        // Initialize the database
        dietappointmentRepository.saveAndFlush(dietappointment);

        // Get the dietappointment
        restDietappointmentMockMvc.perform(get("/api/dietappointments/{id}", dietappointment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dietappointment.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.hour").value(DEFAULT_HOUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDietappointment() throws Exception {
        // Get the dietappointment
        restDietappointmentMockMvc.perform(get("/api/dietappointments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDietappointment() throws Exception {
        // Initialize the database
        dietappointmentRepository.saveAndFlush(dietappointment);
        int databaseSizeBeforeUpdate = dietappointmentRepository.findAll().size();

        // Update the dietappointment
        Dietappointment updatedDietappointment = dietappointmentRepository.findOne(dietappointment.getId());
        updatedDietappointment
                .date(UPDATED_DATE)
                .hour(UPDATED_HOUR);

        restDietappointmentMockMvc.perform(put("/api/dietappointments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDietappointment)))
                .andExpect(status().isOk());

        // Validate the Dietappointment in the database
        List<Dietappointment> dietappointments = dietappointmentRepository.findAll();
        assertThat(dietappointments).hasSize(databaseSizeBeforeUpdate);
        Dietappointment testDietappointment = dietappointments.get(dietappointments.size() - 1);
        assertThat(testDietappointment.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testDietappointment.getHour()).isEqualTo(UPDATED_HOUR);
    }

    @Test
    @Transactional
    public void deleteDietappointment() throws Exception {
        // Initialize the database
        dietappointmentRepository.saveAndFlush(dietappointment);
        int databaseSizeBeforeDelete = dietappointmentRepository.findAll().size();

        // Get the dietappointment
        restDietappointmentMockMvc.perform(delete("/api/dietappointments/{id}", dietappointment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dietappointment> dietappointments = dietappointmentRepository.findAll();
        assertThat(dietappointments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
