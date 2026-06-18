package donaton.msdonaciones.service;

import donaton.msdonaciones.factory.DonacionFactoryProvider;
import donaton.msdonaciones.model.Donacion;
import donaton.msdonaciones.repository.DonacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para donaciones.
 *
 * Orquesta el uso del Repository Pattern (acceso a datos desacoplado)
 * y el Factory Method (creación de donaciones por tipo).
 */
@Service
public class DonacionService {

    private final DonacionRepository donacionRepository;
    private final DonacionFactoryProvider factoryProvider;

    public DonacionService(DonacionRepository donacionRepository,
                           DonacionFactoryProvider factoryProvider) {
        this.donacionRepository = donacionRepository;
        this.factoryProvider = factoryProvider;
    }

    public List<Donacion> obtenerTodas() {
        return donacionRepository.findAll();
    }

    public Optional<Donacion> obtenerPorId(Long id) {
        return donacionRepository.findById(id);
    }

    public List<Donacion> obtenerPorEstado(String estado) {
        return donacionRepository.findByEstado(estado.toUpperCase());
    }

    public List<Donacion> obtenerPorCentro(Long centroAcopioId) {
        return donacionRepository.findByCentroAcopioId(centroAcopioId);
    }

    /**
     * Crea una donación usando el Factory Method correspondiente al tipo.
     * La fábrica aplica las reglas de negocio específicas de cada tipo.
     */
    public Donacion crearDonacion(String tipo, String origen, Integer cantidad,
                                  Long centroAcopioId, String descripcion) {
        Donacion donacion = factoryProvider
                .obtenerFabrica(tipo)
                .crear(origen, cantidad, centroAcopioId, descripcion);
        return donacionRepository.save(donacion);
    }

    public Optional<Donacion> actualizarEstado(Long id, String nuevoEstado) {
        Optional<Donacion> opt = donacionRepository.findById(id);
        if (opt.isPresent()) {
            Donacion d = opt.get();
            d.setEstado(nuevoEstado.toUpperCase());
            donacionRepository.save(d);
            return Optional.of(d);
        }
        return Optional.empty();
    }

    public boolean eliminar(Long id) {
        if (!donacionRepository.existsById(id)) {
            return false;
        }
        donacionRepository.deleteById(id);
        return true;
    }
}
