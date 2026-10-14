package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCliente extends ValidadorReserva {

    @Override
    public void validar(Reserva reserva) {
        if (siguiente != null) {
            siguiente.validar(reserva);
        }
    }
}
