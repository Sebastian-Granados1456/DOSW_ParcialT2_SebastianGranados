package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.models.Reserva;

import java.util.List;

public interface ReservaService {

    Reserva pedirTurno(Reserva reserva);

    List<Reserva> consultarMisReservas(String correo);

    List<Reserva> consultarTodas();
}