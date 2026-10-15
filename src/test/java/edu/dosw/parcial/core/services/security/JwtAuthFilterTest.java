package edu.dosw.parcial.core.services.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UsuarioDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_tokenValido_autenticaUsuario() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-valido");

        Claims claims = mock(Claims.class);

        UserDetails cliente = User.withUsername("cliente1@gmail.com")
                .password("x")
                .roles("CLIENTE")
                .build();

        when(jwtUtil.isValid("token-valido")).thenReturn(true);
        when(jwtUtil.extractClaims("token-valido")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("cliente1@gmail.com");
        when(userDetailsService.loadUserByUsername("cliente1@gmail.com")).thenReturn(cliente);

        jwtAuthFilter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertEquals("cliente1@gmail.com", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Test
    void doFilter_sinHeader_noAutentica() throws Exception {
        jwtAuthFilter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtil, never()).isValid(anyString());
    }

    @Test
    void doFilter_tokenInvalido_noAutentica() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token-malo");

        when(jwtUtil.isValid("token-malo")).thenReturn(false);

        jwtAuthFilter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}