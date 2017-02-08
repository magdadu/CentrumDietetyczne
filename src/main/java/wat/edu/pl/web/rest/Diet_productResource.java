package wat.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import wat.edu.pl.domain.Diet_product;

import wat.edu.pl.repository.Diet_productRepository;
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
 * REST controller for managing Diet_product.
 */
@RestController
@RequestMapping("/api")
public class Diet_productResource {

    private final Logger log = LoggerFactory.getLogger(Diet_productResource.class);
        
    @Inject
    private Diet_productRepository diet_productRepository;

    /**
     * POST  /diet-products : Create a new diet_product.
     *
     * @param diet_product the diet_product to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diet_product, or with status 400 (Bad Request) if the diet_product has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/diet-products")
    @Timed
    public ResponseEntity<Diet_product> createDiet_product(@Valid @RequestBody Diet_product diet_product) throws URISyntaxException {
        log.debug("REST request to save Diet_product : {}", diet_product);
        if (diet_product.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diet_product", "idexists", "A new diet_product cannot already have an ID")).body(null);
        }
        Diet_product result = diet_productRepository.save(diet_product);
        return ResponseEntity.created(new URI("/api/diet-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diet_product", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diet-products : Updates an existing diet_product.
     *
     * @param diet_product the diet_product to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diet_product,
     * or with status 400 (Bad Request) if the diet_product is not valid,
     * or with status 500 (Internal Server Error) if the diet_product couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/diet-products")
    @Timed
    public ResponseEntity<Diet_product> updateDiet_product(@Valid @RequestBody Diet_product diet_product) throws URISyntaxException {
        log.debug("REST request to update Diet_product : {}", diet_product);
        if (diet_product.getId() == null) {
            return createDiet_product(diet_product);
        }
        Diet_product result = diet_productRepository.save(diet_product);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("diet_product", diet_product.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diet-products : get all the diet_products.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of diet_products in body
     */
    @GetMapping("/diet-products")
    @Timed
    public List<Diet_product> getAllDiet_products() {
        log.debug("REST request to get all Diet_products");
        List<Diet_product> diet_products = diet_productRepository.findAll();
        return diet_products;
    }

    /**
     * GET  /diet-products/:id : get the "id" diet_product.
     *
     * @param id the id of the diet_product to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diet_product, or with status 404 (Not Found)
     */
    @GetMapping("/diet-products/{id}")
    @Timed
    public ResponseEntity<Diet_product> getDiet_product(@PathVariable Long id) {
        log.debug("REST request to get Diet_product : {}", id);
        Diet_product diet_product = diet_productRepository.findOne(id);
        return Optional.ofNullable(diet_product)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diet-products/:id : delete the "id" diet_product.
     *
     * @param id the id of the diet_product to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/diet-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteDiet_product(@PathVariable Long id) {
        log.debug("REST request to delete Diet_product : {}", id);
        diet_productRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diet_product", id.toString())).build();
    }

}
