package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Usuario;
import edu.dosw.parcial.core.models.enums.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UsuarioEntityMapperTest {

    private final UsuarioEntityMapper usuarioMapper = new UsuarioEntityMapper();

    @Test
    void toDocument_usuarioValido_conservaDatos() {
        Usuario usuario = Usuario.builder()
                .id("USR-001")
                .nombre("Administrador")
                .correo("admin@bobsbarber.com")
                .password("hash")
                .rol(Rol.ROLE_ADMIN)
                .build();

        UsuarioDocument doc = usuarioMapper.toDocument(usuario);

        assertEquals("admin@bobsbarber.com", doc.getCorreo());
        assertEquals(Rol.ROLE_ADMIN, doc.getRol());
    }

    @Test
    void toDomain_documentoValido_conservaDatos() {
        UsuarioDocument doc = UsuarioDocument.builder()
                .id("USR-002")
                .nombre("Cliente 1")
                .correo("cliente1@gmail.com")
                .password("hash")
                .rol(Rol.ROLE_CLIENTE)
                .build();

        Usuario usuario = usuarioMapper.toDomain(doc);

        assertEquals("cliente1@gmail.com", usuario.getCorreo());
        assertEquals(Rol.ROLE_CLIENTE, usuario.getRol());
    }

    @Test
    void toDomain_documentoNulo_retornaNull() {
        Usuario usuario = usuarioMapper.toDomain(null);

        assertNull(usuario);
    }

    @Test
    void toDocument_usuarioNulo_retornaNull() {
        UsuarioDocument doc = usuarioMapper.toDocument(null);

        assertNull(doc);
    }
}
