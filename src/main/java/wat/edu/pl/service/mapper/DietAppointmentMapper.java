package wat.edu.pl.service.mapper;

import io.undertow.client.ClientRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.PageRequest;
import wat.edu.pl.domain.*;
import wat.edu.pl.service.dto.DietAppointmentDTO;
import wat.edu.pl.service.dto.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity User and its DTO UserDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public class DietAppointmentMapper {




    List<DietAppointmentDTO> dietAppointmentDtos;


    public DietAppointmentMapper(){
        dietAppointmentDtos= new ArrayList<DietAppointmentDTO>();
    }


    public List<DietAppointmentDTO> getAllDietAppointmentDtos() {
        return dietAppointmentDtos;
    }

    public Client getClientByID(long id){

        Client client = new Client();
        client.setId(id);
        return client;
    }
    public Dietetician getDieteticianByID(long id){
        Dietetician dietetician = new Dietetician();
        dietetician.setId(id);
        return dietetician;
    }

  public  Dietappointment fromDTOtoDietappointment(DietAppointmentDTO dietAppointmentDTO){
       return new Dietappointment(dietAppointmentDTO.getDate(),dietAppointmentDTO.getHour(),getClientByID(dietAppointmentDTO.getClient()),getDieteticianByID(dietAppointmentDTO.getDietetician()));

   }

}
