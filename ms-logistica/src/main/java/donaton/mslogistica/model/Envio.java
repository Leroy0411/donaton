package donaton.mslogistica.model;

import java.time.LocalDateTime;

public class Envio {

    private Long id;
    private Long centroOrigenId;
    private Long necesidadId;
    private String destinoDescripcion;
    private String responsableTransporte;
    private String patenteVehiculo;
    private String estado;              // PLANIFICADO, EN_CAMINO, ENTREGADO, CANCELADO
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaDespacho;
    private LocalDateTime fechaEntrega;
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
