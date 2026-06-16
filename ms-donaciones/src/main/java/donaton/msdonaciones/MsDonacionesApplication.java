package donaton.msdonaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio MS-Donaciones — Donaton
 * Puerto: 8081
 *
 * Patrones implementados:
 *  - Repository Pattern: DonacionRepository / DonacionRepositoryImpl
 *  - Factory Method: DonacionFactory y subclases concretas por tipo
 */
@SpringBootApplication
public class MsDonacionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDonacionesApplication.class, args);
    }
}
