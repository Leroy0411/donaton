package donaton.mslogistica.repository;

import donaton.mslogistica.model.CentroAcopio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PATRÓN: Repository Pattern — Centros de Acopio
 * Extiende Spring Data JPA, proveyendo persistencia real sobre H2.
 */
@Repository
public interface CentroAcopioRepository extends JpaRepository<CentroAcopio, Long> {
    List<CentroAcopio> findByEstado(String estado);
}
