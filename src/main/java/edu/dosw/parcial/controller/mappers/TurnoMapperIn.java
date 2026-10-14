package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.TurnoRequestDTO;
import edu.dosw.parcial.core.models.Turno;
import org.springframework.stereotype.Component;

@Component
public class TurnoMapperIn {

    public Turno toDomain(TurnoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        return Turno.builder()
                .barberoNombre(dto.barberoNombre())
                .fecha(dto.fecha())
                .hora(dto.hora())
                .build();
    }
}
