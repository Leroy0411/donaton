package donaton.msdonaciones.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio que representa una donación en el sistema Donaton.
 * Soporta los tipos: ROPA, ALIMENTO, MEDICO, HIGIENE.
 * Ciclo de vida: RECIBIDA → EN_PROCESO → DISTRIBUIDA.
 */
public class Donacion {

    private Long id;
    private String tipo;          // ROPA, ALIMENTO, MEDICO, HIGIENE
    private String origen;
    private Integer cantidad;
    private Long centroAcopioId;
    private String estado;        // RECIBIDA, EN_PROCESO, DISTRIBUIDA
    private String descripcion;
    private LocalDateTime fechaDonacion;

    public Donacion() {}

    public Donacion(Long id, String tipo, String origen, Integer cantidad,
                    Long centroAcopioId, String descripcion) {
        this.id = id;
        this.tipo = tipo;
        this.origen = origen;
        this.cantidad = cantidad;
        this.centroAcopioId = centroAcopioId;
        this.descripcion = descripcion;
        this.estado = "RECIBIDA";
        this.fechaDonacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Long getCentroAcopioId() { return centroAcopioId; }
    public void setCentroAcopioId(Long centroAcopioId) { this.centroAcopioId = centroAcopioId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaDonacion() { return fechaDonacion; }
    public void setFechaDonacion(LocalDateTime fechaDonacion) { this.fechaDonacion = fechaDonacion; }
}
