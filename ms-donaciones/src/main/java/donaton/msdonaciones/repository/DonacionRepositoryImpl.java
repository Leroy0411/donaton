package donaton.msdonaciones.repository;

import donaton.msdonaciones.model.Donacion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación en memoria del repositorio de donaciones.
 * En producción se reemplazaría por una implementación con JPA/PostgreSQL
 * sin necesidad de modificar ninguna clase de servicio (Open/Closed Principle).
 */
@Repository
public class DonacionRepositoryImpl implements DonacionRepository {

    private final List<Donacion> almacen = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public List<Donacion> findAll() {
        return new ArrayList<>(almacen);
    }

    @Override
    public Optional<Donacion> findById(Long id) {
        return almacen.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Donacion> findByEstado(String estado) {
        List<Donacion> resultado = new ArrayList<>();
        for (Donacion d : almacen) {
            if (d.getEstado().equalsIgnoreCase(estado)) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    @Override
    public List<Donacion> findByCentroAcopioId(Long centroAcopioId) {
        List<Donacion> resultado = new ArrayList<>();
        for (Donacion d : almacen) {
            if (d.getCentroAcopioId().equals(centroAcopioId)) {
                resultado.add(d);
            }
        }
        return resultado;
    }

    @Override
    public Donacion save(Donacion donacion) {
        if (donacion.getId() == null) {
            donacion.setId(secuencia.getAndIncrement());
            almacen.add(donacion);
        } else {
            for (int i = 0; i < almacen.size(); i++) {
                if (almacen.get(i).getId().equals(donacion.getId())) {
                    almacen.set(i, donacion);
                    return donacion;
                }
            }
        }
        return donacion;
    }

    @Override
    public boolean deleteById(Long id) {
        return almacen.removeIf(d -> d.getId().equals(id));
    }
}
