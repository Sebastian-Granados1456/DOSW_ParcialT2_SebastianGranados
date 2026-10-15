package edu.dosw.parcial.controller.mappers;

import edu.dosw.parcial.controller.dtos.request.ReservaRequestDTO;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaMapperInTest {

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ReservaMapperIn reservaMapperIn;

    @Test
    void toDomain_turnoYServicioExisten_usaDatosGuardados() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Andrés")
                .fecha(LocalDate.of(2026, 11, 12))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        ServicioDocument servDoc = ServicioDocument.builder()
                .codigo("SER-001")
                .precio(25000.0)
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servDoc));

        ReservaRequestDTO dto = new ReservaRequestDTO("TUR-001", null, null, null, "Juan", "3001234567",
                "juan@gmail.com", List.of("SER-001"));

        Reserva reserva = reservaMapperIn.toDomain(dto);

        assertEquals("Andrés", reserva.getTurnoAsociado().getBarberoNombre());
        assertEquals(25000.0, reserva.getServicios().get(0).getPrecio());
    }

    @Test
    void toDomain_turnoYServicioNoExisten_conservaLoEnviado() {
        when(turnoRepository.findById("TUR-999")).thenReturn(Optional.empty());
        when(servicioRepository.findByCodigo("SER-999")).thenReturn(Optional.empty());

        ReservaRequestDTO dto = new ReservaRequestDTO("TUR-999", LocalDate.of(2026, 11, 12), LocalTime.of(11, 0),
                "Laura", "Ana", "3001234567", "ana@gmail.com", List.of("SER-999"));

        Reserva reserva = reservaMapperIn.toDomain(dto);

        assertEquals("TUR-999", reserva.getTurnoAsociado().getIdTurno());
        assertEquals("SER-999", reserva.getServicios().get(0).getCodigo());
    }

    @Test
    void toDomain_sinTurnoNiServicios_armaTurnoConFechaYHora() {
        ReservaRequestDTO dto = new ReservaRequestDTO(null, LocalDate.of(2026, 11, 12), LocalTime.of(11, 0), "Laura",
                "Ana", "3001234567", "ana@gmail.com", null);

        Reserva reserva = reservaMapperIn.toDomain(dto);

        assertEquals(LocalTime.of(11, 0), reserva.getTurnoAsociado().getHora());
        assertTrue(reserva.getServicios().isEmpty());
    }

    @Test
    void toDomain_barberoNombreNulo_asignaNullSinError() {
        ReservaRequestDTO dto = new ReservaRequestDTO("TUR-001", LocalDate.of(2026, 11, 12), LocalTime.of(11, 0), null,
                "Ana", "3001234567", "ana@gmail.com", List.of());

        Reserva reserva = reservaMapperIn.toDomain(dto);

        org.junit.jupiter.api.Assertions.assertNotNull(reserva);
        assertNull(reserva.getBarberoNombre());
    }

    @Test
    void toDomain_requestNulo_retornaNull() {
        Reserva reserva = reservaMapperIn.toDomain(null);

        assertNull(reserva);
    }
}
