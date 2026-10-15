package edu.dosw.parcial.controller;

import edu.dosw.parcial.controller.dtos.request.LoginRequestDTO;
import edu.dosw.parcial.controller.dtos.response.TokenResponseDTO;
import edu.dosw.parcial.core.services.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_adminValido_retornaTokenConRolAdmin() {
        UserDetails admin = User.withUsername("admin@bobsbarber.com")
                .password("x")
                .roles("ADMIN")
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());

        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken("admin@bobsbarber.com", "ROLE_ADMIN")).thenReturn("token-admin");

        ResponseEntity<TokenResponseDTO> respuesta = authController.login(new LoginRequestDTO("admin@bobsbarber.com",
                "admin123"));

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals("token-admin", respuesta.getBody().token());
        assertEquals("Bearer", respuesta.getBody().tipo());
        assertEquals("ADMIN", respuesta.getBody().rol());
    }

    @Test
    void login_usuarioSinRoles_retornaRolCliente() {
        UserDetails cliente = new User("cliente1@gmail.com", "x", List.of());

        Authentication auth = new UsernamePasswordAuthenticationToken(cliente, null, List.of());

        when(authManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken("cliente1@gmail.com", "ROLE_CLIENTE")).thenReturn("token-cliente");

        ResponseEntity<TokenResponseDTO> respuesta = authController.login(new LoginRequestDTO("cliente1@gmail.com",
                "cliente123"));

        assertEquals("token-cliente", respuesta.getBody().token());
        assertEquals("CLIENTE", respuesta.getBody().rol());
    }

    @Test
    void login_credencialesInvalidas_lanzaBadCredentialsException() {
        LoginRequestDTO dto = new LoginRequestDTO("admin@bobsbarber.com", "malaClave");

        when(authManager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciales inválidas"));

        assertThrows(BadCredentialsException.class, () -> authController.login(dto));
        verify(jwtUtil, never()).generateToken(anyString(), anyString());
    }
}