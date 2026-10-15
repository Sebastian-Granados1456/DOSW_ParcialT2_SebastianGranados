package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.TurnoResponseDTO;
import edu.dosw.parcial.core.models.Turno;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TurnoMapperOutTest {

    private final TurnoMapperOut turnoMapperOut = new TurnoMapperOut();

    @Test
    void toResponse_turnoValido_retornaResponse() {
        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .estado("DISPONIBLE")
                .build();

        TurnoResponseDTO response = turnoMapperOut.toResponse(turno);

        assertEquals("TUR-001", response.idTurno());
        assertEquals("DISPONIBLE", response.estado());
    }

    @Test
    void toResponse_turnoNulo_retornaNull() {
        TurnoResponseDTO response = turnoMapperOut.toResponse(null);

        assertNull(response);
    }
}