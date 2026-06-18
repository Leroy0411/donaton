package donaton.msdonaciones.repository;

import donaton.msdonaciones.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PATRÓN: Repository Pattern
 * ─────────────────────────────────────────────────────────────────────────
 * Extiende Spring Data JPA (JpaRepository), lo que provee persistencia real
 * sobre la base de datos H2 (CRUD, paginación, etc.) sin necesidad de
 * escribir SQL manual. Desacopla completamente la lógica de negocio
 * (DonacionService) de los detalles del motor de persistencia: cambiar de
 * H2 a PostgreSQL/MySQL solo requiere ajustar application.properties.
 */
@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    List<Donacion> findByEstado(String estado);

    List<Donacion> findByCentroAcopioId(Long centroAcopioId);
}
