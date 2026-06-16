package donaton.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Backend For Frontend — Donaton
 * Puerto: 8080
 *
 * Patrón implementado:
 *  - BFF: agrega datos de MS-Donaciones (8081) y MS-Logística (8082)
 *    en responses optimizados para el frontend React.
 */
@SpringBootApplication
public class BffDonatonApplication {

    public static void main(String[] args) {
        SpringApplication.run(BffDonatonApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
