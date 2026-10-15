package edu.dosw.parcial.controller.dtos.request;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String correo, @NotBlank String password) {
}
