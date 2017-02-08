package wat.edu.pl.repository;

import wat.edu.pl.domain.Dietetician;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dietetician entity.
 */
@SuppressWarnings("unused")
public interface DieteticianRepository extends JpaRepository<Dietetician,Long> {

}
