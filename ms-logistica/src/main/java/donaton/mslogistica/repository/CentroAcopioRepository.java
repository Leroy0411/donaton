package donaton.mslogistica.repository;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN: Repository Pattern — Centros de Acopio
 */
public interface CentroAcopioRepository {
    List<CentroAcopio> findAll();
    Optional<CentroAcopio> findById(Long id);
    List<CentroAcopio> findByEstado(String estado);
    CentroAcopio save(CentroAcopio centro);
    boolean deleteById(Long id);
}
