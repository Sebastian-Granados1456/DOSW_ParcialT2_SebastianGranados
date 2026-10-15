package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.controller.handlers.GlobalExceptionHandler;
import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.exceptions.RecursoDuplicadoException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/reservas");

    @Test
    void handleValidacion_camposInvalidos_retorna400() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("dto", "correo", "es obligatorio")));

        ErrorResponse respuesta = handler.handleValidacion(ex, request);

        assertEquals(400, respuesta.status());
        assertTrue(respuesta.message().contains("correo"));
    }

    @Test
    void handleBusinessRule_franjaLlena_retornaEstadoYCodigo() {
        BusinessRuleException ex = new BusinessRuleException("E01", "Franja llena", HttpStatus.CONFLICT);

        ResponseEntity<ErrorResponse> respuesta = handler.handleBusinessRule(ex, request);

        assertEquals(HttpStatus.CONFLICT, respuesta.getStatusCode());
        assertEquals("E01", respuesta.getBody().code());
    }

    @Test
    void handleNoEncontrado_recursoNoExiste_retorna404() {
        ErrorResponse respuesta = handler.handleNoEncontrado(new RecursoNoEncontradoException("Barbero", "B1"),
                request);

        assertEquals(404, respuesta.status());
    }

    @Test
    void handleResourceNotFound_recursoNoExiste_retorna404() {
        ErrorResponse respuesta = handler.handleResourceNotFound(new ResourceNotFoundException("Barbero", "B1"),
                request);

        assertEquals(404, respuesta.status());
    }

    @Test
    void handleConflicto_recursoDuplicado_retorna409() {
        ErrorResponse respuesta = handler.handleConflicto(new RecursoDuplicadoException("Turno duplicado"), request);

        assertEquals(409, respuesta.status());
    }

    @Test
    void handleAutenticacion_credencialesInvalidas_retorna401() {
        ErrorResponse respuesta = handler.handleAutenticacion(new BadCredentialsException("Bad credentials"),
                request);

        assertEquals(401, respuesta.status());
        assertEquals("Email o contraseña incorrectos", respuesta.message());
    }

    @Test
    void handleAccesoDenegado_rolIncorrecto_retorna403() {
        ErrorResponse respuesta = handler.handleAccesoDenegado(new AccessDeniedException("denegado"), request);

        assertEquals(403, respuesta.status());
    }

    @Test
    void handleGeneral_errorInesperado_retorna500() {
        ErrorResponse respuesta = handler.handleGeneral(new RuntimeException("falla"), request);

        assertEquals(500, respuesta.status());
    }
}