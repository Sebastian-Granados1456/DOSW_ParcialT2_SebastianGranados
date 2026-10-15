package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.TurnoRequestDTO;
import edu.dosw.parcial.core.models.Turno;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TurnoMapperInTest {

    private final TurnoMapperIn turnoMapperIn = new TurnoMapperIn();

    @Test
    void toDomain_requestValido_retornaTurno() {
        TurnoRequestDTO request = new TurnoRequestDTO("Miguel", LocalDate.of(2026, 11, 12), LocalTime.of(10, 0));

        Turno turno = turnoMapperIn.toDomain(request);

        assertEquals("Miguel", turno.getBarberoNombre());
        assertEquals(LocalTime.of(10, 0), turno.getHora());
    }

    @Test
    void toDomain_requestNulo_retornaNull() {
        Turno turno = turnoMapperIn.toDomain(null);

        assertNull(turno);
    }
}