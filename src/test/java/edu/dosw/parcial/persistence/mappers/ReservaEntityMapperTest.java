package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.ReservaDocument;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReservaEntityMapperTest {

    private final ReservaEntityMapper reservaMapper = new ReservaEntityMapper();

    @Test
    void toDocument_reservaValida_guardaIdDelTurno() {
        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .build();

        Reserva reserva = Reserva.builder()
                .idReserva("RES-001")
                .turnoAsociado(turno)
                .clienteNombre("Juan")
                .clienteCorreo("juan@gmail.com")
                .total(25000.0)
                .estado("CONFIRMADA")
                .fechaHora(LocalDateTime.of(2026, 11, 12, 10, 0))
                .build();

        ReservaDocument doc = reservaMapper.toDocument(reserva);

        assertEquals("TUR-001", doc.getTurnoId());
        assertEquals("juan@gmail.com", doc.getClienteCorreo());
    }

    @Test
    void toDomain_documentoValido_armaTurnoAsociado() {
        ReservaDocument doc = ReservaDocument.builder()
                .idReserva("RES-001")
                .turnoId("TUR-001")
                .clienteCorreo("juan@gmail.com")
                .estado("CONFIRMADA")
                .build();

        Reserva reserva = reservaMapper.toDomain(doc);

        assertEquals("TUR-001", reserva.getTurnoAsociado().getIdTurno());
        assertEquals("CONFIRMADA", reserva.getEstado());
    }

    @Test
    void toDomain_documentoSinTurno_turnoNulo() {
        ReservaDocument doc = ReservaDocument.builder()
                .idReserva("RES-002")
                .build();

        Reserva reserva = reservaMapper.toDomain(doc);

        assertNull(reserva.getTurnoAsociado());
    }

    @Test
    void toDocument_reservaSinTurno_turnoIdNulo() {
        Reserva reserva = Reserva.builder()
                .idReserva("RES-003")
                .build();

        ReservaDocument doc = reservaMapper.toDocument(reserva);

        assertNull(doc.getTurnoId());
    }

    @Test
    void toDomain_documentoNulo_retornaNull() {
        Reserva reserva = reservaMapper.toDomain(null);

        assertNull(reserva);
    }

    @Test
    void toDocument_reservaNula_retornaNull() {
        ReservaDocument doc = reservaMapper.toDocument(null);

        assertNull(doc);
    }
}
