package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import org.springframework.stereotype.Component;

@Component
public class ServicioEntityMapper {

    public Servicio toDomain(ServicioDocument doc) {
        if (doc == null) return null;
        return Servicio.builder()
                .codigo(doc.getCodigo())
                .servicio(doc.getServicio())
                .precio(doc.getPrecio())
                .especialidad(doc.getEspecialidad())
                .build();
    }

    public ServicioDocument toDocument(Servicio domain) {
        if (domain == null) return null;
        return ServicioDocument.builder()
                .codigo(domain.getCodigo())
                .servicio(domain.getServicio())
                .precio(domain.getPrecio())
                .especialidad(domain.getEspecialidad())
                .build();
    }
}
