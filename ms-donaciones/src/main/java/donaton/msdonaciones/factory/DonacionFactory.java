package donaton.msdonaciones.factory;

import donaton.msdonaciones.model.Donacion;

/**
 * PATRÓN: Factory Method (GoF - Creacional)
 * ─────────────────────────────────────────────────────────────────────────
 * Define la interfaz para crear objetos Donacion, delegando la lógica de
 * construcción y validación a las subclases concretas según el tipo de
 * donación (Ropa, Alimento, Médico, Higiene).
 *
 * Beneficio: agregar un nuevo tipo de donación solo requiere implementar
 * una nueva subclase, sin modificar el código existente (Open/Closed Principle).
 */
public abstract class DonacionFactory {

    /**
     * Factory Method: crea y valida una donación del tipo correspondiente.
     */
    public final Donacion crear(String origen, Integer cantidad,
                                Long centroAcopioId, String descripcion) {
        validarParametros(origen, cantidad);
        Donacion donacion = construir(origen, cantidad, centroAcopioId, descripcion);
        aplicarReglasTipo(donacion);
        return donacion;
    }

    /** Construye la instancia concreta con el tipo definido por la subclase. */
    protected abstract Donacion construir(String origen, Integer cantidad,
                                          Long centroAcopioId, String descripcion);

    /** Aplica reglas de negocio específicas al tipo de donación. */
    protected abstract void aplicarReglasTipo(Donacion donacion);

    /** Tipo de donación que produce esta fábrica. */
    public abstract String getTipo();

    private void validarParametros(String origen, Integer cantidad) {
        if (origen == null || origen.isBlank()) {
            throw new IllegalArgumentException("El origen de la donación es requerido");
        }
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }
}
