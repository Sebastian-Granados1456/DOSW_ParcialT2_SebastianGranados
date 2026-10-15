package edu.dosw.parcial.core.validators;

import edu.dosw.parcial.core.exceptions.BusinessRuleException;
import edu.dosw.parcial.core.models.Reserva;
import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorServicios extends ValidadorReserva {

    private final ServicioRepository servicioRepository;

    public ValidadorServicios() {
        this.servicioRepository = null;
    }

    @Autowired
    public ValidadorServicios(@Autowired(required = false) ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public void validar(Reserva reserva) {
        if (reserva.getServicios() == null || reserva.getServicios().isEmpty()) {
            throw new BusinessRuleException("E09", "Servicio inexistente");
        }

        if (servicioRepository != null) {
            for (Servicio s : reserva.getServicios()) {
                if (s == null || s.getCodigo() == null || s.getCodigo().isBlank()) {
                    throw new BusinessRuleException("E09", "Servicio inexistente");
                }
                ServicioDocument doc = servicioRepository.findByCodigo(s.getCodigo())
                        .orElseThrow(() -> new BusinessRuleException("E09", "Servicio inexistente"));

                if (s.getPrecio() == null) {
                    s.setPrecio(doc.getPrecio());
                }
                if (s.getEspecialidad() == null) {
                    s.setEspecialidad(doc.getEspecialidad());
                }
                if (s.getServicio() == null) {
                    s.setServicio(doc.getServicio());
                }
            }
        }

        ejecutarSiguiente(reserva);
    }
}
