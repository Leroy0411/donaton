package donaton.mslogistica.repository;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CentroAcopioRepositoryImpl implements CentroAcopioRepository {

    private final List<CentroAcopio> almacen = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public List<CentroAcopio> findAll() { return new ArrayList<>(almacen); }

    @Override
    public Optional<CentroAcopio> findById(Long id) {
        return almacen.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public List<CentroAcopio> findByEstado(String estado) {
        List<CentroAcopio> res = new ArrayList<>();
        for (CentroAcopio c : almacen) {
            if (c.getEstado().equalsIgnoreCase(estado)) res.add(c);
        }
        return res;
    }

    @Override
    public CentroAcopio save(CentroAcopio centro) {
        if (centro.getId() == null) {
            centro.setId(secuencia.getAndIncrement());
            almacen.add(centro);
        } else {
            for (int i = 0; i < almacen.size(); i++) {
                if (almacen.get(i).getId().equals(centro.getId())) {
                    almacen.set(i, centro);
                    return centro;
                }
            }
        }
        return centro;
    }

    @Override
    public boolean deleteById(Long id) {
        return almacen.removeIf(c -> c.getId().equals(id));
    }
}
