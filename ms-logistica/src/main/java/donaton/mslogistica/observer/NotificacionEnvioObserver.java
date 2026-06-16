package donaton.mslogistica.observer;

import donaton.mslogistica.model.Envio;
import org.springframework.stereotype.Component;

/**
 * Observador de notificaciones: simula el disparo de alertas
 * al responsable de transporte cuando el estado del envío cambia.
 *
 * En producción publicaría un evento en RabbitMQ que MS-Notificaciones consumiría.
 */
@Component
public class NotificacionEnvioObserver implements EnvioObserver {

    @Override
    public void onEnvioActualizado(Envio envio, String estadoAnterior) {
        switch (envio.getEstado()) {
            case "EN_CAMINO" ->
                System.out.printf("[NOTIFICACIÓN] SMS al responsable %s: El envío #%d ha salido hacia %s%n",
                        envio.getResponsableTransporte(), envio.getId(), envio.getDestinoDescripcion());
            case "ENTREGADO" ->
                System.out.printf("[NOTIFICACIÓN] Email confirmación: Envío #%d entregado en %s%n",
                        envio.getId(), envio.getDestinoDescripcion());
            case "CANCELADO" ->
                System.out.printf("[NOTIFICACIÓN] Alerta: Envío #%d cancelado. Observaciones: %s%n",
                        envio.getId(), envio.getObservaciones());
            default ->
                System.out.printf("[NOTIFICACIÓN] Envío #%d actualizó estado a %s%n",
                        envio.getId(), envio.getEstado());
        }
    }
}
