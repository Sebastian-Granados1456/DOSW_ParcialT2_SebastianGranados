package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValidadorBarbero extends ValidadorReserva {

    private final BarberoRepository barberoRepository;
    private final TurnoRepository turnoRepository;

    public ValidadorBarbero(BarberoRepository barberoRepository,
                            TurnoRepository turnoRepository) {
        this.barberoRepository = barberoRepository;
        this.turnoRepository = turnoRepository;
    }

    @Override
    public void validar(Reserva reserva) {
        Turno turno = reserva.getTurnoAsociado();
        String barberoNombre = reserva.getBarberoNombre() != null && !reserva.getBarberoNombre().isBlank()
                ? reserva.getBarberoNombre()
                : (turno != null ? turno.getBarberoNombre() : null);

        List<Especialidad> especialidadesRequeridas = obtenerEspecialidadesRequeridas(reserva.getServicios());

        if (barberoNombre != null && !barberoNombre.isBlank()) {
            BarberoDocument barbero = barberoRepository.findByNombre(barberoNombre).orElse(null);
            if (barbero == null || !cumpleEspecialidades(barbero, especialidadesRequeridas)) {
                throw new BusinessRuleException("E02", "El barbero seleccionado no cuenta con la especialidad requerida para los servicios solicitados");
            }
            if (turno != null) {
                turno.setBarberoNombre(barbero.getNombre());
            }
            reserva.setBarberoNombre(barbero.getNombre());
        } else {
            if (turno == null || turno.getFecha() == null || turno.getHora() == null) {
                throw new BusinessRuleException("E100", "Barbero no disponible");
            }

            List<TurnoDocument> turnosDisponibles = turnoRepository.findByFechaAndHora(turno.getFecha(), turno.getHora())
                    .stream()
                    .filter(t -> "DISPONIBLE".equalsIgnoreCase(t.getEstado()))
                    .toList();

            BarberoDocument barberoSeleccionado = null;
            TurnoDocument turnoSeleccionado = null;

            for (TurnoDocument tDoc : turnosDisponibles) {
                if (tDoc.getBarberoNombre() != null) {
                    BarberoDocument bDoc = barberoRepository.findByNombre(tDoc.getBarberoNombre()).orElse(null);
                    if (bDoc != null && cumpleEspecialidades(bDoc, especialidadesRequeridas)) {
                        barberoSeleccionado = bDoc;
                        turnoSeleccionado = tDoc;
                        break;
                    }
                }
            }

            if (barberoSeleccionado == null) {
                throw new BusinessRuleException("E100", "Barbero no disponible");
            }

            turno.setIdTurno(turnoSeleccionado.getIdTurno());
            turno.setBarberoNombre(barberoSeleccionado.getNombre());
            reserva.setBarberoNombre(barberoSeleccionado.getNombre());
        }

        ejecutarSiguiente(reserva);
    }

    private List<Especialidad> obtenerEspecialidadesRequeridas(List<Servicio> servicios) {
        List<Especialidad> lista = new ArrayList<>();
        if (servicios == null) {
            return lista;
        }
        for (Servicio s : servicios) {
            if (s.getEspecialidad() != null && !lista.contains(s.getEspecialidad())) {
                lista.add(s.getEspecialidad());
            }
        }
        return lista;
    }

    private boolean cumpleEspecialidades(BarberoDocument barbero, List<Especialidad> requeridas) {
        if (requeridas == null || requeridas.isEmpty()) {
            return true;
        }
        List<Especialidad> barberoEsp = barbero.getEspecialidades();
        if (barberoEsp == null) {
            return false;
        }
        return barberoEsp.containsAll(requeridas);
    }
}
