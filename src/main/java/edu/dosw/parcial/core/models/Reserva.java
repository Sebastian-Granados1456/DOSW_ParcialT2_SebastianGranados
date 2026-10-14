package edu.dosw.parcial.core.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Reserva {

    private String idReserva;
    private Turno turnoAsociado;
    private String barberoNombre;
    private Double precio;
    private String clienteNombre;
    private String clienteTlf;
    private String clienteCorreo;
    private Double total;
    private String estado;
    private List<Servicio> servicios;
    private LocalDateTime fechaHora;

    public Reserva() {
    }

    public Reserva(String idReserva, Turno turnoAsociado, String barberoNombre, Double precio, String clienteNombre, String clienteTlf, String clienteCorreo, Double total, String estado, List<Servicio> servicios, LocalDateTime fechaHora) {
        this.idReserva = idReserva;
        this.turnoAsociado = turnoAsociado;
        this.barberoNombre = barberoNombre;
        this.precio = precio;
        this.clienteNombre = clienteNombre;
        this.clienteTlf = clienteTlf;
        this.clienteCorreo = clienteCorreo;
        this.total = total;
        this.estado = estado;
        this.servicios = servicios;
        this.fechaHora = fechaHora;
    }

    public Reserva(String idReserva, Turno turnoAsociado, Double precio, String clienteNombre, String clienteTlf, String clienteCorreo, Double total, String estado, List<Servicio> servicios, LocalDateTime fechaHora) {
        this(idReserva, turnoAsociado, null, precio, clienteNombre, clienteTlf, clienteCorreo, total, estado, servicios, fechaHora);
    }

    public Reserva(String idReserva, Turno turnoAsociado, Double precio, String clienteNombre, String clienteTlf, String clienteCorreo, Double total, String estado, List<Servicio> servicios) {
        this(idReserva, turnoAsociado, null, precio, clienteNombre, clienteTlf, clienteCorreo, total, estado, servicios, null);
    }

    public static ReservaBuilder builder() {
        return new ReservaBuilder();
    }

    public static class ReservaBuilder {
        private String idReserva;
        private Turno turnoAsociado;
        private String barberoNombre;
        private Double precio;
        private String clienteNombre;
        private String clienteTlf;
        private String clienteCorreo;
        private Double total;
        private String estado;
        private List<Servicio> servicios;
        private LocalDateTime fechaHora;

        public ReservaBuilder idReserva(String idReserva) {
            this.idReserva = idReserva;
            return this;
        }

        public ReservaBuilder turnoAsociado(Turno turnoAsociado) {
            this.turnoAsociado = turnoAsociado;
            return this;
        }

        public ReservaBuilder barberoNombre(String barberoNombre) {
            this.barberoNombre = barberoNombre;
            return this;
        }

        public ReservaBuilder precio(Double precio) {
            this.precio = precio;
            return this;
        }

        public ReservaBuilder clienteNombre(String clienteNombre) {
            this.clienteNombre = clienteNombre;
            return this;
        }

        public ReservaBuilder clienteTlf(String clienteTlf) {
            this.clienteTlf = clienteTlf;
            return this;
        }

        public ReservaBuilder clienteCorreo(String clienteCorreo) {
            this.clienteCorreo = clienteCorreo;
            return this;
        }

        public ReservaBuilder total(Double total) {
            this.total = total;
            return this;
        }

        public ReservaBuilder estado(String estado) {
            this.estado = estado;
            return this;
        }

        public ReservaBuilder servicios(List<Servicio> servicios) {
            this.servicios = servicios;
            return this;
        }

        public ReservaBuilder fechaHora(LocalDateTime fechaHora) {
            this.fechaHora = fechaHora;
            return this;
        }

        public Reserva build() {
            return new Reserva(idReserva, turnoAsociado, barberoNombre, precio, clienteNombre, clienteTlf, clienteCorreo, total, estado, servicios, fechaHora);
        }
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public Turno getTurnoAsociado() {
        return turnoAsociado;
    }

    public void setTurnoAsociado(Turno turnoAsociado) {
        this.turnoAsociado = turnoAsociado;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public void setBarberoNombre(String barberoNombre) {
        this.barberoNombre = barberoNombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteTlf() {
        return clienteTlf;
    }

    public void setClienteTlf(String clienteTlf) {
        this.clienteTlf = clienteTlf;
    }

    public String getClienteCorreo() {
        return clienteCorreo;
    }

    public void setClienteCorreo(String clienteCorreo) {
        this.clienteCorreo = clienteCorreo;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
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
