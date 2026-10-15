package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.utils.HorarioUtil;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ValidadorBarbero extends ValidadorReserva {

    private final BarberoRepository barberoRepository;
    private final TurnoRepository turnoRepository;
    private final ServicioRepository servicioRepository;

    public ValidadorBarbero(BarberoRepository barberoRepository,
                            TurnoRepository turnoRepository) {
        this(barberoRepository, turnoRepository, null);
    }

    @Autowired
    public ValidadorBarbero(BarberoRepository barberoRepository,
                            TurnoRepository turnoRepository,
                            @Autowired(required = false) ServicioRepository servicioRepository) {
        this.barberoRepository = barberoRepository;
        this.turnoRepository = turnoRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public void validar(Reserva reserva) {
        Turno turno = reserva.getTurnoAsociado();
        List<Especialidad> especialidadesRequeridas = obtenerEspecialidadesRequeridas(reserva.getServicios());

        if (reserva.getBarberoNombre() == null || reserva.getBarberoNombre().isBlank()) {
            LocalDate fecha = null;
            LocalTime hora = null;
            if (reserva.getFechaHora() != null) {
                fecha = reserva.getFechaHora().toLocalDate();
                hora = reserva.getFechaHora().toLocalTime();
            } else if (turno != null) {
                fecha = turno.getFecha();
                hora = turno.getHora();
            }

            if (fecha == null || hora == null) {
                throw new BusinessRuleException("E100", "Barbero no disponible");
            }

            final LocalDate fechaFinal = fecha;
            final LocalTime horaFinal = hora;

            List<BarberoDocument> todosLosBarberos = barberoRepository.findAll();
            BarberoDocument candidato = todosLosBarberos.stream()
                    .filter(b -> HorarioUtil.esDiaDisponible(fechaFinal.getDayOfWeek(), b.getDiasDisponible()))
                    .filter(b -> HorarioUtil.esHoraDisponible(horaFinal, b.getHorario()))
                    .filter(b -> cumpleEspecialidades(b, especialidadesRequeridas))
                    .filter(b -> !tieneTurnoOcupado(b.getNombre(), fechaFinal, horaFinal))
                    .findFirst()
                    .orElse(null);

            if (candidato == null) {
                throw new BusinessRuleException("E100", "Barbero no disponible");
            }

            reserva.setBarberoNombre(candidato.getNombre());
            if (turno != null) {
                turno.setBarberoNombre(candidato.getNombre());
            }
        } else {
            String barberoNombre = reserva.getBarberoNombre().trim();
            BarberoDocument barbero = barberoRepository.findByNombre(barberoNombre).orElse(null);
            if (barbero == null || !cumpleEspecialidades(barbero, especialidadesRequeridas)) {
                throw new BusinessRuleException("E02", "El barbero seleccionado no cuenta con la especialidad requerida");
            }
            reserva.setBarberoNombre(barbero.getNombre());
            if (turno != null) {
                turno.setBarberoNombre(barbero.getNombre());
            }
        }

        ejecutarSiguiente(reserva);
    }

    private boolean tieneTurnoOcupado(String barberoNombre, LocalDate fecha, LocalTime hora) {
        if (turnoRepository == null || barberoNombre == null) {
            return false;
        }
        if (turnoRepository.existsByBarberoNombreAndFechaAndHoraAndEstado(barberoNombre, fecha, hora, "OCUPADO")) {
            return true;
        }
        List<TurnoDocument> turnos = turnoRepository.findByFechaAndHora(fecha, hora);
        if (turnos != null) {
            return turnos.stream()
                    .anyMatch(t -> barberoNombre.equalsIgnoreCase(t.getBarberoNombre())
                            && "OCUPADO".equalsIgnoreCase(t.getEstado()));
        }
        return false;
    }

    private List<Especialidad> obtenerEspecialidadesRequeridas(List<Servicio> servicios) {
        List<Especialidad> lista = new ArrayList<>();
        if (servicios == null) {
            return lista;
        }
        for (Servicio s : servicios) {
            if (s == null) {
                continue;
            }
            if (s.getEspecialidad() != null) {
                if (!lista.contains(s.getEspecialidad())) {
                    lista.add(s.getEspecialidad());
                }
            } else if (s.getCodigo() != null && servicioRepository != null) {
                servicioRepository.findByCodigo(s.getCodigo()).ifPresent(doc -> {
                    if (doc.getEspecialidad() != null && !lista.contains(doc.getEspecialidad())) {
                        lista.add(doc.getEspecialidad());
                    }
                });
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
