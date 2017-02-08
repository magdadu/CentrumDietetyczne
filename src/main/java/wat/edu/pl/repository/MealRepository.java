package wat.edu.pl.repository;

import wat.edu.pl.domain.Meal;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Meal entity.
 */
@SuppressWarnings("unused")
public interface MealRepository extends JpaRepository<Meal,Long> {

}
