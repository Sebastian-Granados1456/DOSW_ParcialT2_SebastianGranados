package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.response.ReservaResponseDTO;
import edu.dosw.parcial.controller.mappers.ReservaMapperIn;
import edu.dosw.parcial.controller.mappers.ReservaMapperOut;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.services.ReservaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservaMapperIn reservaMapperIn;

    @Mock
    private ReservaMapperOut reservaMapperOut;

    @InjectMocks
    private ReservaController reservaController;

    @Test
    void misReservas_clienteAutenticado_usaCorreoDelToken() {
        Authentication auth = new UsernamePasswordAuthenticationToken("cliente1@gmail.com", null);

        Reserva reserva = Reserva.builder()
                .idReserva("RES-001")
                .build();

        ReservaResponseDTO dto = new ReservaResponseDTO("RES-001", null, null, null, "cliente1@gmail.com", List.of(),
                25000.0, "CONFIRMADA", null);

        when(reservaService.consultarMisReservas("cliente1@gmail.com")).thenReturn(List.of(reserva));
        when(reservaMapperOut.toResponse(reserva)).thenReturn(dto);

        ResponseEntity<List<ReservaResponseDTO>> respuesta = reservaController.misReservas(auth);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(reservaService).consultarMisReservas("cliente1@gmail.com");
    }

    @Test
    void todasLasReservas_admin_retornaTodas() {
        Reserva reserva1 = Reserva.builder()
                .idReserva("RES-002")
                .build();

        Reserva reserva2 = Reserva.builder()
                .idReserva("RES-001")
                .build();

        ReservaResponseDTO dto1 = new ReservaResponseDTO("RES-002", null, null, null, null, List.of(), 0.0,
                "CONFIRMADA", null);
        ReservaResponseDTO dto2 = new ReservaResponseDTO("RES-001", null, null, null, null, List.of(), 0.0,
                "CONFIRMADA", null);

        when(reservaService.consultarTodas()).thenReturn(List.of(reserva1, reserva2));
        when(reservaMapperOut.toResponse(reserva1)).thenReturn(dto1);
        when(reservaMapperOut.toResponse(reserva2)).thenReturn(dto2);

        ResponseEntity<List<ReservaResponseDTO>> respuesta = reservaController.todasLasReservas();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(2, respuesta.getBody().size());
        assertEquals("RES-002", respuesta.getBody().get(0).idReserva());
    }
}
