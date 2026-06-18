package donaton.msdonaciones.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una donación en el sistema Donaton.
 * Persistida en base de datos H2 mediante Spring Data JPA (Hibernate).
 * Soporta los tipos: ROPA, ALIMENTO, MEDICO, HIGIENE.
 * Ciclo de vida: RECIBIDA → EN_PROCESO → DISTRIBUIDA.
 */
@Entity
@Table(name = "donaciones")
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String tipo;          // ROPA, ALIMENTO, MEDICO, HIGIENE

    @NotBlank
    @Column(nullable = false)
    private String origen;

    @Min(1)
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "centro_acopio_id", nullable = false)
    private Long centroAcopioId;

    @Column(nullable = false, length = 20)
    private String estado;        // RECIBIDA, EN_PROCESO, DISTRIBUIDA

    @Column(length = 500)
    private String descripcion;

    @Column(name = "fecha_donacion", nullable = false)
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
