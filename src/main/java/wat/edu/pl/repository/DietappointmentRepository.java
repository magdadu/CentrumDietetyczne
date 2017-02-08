package wat.edu.pl.repository;

import wat.edu.pl.domain.Dietappointment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dietappointment entity.
 */
@SuppressWarnings("unused")
public interface DietappointmentRepository extends JpaRepository<Dietappointment,Long> {

}
