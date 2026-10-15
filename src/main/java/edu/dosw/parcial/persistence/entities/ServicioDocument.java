package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.models.enums.TipoServicio;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicios")
@Data
@Builder
public class ServicioDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String codigo;

    private TipoServicio servicio;
    private Double precio;
    private Especialidad especialidad;

    public ServicioDocument() {
    }

    public ServicioDocument(String id, String codigo, TipoServicio servicio, Double precio, Especialidad especialidad) {
        this.id = id;
        this.codigo = codigo;
        this.servicio = servicio;
        this.precio = precio;
        this.especialidad = especialidad;
    }
}
