package edu.dosw.parcial.core.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class Turno {

    private String idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    public Turno() {
    }

    public Turno(String idTurno, LocalDate fecha, LocalTime hora, String estado) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public void verTurno() {
    }
}
