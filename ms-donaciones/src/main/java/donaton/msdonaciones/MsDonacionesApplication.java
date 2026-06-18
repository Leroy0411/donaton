package donaton.msdonaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio MS-Donaciones — Donaton
 * Puerto: 8081
 *
 * Patrones implementados:
 *  - Repository Pattern: DonacionRepository (Spring Data JPA) sobre base H2
 *  - Factory Method: DonacionFactory y subclases concretas por tipo
 *
 * Persistencia: JPA/Hibernate + H2 (archivo ./data/donaciones-db).
 * Documentación API: /swagger-ui.html
 */
@SpringBootApplication
public class MsDonacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDonacionesApplication.class, args);
    }
}
