package edu.dosw.parcial.controller.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record TurnoRequestDTO(
        @NotBlank(message = "El nombre del barbero es obligatorio")
        String barberoNombre,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La hora es obligatoria")
        LocalTime hora
) {
    public String getBarberoNombre() {
        return barberoNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }
}
