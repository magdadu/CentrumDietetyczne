package wat.edu.pl.domain;
import javax.persistence.*;

/**
 * Created by Magda on 06.01.2017.
 */
@Entity
@Table(name = "inbox")
public class Message {
    @Id
    @GeneratedValue
    private Long id;
    public Message(){}
    public Message( String firstname, String lastname, String email, String message, String subject) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.message = message;
        this.subject = subject;
    }
    @Column(name = "firstname")
    public String firstname;
    @Column(name = "lastname")
    public String lastname;
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

    public String getLastname() {
        return lastname;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
