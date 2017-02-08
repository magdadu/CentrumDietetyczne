package wat.edu.pl.repository;

import wat.edu.pl.domain.Diet_product;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Diet_product entity.
 */
@SuppressWarnings("unused")
public interface Diet_productRepository extends JpaRepository<Diet_product,Long> {

}
