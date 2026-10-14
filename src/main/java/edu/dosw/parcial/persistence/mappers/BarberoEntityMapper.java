package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Barbero;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import org.springframework.stereotype.Component;

@Component
public class BarberoEntityMapper {

    public Barbero toDomain(BarberoDocument doc) {
        if (doc == null) return null;
        return Barbero.builder()
                .nombre(doc.getNombre())
                .especialidades(doc.getEspecialidades())
                .diasDisponible(doc.getDiasDisponible())
                .horario(doc.getHorario())
                .build();
    }

    public BarberoDocument toDocument(Barbero domain) {
        if (domain == null) return null;
        return BarberoDocument.builder()
                .nombre(domain.getNombre())
                .especialidades(domain.getEspecialidades())
                .diasDisponible(domain.getDiasDisponible())
                .horario(domain.getHorario())
                .build();
    }
}
