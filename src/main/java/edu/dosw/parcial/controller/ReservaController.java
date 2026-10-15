package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.ReservaRequestDTO;
import edu.dosw.parcial.controller.dtos.response.ReservaResponseDTO;
import edu.dosw.parcial.controller.mappers.ReservaMapperIn;
import edu.dosw.parcial.controller.mappers.ReservaMapperOut;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Reservas")
@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapperIn reservaMapperIn;
    private final ReservaMapperOut reservaMapperOut;

    public ReservaController(ReservaService reservaService,
                             ReservaMapperIn reservaMapperIn,
                             ReservaMapperOut reservaMapperOut) {
        this.reservaService = reservaService;
        this.reservaMapperIn = reservaMapperIn;
        this.reservaMapperOut = reservaMapperOut;
    }

    @Operation(summary = "Pedir un turno", description = "Rol requerido: CLIENTE")
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> pedirTurno(@Valid @RequestBody ReservaRequestDTO request) {
        Reserva reserva = reservaMapperIn.toDomain(request);
        Reserva creada = reservaService.pedirTurno(reserva);
        ReservaResponseDTO response = reservaMapperOut.toResponse(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Ver mis turnos realizados", description = "Rol requerido: CLIENTE. El cliente se toma del token")
    public ResponseEntity<List<ReservaResponseDTO>> misReservas(Authentication authentication) {
        List<ReservaResponseDTO> response = reservaService.consultarMisReservas(authentication.getName()).stream()
                .map(reservaMapperOut::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Ver todos los turnos realizados", description = "Rol requerido: ADMIN")
    public ResponseEntity<List<ReservaResponseDTO>> todasLasReservas() {
        List<ReservaResponseDTO> response = reservaService.consultarTodas().stream()
                .map(reservaMapperOut::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
