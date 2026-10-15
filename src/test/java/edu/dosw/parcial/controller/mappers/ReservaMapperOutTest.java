package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.response.ReservaResponseDTO;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaMapperOutTest {

    private final ReservaMapperOut reservaMapperOut = new ReservaMapperOut();

    @Test
    void toResponse_reservaCompleta_retornaCodigosDeServicios() {
        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .build();

        Servicio serv = Servicio.builder()
                .codigo("SER-001")
                .build();

        Reserva reserva = Reserva.builder()
                .idReserva("RES-001")
                .turnoAsociado(turno)
                .servicios(List.of(serv))
                .total(25000.0)
                .estado("CONFIRMADA")
                .build();

        ReservaResponseDTO response = reservaMapperOut.toResponse(reserva);

        assertEquals("Miguel", response.barberoNombre());
        assertEquals(List.of("SER-001"), response.servicios());
    }

    @Test
    void toResponse_sinTurnoNiServicios_retornaListaVacia() {
        Reserva reserva = Reserva.builder()
                .idReserva("RES-002")
                .build();

        ReservaResponseDTO response = reservaMapperOut.toResponse(reserva);

        assertNull(response.idTurno());
        assertTrue(response.servicios().isEmpty());
    }

    @Test
    void toResponse_reservaNula_retornaNull() {
        ReservaResponseDTO response = reservaMapperOut.toResponse(null);

        assertNull(response);
    }
}
