package edu.dosw.parcial.persistence.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document(collection = "turnos")
@Data
@Builder
public class TurnoDocument {

    @Id
    private String idTurno;
    private String barberoNombre;
    private LocalDate fecha;
    private LocalTime hora;
    private String estado;

    public TurnoDocument() {
    }

    public TurnoDocument(String idTurno, String barberoNombre, LocalDate fecha, LocalTime hora, String estado) {
        this.idTurno = idTurno;
        this.barberoNombre = barberoNombre;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }
}
