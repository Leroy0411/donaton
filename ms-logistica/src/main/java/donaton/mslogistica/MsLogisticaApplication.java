package donaton.mslogistica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio MS-Logística — Donaton
 * Puerto: 8082
 *
 * Patrones implementados:
 *  - Repository Pattern: CentroAcopioRepository / EnvioRepository (Spring Data JPA) sobre base H2
 *  - Observer Pattern: EnvioObserver (AuditoriaEnvioObserver, NotificacionEnvioObserver)
 *
 * Persistencia: JPA/Hibernate + H2 (archivo ./data/logistica-db).
 * Documentación API: /swagger-ui.html
 */
@SpringBootApplication
public class MsLogisticaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsLogisticaApplication.class, args);
    }
}
