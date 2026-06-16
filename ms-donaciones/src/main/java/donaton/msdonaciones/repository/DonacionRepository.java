package donaton.msdonaciones.repository;

import donaton.msdonaciones.model.Donacion;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN: Repository Pattern
 * ─────────────────────────────────────────────────────────────────────────
 * Define el contrato de acceso a datos para Donacion, desacoplando
 * completamente la lógica de negocio de la capa de persistencia.
 *
 * Beneficio: la lógica de servicio nunca accede directamente al almacén de
 * datos; en su lugar invoca métodos de este repositorio. Permite cambiar
 * el motor de base de datos (memoria → PostgreSQL → MongoDB) sin modificar
 * ninguna clase de servicio ni controller.
 */
public interface DonacionRepository {

    List<Donacion> findAll();

    Optional<Donacion> findById(Long id);

    List<Donacion> findByEstado(String estado);

    List<Donacion> findByCentroAcopioId(Long centroAcopioId);

    Donacion save(Donacion donacion);

    boolean deleteById(Long id);
}
