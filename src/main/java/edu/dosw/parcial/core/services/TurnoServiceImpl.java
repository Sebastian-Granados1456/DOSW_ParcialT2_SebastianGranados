package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.exceptions.ResourceNotFoundException;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.mappers.TurnoEntityMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
import edu.dosw.parcial.core.utils.HorarioUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Service
public class TurnoServiceImpl implements TurnoService {

    private static final Logger log = LoggerFactory.getLogger(TurnoServiceImpl.class);

    private final BarberoRepository barberoRepository;
    private final TurnoRepository turnoRepository;
    private final TurnoEntityMapper turnoEntityMapper;

    public TurnoServiceImpl(BarberoRepository barberoRepository,
                            TurnoRepository turnoRepository,
                            TurnoEntityMapper turnoEntityMapper) {
        this.barberoRepository = barberoRepository;
        this.turnoRepository = turnoRepository;
        this.turnoEntityMapper = turnoEntityMapper;
    }

    @Override
    public Turno crearTurno(Turno turno) {
        BarberoDocument barbero = barberoRepository.findByNombre(turno.getBarberoNombre())
                .orElseThrow(() -> {
                    log.warn("Barbero no encontrado: {}", turno.getBarberoNombre());
                    return new ResourceNotFoundException("Barbero no encontrado: " + turno.getBarberoNombre());
                });

        if (!esDiaDisponible(turno.getFecha().getDayOfWeek(), barbero.getDiasDisponible())
                || !esHoraDisponible(turno.getHora(), barbero.getHorario())) {
            log.warn("El barbero no atiende en el horario o día especificado: barbero={}, fecha={}, hora={}",
                    turno.getBarberoNombre(), turno.getFecha(), turno.getHora());
            throw new BusinessRuleException("E02", "El barbero no atiende en el horario o día especificado", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (turnoRepository.existsByBarberoNombreAndFechaAndHora(turno.getBarberoNombre(), turno.getFecha(), turno.getHora())) {
            log.warn("Ya existe un turno programado para este barbero en esa fecha y hora: barbero={}, fecha={}, hora={}",
                    turno.getBarberoNombre(), turno.getFecha(), turno.getHora());
            throw new BusinessRuleException("E01", "Ya existe un turno programado para este barbero en esa fecha y hora", HttpStatus.CONFLICT);
        }

        turno.setEstado("DISPONIBLE");
        TurnoDocument doc = turnoEntityMapper.toDocument(turno);
        TurnoDocument saved = turnoRepository.save(doc);

        log.info("Turno creado exitosamente para el barbero {} en fecha {} {}",
                turno.getBarberoNombre(), turno.getFecha(), turno.getHora());

        Turno resultado = turnoEntityMapper.toDomain(saved);
        if (resultado != null) {
            return resultado;
        }
        turno.setIdTurno(saved != null ? saved.getIdTurno() : null);
        return turno;
    }

    private boolean esDiaDisponible(DayOfWeek dia, String diasDisponible) {
        return HorarioUtil.esDiaDisponible(dia, diasDisponible);
    }

    private boolean esHoraDisponible(LocalTime hora, String horario) {
        return HorarioUtil.esHoraDisponible(hora, horario);
    }
}
