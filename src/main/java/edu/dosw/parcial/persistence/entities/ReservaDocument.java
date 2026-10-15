package edu.dosw.parcial.persistence.entities;

import edu.dosw.parcial.core.models.Servicio;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reservas")
@Data
@Builder
public class ReservaDocument {

    @Id
    private String idReserva;
    private String turnoId;
    private String barberoNombre;
    private String clienteNombre;
    private String clienteTlf;
    private String clienteCorreo;
    private Double total;
    private Double precio;
    private String estado;
    private List<Servicio> servicios;
    private LocalDateTime fechaHora;

    public ReservaDocument() {
    }

    public ReservaDocument(String idReserva, String turnoId, String barberoNombre, String clienteNombre, String clienteTlf, String clienteCorreo, Double total, Double precio, String estado, List<Servicio> servicios, LocalDateTime fechaHora) {
        this.idReserva = idReserva;
        this.turnoId = turnoId;
        this.barberoNombre = barberoNombre;
        this.clienteNombre = clienteNombre;
        this.clienteTlf = clienteTlf;
        this.clienteCorreo = clienteCorreo;
        this.total = total;
        this.precio = precio;
        this.estado = estado;
        this.servicios = servicios;
        this.fechaHora = fechaHora;
    }
}
