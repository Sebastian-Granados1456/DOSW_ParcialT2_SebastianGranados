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
        String barberoFinal = domain.getBarberoNombre();
        if (barberoFinal == null && domain.getTurnoAsociado() != null) {
            barberoFinal = domain.getTurnoAsociado().getBarberoNombre();
        }
        List<String> codigosServicios = domain.getServicios() != null
                ? domain.getServicios().stream().map(Servicio::getCodigo).toList()
                : List.of();

        return new ReservaResponseDTO(
                domain.getIdReserva(),
                idTurno,
                barberoFinal,
                domain.getClienteNombre(),
                domain.getClienteCorreo(),
                codigosServicios,
                domain.getTotal(),
                domain.getEstado(),
                domain.getFechaHora()
        );
    }
}
