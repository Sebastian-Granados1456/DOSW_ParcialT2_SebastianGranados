package edu.dosw.parcial.core.models;

import edu.dosw.parcial.core.models.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Usuario {

    private String id;
    private String nombre;
    private String correo;
    private String password;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String correo, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }
}
