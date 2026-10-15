package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.ReservaDocument;
import org.springframework.stereotype.Component;

@Component
public class ReservaEntityMapper {

    public Reserva toDomain(ReservaDocument doc) {
        if (doc == null) {
            return null;
        }
        Turno turno = null;
        if (doc.getTurnoId() != null) {
            turno = Turno.builder()
                    .idTurno(doc.getTurnoId())
                    .barberoNombre(doc.getBarberoNombre())
                    .build();
        }
        return Reserva.builder()
                .idReserva(doc.getIdReserva())
                .turnoAsociado(turno)
                .barberoNombre(doc.getBarberoNombre())
                .clienteNombre(doc.getClienteNombre())
                .clienteTlf(doc.getClienteTlf())
                .clienteCorreo(doc.getClienteCorreo())
                .total(doc.getTotal())
                .precio(doc.getPrecio())
                .estado(doc.getEstado())
                .servicios(doc.getServicios())
                .fechaHora(doc.getFechaHora())
                .build();
    }

    public ReservaDocument toDocument(Reserva domain) {
        if (domain == null) {
            return null;
        }
        String turnoId = domain.getTurnoAsociado() != null ? domain.getTurnoAsociado().getIdTurno() : null;
        String barberoNombre = domain.getBarberoNombre();
        if (barberoNombre == null && domain.getTurnoAsociado() != null) {
            barberoNombre = domain.getTurnoAsociado().getBarberoNombre();
        }
        return ReservaDocument.builder()
                .idReserva(domain.getIdReserva())
                .turnoId(turnoId)
                .barberoNombre(barberoNombre)
                .clienteNombre(domain.getClienteNombre())
                .clienteTlf(domain.getClienteTlf())
                .clienteCorreo(domain.getClienteCorreo())
                .total(domain.getTotal())
                .precio(domain.getPrecio())
                .estado(domain.getEstado())
                .servicios(domain.getServicios())
                .fechaHora(domain.getFechaHora())
                .build();
    }
}
