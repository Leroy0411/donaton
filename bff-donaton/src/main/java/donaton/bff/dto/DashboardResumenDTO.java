package donaton.bff.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO de resumen del dashboard principal.
 *
 * PATRÓN: Backend For Frontend (BFF)
 * ─────────────────────────────────────────────────────────────────────────
 * Agrega datos de MS-Donaciones y MS-Logística en un único payload
 * optimizado para la vista principal del frontend React, reduciendo el
 * número de llamadas HTTP desde el cliente de N a 1.
 */
public class DashboardResumenDTO {

    private long totalDonaciones;
    private long donacionesRecibidas;
    private long donacionesEnProceso;
    private long donacionesDistribuidas;

    private long totalCentros;
    private long centrosActivos;
    private long centrosSaturados;

    private long totalEnvios;
    private long enviosPlanificados;
    private long enviosEnCamino;
    private long enviosEntregados;

    private List<String> alertas;

    // Getters y Setters
    public long getTotalDonaciones() { return totalDonaciones; }
    public void setTotalDonaciones(long totalDonaciones) { this.totalDonaciones = totalDonaciones; }
    public long getDonacionesRecibidas() { return donacionesRecibidas; }
    public void setDonacionesRecibidas(long donacionesRecibidas) { this.donacionesRecibidas = donacionesRecibidas; }
    public long getDonacionesEnProceso() { return donacionesEnProceso; }
    public void setDonacionesEnProceso(long donacionesEnProceso) { this.donacionesEnProceso = donacionesEnProceso; }
    public long getDonacionesDistribuidas() { return donacionesDistribuidas; }
    public void setDonacionesDistribuidas(long donacionesDistribuidas) { this.donacionesDistribuidas = donacionesDistribuidas; }
    public long getTotalCentros() { return totalCentros; }
    public void setTotalCentros(long totalCentros) { this.totalCentros = totalCentros; }
    public long getCentrosActivos() { return centrosActivos; }
    public void setCentrosActivos(long centrosActivos) { this.centrosActivos = centrosActivos; }
    public long getCentrosSaturados() { return centrosSaturados; }
    public void setCentrosSaturados(long centrosSaturados) { this.centrosSaturados = centrosSaturados; }
    public long getTotalEnvios() { return totalEnvios; }
    public void setTotalEnvios(long totalEnvios) { this.totalEnvios = totalEnvios; }
    public long getEnviosPlanificados() { return enviosPlanificados; }
    public void setEnviosPlanificados(long enviosPlanificados) { this.enviosPlanificados = enviosPlanificados; }
    public long getEnviosEnCamino() { return enviosEnCamino; }
    public void setEnviosEnCamino(long enviosEnCamino) { this.enviosEnCamino = enviosEnCamino; }
    public long getEnviosEntregados() { return enviosEntregados; }
    public void setEnviosEntregados(long enviosEntregados) { this.enviosEntregados = enviosEntregados; }
    public List<String> getAlertas() { return alertas; }
    public void setAlertas(List<String> alertas) { this.alertas = alertas; }
}
