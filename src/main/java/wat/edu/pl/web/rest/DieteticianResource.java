package wat.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import wat.edu.pl.domain.Dietetician;
import wat.edu.pl.service.DieteticianService;
import wat.edu.pl.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dietetician.
 */
@RestController
@RequestMapping("/api")
public class DieteticianResource {

    private final Logger log = LoggerFactory.getLogger(DieteticianResource.class);
        
    @Inject
    private DieteticianService dieteticianService;

    /**
     * POST  /dieteticians : Create a new dietetician.
     *
     * @param dietetician the dietetician to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dietetician, or with status 400 (Bad Request) if the dietetician has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dieteticians")
    @Timed
    public ResponseEntity<Dietetician> createDietetician(@Valid @RequestBody Dietetician dietetician) throws URISyntaxException {
        log.debug("REST request to save Dietetician : {}", dietetician);
        if (dietetician.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dietetician", "idexists", "A new dietetician cannot already have an ID")).body(null);
        }
        Dietetician result = dieteticianService.save(dietetician);
        return ResponseEntity.created(new URI("/api/dieteticians/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dietetician", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dieteticians : Updates an existing dietetician.
     *
     * @param dietetician the dietetician to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dietetician,
     * or with status 400 (Bad Request) if the dietetician is not valid,
     * or with status 500 (Internal Server Error) if the dietetician couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dieteticians")
    @Timed
    public ResponseEntity<Dietetician> updateDietetician(@Valid @RequestBody Dietetician dietetician) throws URISyntaxException {
        log.debug("REST request to update Dietetician : {}", dietetician);
        if (dietetician.getId() == null) {
            return createDietetician(dietetician);
        }
        Dietetician result = dieteticianService.save(dietetician);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dietetician", dietetician.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dieteticians : get all the dieteticians.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of dieteticians in body
     */
    @GetMapping("/dieteticians")
    @Timed
    public List<Dietetician> getAllDieteticians() {
        log.debug("REST request to get all Dieteticians");
        return dieteticianService.findAll();
    }

    /**
     * GET  /dieteticians/:id : get the "id" dietetician.
     *
     * @param id the id of the dietetician to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dietetician, or with status 404 (Not Found)
     */
    @GetMapping("/dieteticians/{id}")
    @Timed
    public ResponseEntity<Dietetician> getDietetician(@PathVariable Long id) {
        log.debug("REST request to get Dietetician : {}", id);
        Dietetician dietetician = dieteticianService.findOne(id);
        return Optional.ofNullable(dietetician)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dieteticians/:id : delete the "id" dietetician.
     *
     * @param id the id of the dietetician to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dieteticians/{id}")
    @Timed
    public ResponseEntity<Void> deleteDietetician(@PathVariable Long id) {
        log.debug("REST request to delete Dietetician : {}", id);
        dieteticianService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dietetician", id.toString())).build();
    }

}
