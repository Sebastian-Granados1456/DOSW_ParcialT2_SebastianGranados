package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.enums.Especialidad;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "barberos")
@Data
@Builder
public class BarberoDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String nombre;

    private List<Especialidad> especialidades;
    private String diasDisponible;
    private String horario;

    public BarberoDocument() {
    }

    public BarberoDocument(String id, String nombre, List<Especialidad> especialidades, String diasDisponible, String horario) {
        this.id = id;
        this.nombre = nombre;
        this.especialidades = especialidades;
        this.diasDisponible = diasDisponible;
        this.horario = horario;
    }
}
