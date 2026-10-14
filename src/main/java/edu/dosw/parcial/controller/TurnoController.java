package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.TurnoRequestDTO;
import edu.dosw.parcial.controller.dtos.response.TurnoResponseDTO;
import edu.dosw.parcial.controller.mappers.TurnoMapperIn;
import edu.dosw.parcial.controller.mappers.TurnoMapperOut;
import edu.dosw.parcial.core.models.Turno;
import edu.dosw.parcial.core.services.TurnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Turnos")
@RestController
@RequestMapping("/api/v1/turnos")
public class TurnoController {

    private final TurnoService turnoService;
    private final TurnoMapperIn turnoMapperIn;
    private final TurnoMapperOut turnoMapperOut;

    public TurnoController(TurnoService turnoService,
                           TurnoMapperIn turnoMapperIn,
                           TurnoMapperOut turnoMapperOut) {
        this.turnoService = turnoService;
        this.turnoMapperIn = turnoMapperIn;
        this.turnoMapperOut = turnoMapperOut;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TurnoResponseDTO> crearTurno(@Valid @RequestBody TurnoRequestDTO request) {
        Turno turno = turnoMapperIn.toDomain(request);
        Turno creado = turnoService.crearTurno(turno);
        TurnoResponseDTO response = turnoMapperOut.toResponse(creado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
