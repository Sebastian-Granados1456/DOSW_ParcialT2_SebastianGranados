package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ValidadorServicios extends ValidadorReserva {

    @Override
    public void validar(Reserva reserva) {
        if (reserva.getServicios() == null || reserva.getServicios().isEmpty()) {
            throw new BusinessRuleException("E09", "Servicio inexistente");
        }

        ejecutarSiguiente(reserva);
    }
}
