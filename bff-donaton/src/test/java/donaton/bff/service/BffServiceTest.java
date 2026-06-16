package donaton.bff.service;

import donaton.bff.dto.DashboardResumenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para BffService.
 * Valida que el BFF agrega correctamente datos de ambos microservicios
 * y maneja gracefully la indisponibilidad de alguno.
 */
@DisplayName("BffService - Pruebas Unitarias")
class BffServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private BffService bffService;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bffService = new BffService(restTemplate);

        // Datos mock para MS-Donaciones
        List<Map<String, Object>> donaciones = List.of(
                Map.of("id", 1, "tipo", "ROPA",    "estado", "RECIBIDA"),
                Map.of("id", 2, "tipo", "MEDICO",  "estado", "EN_PROCESO"),
                Map.of("id", 3, "tipo", "ALIMENTO","estado", "DISTRIBUIDA")
        );

        // Datos mock para centros
        List<Map<String, Object>> centros = List.of(
                Map.of("id", 1, "nombre", "Centro A", "estado", "ACTIVO"),
                Map.of("id", 2, "nombre", "Centro B", "estado", "SATURADO")
        );

        // Datos mock para envíos
        List<Map<String, Object>> envios = List.of(
                Map.of("id", 1, "estado", "PLANIFICADO"),
                Map.of("id", 2, "estado", "EN_CAMINO"),
                Map.of("id", 3, "estado", "ENTREGADO")
        );

        when(restTemplate.exchange(contains("/api/donaciones"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(donaciones));

        when(restTemplate.exchange(contains("/api/logistica/centros"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(centros));

        when(restTemplate.exchange(contains("/api/logistica/envios"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(envios));
    }

    @Test
    @DisplayName("Dashboard debe agregar correctamente estadísticas de donaciones")
    void dashboard_agregaDonacionesCorrectamente() {
        DashboardResumenDTO resultado = bffService.obtenerResumenDashboard();

        assertEquals(3, resultado.getTotalDonaciones());
        assertEquals(1, resultado.getDonacionesRecibidas());
        assertEquals(1, resultado.getDonacionesEnProceso());
        assertEquals(1, resultado.getDonacionesDistribuidas());
    }

    @Test
    @DisplayName("Dashboard debe agregar correctamente estadísticas de centros")
    void dashboard_agregaCentrosCorrectamente() {
        DashboardResumenDTO resultado = bffService.obtenerResumenDashboard();

        assertEquals(2, resultado.getTotalCentros());
        assertEquals(1, resultado.getCentrosActivos());
        assertEquals(1, resultado.getCentrosSaturados());
    }

    @Test
    @DisplayName("Dashboard debe agregar correctamente estadísticas de envíos")
    void dashboard_agregaEnviosCorrectamente() {
        DashboardResumenDTO resultado = bffService.obtenerResumenDashboard();

        assertEquals(3, resultado.getTotalEnvios());
        assertEquals(1, resultado.getEnviosPlanificados());
        assertEquals(1, resultado.getEnviosEnCamino());
        assertEquals(1, resultado.getEnviosEntregados());
    }

    @Test
    @DisplayName("Dashboard debe incluir alerta cuando hay centros saturados")
    @SuppressWarnings("unchecked")
    void dashboard_centroSaturado_generaAlerta() {
        DashboardResumenDTO resultado = bffService.obtenerResumenDashboard();

        assertFalse(resultado.getAlertas().isEmpty());
        assertTrue(resultado.getAlertas().stream()
                .anyMatch(a -> a.contains("saturado")));
    }

    @Test
    @DisplayName("Si MS-Donaciones falla, el BFF debe registrar la alerta y continuar")
    @SuppressWarnings("unchecked")
    void dashboard_msDonacionesFalla_registraAlertaYContinua() {
        when(restTemplate.exchange(contains("/api/donaciones"), eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new RestClientException("Connection refused"));

        DashboardResumenDTO resultado = bffService.obtenerResumenDashboard();

        // No debe lanzar excepción
        assertNotNull(resultado);
        // Debe registrar alerta de indisponibilidad
        assertTrue(resultado.getAlertas().stream()
                .anyMatch(a -> a.contains("MS-Donaciones")));
        // Las estadísticas de logística aún deben estar disponibles
        assertEquals(2, resultado.getTotalCentros());
    }
}
