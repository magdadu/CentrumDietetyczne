package wat.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import wat.edu.pl.domain.Result;

import wat.edu.pl.repository.ResultRepository;
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
 * REST controller for managing Result.
 */
@RestController
@RequestMapping("/api")
public class ResultResource {

    private final Logger log = LoggerFactory.getLogger(ResultResource.class);

    @Inject
    private ResultRepository resultRepository;

    /**
     * POST  /results : Create a new result.
     *
     * @param result the result to create
     * @return the ResponseEntity with status 201 (Created) and with body the new result, or with status 400 (Bad Request) if the result has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/results")
    @Timed
    public ResponseEntity<Result> createResult(@RequestBody Result result) throws URISyntaxException {
        log.debug("REST request to save Result : {}", result);
        if (result.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("result", "idexists", "A new result cannot already have an ID")).body(null);
        }
        Result results= resultRepository.save(result);
        return ResponseEntity.created(new URI("/api/results/" + results.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("result", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /results : Updates an existing result.
     *
     * @param result the result to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated result,
     * or with status 400 (Bad Request) if the result is not valid,
     * or with status 500 (Internal Server Error) if the result couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/results")
    @Timed
    public ResponseEntity<Result> updateResult(@RequestBody Result result) throws URISyntaxException {
        log.debug("REST request to update Result : {}", result);
        if (result.getId() == null) {
            return createResult(result);
        }
        Result results = resultRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("result", results.getId().toString()))
            .body(result);
    }

    /**
     * GET  /results : get all the results.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of results in body
     */
    @GetMapping("/results")
    @Timed
    public List<Result> getAllResults() {
        log.debug("REST request to get all Results");
        List<Result> results = resultRepository.findAll();
        return results;
    }

    /**
     * GET  /results/:id : get the "id" result.
     *
     * @param id the id of the result to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the result, or with status 404 (Not Found)
     */
    @GetMapping("/results/{id}")
    @Timed
    public ResponseEntity<Result> getResult(@PathVariable Long id) {
        log.debug("REST request to get Result : {}", id);
        Result results = resultRepository.findOne(id);
        return Optional.ofNullable(results)
            .map(result -> new ResponseEntity<>(
                results,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /results/:id : delete the "id" result.
     *
     * @param id the id of the result to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/results/{id}")
    @Timed
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        log.debug("REST request to delete Result : {}", id);
        resultRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("result", id.toString())).build();
    }

}
