package donaton.mslogistica.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import donaton.mslogistica.service.LogisticaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias del LogisticaController mediante @WebMvcTest.
 * Verifica el contrato HTTP de los endpoints de centros de acopio y envíos.
 */
@WebMvcTest(LogisticaController.class)
@DisplayName("LogisticaController - Pruebas Unitarias")
class LogisticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogisticaService logisticaService;

    @Autowired
    private ObjectMapper objectMapper;

    private CentroAcopio centroEjemplo;
    private Envio envioEjemplo;

    @BeforeEach
    void setUp() {
        centroEjemplo = new CentroAcopio(1L, "Centro A", "Dir 123", "Maipú", 100, "Resp", "contacto");
        envioEjemplo = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
    }

    @Test
    @DisplayName("GET /api/logistica/centros retorna 200 con la lista completa")
    void listarCentros_retornaOk() throws Exception {
        when(logisticaService.obtenerCentros()).thenReturn(List.of(centroEjemplo));

        mockMvc.perform(get("/api/logistica/centros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Centro A"));
    }

    @Test
    @DisplayName("GET /api/logistica/centros/activos retorna 200")
    void listarActivos_retornaOk() throws Exception {
        when(logisticaService.obtenerCentrosActivos()).thenReturn(List.of(centroEjemplo));

        mockMvc.perform(get("/api/logistica/centros/activos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/logistica/centros retorna 201 con el centro creado")
    void crearCentro_retornaCreated() throws Exception {
        when(logisticaService.agregarCentro(any())).thenReturn(centroEjemplo);

        mockMvc.perform(post("/api/logistica/centros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(centroEjemplo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Centro A"));
    }

    @Test
    @DisplayName("PUT /api/logistica/centros/{id}/ocupacion con ID existente retorna 200")
    void actualizarOcupacion_existente_retornaOk() throws Exception {
        when(logisticaService.actualizarOcupacion(1L, 50)).thenReturn(Optional.of(centroEjemplo));

        mockMvc.perform(put("/api/logistica/centros/1/ocupacion").param("ocupacion", "50"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/logistica/centros/{id}/ocupacion con ID inexistente retorna 404")
    void actualizarOcupacion_inexistente_retornaNotFound() throws Exception {
        when(logisticaService.actualizarOcupacion(999L, 50)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/logistica/centros/999/ocupacion").param("ocupacion", "50"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/logistica/centros/{id} existente retorna 200")
    void eliminarCentro_existente_retornaOk() throws Exception {
        when(logisticaService.eliminarCentro(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/logistica/centros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    @DisplayName("GET /api/logistica/envios retorna 200 con la lista completa")
    void listarEnvios_retornaOk() throws Exception {
        when(logisticaService.obtenerEnvios()).thenReturn(List.of(envioEjemplo));

        mockMvc.perform(get("/api/logistica/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].destinoDescripcion").value("Pudahuel"));
    }

    @Test
    @DisplayName("POST /api/logistica/envios retorna 201 con el envío creado")
    void crearEnvio_retornaCreated() throws Exception {
        when(logisticaService.crearEnvio(any())).thenReturn(envioEjemplo);

        mockMvc.perform(post("/api/logistica/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(envioEjemplo)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("PUT /api/logistica/envios/{id}/despachar con ID existente retorna 200")
    void despachar_existente_retornaOk() throws Exception {
        when(logisticaService.despacharEnvio(1L)).thenReturn(Optional.of(envioEjemplo));

        mockMvc.perform(put("/api/logistica/envios/1/despachar"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/logistica/envios/{id}/despachar con ID inexistente retorna 404")
    void despachar_inexistente_retornaNotFound() throws Exception {
        when(logisticaService.despacharEnvio(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/logistica/envios/999/despachar"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/logistica/envios/{id}/entregar retorna 200")
    void entregar_existente_retornaOk() throws Exception {
        when(logisticaService.confirmarEntrega(1L, "Todo OK")).thenReturn(Optional.of(envioEjemplo));

        mockMvc.perform(put("/api/logistica/envios/1/entregar").param("observaciones", "Todo OK"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /api/logistica/envios/{id}/cancelar retorna 200")
    void cancelar_existente_retornaOk() throws Exception {
        when(logisticaService.cancelarEnvio(1L, "Sin transporte")).thenReturn(Optional.of(envioEjemplo));

        mockMvc.perform(put("/api/logistica/envios/1/cancelar").param("motivo", "Sin transporte"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/logistica/envios/{id} inexistente retorna 404")
    void eliminarEnvio_inexistente_retornaNotFound() throws Exception {
        when(logisticaService.eliminarEnvio(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/logistica/envios/999"))
                .andExpect(status().isNotFound());
    }
}
