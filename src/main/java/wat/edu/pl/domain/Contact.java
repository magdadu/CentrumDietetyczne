package wat.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Contact.
 */
@Entity
@Table(name = "contact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;
    public Contact(){}
    public Contact( String firstname, String name, String email, String message, String subject) {

        this.firstname = firstname;
        this.name = name;
        this.email = email;
        this.message = message;
        this.subject = subject;
    }
    @Column(name = "firstname")
    public String firstname;
    @Column(name = "name")
    public String name;
    @Column(name = "email")
    public String email;
    @Column(name = "message")
    public String message;
    @Column(name = "subject")
    public String subject;

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }



}
