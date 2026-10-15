package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.ReservaRequestDTO;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservaMapperIn {

    private final TurnoRepository turnoRepository;
    private final ServicioRepository servicioRepository;

    public ReservaMapperIn(TurnoRepository turnoRepository, ServicioRepository servicioRepository) {
        this.turnoRepository = turnoRepository;
        this.servicioRepository = servicioRepository;
    }

    public Reserva toDomain(ReservaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Turno turno = null;
        if (dto.idTurno() != null && !dto.idTurno().isBlank()) {
            turno = turnoRepository.findById(dto.idTurno())
                    .map(doc -> Turno.builder()
                            .idTurno(doc.getIdTurno())
                            .barberoNombre(doc.getBarberoNombre())
                            .fecha(doc.getFecha())
                            .hora(doc.getHora())
                            .estado(doc.getEstado())
                            .build())
                    .orElseGet(() -> Turno.builder()
                            .idTurno(dto.idTurno())
                            .barberoNombre(dto.barberoNombre())
                            .fecha(dto.fecha())
                            .hora(dto.hora())
                            .build());
        } else {
            turno = Turno.builder()
                    .barberoNombre(dto.barberoNombre())
                    .fecha(dto.fecha())
                    .hora(dto.hora())
                    .build();
        }

        List<Servicio> servicios = new ArrayList<>();
        if (dto.codigosServicios() != null) {
            for (String codigo : dto.codigosServicios()) {
                ServicioDocument doc = servicioRepository.findByCodigo(codigo).orElse(null);
                if (doc != null) {
                    servicios.add(Servicio.builder()
                            .codigo(doc.getCodigo())
                            .servicio(doc.getServicio())
                            .precio(doc.getPrecio())
                            .especialidad(doc.getEspecialidad())
                            .build());
                } else {
                    servicios.add(Servicio.builder()
                            .codigo(codigo)
                            .build());
                }
            }
        }

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turno)
                .clienteNombre(dto.clienteNombre())
                .clienteTlf(dto.clienteTlf())
                .clienteCorreo(dto.clienteCorreo())
                .servicios(servicios)
                .build();
        reserva.setBarberoNombre(dto.barberoNombre());
        return reserva;
    }
}
