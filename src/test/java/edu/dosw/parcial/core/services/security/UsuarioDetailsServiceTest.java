package edu.dosw.parcial.core.services.security;

import edu.dosw.parcial.core.models.enums.Rol;
import edu.dosw.parcial.persistence.entities.UsuarioDocument;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void loadUserByUsername_adminExiste_retornaRolAdmin() {
        UsuarioDocument admin = UsuarioDocument.builder()
                .correo("admin@bobsbarber.com")
                .password("hash")
                .rol(Rol.ROLE_ADMIN)
                .build();

        when(repository.findByCorreo("admin@bobsbarber.com")).thenReturn(Optional.of(admin));

        UserDetails user = usuarioDetailsService.loadUserByUsername("admin@bobsbarber.com");

        assertEquals("admin@bobsbarber.com", user.getUsername());
        assertEquals("ROLE_ADMIN", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_usuarioSinRol_quedaComoCliente() {
        UsuarioDocument usuario = UsuarioDocument.builder()
                .correo("cliente1@gmail.com")
                .password("hash")
                .build();

        when(repository.findByCorreo("cliente1@gmail.com")).thenReturn(Optional.of(usuario));

        UserDetails user = usuarioDetailsService.loadUserByUsername("cliente1@gmail.com");

        assertEquals("ROLE_CLIENTE", user.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaUsernameNotFoundException() {
        when(repository.findByCorreo("nadie@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                usuarioDetailsService.loadUserByUsername("nadie@gmail.com"));
    }
}