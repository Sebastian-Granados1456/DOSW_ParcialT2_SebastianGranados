package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.TurnoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends MongoRepository<TurnoDocument, String> {

    boolean existsByBarberoNombreAndFechaAndHora(String barberoNombre, LocalDate fecha, LocalTime hora);

    List<TurnoDocument> findByFechaAndHora(LocalDate fecha, LocalTime hora);
}
