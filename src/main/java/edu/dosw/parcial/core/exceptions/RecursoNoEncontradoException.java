package edu.dosw.parcial.core.exceptions;

public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String message) {
        super(message);
    }

    public RecursoNoEncontradoException(String resourceName, Object id) {
        super(String.format("No se encontró el recurso %s con identificador: %s", resourceName, id));
    }
}
