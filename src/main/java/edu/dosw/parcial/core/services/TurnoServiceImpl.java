package edu.dosw.parcial.core.services;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.exceptions.ResourceNotFoundException;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.TurnoDocument;
import edu.dosw.parcial.persistence.mappers.TurnoEntityMapper;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.TurnoRepository;
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
        if (diasDisponible == null || diasDisponible.isBlank()) {
            return false;
        }
        String texto = normalizar(diasDisponible);
        if (texto.contains("lunes a domingo") || texto.contains("todos los dias") || texto.equals("todos")) {
            return true;
        }
        if (texto.contains("lunes a sabado")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.SATURDAY.getValue();
        }
        if (texto.contains("lunes a viernes")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.FRIDAY.getValue();
        }
        if (texto.contains("lunes a jueves")) {
            return dia.getValue() >= DayOfWeek.MONDAY.getValue() && dia.getValue() <= DayOfWeek.THURSDAY.getValue();
        }
        if (texto.contains("martes a sabado")) {
            return dia.getValue() >= DayOfWeek.TUESDAY.getValue() && dia.getValue() <= DayOfWeek.SATURDAY.getValue();
        }
        if ((texto.contains("fin de semana") || texto.contains("fines de semana"))
                && (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY)) {
            return true;
        }
        return switch (dia) {
            case MONDAY -> texto.contains("lunes") || texto.contains("monday");
            case TUESDAY -> texto.contains("martes") || texto.contains("tuesday");
            case WEDNESDAY -> texto.contains("miercoles") || texto.contains("wednesday");
            case THURSDAY -> texto.contains("jueves") || texto.contains("thursday");
            case FRIDAY -> texto.contains("viernes") || texto.contains("friday");
            case SATURDAY -> texto.contains("sabado") || texto.contains("saturday");
            case SUNDAY -> texto.contains("domingo") || texto.contains("sunday");
        };
    }

    private boolean esHoraDisponible(LocalTime hora, String horario) {
        if (hora == null || horario == null || horario.isBlank()) {
            return false;
        }
        try {
            String[] partes = horario.split("(?i)\\s*(-|\\ba\\b|\\bto\\b)\\s*");
            if (partes.length < 2) {
                return false;
            }
            LocalTime inicio = parsearHora(partes[0].trim());
            LocalTime fin = parsearHora(partes[1].trim());
            return !hora.isBefore(inicio) && !hora.isAfter(fin);
        } catch (Exception e) {
            return false;
        }
    }

    private LocalTime parsearHora(String valor) {
        String texto = valor.trim().toUpperCase();
        boolean esPm = texto.contains("PM");
        boolean esAm = texto.contains("AM");
        String limpio = texto.replace("AM", "").replace("PM", "").trim();
        String[] partes = limpio.split(":");
        int horas = Integer.parseInt(partes[0].trim());
        int minutos = partes.length > 1 ? Integer.parseInt(partes[1].trim()) : 0;
        if (esPm && horas < 12) {
            horas += 12;
        }
        if (esAm && horas == 12) {
            horas = 0;
        }
        return LocalTime.of(horas, minutos);
    }

    private String normalizar(String input) {
        return input.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .trim();
    }
}
