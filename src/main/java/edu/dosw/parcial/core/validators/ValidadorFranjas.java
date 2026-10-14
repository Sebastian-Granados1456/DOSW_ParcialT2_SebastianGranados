package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.repositories.ReservaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorFranjas extends ValidadorReserva {

    private final ReservaRepository reservaRepository;

    public ValidadorFranjas(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void validar(Reserva reserva) {
        Turno turno = reserva.getTurnoAsociado();
        if (turno == null || !"DISPONIBLE".equalsIgnoreCase(turno.getEstado())) {
            throw new BusinessRuleException("E01", "Franja llena o turno no disponible");
        }

        LocalDateTime fechaHora = reserva.getFechaHora();
        if (fechaHora == null && turno.getFecha() != null && turno.getHora() != null) {
            fechaHora = LocalDateTime.of(turno.getFecha(), turno.getHora());
            reserva.setFechaHora(fechaHora);
        }

        if (fechaHora != null && reservaRepository.countByFechaHora(fechaHora) >= 3) {
            throw new BusinessRuleException("E01", "Franja llena o turno no disponible");
        }

        ejecutarSiguiente(reserva);
    }
}
