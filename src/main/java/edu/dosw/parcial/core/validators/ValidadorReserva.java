package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.models.Reserva;

public abstract class ValidadorReserva {

    protected ValidadorReserva siguiente;

    public void setSiguiente(ValidadorReserva siguiente) {
        this.siguiente = siguiente;
    }

    public abstract void validar(Reserva reserva);

    protected void ejecutarSiguiente(Reserva reserva) {
        if (siguiente != null) {
            siguiente.validar(reserva);
        }
    }
}
