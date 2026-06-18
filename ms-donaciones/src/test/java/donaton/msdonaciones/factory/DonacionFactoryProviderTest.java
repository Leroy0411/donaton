package donaton.msdonaciones.factory;

import donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para DonacionFactoryProvider.
 * Verifica el registro automático de fábricas y la resolución
 * por tipo (incluyendo manejo de tipos no soportados).
 */
@DisplayName("DonacionFactoryProvider - Pruebas Unitarias")
class DonacionFactoryProviderTest {

    private DonacionFactoryProvider provider;

    @BeforeEach
    void setUp() {
        provider = new DonacionFactoryProvider(List.of(
                new DonacionRopaFactory(),
                new DonacionAlimentoFactory(),
                new DonacionMedicoFactory(),
                new DonacionHigieneFactory()
        ));
    }

    @Test
    @DisplayName("obtenerFabrica devuelve la fábrica correcta para cada tipo soportado")
    void obtenerFabrica_tiposValidos_retornaFabricaCorrecta() {
        assertEquals("ROPA", provider.obtenerFabrica("ROPA").getTipo());
        assertEquals("ALIMENTO", provider.obtenerFabrica("ALIMENTO").getTipo());
        assertEquals("MEDICO", provider.obtenerFabrica("MEDICO").getTipo());
        assertEquals("HIGIENE", provider.obtenerFabrica("HIGIENE").getTipo());
    }

    @Test
    @DisplayName("obtenerFabrica es insensible a mayúsculas/minúsculas")
    void obtenerFabrica_minusculas_resuelveCorrectamente() {
        assertEquals("ROPA", provider.obtenerFabrica("ropa").getTipo());
    }

    @Test
    @DisplayName("obtenerFabrica con tipo no soportado lanza IllegalArgumentException")
    void obtenerFabrica_tipoInvalido_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> provider.obtenerFabrica("ELECTRONICA"));
        assertTrue(ex.getMessage().contains("ELECTRONICA"));
    }

    @Test
    @DisplayName("La fábrica resuelta efectivamente crea donaciones del tipo esperado")
    void fabricaResuelta_creaDonacionDelTipoCorrecto() {
        Donacion d = provider.obtenerFabrica("ALIMENTO").crear("Origen", 10, 1L, "desc");
        assertEquals("ALIMENTO", d.getTipo());
    }
}
