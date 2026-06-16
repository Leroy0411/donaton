package donaton.mslogistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio MS-Logística — Donaton
 * Puerto: 8082
 *
 * Patrones implementados:
 *  - Repository Pattern: CentroAcopioRepository / EnvioRepository
 *  - Observer Pattern: EnvioObserver (AuditoriaEnvioObserver, NotificacionEnvioObserver)
 */
@SpringBootApplication
public class MsLogisticaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsLogisticaApplication.class, args);
    }
}
