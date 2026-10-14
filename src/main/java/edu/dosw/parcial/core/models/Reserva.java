package edu.dosw.parcial.core.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Reserva {

    private String idReserva;
    private Turno turnoAsociado;
    private Double precio;
    private String clienteNombre;
    private String clienteTlf;
    private String clienteCorreo;
    private Double total;
    private String estado;
    private List<Servicio> servicios;

    public Reserva() {
    }

    public Reserva(String idReserva, Turno turnoAsociado, Double precio, String clienteNombre, String clienteTlf, String clienteCorreo, Double total, String estado, List<Servicio> servicios) {
        this.idReserva = idReserva;
        this.turnoAsociado = turnoAsociado;
        this.precio = precio;
        this.clienteNombre = clienteNombre;
        this.clienteTlf = clienteTlf;
        this.clienteCorreo = clienteCorreo;
        this.total = total;
        this.estado = estado;
        this.servicios = servicios;
    }

    public void verReservas() {
    }

    public Double calcularTotal() {
        if (servicios == null || servicios.isEmpty()) {
            this.total = 0.0;
            this.precio = 0.0;
            return 0.0;
        }
        this.total = servicios.stream().filter(s -> s.getPrecio() != null).mapToDouble(Servicio::getPrecio).sum();
        this.precio = this.total;
        return this.total;
    }
}
