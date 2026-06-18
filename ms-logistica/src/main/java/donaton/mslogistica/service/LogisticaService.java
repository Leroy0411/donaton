package donaton.mslogistica.service;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import donaton.mslogistica.observer.EnvioObserver;
import donaton.mslogistica.repository.CentroAcopioRepository;
import donaton.mslogistica.repository.EnvioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de logística y distribución.
 *
 * Aplica el patrón Observer: cada cambio de estado en un envío
 * notifica automáticamente a todos los observadores registrados
 * (auditoría, notificaciones, métricas, etc.).
 */
@Service
public class LogisticaService {

    private final CentroAcopioRepository centroRepository;
    private final EnvioRepository envioRepository;
    private final List<EnvioObserver> observadores;  // Inyectados por Spring

    public LogisticaService(CentroAcopioRepository centroRepository,
                             EnvioRepository envioRepository,
                             List<EnvioObserver> observadores) {
        this.centroRepository = centroRepository;
        this.envioRepository  = envioRepository;
        this.observadores     = observadores;
    }

    // ── Centros de Acopio ────────────────────────────────────────────────

    public List<CentroAcopio> obtenerCentros() {
        return centroRepository.findAll();
    }

    public List<CentroAcopio> obtenerCentrosActivos() {
        return centroRepository.findByEstado("ACTIVO");
    }

    // Nota: "ACTIVO" ya está en mayúsculas en este servicio, por lo que
    // findByEstado(String) exacto de Spring Data JPA es consistente
    // siempre que las entidades se guarden siempre en mayúsculas (ver agregarCentro/actualizarOcupacion).

    public List<CentroAcopio> obtenerCentrosConCapacidad() {
        return centroRepository.findAll().stream()
                .filter(c -> "ACTIVO".equalsIgnoreCase(c.getEstado()) && c.tieneCapacidadDisponible())
                .toList();
    }

    public CentroAcopio agregarCentro(CentroAcopio centro) {
        if (centro.getEstado() == null || centro.getEstado().isBlank()) {
            centro.setEstado("ACTIVO");
        }
        if (centro.getOcupacionActual() == null) {
            centro.setOcupacionActual(0);
        }
        return centroRepository.save(centro);
    }

    public Optional<CentroAcopio> actualizarOcupacion(Long id, Integer nuevaOcupacion) {
        return centroRepository.findById(id).map(centro -> {
            centro.setOcupacionActual(nuevaOcupacion);
            centro.setEstado(nuevaOcupacion >= centro.getCapacidadMaxima() ? "SATURADO" : "ACTIVO");
            return centroRepository.save(centro);
        });
    }

    public boolean eliminarCentro(Long id) {
        if (!centroRepository.existsById(id)) {
            return false;
        }
        centroRepository.deleteById(id);
        return true;
    }

    // ── Envíos con patrón Observer ───────────────────────────────────────

    public List<Envio> obtenerEnvios() {
        return envioRepository.findAll();
    }

    public List<Envio> obtenerPorEstado(String estado) {
        return envioRepository.findByEstado(estado.toUpperCase());
    }

    public Envio crearEnvio(Envio envio) {
        envio.setEstado("PLANIFICADO");
        Envio creado = envioRepository.save(envio);
        notificarObservadores(creado, "NUEVO");
        return creado;
    }

    public Optional<Envio> despacharEnvio(Long id) {
        return envioRepository.findById(id).map(envio -> {
            String estadoAnterior = envio.getEstado();
            envio.marcarDespachado();
            envioRepository.save(envio);
            notificarObservadores(envio, estadoAnterior);
            return envio;
        });
    }

    public Optional<Envio> confirmarEntrega(Long id, String observaciones) {
        return envioRepository.findById(id).map(envio -> {
            String estadoAnterior = envio.getEstado();
            envio.marcarEntregado(observaciones);
            envioRepository.save(envio);
            notificarObservadores(envio, estadoAnterior);
            return envio;
        });
    }

    public Optional<Envio> cancelarEnvio(Long id, String motivo) {
        return envioRepository.findById(id).map(envio -> {
            String estadoAnterior = envio.getEstado();
            envio.cancelar(motivo);
            envioRepository.save(envio);
            notificarObservadores(envio, estadoAnterior);
            return envio;
        });
    }

    public boolean eliminarEnvio(Long id) {
        if (!envioRepository.existsById(id)) {
            return false;
        }
        envioRepository.deleteById(id);
        return true;
    }

    /**
     * Notifica a todos los observadores registrados.
     * La adición de nuevos observadores no requiere modificar este método.
     */
    private void notificarObservadores(Envio envio, String estadoAnterior) {
        for (EnvioObserver obs : observadores) {
            obs.onEnvioActualizado(envio, estadoAnterior);
        }
    }
}
