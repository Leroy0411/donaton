package donaton.msdonaciones.factory;

import donaton.msdonaciones.model.Donacion;
import org.springframework.stereotype.Component;

// ══════════════════════════════════════════════════════════
//  Fábricas concretas — una por tipo de donación
//  Cada subclase encapsula sus propias reglas de validación
// ══════════════════════════════════════════════════════════

/** Fábrica para donaciones de ropa y vestimenta */
@Component
class DonacionRopaFactory extends DonacionFactory {

    @Override
    public String getTipo() { return "ROPA"; }

    @Override
    protected Donacion construir(String origen, Integer cantidad,
                                 Long centroAcopioId, String descripcion) {
        return new Donacion(null, "ROPA", origen, cantidad, centroAcopioId, descripcion);
    }

    @Override
    protected void aplicarReglasTipo(Donacion donacion) {
        // Regla de negocio: lotes de ropa mayores a 100 unidades pasan directo a EN_PROCESO
        if (donacion.getCantidad() >= 100) {
            donacion.setEstado("EN_PROCESO");
        }
    }
}

/** Fábrica para donaciones de alimentos */
@Component
class DonacionAlimentoFactory extends DonacionFactory {

    @Override
    public String getTipo() { return "ALIMENTO"; }

    @Override
    protected Donacion construir(String origen, Integer cantidad,
                                 Long centroAcopioId, String descripcion) {
        return new Donacion(null, "ALIMENTO", origen, cantidad, centroAcopioId, descripcion);
    }

    @Override
    protected void aplicarReglasTipo(Donacion donacion) {
        // Regla de negocio: alimentos siempre inician en RECIBIDA para control de calidad
        donacion.setEstado("RECIBIDA");
    }
}

/** Fábrica para donaciones de insumos médicos */
@Component
class DonacionMedicoFactory extends DonacionFactory {

    @Override
    public String getTipo() { return "MEDICO"; }

    @Override
    protected Donacion construir(String origen, Integer cantidad,
                                 Long centroAcopioId, String descripcion) {
        return new Donacion(null, "MEDICO", origen, cantidad, centroAcopioId, descripcion);
    }

    @Override
    protected void aplicarReglasTipo(Donacion donacion) {
        // Regla de negocio: insumos médicos tienen prioridad alta, pasan a EN_PROCESO
        donacion.setEstado("EN_PROCESO");
    }
}

/** Fábrica para donaciones de artículos de higiene */
@Component
class DonacionHigieneFactory extends DonacionFactory {

    @Override
    public String getTipo() { return "HIGIENE"; }

    @Override
    protected Donacion construir(String origen, Integer cantidad,
                                 Long centroAcopioId, String descripcion) {
        return new Donacion(null, "HIGIENE", origen, cantidad, centroAcopioId, descripcion);
    }

    @Override
    protected void aplicarReglasTipo(Donacion donacion) {
        donacion.setEstado("RECIBIDA");
    }
}
