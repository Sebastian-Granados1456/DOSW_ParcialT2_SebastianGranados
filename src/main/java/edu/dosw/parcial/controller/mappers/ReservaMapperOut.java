package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.ReservaResponseDTO;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservaMapperOut {

    public ReservaResponseDTO toResponse(Reserva domain) {
        if (domain == null) {
            return null;
        }

        String idTurno = domain.getTurnoAsociado() != null ? domain.getTurnoAsociado().getIdTurno() : null;
        String barberoNombre = domain.getTurnoAsociado() != null ? domain.getTurnoAsociado().getBarberoNombre() : null;
        List<String> codigosServicios = domain.getServicios() != null
                ? domain.getServicios().stream().map(Servicio::getCodigo).toList()
                : List.of();

        return new ReservaResponseDTO(
                domain.getIdReserva(),
                idTurno,
                barberoNombre,
                domain.getClienteNombre(),
                domain.getClienteCorreo(),
                codigosServicios,
                domain.getTotal(),
                domain.getEstado(),
                domain.getFechaHora()
        );
    }
}
