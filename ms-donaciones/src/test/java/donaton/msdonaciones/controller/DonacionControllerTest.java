package donaton.msdonaciones.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import donaton.msdonaciones.model.Donacion;
import donaton.msdonaciones.service.DonacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias del DonacionController mediante @WebMvcTest.
 * Verifica el contrato HTTP (status codes, payloads) sin levantar
 * el contexto completo de Spring ni la base de datos.
 */
@WebMvcTest(DonacionController.class)
@DisplayName("DonacionController - Pruebas Unitarias")
class DonacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonacionService donacionService;

    @Autowired
    private ObjectMapper objectMapper;

    private Donacion donacionEjemplo;

    @BeforeEach
    void setUp() {
        donacionEjemplo = new Donacion(1L, "ROPA", "Santiago", 20, 1L, "Ropa de invierno");
    }

    @Test
    @DisplayName("GET /api/donaciones retorna 200 con la lista completa")
    void listarTodas_retornaOk() throws Exception {
        when(donacionService.obtenerTodas()).thenReturn(List.of(donacionEjemplo));

        mockMvc.perform(get("/api/donaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("ROPA"));
    }

    @Test
    @DisplayName("GET /api/donaciones/{id} con ID existente retorna 200")
    void obtenerPorId_existente_retornaOk() throws Exception {
        when(donacionService.obtenerPorId(1L)).thenReturn(Optional.of(donacionEjemplo));

        mockMvc.perform(get("/api/donaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("GET /api/donaciones/{id} con ID inexistente retorna 404")
    void obtenerPorId_inexistente_retornaNotFound() throws Exception {
        when(donacionService.obtenerPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/donaciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/donaciones con datos válidos retorna 201")
    void crearDonacion_valida_retornaCreated() throws Exception {
        when(donacionService.crearDonacion(any(), any(), any(), any(), any()))
                .thenReturn(donacionEjemplo);

        Map<String, Object> body = Map.of(
                "tipo", "ROPA", "origen", "Santiago",
                "cantidad", 20, "centroAcopioId", 1, "descripcion", "Ropa de invierno"
        );

        mockMvc.perform(post("/api/donaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("ROPA"));
    }

    @Test
    @DisplayName("POST /api/donaciones con tipo inválido retorna 400")
    void crearDonacion_tipoInvalido_retornaBadRequest() throws Exception {
        when(donacionService.crearDonacion(any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Tipo de donación no soportado: XYZ"));

        Map<String, Object> body = Map.of(
                "tipo", "XYZ", "origen", "Santiago",
                "cantidad", 20, "centroAcopioId", 1
        );

        mockMvc.perform(post("/api/donaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("PUT /api/donaciones/{id}/estado con ID existente retorna 200")
    void actualizarEstado_existente_retornaOk() throws Exception {
        donacionEjemplo.setEstado("EN_PROCESO");
        when(donacionService.actualizarEstado(1L, "EN_PROCESO")).thenReturn(Optional.of(donacionEjemplo));

        mockMvc.perform(put("/api/donaciones/1/estado").param("estado", "EN_PROCESO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_PROCESO"));
    }

    @Test
    @DisplayName("PUT /api/donaciones/{id}/estado con ID inexistente retorna 404")
    void actualizarEstado_inexistente_retornaNotFound() throws Exception {
        when(donacionService.actualizarEstado(999L, "EN_PROCESO")).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/donaciones/999/estado").param("estado", "EN_PROCESO"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/donaciones/{id} existente retorna 200 con mensaje")
    void eliminar_existente_retornaOk() throws Exception {
        when(donacionService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/donaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    @DisplayName("DELETE /api/donaciones/{id} inexistente retorna 404")
    void eliminar_inexistente_retornaNotFound() throws Exception {
        when(donacionService.eliminar(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/donaciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/donaciones/estado/{estado} retorna lista filtrada")
    void listarPorEstado_retornaOk() throws Exception {
        when(donacionService.obtenerPorEstado("RECIBIDA")).thenReturn(List.of(donacionEjemplo));

        mockMvc.perform(get("/api/donaciones/estado/RECIBIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/donaciones/centro/{id} retorna lista filtrada por centro")
    void listarPorCentro_retornaOk() throws Exception {
        when(donacionService.obtenerPorCentro(1L)).thenReturn(List.of(donacionEjemplo));

        mockMvc.perform(get("/api/donaciones/centro/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
