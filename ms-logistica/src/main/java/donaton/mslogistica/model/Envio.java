package donaton.mslogistica.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un envío de logística en la plataforma Donaton.
 * Persistida en base de datos H2 mediante Spring Data JPA (Hibernate).
 */
@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "centro_origen_id", nullable = false)
    private Long centroOrigenId;

    @Column(name = "necesidad_id")
    private Long necesidadId;

    @Column(name = "destino_descripcion", nullable = false)
    private String destinoDescripcion;

    @Column(name = "responsable_transporte")
    private String responsableTransporte;

    @Column(name = "patente_vehiculo")
    private String patenteVehiculo;

    @Column(nullable = false, length = 20)
    private String estado;              // PLANIFICADO, EN_CAMINO, ENTREGADO, CANCELADO

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(length = 500)
    private String observaciones;

    public Envio() {}

    public Envio(Long id, Long centroOrigenId, Long necesidadId, String destinoDescripcion,
                 String responsableTransporte, String patenteVehiculo) {
        this.id = id;
        this.centroOrigenId = centroOrigenId;
        this.necesidadId = necesidadId;
        this.destinoDescripcion = destinoDescripcion;
        this.responsableTransporte = responsableTransporte;
        this.patenteVehiculo = patenteVehiculo;
        this.estado = "PLANIFICADO";
        this.fechaCreacion = LocalDateTime.now();
    }

    public void marcarDespachado() {
        this.estado = "EN_CAMINO";
        this.fechaDespacho = LocalDateTime.now();
    }

    public void marcarEntregado(String observaciones) {
        this.estado = "ENTREGADO";
        this.fechaEntrega = LocalDateTime.now();
        this.observaciones = observaciones;
    }

    public void cancelar(String motivo) {
        this.estado = "CANCELADO";
        this.observaciones = motivo;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCentroOrigenId() { return centroOrigenId; }
    public void setCentroOrigenId(Long centroOrigenId) { this.centroOrigenId = centroOrigenId; }
    public Long getNecesidadId() { return necesidadId; }
    public void setNecesidadId(Long necesidadId) { this.necesidadId = necesidadId; }
    public String getDestinoDescripcion() { return destinoDescripcion; }
    public void setDestinoDescripcion(String destinoDescripcion) { this.destinoDescripcion = destinoDescripcion; }
    public String getResponsableTransporte() { return responsableTransporte; }
    public void setResponsableTransporte(String responsableTransporte) { this.responsableTransporte = responsableTransporte; }
    public String getPatenteVehiculo() { return patenteVehiculo; }
    public void setPatenteVehiculo(String patenteVehiculo) { this.patenteVehiculo = patenteVehiculo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDateTime fechaDespacho) { this.fechaDespacho = fechaDespacho; }
    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
