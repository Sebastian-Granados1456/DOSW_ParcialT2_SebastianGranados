package edu.dosw.parcial.core.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Turno {

    private String idTurno;
    private String barberoNombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    public Turno() {
    }

    public Turno(String idTurno, String barberoNombre, LocalDate fecha, LocalTime hora, String estado) {
        this.idTurno = idTurno;
        this.barberoNombre = barberoNombre;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public Turno(String idTurno, LocalDate fecha, LocalTime hora, String estado) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getBarberoNombre() {
        return barberoNombre;
    }

    public void setBarberoNombre(String barberoNombre) {
        this.barberoNombre = barberoNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void verTurno() {
    }
}
