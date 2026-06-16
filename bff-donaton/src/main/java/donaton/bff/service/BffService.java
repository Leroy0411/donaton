package donaton.bff.service;

import donaton.bff.dto.DashboardResumenDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * PATRÓN: Backend For Frontend (BFF)
 * ─────────────────────────────────────────────────────────────────────────
 * Actúa como capa de composición entre el frontend React y los microservicios
 * internos. Agrega y transforma datos de MS-Donaciones y MS-Logística en un
 * único response optimizado para cada vista del cliente.
 *
 * Beneficio: el frontend realiza 1 llamada en lugar de N llamadas a distintos
 * servicios, simplificando la lógica del cliente y reduciendo la latencia
 * percibida por el usuario.
 */
@Service
public class BffService {

    private final RestTemplate restTemplate;

    @Value("${ms.donaciones.url:http://localhost:8081}")
    private String urlDonaciones;

    @Value("${ms.logistica.url:http://localhost:8082}")
    private String urlLogistica;

    public BffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Agrega estadísticas de donaciones y logística en un único DTO de dashboard.
     * Reduce N llamadas HTTP del frontend a 1 sola petición al BFF.
     */
    public DashboardResumenDTO obtenerResumenDashboard() {
        DashboardResumenDTO resumen = new DashboardResumenDTO();
        List<String> alertas = new ArrayList<>();

        try {
            // Consulta MS-Donaciones
            List<Map<String, Object>> donaciones = restTemplate.exchange(
                    urlDonaciones + "/api/donaciones",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            if (donaciones != null) {
                resumen.setTotalDonaciones(donaciones.size());
                resumen.setDonacionesRecibidas(contarPorEstado(donaciones, "RECIBIDA"));
                resumen.setDonacionesEnProceso(contarPorEstado(donaciones, "EN_PROCESO"));
                resumen.setDonacionesDistribuidas(contarPorEstado(donaciones, "DISTRIBUIDA"));
            }
        } catch (Exception e) {
            alertas.add("⚠ MS-Donaciones no disponible: " + e.getMessage());
        }

        try {
            // Consulta MS-Logística — Centros
            List<Map<String, Object>> centros = restTemplate.exchange(
                    urlLogistica + "/api/logistica/centros",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            if (centros != null) {
                resumen.setTotalCentros(centros.size());
                resumen.setCentrosActivos(contarPorEstado(centros, "ACTIVO"));
                resumen.setCentrosSaturados(contarPorEstado(centros, "SATURADO"));
                if (resumen.getCentrosSaturados() > 0) {
                    alertas.add("🔴 " + resumen.getCentrosSaturados() + " centros de acopio saturados");
                }
            }

            // Consulta MS-Logística — Envíos
            List<Map<String, Object>> envios = restTemplate.exchange(
                    urlLogistica + "/api/logistica/envios",
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            ).getBody();

            if (envios != null) {
                resumen.setTotalEnvios(envios.size());
                resumen.setEnviosPlanificados(contarPorEstado(envios, "PLANIFICADO"));
                resumen.setEnviosEnCamino(contarPorEstado(envios, "EN_CAMINO"));
                resumen.setEnviosEntregados(contarPorEstado(envios, "ENTREGADO"));
            }
        } catch (Exception e) {
            alertas.add("⚠ MS-Logística no disponible: " + e.getMessage());
        }

        resumen.setAlertas(alertas);
        return resumen;
    }

    private long contarPorEstado(List<Map<String, Object>> lista, String estado) {
        return lista.stream()
                .filter(m -> estado.equalsIgnoreCase((String) m.get("estado")))
                .count();
    }
}
