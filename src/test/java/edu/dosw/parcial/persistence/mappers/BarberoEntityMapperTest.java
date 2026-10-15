package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Barbero;
import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BarberoEntityMapperTest {

    private final BarberoEntityMapper barberoMapper = new BarberoEntityMapper();

    @Test
    void toDocument_barberoValido_conservaDatos() {
        Barbero barbero = Barbero.builder()
                .nombre("Miguel")
                .especialidades(List.of(Especialidad.CORTE))
                .diasDisponible("Lunes a Jueves")
                .horario("10:00 AM - 5:00 PM")
                .build();

        BarberoDocument doc = barberoMapper.toDocument(barbero);

        assertEquals("Miguel", doc.getNombre());
        assertEquals("Lunes a Jueves", doc.getDiasDisponible());
    }

    @Test
    void toDomain_documentoValido_conservaDatos() {
        BarberoDocument doc = BarberoDocument.builder()
                .nombre("Laura")
                .especialidades(List.of(Especialidad.CAPILAR))
                .diasDisponible("Lunes a Viernes")
                .horario("10:00 AM - 5:00 PM")
                .build();

        Barbero barbero = barberoMapper.toDomain(doc);

        assertEquals("Laura", barbero.getNombre());
        assertEquals("Lunes a Viernes", barbero.getDiasDisponible());
    }

    @Test
    void toDomain_documentoNulo_retornaNull() {
        Barbero barbero = barberoMapper.toDomain(null);

        assertNull(barbero);
    }

    @Test
    void toDocument_barberoNulo_retornaNull() {
        BarberoDocument doc = barberoMapper.toDocument(null);

        assertNull(doc);
    }
}
