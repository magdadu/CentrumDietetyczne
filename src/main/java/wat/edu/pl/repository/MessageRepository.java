package wat.edu.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wat.edu.pl.domain.Message;
import java.util.List;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
   // List<Message> findAllMessages();
}
