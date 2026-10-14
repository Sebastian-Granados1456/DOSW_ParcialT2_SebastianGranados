package edu.dosw.parcial.persistence.repositories;

import edu.dosw.parcial.persistence.entities.ReservaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends MongoRepository<ReservaDocument, String> {

    List<ReservaDocument> findByClienteCorreoOrderByFechaHoraDesc(String clienteCorreo);

    List<ReservaDocument> findAllByOrderByFechaHoraDesc();

    long countByFechaHora(LocalDateTime fechaHora);
}
