package donaton.mslogistica.service;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import donaton.mslogistica.observer.EnvioObserver;
import donaton.mslogistica.repository.CentroAcopioRepository;
import donaton.mslogistica.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para LogisticaService.
 * Verifica el patrón Observer: los observadores son notificados en cada
 * cambio de estado de un envío.
 */
@DisplayName("LogisticaService - Pruebas Unitarias")
class LogisticaServiceTest {

    @Mock private CentroAcopioRepository centroRepository;
    @Mock private EnvioRepository envioRepository;
    @Mock private EnvioObserver observadorMock;

    private LogisticaService logisticaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logisticaService = new LogisticaService(centroRepository, envioRepository,
                List.of(observadorMock));
    }

    // ─── OBSERVER: notificación en cambios de estado ─────────────────────

    @Test
    @DisplayName("Crear envío debe notificar a todos los observadores")
    void crearEnvio_notificaObservadores() {
        // Arrange
        Envio envio = new Envio(null, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        when(envioRepository.save(any())).thenAnswer(inv -> {
            Envio e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        // Act
        logisticaService.crearEnvio(envio);

        // Assert: el observador fue notificado exactamente una vez
        verify(observadorMock, times(1)).onEnvioActualizado(any(Envio.class), eq("NUEVO"));
    }

    @Test
    @DisplayName("Despachar envío debe cambiar estado a EN_CAMINO y notificar observadores")
    void despacharEnvio_cambiaEstadoYNotifica() {
        // Arrange
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any())).thenReturn(envio);

        // Act
        Optional<Envio> resultado = logisticaService.despacharEnvio(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("EN_CAMINO", resultado.get().getEstado());
        verify(observadorMock).onEnvioActualizado(any(Envio.class), eq("PLANIFICADO"));
    }

    @Test
    @DisplayName("Confirmar entrega debe cambiar estado a ENTREGADO y notificar")
    void confirmarEntrega_cambiaEstadoYNotifica() {
        // Arrange
        Envio envio = new Envio(1L, 1L, 1L, "Maipú", "Pedro", "CD-5678");
        envio.setEstado("EN_CAMINO");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any())).thenReturn(envio);

        // Act
        Optional<Envio> resultado = logisticaService.confirmarEntrega(1L, "Todo OK");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("ENTREGADO", resultado.get().getEstado());
        verify(observadorMock).onEnvioActualizado(any(Envio.class), eq("EN_CAMINO"));
    }

    @Test
    @DisplayName("Cancelar envío debe notificar con estado anterior correcto")
    void cancelarEnvio_notificaConEstadoAnterior() {
        // Arrange
        Envio envio = new Envio(1L, 1L, 1L, "Destino", "Resp", "ZZ-9999");
        envio.setEstado("PLANIFICADO");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(envio));
        when(envioRepository.save(any())).thenReturn(envio);

        // Act
        logisticaService.cancelarEnvio(1L, "Sin transporte disponible");

        // Assert
        verify(observadorMock).onEnvioActualizado(any(Envio.class), eq("PLANIFICADO"));
        assertEquals("CANCELADO", envio.getEstado());
    }

    @Test
    @DisplayName("Despachar envío inexistente debe retornar Optional vacío sin notificar")
    void despacharEnvio_inexistente_noNotifica() {
        // Arrange
        when(envioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Envio> resultado = logisticaService.despacharEnvio(999L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(observadorMock, never()).onEnvioActualizado(any(), any());
    }

    // ─── CENTROS DE ACOPIO ───────────────────────────────────────────────

    @Test
    @DisplayName("ActualizarOcupacion al máximo debe marcar centro como SATURADO")
    void actualizarOcupacion_maxima_marcaSaturado() {
        // Arrange
        CentroAcopio centro = new CentroAcopio(1L, "Centro A", "Dir", "Santiago",
                100, "Resp", "contacto");
        when(centroRepository.findById(1L)).thenReturn(Optional.of(centro));
        when(centroRepository.save(any())).thenReturn(centro);

        // Act
        Optional<CentroAcopio> resultado = logisticaService.actualizarOcupacion(1L, 100);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("SATURADO", resultado.get().getEstado());
    }

    @Test
    @DisplayName("ActualizarOcupacion bajo el máximo debe mantener estado ACTIVO")
    void actualizarOcupacion_parcial_mantieneActivo() {
        // Arrange
        CentroAcopio centro = new CentroAcopio(1L, "Centro B", "Dir", "Maipú",
                100, "Resp", "contacto");
        when(centroRepository.findById(1L)).thenReturn(Optional.of(centro));
        when(centroRepository.save(any())).thenReturn(centro);

        // Act
        Optional<CentroAcopio> resultado = logisticaService.actualizarOcupacion(1L, 50);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("ACTIVO", resultado.get().getEstado());
    }
}
