package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ValidadorCliente extends ValidadorReserva {

    @Override
    public void validar(Reserva reserva) {
        if (reserva.getClienteNombre() == null || reserva.getClienteNombre().trim().isEmpty()) {
            throw new BusinessRuleException("E03", "Nombre de cliente inválido");
        }

        if (reserva.getClienteTlf() == null || !reserva.getClienteTlf().trim().matches("^\\d{10}$")) {
            throw new BusinessRuleException("E07", "El teléfono debe contener 10 dígitos");
        }

        if (reserva.getClienteCorreo() == null
                || !reserva.getClienteCorreo().trim().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new BusinessRuleException("E08", "Correo electrónico inválido");
        }

        ejecutarSiguiente(reserva);
    }
}
