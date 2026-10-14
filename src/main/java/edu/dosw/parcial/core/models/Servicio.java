package edu.dosw.parcial.core.models;

import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.models.enums.TipoServicio;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Servicio {

    private String codigo;
    private TipoServicio servicio;
    private Double precio;
    private Especialidad especialidad;

    public Servicio() {
    }

    public Servicio(String codigo, TipoServicio servicio, Double precio, Especialidad especialidad) {
        this.codigo = codigo;
        this.servicio = servicio;
        this.precio = precio;
        this.especialidad = especialidad;
    }

    public boolean estaDisponible() {
        return precio != null && precio > 0;
    }
}
