package donaton.mslogistica.observer;

import donaton.mslogistica.model.Envio;

/**
 * PATRÓN: Observer (GoF - Comportamiento)
 * ─────────────────────────────────────────────────────────────────────────
 * Define el contrato para los observadores que reaccionan a cambios
 * de estado en los envíos de la plataforma Donaton.
 *
 * Beneficio: desacopla el sujeto (EnvioService) de sus dependientes
 * (notificaciones, auditoría, métricas). Nuevas reacciones se agregan
 * implementando esta interfaz sin modificar código existente.
 */
public interface EnvioObserver {

    /**
     * Notifica al observador que el estado de un envío ha cambiado.
     * @param envio   el envío que cambió de estado
     * @param estadoAnterior  estado previo del envío
     */
    void onEnvioActualizado(Envio envio, String estadoAnterior);
}
