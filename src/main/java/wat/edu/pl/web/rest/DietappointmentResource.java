package wat.edu.pl.web.rest;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.codahale.metrics.annotation.Timed;
import wat.edu.pl.domain.Dietappointment;

import wat.edu.pl.repository.DietappointmentRepository;
import wat.edu.pl.service.dto.DietAppointmentDTO;
import wat.edu.pl.service.mapper.DietAppointmentMapper;
import wat.edu.pl.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dietappointment.
 */
@RestController
@RequestMapping("/api")
public class DietappointmentResource {

    private final Logger log = LoggerFactory.getLogger(DietappointmentResource.class);

    @Inject
    private DietappointmentRepository dietappointmentRepository;

    /**
     * POST  /dietappointments : Create a new dietappointment.
     *
     * @param dietappointmentDTO the dietappointment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dietappointment, or with status 400 (Bad Request) if the dietappointment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diet-appointments")
    @Timed
    public ResponseEntity<Dietappointment> createDietappointment(@RequestBody DietAppointmentDTO dietappointmentDTO) throws URISyntaxException {

        DietAppointmentMapper dietAppointmentMapper=new DietAppointmentMapper();
        Dietappointment dietappointment =dietAppointmentMapper.fromDTOtoDietappointment(dietappointmentDTO);

        log.debug("REST request to save Dietappointment : {}", dietappointment);
        if (dietappointment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dietappointment", "idexists", "A new dietappointment cannot already have an ID")).body(null);
        }


        Dietappointment result = dietappointmentRepository.save(dietappointment);
        return ResponseEntity.created(new URI("/api/diet-appointments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dietappointment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dietappointments : Updates an existing dietappointment.
     *
     * @param dietappointment the dietappointment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dietappointment,
     * or with status 400 (Bad Request) if the dietappointment is not valid,
     * or with status 500 (Internal Server Error) if the dietappointment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diet-appointments")
    @Timed
    public ResponseEntity<Dietappointment> updateDietappointment(@RequestBody DietAppointmentDTO dietappointment) throws URISyntaxException {
        log.debug("REST request to update Dietappointment : {}", dietappointment);

       DietAppointmentMapper dietAppointmentMapper=new DietAppointmentMapper();

        if (dietAppointmentMapper.fromDTOtoDietappointment(dietappointment).getId() == null) {
            return createDietappointment(dietappointment);
        }
        Dietappointment result = dietappointmentRepository.save(dietAppointmentMapper.fromDTOtoDietappointment(dietappointment));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dietappointment", dietAppointmentMapper.fromDTOtoDietappointment(dietappointment).getId().toString()))
            .body(result);
    }

    /**
     * GET  /dietappointments : get all the dietappointments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dietappointments in body
     */
    @GetMapping("/diet-appointments")
    @Timed
    public List<Dietappointment> getAllDietappointments() {
        log.debug("REST request to get all Dietappointments");
        List<Dietappointment> dietappointments = dietappointmentRepository.findAll();
        return dietappointments;
    }

    /**
     * GET  /dietappointments/:id : get the "id" dietappointment.
     *
     * @param id the id of the dietappointment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dietappointment, or with status 404 (Not Found)
     */
    @GetMapping("/diet-appointments/{id}")
    @Timed
    public ResponseEntity<Dietappointment> getDietappointment(@PathVariable Long id) {
        log.debug("REST request to get Dietappointment : {}", id);
        Dietappointment dietappointment = dietappointmentRepository.findOne(id);
        return Optional.ofNullable(dietappointment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dietappointments/:id : delete the "id" dietappointment.
     *
     * @param id the id of the dietappointment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diet-appointments/{id}")
    @Timed
    public ResponseEntity<Void> deleteDietappointment(@PathVariable Long id) {
        log.debug("REST request to delete Dietappointment : {}", id);
        dietappointmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dietappointment", id.toString())).build();
    }

}
