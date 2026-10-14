package edu.dosw.parcial.core.models;

import edu.dosw.parcial.core.models.enums.Especialidad;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Barbero {

    private String nombre;
    private List<Especialidad> especialidades;
    private String diasDisponible;
    private String horario;

    public Barbero() {
    }

    public Barbero(String nombre, List<Especialidad> especialidades, String diasDisponible, String horario) {
        this.nombre = nombre;
        this.especialidades = especialidades;
        this.diasDisponible = diasDisponible;
        this.horario = horario;
    }

    public boolean estaDisponible() {
        return diasDisponible != null && !diasDisponible.trim().isEmpty();
    }
}
