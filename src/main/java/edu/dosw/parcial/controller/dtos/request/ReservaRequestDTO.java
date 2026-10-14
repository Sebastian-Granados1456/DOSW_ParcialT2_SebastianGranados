package edu.dosw.parcial.controller.dtos.request;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReservaRequestDTO(
        String idTurno,
        LocalDate fecha,
        LocalTime hora,
        String barberoNombre,
        String clienteNombre,
        String clienteTlf,
        String clienteCorreo,
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
