package edu.dosw.parcial.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class HealthControllerTest {

    @Test
    void shouldReturnStatusUp() {
        Map<String, String> response = new HealthController().health();

        assertEquals("UP", response.get("status"));
        assertEquals("DOSW-ParcialT2", response.get("service"));
    }
}
