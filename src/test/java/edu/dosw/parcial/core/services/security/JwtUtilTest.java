package edu.dosw.parcial.core.services.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "TXkgc3VwZXIgc2VjcmV0IGtleSBwYXJhIGVsIHJlc3RhdXJhbnRl");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
    }

    @Test
    void generateToken_datosValidos_tokenValido() {
        String token = jwtUtil.generateToken("admin@bobsbarber.com", "ROLE_ADMIN");

        assertTrue(jwtUtil.isValid(token));
    }

    @Test
    void extractClaims_tokenValido_retornaCorreoYRol() {
        String token = jwtUtil.generateToken("admin@bobsbarber.com", "ROLE_ADMIN");

        assertEquals("admin@bobsbarber.com", jwtUtil.extractClaims(token).getSubject());
        assertEquals("ROLE_ADMIN", jwtUtil.extractClaims(token).get("rol"));
    }

    @Test
    void isValid_tokenAlterado_retornaFalse() {
        String token = jwtUtil.generateToken("cliente1@gmail.com", "ROLE_CLIENTE") + "x";

        assertFalse(jwtUtil.isValid(token));
    }

    @Test
    void isValid_tokenVencido_retornaFalse() {
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1000L);

        String token = jwtUtil.generateToken("cliente1@gmail.com", "ROLE_CLIENTE");

        assertFalse(jwtUtil.isValid(token));
    }
}
