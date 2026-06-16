package donaton.msdonaciones.factory;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Registro de fábricas de donación.
 * Spring inyecta automáticamente todas las implementaciones de DonacionFactory.
 * El cliente solo necesita indicar el tipo para obtener la fábrica correcta.
 */
@Component
public class DonacionFactoryProvider {

    private final Map<String, DonacionFactory> fabricas;

    public DonacionFactoryProvider(List<DonacionFactory> listaFabricas) {
        this.fabricas = listaFabricas.stream()
                .collect(Collectors.toMap(
                        f -> f.getTipo().toUpperCase(),
                        f -> f
                ));
    }

    /**
     * Retorna la fábrica correspondiente al tipo de donación.
     * @throws IllegalArgumentException si el tipo no está soportado.
     */
    public DonacionFactory obtenerFabrica(String tipo) {
        DonacionFactory fabrica = fabricas.get(tipo.toUpperCase());
        if (fabrica == null) {
            throw new IllegalArgumentException(
                    "Tipo de donación no soportado: " + tipo +
                    ". Tipos válidos: " + fabricas.keySet()
            );
        }
        return fabrica;
    }
}
