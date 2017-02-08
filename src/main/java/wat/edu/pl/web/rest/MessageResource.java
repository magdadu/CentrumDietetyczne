package wat.edu.pl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mysql.jdbc.Messages;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wat.edu.pl.config.JHipsterProperties;
import wat.edu.pl.domain.Contact;
import wat.edu.pl.domain.Message;
import wat.edu.pl.domain.PersistentToken;
import wat.edu.pl.domain.User;
import wat.edu.pl.repository.MessageRepository;
import wat.edu.pl.repository.PersistentTokenRepository;
import wat.edu.pl.repository.UserRepository;
import wat.edu.pl.security.SecurityUtils;
import wat.edu.pl.service.MailService;
import wat.edu.pl.service.UserService;
import wat.edu.pl.service.dto.UserDTO;
import wat.edu.pl.web.rest.util.HeaderUtil;
import wat.edu.pl.web.rest.vm.KeyAndPasswordVM;
import wat.edu.pl.web.rest.vm.ManagedUserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for messages.
 */

@RestController
@RequestMapping("/api")
public class MessageResource {
    /**
     * GET  /messages : get all the messages.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of messages in body
     */
    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageRepository messageRepository;

    @GetMapping(value = "/messages")
    @Timed
    public List<Message> getAllMessages() {

        log.debug("REST request to get all Messages");
        List<Message> messages = messageRepository.findAll();
        return messages;
    }

    /**
     * POST  /messages : Create a new message.
     *
     * @param message the message to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contact, or with status 400 (Bad Request) if the contact has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/messages")
    @Timed
    public ResponseEntity<Message> createMessage(@RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("message", "idexists", "A new message cannot already have an ID")).body(null);
        }
       Message result = messageRepository.save(message);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /message/:id : get the "id" message.
     *
     * @param id the id of the message to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the message, or with status 404 (Not Found)
     */
    /*@GetMapping("/messages/{id}")
    @Timed
    public ResponseEntity<Message> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        Message message = messageRepository.findOne(id);
        return Optional.ofNullable(message)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/


}
