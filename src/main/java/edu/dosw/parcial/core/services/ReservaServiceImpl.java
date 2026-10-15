package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.validators.ValidadorBarbero;
import edu.dosw.parcial.core.validators.ValidadorCliente;
import edu.dosw.parcial.core.validators.ValidadorFranjas;
import edu.dosw.parcial.core.validators.ValidadorServicios;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);

    private final ReservaRepository reservaRepository;
    private final TurnoRepository turnoRepository;
    private final ServicioRepository servicioRepository;
    private final BarberoRepository barberoRepository;
    private final ReservaEntityMapper reservaEntityMapper;
    private final TurnoEntityMapper turnoEntityMapper;
    private final ServicioEntityMapper servicioEntityMapper;
    private final ValidadorFranjas validadorFranjas;
    private final ValidadorBarbero validadorBarbero;
    private final ValidadorCliente validadorCliente;
    private final ValidadorServicios validadorServicios;

    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              TurnoRepository turnoRepository,
                              ServicioRepository servicioRepository,
                              BarberoRepository barberoRepository,
                              ReservaEntityMapper reservaEntityMapper,
                              TurnoEntityMapper turnoEntityMapper,
                              ServicioEntityMapper servicioEntityMapper,
                              ValidadorFranjas validadorFranjas,
                              ValidadorBarbero validadorBarbero,
                              ValidadorCliente validadorCliente,
                              ValidadorServicios validadorServicios) {
        this.reservaRepository = reservaRepository;
        this.turnoRepository = turnoRepository;
        this.servicioRepository = servicioRepository;
        this.barberoRepository = barberoRepository;
        this.reservaEntityMapper = reservaEntityMapper;
        this.turnoEntityMapper = turnoEntityMapper;
        this.servicioEntityMapper = servicioEntityMapper;
        this.validadorFranjas = validadorFranjas;
        this.validadorBarbero = validadorBarbero;
        this.validadorCliente = validadorCliente;
        this.validadorServicios = validadorServicios;
    }

    @Override
    public Reserva pedirTurno(Reserva reserva) {
        if (reserva.getTurnoAsociado() == null || reserva.getTurnoAsociado().getIdTurno() == null || reserva.getTurnoAsociado().getIdTurno().isBlank()) {
            throw new BusinessRuleException("E01", "Franja llena o turno no disponible");
        }

        TurnoDocument turnoDoc = turnoRepository.findById(reserva.getTurnoAsociado().getIdTurno())
                .orElseThrow(() -> new BusinessRuleException("E01", "Franja llena o turno no disponible"));
        Turno turnoReal = turnoEntityMapper.toDomain(turnoDoc);
        reserva.setTurnoAsociado(turnoReal);

        if (reserva.getFechaHora() == null && turnoReal.getFecha() != null && turnoReal.getHora() != null) {
            reserva.setFechaHora(LocalDateTime.of(turnoReal.getFecha(), turnoReal.getHora()));
        }

        if (reserva.getServicios() == null || reserva.getServicios().isEmpty()) {
            throw new BusinessRuleException("E09", "Servicio inexistente");
        }

        List<Servicio> serviciosReales = new ArrayList<>();
        for (Servicio s : reserva.getServicios()) {
            if (s.getCodigo() == null || s.getCodigo().isBlank()) {
                throw new BusinessRuleException("E09", "Servicio inexistente");
            }
            ServicioDocument servDoc = servicioRepository.findByCodigo(s.getCodigo())
                    .orElseThrow(() -> new BusinessRuleException("E09", "Servicio inexistente: " + s.getCodigo()));
            serviciosReales.add(servicioEntityMapper.toDomain(servDoc));
        }
        reserva.setServicios(serviciosReales);

        String barberoDeseado = reserva.getBarberoNombre() != null && !reserva.getBarberoNombre().isBlank()
                ? reserva.getBarberoNombre()
                : (turnoReal.getBarberoNombre() != null ? turnoReal.getBarberoNombre() : null);

        if (barberoDeseado != null && !barberoDeseado.isBlank()) {
            barberoRepository.findByNombre(barberoDeseado).ifPresent(b -> {
                turnoReal.setBarberoNombre(b.getNombre());
                reserva.setBarberoNombre(b.getNombre());
            });
        }

        validadorFranjas.setSiguiente(validadorBarbero);
        validadorBarbero.setSiguiente(validadorCliente);
        validadorCliente.setSiguiente(validadorServicios);
        validadorServicios.setSiguiente(null);

        try {
            validadorFranjas.validar(reserva);
        } catch (BusinessRuleException e) {
            log.warn("Error de regla de negocio [{}] al pedir turno: {}", e.getCode(), e.getMessage());
            throw e;
        }

        double total = serviciosReales.stream()
                .filter(s -> s.getPrecio() != null)
                .mapToDouble(Servicio::getPrecio)
                .sum();
        reserva.setTotal(total);
        reserva.setPrecio(total);

        turnoDoc.setEstado("OCUPADO");
        turnoRepository.save(turnoDoc);
        turnoReal.setEstado("OCUPADO");

        reserva.setEstado("CONFIRMADA");
        if (reserva.getFechaHora() == null) {
            reserva.setFechaHora(LocalDateTime.now());
        }

        ReservaDocument doc = reservaEntityMapper.toDocument(reserva);
        ReservaDocument saved = reservaRepository.save(doc);

        reserva.setIdReserva(saved != null ? saved.getIdReserva() : null);

        log.info("Reserva {} creada exitosamente para {}", reserva.getIdReserva(), reserva.getClienteCorreo());

        Reserva resultado = reservaEntityMapper.toDomain(saved);
        if (resultado != null) {
            return resultado;
        }
        return reserva;
    }

    @Override
    public List<Reserva> consultarMisReservas(String correo) {
        log.info("Consultando reservas del cliente {}", correo);
        return reservaRepository.findByClienteCorreoOrderByFechaHoraDesc(correo).stream()
                .map(reservaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reserva> consultarTodas() {
        log.info("Consultando todas las reservas");
        return reservaRepository.findAllByOrderByFechaHoraDesc().stream()
                .map(reservaEntityMapper::toDomain)
                .toList();
    }
}
