package donaton.mslogistica.repository;

import donaton.mslogistica.model.Envio;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EnvioRepositoryImpl implements EnvioRepository {

    private final List<Envio> almacen = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public List<Envio> findAll() { return new ArrayList<>(almacen); }

    @Override
    public Optional<Envio> findById(Long id) {
        return almacen.stream().filter(e -> e.getId().equals(id)).findFirst();
    }

    @Override
    public List<Envio> findByEstado(String estado) {
        List<Envio> res = new ArrayList<>();
        for (Envio e : almacen) {
            if (e.getEstado().equalsIgnoreCase(estado)) res.add(e);
        }
        return res;
    }

    @Override
    public Envio save(Envio envio) {
        if (envio.getId() == null) {
            envio.setId(secuencia.getAndIncrement());
            almacen.add(envio);
        } else {
            for (int i = 0; i < almacen.size(); i++) {
                if (almacen.get(i).getId().equals(envio.getId())) {
                    almacen.set(i, envio);
                    return envio;
                }
            }
        }
        return envio;
    }

    @Override
    public boolean deleteById(Long id) {
        return almacen.removeIf(e -> e.getId().equals(id));
    }
}
