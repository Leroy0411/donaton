package donaton.mslogistica.repository;

import donaton.mslogistica.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PATRÓN: Repository Pattern — Envíos
 * Extiende Spring Data JPA, proveyendo persistencia real sobre H2.
 */
@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByEstado(String estado);
}
