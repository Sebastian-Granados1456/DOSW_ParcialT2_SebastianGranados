package edu.dosw.parcial.controller.handlers;

import edu.dosw.parcial.controller.dtos.response.ErrorResponse;
import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.exceptions.RecursoDuplicadoException;
import edu.dosw.parcial.core.exceptions.RecursoNoEncontradoException;
import edu.dosw.parcial.core.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidacion(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        log.warn("Validacion de input fallida: {}", errores);
        return ErrorResponse.of(400, "Datos de entrada invalidos", "VALIDATION_ERROR", errores, request.getRequestURI());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoEncontrado(RecursoNoEncontradoException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ErrorResponse.of(404, "No encontrado", "NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return ErrorResponse.of(404, "No encontrado", "NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflicto(RecursoDuplicadoException ex, HttpServletRequest request) {
        log.warn("Conflicto de negocio: {}", ex.getMessage());
        return ErrorResponse.of(409, "Conflicto", "CONFLICT", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(BusinessRuleException ex, HttpServletRequest request) {
        log.warn("Regla de negocio violada [{}] : {}", ex.getCode(), ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.of(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAutenticacion(AuthenticationException ex, HttpServletRequest request) {
        log.warn("Intento de login fallido en {}: {}", request.getRequestURI(), ex.getMessage());
        return ErrorResponse.of(401, "No autenticado", "UNAUTHORIZED", "Email o contraseña incorrectos", request.getRequestURI());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccesoDenegado(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("Acceso denegado en {}: {}", request.getRequestURI(), ex.getMessage());
        return ErrorResponse.of(403, "Acceso denegado", "FORBIDDEN", "No tienes permisos para realizar esta operacion", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error no controlado en {}: ", request.getRequestURI(), ex);
        return ErrorResponse.of(500, "Error interno", "INTERNAL_SERVER_ERROR", "Ocurrio un error inesperado en el servidor", request.getRequestURI());
    }
}
