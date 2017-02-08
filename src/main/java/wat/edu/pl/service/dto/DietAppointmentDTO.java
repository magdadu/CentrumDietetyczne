package wat.edu.pl.service.dto;

import org.hibernate.validator.constraints.Email;
import wat.edu.pl.config.Constants;
import wat.edu.pl.domain.Authority;
import wat.edu.pl.domain.Dietappointment;
import wat.edu.pl.domain.User;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
public class DietAppointmentDTO {

    @Size(max = 50)
    private long dietetician;
    @Size(max = 50)
    private long client;
    @Size(max = 50)
    private String hour;
    @Size(max = 50)
    LocalDate date;

    public DietAppointmentDTO(long dietetician, long client, String hour, LocalDate date) {
        this.dietetician = dietetician;
        this.client = client;
        this.hour = hour;
        this.date = date;
    }



    @Override
    public String toString() {
        return "DietAppointmentDTO{" +
            "dietetician=" + dietetician +
            ", client=" + client +
            ", hour='" + hour + '\'' +
            ", date='" + date + '\'' +
            '}';
    }



    public long getDietetician() {
        return dietetician;
    }

    public void setDietetician(long dietetician) {
        this.dietetician = dietetician;
    }

    public long getClient() {
        return client;
    }

    public void setClient(long client) {
        this.client = client;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public DietAppointmentDTO() {
    }

    public DietAppointmentDTO(Dietappointment dietAppointment) {
       this(dietAppointment.getDietetician().getId(), dietAppointment.getClient().getId(), dietAppointment.getHour(), dietAppointment.getDate()
        );
    }





}
