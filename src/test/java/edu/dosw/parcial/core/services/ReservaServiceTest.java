package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.validators.ValidadorBarbero;
import edu.dosw.parcial.core.validators.ValidadorCliente;
import edu.dosw.parcial.core.validators.ValidadorFranjas;
import edu.dosw.parcial.core.validators.ValidadorServicios;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.ReservaDocument;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.mappers.ReservaEntityMapper;
import edu.dosw.parcial.persistence.mappers.ServicioEntityMapper;
import edu.dosw.parcial.persistence.mappers.TurnoEntityMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ReservaRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
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
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private TurnoRepository turnoRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private BarberoRepository barberoRepository;

    @Mock
    private ReservaEntityMapper reservaEntityMapper;

    private TurnoEntityMapper turnoEntityMapper;
    private ServicioEntityMapper servicioEntityMapper;
    private ValidadorFranjas validadorFranjas;
    private ValidadorBarbero validadorBarbero;
    private ValidadorCliente validadorCliente;
    private ValidadorServicios validadorServicios;

    private ReservaServiceImpl reservaService;

    @BeforeEach
    void setUp() {
        turnoEntityMapper = new TurnoEntityMapper();
        servicioEntityMapper = new ServicioEntityMapper();
        validadorFranjas = new ValidadorFranjas(reservaRepository);
        validadorBarbero = new ValidadorBarbero(barberoRepository, turnoRepository);
        validadorCliente = new ValidadorCliente();
        validadorServicios = new ValidadorServicios();

        reservaService = new ReservaServiceImpl(
                reservaRepository,
                turnoRepository,
                servicioRepository,
                barberoRepository,
                reservaEntityMapper,
                turnoEntityMapper,
                servicioEntityMapper,
                validadorFranjas,
                validadorBarbero,
                validadorCliente,
                validadorServicios
        );
    }

    @Test
    void pedirTurno_datosValidos_reservaExitosa() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        BarberoDocument barberoDoc = BarberoDocument.builder()
                .nombre("Miguel")
                .especialidades(List.of(Especialidad.CORTE))
                .build();

        ServicioDocument servDoc = ServicioDocument.builder()
                .codigo("SER-001")
                .precio(25000.0)
                .especialidad(Especialidad.CORTE)
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servDoc));
        when(reservaRepository.countByFechaHora(any())).thenReturn(0L);
        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barberoDoc));

        ReservaDocument docGuardado = ReservaDocument.builder()
                .idReserva("RES-001")
                .turnoId("TUR-001")
                .estado("CONFIRMADA")
                .total(25000.0)
                .build();

        when(reservaEntityMapper.toDocument(any())).thenReturn(docGuardado);
        when(reservaRepository.save(any())).thenReturn(docGuardado);

        Reserva domainGuardado = Reserva.builder()
                .idReserva("RES-001")
                .estado("CONFIRMADA")
                .total(25000.0)
                .build();

        when(reservaEntityMapper.toDomain(docGuardado)).thenReturn(domainGuardado);

        Turno turnoInput = Turno.builder()
                .idTurno("TUR-001")
                .build();

        Servicio servInput = Servicio.builder()
                .codigo("SER-001")
                .build();

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turnoInput)
                .clienteNombre("Juan")
                .clienteTlf("3001234567")
                .clienteCorreo("juan@test.com")
                .servicios(List.of(servInput))
                .build();

        Reserva resultado = reservaService.pedirTurno(reserva);

        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        assertEquals("OCUPADO", turnoDoc.getEstado());
        verify(turnoRepository, times(1)).save(any(TurnoDocument.class));
        verify(reservaRepository, times(1)).save(any(ReservaDocument.class));
    }

    @Test
    void pedirTurno_franjaLlena_lanzaE01() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        ServicioDocument servDoc = ServicioDocument.builder()
                .codigo("SER-001")
                .precio(25000.0)
                .especialidad(Especialidad.CORTE)
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servDoc));
        when(reservaRepository.countByFechaHora(any())).thenReturn(3L);

        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .build();

        Servicio serv = Servicio.builder()
                .codigo("SER-001")
                .build();

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turno)
                .servicios(List.of(serv))
                .clienteNombre("Juan")
                .clienteTlf("3001234567")
                .clienteCorreo("juan@test.com")
                .build();

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reservaService.pedirTurno(reserva));
        assertEquals("E01", ex.getCode());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void pedirTurno_especialidadIncompatible_lanzaE02() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        ServicioDocument servDoc = ServicioDocument.builder()
                .codigo("SER-005")
                .precio(45000.0)
                .especialidad(Especialidad.CAPILAR)
                .build();

        BarberoDocument barberoDoc = BarberoDocument.builder()
                .nombre("Miguel")
                .especialidades(List.of(Especialidad.CORTE))
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-005")).thenReturn(Optional.of(servDoc));
        when(reservaRepository.countByFechaHora(any())).thenReturn(0L);
        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barberoDoc));

        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .build();

        Servicio serv = Servicio.builder()
                .codigo("SER-005")
                .build();

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turno)
                .barberoNombre("Miguel")
                .servicios(List.of(serv))
                .clienteNombre("Juan")
                .clienteTlf("3001234567")
                .clienteCorreo("juan@test.com")
                .build();

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reservaService.pedirTurno(reserva));
        assertEquals("E02", ex.getCode());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void pedirTurno_datosClienteInvalidos_lanzaE07oE08() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        ServicioDocument servDoc = ServicioDocument.builder()
                .codigo("SER-001")
                .precio(25000.0)
                .especialidad(Especialidad.CORTE)
                .build();

        BarberoDocument barberoDoc = BarberoDocument.builder()
                .nombre("Miguel")
                .especialidades(List.of(Especialidad.CORTE))
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-001")).thenReturn(Optional.of(servDoc));
        when(reservaRepository.countByFechaHora(any())).thenReturn(0L);
        when(barberoRepository.findByNombre("Miguel")).thenReturn(Optional.of(barberoDoc));

        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .build();

        Servicio serv = Servicio.builder()
                .codigo("SER-001")
                .build();

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turno)
                .barberoNombre("Miguel")
                .servicios(List.of(serv))
                .clienteNombre("Juan")
                .clienteTlf("123")
                .clienteCorreo("correo-invalido")
                .build();

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reservaService.pedirTurno(reserva));
        assertEquals("E07", ex.getCode());
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void pedirTurno_servicioInexistente_lanzaE09() {
        TurnoDocument turnoDoc = TurnoDocument.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .fecha(LocalDate.of(2026, 10, 19))
                .hora(LocalTime.of(11, 0))
                .estado("DISPONIBLE")
                .build();

        when(turnoRepository.findById("TUR-001")).thenReturn(Optional.of(turnoDoc));
        when(servicioRepository.findByCodigo("SER-INEXISTENTE")).thenReturn(Optional.empty());

        Turno turno = Turno.builder()
                .idTurno("TUR-001")
                .barberoNombre("Miguel")
                .build();

        Servicio serv = Servicio.builder()
                .codigo("SER-INEXISTENTE")
                .build();

        Reserva reserva = Reserva.builder()
                .turnoAsociado(turno)
                .barberoNombre("Miguel")
                .servicios(List.of(serv))
                .clienteNombre("Juan")
                .clienteTlf("3001234567")
                .clienteCorreo("juan@test.com")
                .build();

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> reservaService.pedirTurno(reserva));
        assertEquals("E09", ex.getCode());
        verify(reservaRepository, never()).save(any());
    }
}
