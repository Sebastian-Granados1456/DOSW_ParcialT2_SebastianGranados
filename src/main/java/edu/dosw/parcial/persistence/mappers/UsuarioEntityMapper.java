package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.persistence.entities.UsuarioDocument;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {

    public Usuario toDomain(UsuarioDocument document) {
        if (document == null) {
            return null;
        }
        return Usuario.builder()
                .id(document.getId())
                .nombre(document.getNombre())
                .correo(document.getCorreo())
                .password(document.getPassword())
                .rol(document.getRol())
                .build();
    }

    public UsuarioDocument toDocument(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioDocument.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .correo(domain.getCorreo())
                .password(domain.getPassword())
                .rol(domain.getRol())
                .build();
    }
}
