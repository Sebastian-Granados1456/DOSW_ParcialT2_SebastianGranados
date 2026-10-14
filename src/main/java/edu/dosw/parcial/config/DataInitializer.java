package edu.dosw.parcial.config;

import edu.dosw.parcial.core.models.enums.Especialidad;
import edu.dosw.parcial.core.models.enums.Rol;
import edu.dosw.parcial.core.models.enums.TipoServicio;
import edu.dosw.parcial.persistence.entities.BarberoDocument;
import edu.dosw.parcial.persistence.entities.ServicioDocument;
import edu.dosw.parcial.persistence.entities.UsuarioDocument;
import edu.dosw.parcial.persistence.repositories.BarberoRepository;
import edu.dosw.parcial.persistence.repositories.ServicioRepository;
import edu.dosw.parcial.persistence.repositories.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ServicioRepository servicioRepository;
    private final BarberoRepository barberoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ServicioRepository servicioRepository,
                           BarberoRepository barberoRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.servicioRepository = servicioRepository;
        this.barberoRepository = barberoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (servicioRepository.count() == 0) {
            log.info("Inicializando servicios oficiales...");
            List<ServicioDocument> servicios = List.of(
                    ServicioDocument.builder().codigo("SER-001").servicio(TipoServicio.CORTE_CLASICO).precio(25000.0).especialidad(Especialidad.CORTE).build(),
                    ServicioDocument.builder().codigo("SER-002").servicio(TipoServicio.CORTE_BARBA).precio(38000.0).especialidad(Especialidad.CORTE_CON_BARBA).build(),
                    ServicioDocument.builder().codigo("SER-003").servicio(TipoServicio.DISENO_CEJAS).precio(15000.0).especialidad(Especialidad.CEJAS).build(),
                    ServicioDocument.builder().codigo("SER-004").servicio(TipoServicio.AFEITADO_NAVAJA).precio(20000.0).especialidad(Especialidad.BARBA).build(),
                    ServicioDocument.builder().codigo("SER-005").servicio(TipoServicio.TRATAMIENTO_CAPILAR).precio(45000.0).especialidad(Especialidad.CAPILAR).build()
            );
            for (ServicioDocument s : servicios) {
                servicioRepository.save(s);
            }
        }

        if (barberoRepository.count() == 0) {
            log.info("Inicializando barberos oficiales...");
            List<BarberoDocument> barberos = List.of(
                    BarberoDocument.builder().nombre("Miguel").especialidades(List.of(Especialidad.CORTE, Especialidad.CORTE_CON_BARBA)).diasDisponible("Lunes a Jueves").horario("10:00 AM - 5:00 PM").build(),
                    BarberoDocument.builder().nombre("Laura").especialidades(List.of(Especialidad.CAPILAR, Especialidad.CEJAS)).diasDisponible("Lunes a Viernes").horario("10:00 AM - 5:00 PM").build(),
                    BarberoDocument.builder().nombre("Andrés").especialidades(List.of(Especialidad.CORTE, Especialidad.BARBA)).diasDisponible("Martes, Jueves y Fines de Semana").horario("10:00 AM - 2:00 PM").build()
            );
            for (BarberoDocument b : barberos) {
                barberoRepository.save(b);
            }
        }

        if (usuarioRepository.count() == 0) {
            log.info("Inicializando usuarios semilla...");
            List<UsuarioDocument> usuarios = List.of(
                    UsuarioDocument.builder().nombre("Administrador").correo("admin@bobsbarber.com").password(passwordEncoder.encode("admin123")).rol(Rol.ROLE_ADMIN).build(),
                    UsuarioDocument.builder().nombre("Cliente 1").correo("cliente1@gmail.com").password(passwordEncoder.encode("cliente123")).rol(Rol.ROLE_CLIENTE).build()
            );
            for (UsuarioDocument u : usuarios) {
                usuarioRepository.save(u);
            }
        }
    }
}
