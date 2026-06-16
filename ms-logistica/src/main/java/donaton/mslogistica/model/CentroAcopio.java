package donaton.mslogistica.model;

public class CentroAcopio {

    private Long id;
    private String nombre;
    private String direccion;
    private String comuna;
    private Integer capacidadMaxima;
    private Integer ocupacionActual;
    private String estado;              // ACTIVO, INACTIVO, SATURADO
    private String responsableNombre;
    private String responsableContacto;

    public CentroAcopio() {}

    public CentroAcopio(Long id, String nombre, String direccion, String comuna,
                        Integer capacidadMaxima, String responsableNombre, String responsableContacto) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.comuna = comuna;
        this.capacidadMaxima = capacidadMaxima;
        this.ocupacionActual = 0;
        this.estado = "ACTIVO";
        this.responsableNombre = responsableNombre;
        this.responsableContacto = responsableContacto;
    }

    public boolean tieneCapacidadDisponible() {
        return ocupacionActual < capacidadMaxima;
    }

    public int getCapacidadDisponible() {
        return capacidadMaxima - ocupacionActual;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getComuna() { return comuna; }
    public void setComuna(String comuna) { this.comuna = comuna; }
    public Integer getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(Integer capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }
    public Integer getOcupacionActual() { return ocupacionActual; }
    public void setOcupacionActual(Integer ocupacionActual) { this.ocupacionActual = ocupacionActual; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getResponsableNombre() { return responsableNombre; }
    public void setResponsableNombre(String responsableNombre) { this.responsableNombre = responsableNombre; }
    public String getResponsableContacto() { return responsableContacto; }
    public void setResponsableContacto(String responsableContacto) { this.responsableContacto = responsableContacto; }
}
