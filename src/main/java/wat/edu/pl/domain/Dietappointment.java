package wat.edu.pl.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Dietappointment.
 */
@Entity
@Table(name = "diet_appointment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dietappointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Dietappointment(LocalDate date, String hour, Client client, Dietetician dietetician) {
        this.date = date;
        this.hour = hour;
        this.client = client;
        this.dietetician = dietetician;
    }

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "hour")
    private String hour;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Dietetician dietetician;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Dietappointment() {

    }

    public Dietappointment date(LocalDate date) {
        this.date = date;
        return this;

    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public Dietappointment hour(String hour) {
        this.hour = hour;
        return this;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Client getClient() {
        return client;
    }

    public Dietappointment client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Dietetician getDietetician() {
        return dietetician;
    }

    public Dietappointment dietetician(Dietetician dietetician) {
        this.dietetician = dietetician;
        return this;
    }

    public void setDietetician(Dietetician dietetician) {
        this.dietetician = dietetician;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dietappointment dietappointment = (Dietappointment) o;
        if(dietappointment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dietappointment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dietappointment{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", hour='" + hour + "'" +
            '}';
    }
}
