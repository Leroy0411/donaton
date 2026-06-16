package donaton.mslogistica.observer;

import donaton.mslogistica.model.Envio;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// ══════════════════════════════════════════════════════════
//  Observadores concretos — reaccionan a cambios de estado
// ══════════════════════════════════════════════════════════

/**
 * Observador de auditoría: registra todos los cambios de estado de envíos.
 */
@Component
public class AuditoriaEnvioObserver implements EnvioObserver {

    // En producción esto se persistiría en base de datos
    private final List<String> bitacora = new ArrayList<>();

    @Override
    public void onEnvioActualizado(Envio envio, String estadoAnterior) {
        String entrada = String.format("[%s] Envío #%d cambió de %s → %s (destino: %s)",
                LocalDateTime.now(), envio.getId(), estadoAnterior,
                envio.getEstado(), envio.getDestinoDescripcion());
        bitacora.add(entrada);
        System.out.println("[AUDITORÍA] " + entrada);
    }

    public List<String> getBitacora() {
        return new ArrayList<>(bitacora);
    }
}
