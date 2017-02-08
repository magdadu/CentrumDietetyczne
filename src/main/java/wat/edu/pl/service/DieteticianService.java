package wat.edu.pl.service;

import wat.edu.pl.domain.Dietetician;
import wat.edu.pl.repository.DieteticianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Dietetician.
 */
@Service
@Transactional
public class DieteticianService {

    private final Logger log = LoggerFactory.getLogger(DieteticianService.class);
    
    @Inject
    private DieteticianRepository dieteticianRepository;

    /**
     * Save a dietetician.
     *
     * @param dietetician the entity to save
     * @return the persisted entity
     */
    public Dietetician save(Dietetician dietetician) {
        log.debug("Request to save Dietetician : {}", dietetician);
        Dietetician result = dieteticianRepository.save(dietetician);
        return result;
    }

    /**
     *  Get all the dieteticians.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Dietetician> findAll() {
        log.debug("Request to get all Dieteticians");
        List<Dietetician> result = dieteticianRepository.findAll();

        return result;
    }

    /**
     *  Get one dietetician by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Dietetician findOne(Long id) {
        log.debug("Request to get Dietetician : {}", id);
        Dietetician dietetician = dieteticianRepository.findOne(id);
        return dietetician;
    }

    /**
     *  Delete the  dietetician by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Dietetician : {}", id);
        dieteticianRepository.delete(id);
    }
}
