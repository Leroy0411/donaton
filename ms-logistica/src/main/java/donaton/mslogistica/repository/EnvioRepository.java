package donaton.mslogistica.repository;

import donaton.mslogistica.model.Envio;

import java.util.List;
import java.util.Optional;

/**
 * PATRÓN: Repository Pattern — Envíos
 */
public interface EnvioRepository {
    List<Envio> findAll();
    Optional<Envio> findById(Long id);
    List<Envio> findByEstado(String estado);
    Envio save(Envio envio);
    boolean deleteById(Long id);
}
