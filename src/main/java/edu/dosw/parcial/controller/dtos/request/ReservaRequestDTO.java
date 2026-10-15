package edu.dosw.parcial.controller.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReservaRequestDTO(
        @NotBlank(message = "El idTurno es obligatorio")
        String idTurno,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La hora es obligatoria")
        LocalTime hora,

        @Schema(description = "Nombre del barbero (opcional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "Miguel")
        String barberoNombre,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String clienteNombre,

        @NotBlank(message = "El teléfono del cliente es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener 10 dígitos")
        String clienteTlf,

        @NotBlank(message = "El correo del cliente es obligatorio")
        String clienteCorreo,

        @NotEmpty(message = "La lista de servicios es obligatoria")
        List<String> codigosServicios
) {
    public String getIdTurno() {
        return idTurno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteTlf() {
        return clienteTlf;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public List<String> getCodigosServicios() {
        return codigosServicios;
    }
}
