package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.ReservaRequestDTO;
import edu.dosw.parcial.controller.dtos.response.ReservaResponseDTO;
import edu.dosw.parcial.controller.mappers.ReservaMapperIn;
import edu.dosw.parcial.controller.mappers.ReservaMapperOut;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.services.ReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ReservaResponseDTO> pedirTurno(@Valid @RequestBody ReservaRequestDTO request) {
        Reserva reserva = reservaMapperIn.toDomain(request);
        Reserva creada = reservaService.pedirTurno(reserva);
        ReservaResponseDTO response = reservaMapperOut.toResponse(creada);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
