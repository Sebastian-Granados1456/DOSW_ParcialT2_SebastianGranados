package edu.dosw.parcial.controller.dtos.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoResponseDTO(
        String idTurno,
        String barberoNombre,
        LocalDate fecha,
        LocalTime hora,
        String estado
) {
    public String getIdTurno() {
        return idTurno;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }
}
