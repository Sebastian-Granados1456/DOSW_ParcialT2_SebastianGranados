package edu.dosw.parcial.controller.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public record ReservaResponseDTO(
        String idReserva,
        String idTurno,
        String barberoNombre,
        String clienteNombre,
        String clienteCorreo,
        List<String> servicios,
        Double total,
        String estado,
        LocalDateTime fechaHora
) {
    public String getIdReserva() {
        return idReserva;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public List<String> getServicios() {
        return servicios;
    }

    public Double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}
