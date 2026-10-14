package edu.dosw.parcial.core.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, Object id) {
        super(String.format("No se encontró el recurso %s con identificador: %s", resourceName, id));
    }
}
