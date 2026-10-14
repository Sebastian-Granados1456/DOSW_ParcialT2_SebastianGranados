package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import org.springframework.stereotype.Component;

@Component
public class TurnoEntityMapper {

    public Turno toDomain(TurnoDocument doc) {
        if (doc == null) return null;
        return Turno.builder()
                .idTurno(doc.getIdTurno())
                .fecha(doc.getFecha())
                .hora(doc.getHora())
                .estado(doc.getEstado())
                .build();
    }

    public TurnoDocument toDocument(Turno domain) {
        if (domain == null) return null;
        return TurnoDocument.builder()
                .idTurno(domain.getIdTurno())
                .fecha(domain.getFecha())
                .hora(domain.getHora())
                .estado(domain.getEstado())
                .build();
    }
}
