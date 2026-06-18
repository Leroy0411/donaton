package donaton.mslogistica.observer;

import donaton.mslogistica.model.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para los observadores concretos (patrón Observer).
 * Verifica que cada observador reacciona correctamente a los cambios
 * de estado de un envío.
 */
@DisplayName("Observadores de Envío - Pruebas Unitarias")
class EnvioObserversTest {

    @Test
    @DisplayName("AuditoriaEnvioObserver registra cada cambio de estado en su bitácora")
    void auditoriaObserver_registraEnBitacora() {
        AuditoriaEnvioObserver observer = new AuditoriaEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        envio.setEstado("EN_CAMINO");

        observer.onEnvioActualizado(envio, "PLANIFICADO");

        assertEquals(1, observer.getBitacora().size());
        assertTrue(observer.getBitacora().get(0).contains("PLANIFICADO"));
        assertTrue(observer.getBitacora().get(0).contains("EN_CAMINO"));
    }

    @Test
    @DisplayName("AuditoriaEnvioObserver acumula múltiples entradas en orden")
    void auditoriaObserver_acumulaMultiplesEntradas() {
        AuditoriaEnvioObserver observer = new AuditoriaEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");

        observer.onEnvioActualizado(envio, "PLANIFICADO");
        envio.setEstado("ENTREGADO");
        observer.onEnvioActualizado(envio, "EN_CAMINO");

        assertEquals(2, observer.getBitacora().size());
    }

    @Test
    @DisplayName("NotificacionEnvioObserver no lanza excepción al notificar EN_CAMINO")
    void notificacionObserver_enCamino_noLanzaExcepcion() {
        NotificacionEnvioObserver observer = new NotificacionEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        envio.setEstado("EN_CAMINO");

        assertDoesNotThrow(() -> observer.onEnvioActualizado(envio, "PLANIFICADO"));
    }

    @Test
    @DisplayName("NotificacionEnvioObserver no lanza excepción al notificar ENTREGADO")
    void notificacionObserver_entregado_noLanzaExcepcion() {
        NotificacionEnvioObserver observer = new NotificacionEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        envio.setEstado("ENTREGADO");

        assertDoesNotThrow(() -> observer.onEnvioActualizado(envio, "EN_CAMINO"));
    }

    @Test
    @DisplayName("NotificacionEnvioObserver no lanza excepción al notificar CANCELADO")
    void notificacionObserver_cancelado_noLanzaExcepcion() {
        NotificacionEnvioObserver observer = new NotificacionEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        envio.cancelar("Sin transporte disponible");

        assertDoesNotThrow(() -> observer.onEnvioActualizado(envio, "PLANIFICADO"));
    }

    @Test
    @DisplayName("NotificacionEnvioObserver maneja el caso por defecto sin lanzar excepción")
    void notificacionObserver_estadoDesconocido_noLanzaExcepcion() {
        NotificacionEnvioObserver observer = new NotificacionEnvioObserver();
        Envio envio = new Envio(1L, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        envio.setEstado("PLANIFICADO");

        assertDoesNotThrow(() -> observer.onEnvioActualizado(envio, "NUEVO"));
    }
}
