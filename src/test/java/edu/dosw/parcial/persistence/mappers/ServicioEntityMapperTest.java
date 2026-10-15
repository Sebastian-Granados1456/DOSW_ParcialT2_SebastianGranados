package edu.dosw.parcial.persistence.mappers;

import edu.dosw.parcial.core.models.Servicio;
import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.models.enums.TipoServicio;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ServicioEntityMapperTest {

    private final ServicioEntityMapper servicioMapper = new ServicioEntityMapper();

    @Test
    void toDocument_servicioValido_conservaDatos() {
        Servicio servicio = Servicio.builder()
                .codigo("SER-001")
                .servicio(TipoServicio.CORTE_CLASICO)
                .precio(25000.0)
                .especialidad(Especialidad.CORTE)
                .build();

        ServicioDocument doc = servicioMapper.toDocument(servicio);

        assertEquals("SER-001", doc.getCodigo());
        assertEquals(25000.0, doc.getPrecio());
    }

    @Test
    void toDomain_documentoValido_conservaDatos() {
        ServicioDocument doc = ServicioDocument.builder()
                .codigo("SER-004")
                .servicio(TipoServicio.AFEITADO_NAVAJA)
                .precio(20000.0)
                .especialidad(Especialidad.BARBA)
                .build();

        Servicio servicio = servicioMapper.toDomain(doc);

        assertEquals("SER-004", servicio.getCodigo());
        assertEquals(Especialidad.BARBA, servicio.getEspecialidad());
    }

    @Test
    void toDomain_documentoNulo_retornaNull() {
        Servicio servicio = servicioMapper.toDomain(null);

        assertNull(servicio);
    }

    @Test
    void toDocument_servicioNulo_retornaNull() {
        ServicioDocument doc = servicioMapper.toDocument(null);

        assertNull(doc);
    }
}
