package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TurnoEntityMapperTest {

    private final TurnoEntityMapper turnoMapper = new TurnoEntityMapper();

    @Test
    void toDocument_turnoValido_conservaDatos() {
        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Laura")
                .fecha(LocalDate.of(2026, 11, 12))
                .hora(LocalTime.of(10, 0))
                .estado("DISPONIBLE")
                .build();

        TurnoDocument doc = turnoMapper.toDocument(turno);

        assertEquals("TUR-001", doc.getIdTurno());
        assertEquals("DISPONIBLE", doc.getEstado());
    }

    @Test
    void toDomain_documentoValido_conservaDatos() {
        TurnoDocument doc = TurnoDocument.builder()
                .idTurno("TUR-002")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 11, 12))
                .hora(LocalTime.of(11, 0))
                .estado("OCUPADO")
                .build();

        Turno turno = turnoMapper.toDomain(doc);

        assertEquals("TUR-002", turno.getIdTurno());
        assertEquals("OCUPADO", turno.getEstado());
    }

    @Test
    void toDomain_documentoNulo_retornaNull() {
        Turno turno = turnoMapper.toDomain(null);

        assertNull(turno);
    }

    @Test
    void toDocument_turnoNulo_retornaNull() {
        TurnoDocument doc = turnoMapper.toDocument(null);

        assertNull(doc);
    }
}
