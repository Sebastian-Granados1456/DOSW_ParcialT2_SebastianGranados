package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.exceptions.ResourceNotFoundException;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.mappers.TurnoEntityMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private TurnoEntityMapper turnoEntityMapper;

    @InjectMocks
    private TurnoServiceImpl turnoService;

    @Test
    void crearTurno_datosValidos_creaTurnoExitosamente() {
        BarberoDocument barbero = BarberoDocument.builder()
                .nombre("Miguel")
                .diasDisponible("Lunes a Jueves")
                .horario("10:00 AM - 5:00 PM")
                .build();

        LocalDate fechaValida = LocalDate.of(2026, 10, 19);
        LocalTime horaValida = LocalTime.of(11, 0);

        Turno turno = Turno.builder()
                .barberoNombre("Miguel")
                .fecha(fechaValida)
                .hora(horaValida)
                .build();

        TurnoDocument docMock = TurnoDocument.builder()
                .idTurno("TUR-123")
                .barberoNombre("Miguel")
                .fecha(fechaValida)
                .hora(horaValida)
                .estado("DISPONIBLE")
                .build();

        Turno turnoGuardado = Turno.builder()
                .idTurno("TUR-123")
                .barberoNombre("Miguel")
                .fecha(fechaValida)
                .hora(horaValida)
                .estado("DISPONIBLE")
                .build();

        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barbero));
        when(turnoRepository.existsByBarberoNombreAndFechaAndHora("Miguel", fechaValida, horaValida)).thenReturn(false);
        when(turnoEntityMapper.toDocument(any(Turno.class))).thenReturn(docMock);
        when(turnoRepository.save(any(TurnoDocument.class))).thenReturn(docMock);
        when(turnoEntityMapper.toDomain(docMock)).thenReturn(turnoGuardado);

        Turno resultado = turnoService.crearTurno(turno);

        assertNotNull(resultado);
        assertEquals("DISPONIBLE", resultado.getEstado());
        assertEquals("TUR-123", resultado.getIdTurno());
        verify(turnoRepository, times(1)).save(any(TurnoDocument.class));
    }

    @Test
    void crearTurno_barberoNoExiste_lanzaResourceNotFoundException() {
        Turno turno = Turno.builder()
                .barberoNombre("Carlos")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .build();

        when(barberoRepository.findByNombre("Carlos")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> turnoService.crearTurno(turno));
        verify(turnoRepository, never()).save(any());
    }

    @Test
    void crearTurno_fueraDeHorario_lanzaBusinessRuleException() {
        BarberoDocument barbero = BarberoDocument.builder()
                .nombre("Miguel")
                .diasDisponible("Lunes a Jueves")
                .horario("10:00 AM - 5:00 PM")
                .build();

        Turno turno = Turno.builder()
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 18))
                .hora(LocalTime.of(11, 0))
                .build();

        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barbero));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> turnoService.crearTurno(turno));
        assertEquals("E02", ex.getCode());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
        verify(turnoRepository, never()).save(any());
    }

    @Test
    void crearTurno_turnoDuplicado_lanzaBusinessRuleException() {
        BarberoDocument barbero = BarberoDocument.builder()
                .nombre("Miguel")
                .diasDisponible("Lunes a Jueves")
                .horario("10:00 AM - 5:00 PM")
                .build();

        LocalDate fechaValida = LocalDate.of(2026, 10, 19);
        LocalTime horaValida = LocalTime.of(11, 0);

        Turno turno = Turno.builder()
                .barberoNombre("Miguel")
                .fecha(fechaValida)
                .hora(horaValida)
                .build();

        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barbero));
        when(turnoRepository.existsByBarberoNombreAndFechaAndHora("Miguel", fechaValida, horaValida)).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> turnoService.crearTurno(turno));
        assertEquals("E01", ex.getCode());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
        verify(turnoRepository, never()).save(any());
    }
}
