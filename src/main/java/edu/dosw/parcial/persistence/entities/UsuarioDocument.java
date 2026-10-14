package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.enums.Rol;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
@Data
@Builder
public class UsuarioDocument {

    @Id
    private String id;
    private String nombre;

    @Indexed(unique = true)
    private String correo;

    private String password;
    private Rol rol;

    public UsuarioDocument() {
    }

    public UsuarioDocument(String id, String nombre, String correo, String password, Rol rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
    }
}
