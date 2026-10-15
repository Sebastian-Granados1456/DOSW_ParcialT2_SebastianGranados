package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.TurnoResponseDTO;
import edu.dosw.parcial.core.models.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapperOut {

    public TurnoResponseDTO toResponse(Turno domain) {
        if (domain == null) {
            return null;
        }
        return new TurnoResponseDTO(
                domain.getIdTurno(),
                domain.getBarberoNombre(),
                domain.getFecha(),
                domain.getHora(),
                domain.getEstado()
        );
    }
}
