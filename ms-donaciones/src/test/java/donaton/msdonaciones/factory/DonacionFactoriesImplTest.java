package donaton.msdonaciones.factory;

import donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para las fábricas concretas (Factory Method).
 * Verifica las reglas de negocio específicas de cada tipo de donación
 * y la validación común definida en DonacionFactory.
 */
@DisplayName("Fábricas de Donación - Pruebas Unitarias")
class DonacionFactoriesImplTest {

    @Test
    @DisplayName("DonacionRopaFactory: lote >= 100 unidades pasa directo a EN_PROCESO")
    void ropaFactory_loteGrande_pasaAEnProceso() {
        DonacionFactory fabrica = new DonacionRopaFactory();
        Donacion d = fabrica.crear("Santiago", 150, 1L, "Lote grande de ropa");

        assertEquals("ROPA", d.getTipo());
        assertEquals("EN_PROCESO", d.getEstado());
    }

    @Test
    @DisplayName("DonacionRopaFactory: lote pequeño se mantiene en RECIBIDA")
    void ropaFactory_loteChico_mantieneRecibida() {
        DonacionFactory fabrica = new DonacionRopaFactory();
        Donacion d = fabrica.crear("Santiago", 5, 1L, "Lote chico");

        assertEquals("RECIBIDA", d.getEstado());
    }

    @Test
    @DisplayName("DonacionAlimentoFactory: siempre inicia en RECIBIDA")
    void alimentoFactory_siempreRecibida() {
        DonacionFactory fabrica = new DonacionAlimentoFactory();
        Donacion d = fabrica.crear("Maipú", 30, 2L, "Conservas");

        assertEquals("ALIMENTO", d.getTipo());
        assertEquals("RECIBIDA", d.getEstado());
    }

    @Test
    @DisplayName("DonacionMedicoFactory: siempre pasa a EN_PROCESO por prioridad alta")
    void medicoFactory_prioridadAlta_pasaAEnProceso() {
        DonacionFactory fabrica = new DonacionMedicoFactory();
        Donacion d = fabrica.crear("Ñuñoa", 10, 1L, "Insumos médicos");

        assertEquals("MEDICO", d.getTipo());
        assertEquals("EN_PROCESO", d.getEstado());
    }

    @Test
    @DisplayName("DonacionHigieneFactory: inicia en RECIBIDA")
    void higieneFactory_iniciaRecibida() {
        DonacionFactory fabrica = new DonacionHigieneFactory();
        Donacion d = fabrica.crear("Maipú", 15, 1L, "Artículos de higiene");

        assertEquals("HIGIENE", d.getTipo());
        assertEquals("RECIBIDA", d.getEstado());
    }

    @Test
    @DisplayName("getTipo() retorna el identificador correcto de cada fábrica")
    void getTipo_retornaIdentificadorCorrecto() {
        assertEquals("ROPA", new DonacionRopaFactory().getTipo());
        assertEquals("ALIMENTO", new DonacionAlimentoFactory().getTipo());
        assertEquals("MEDICO", new DonacionMedicoFactory().getTipo());
        assertEquals("HIGIENE", new DonacionHigieneFactory().getTipo());
    }

    @Test
    @DisplayName("crear() con origen vacío lanza IllegalArgumentException")
    void crear_origenVacio_lanzaExcepcion() {
        DonacionFactory fabrica = new DonacionRopaFactory();
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crear("", 10, 1L, "desc"));
    }

    @Test
    @DisplayName("crear() con origen nulo lanza IllegalArgumentException")
    void crear_origenNulo_lanzaExcepcion() {
        DonacionFactory fabrica = new DonacionAlimentoFactory();
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crear(null, 10, 1L, "desc"));
    }

    @Test
    @DisplayName("crear() con cantidad cero o negativa lanza IllegalArgumentException")
    void crear_cantidadInvalida_lanzaExcepcion() {
        DonacionFactory fabrica = new DonacionMedicoFactory();
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crear("Origen", 0, 1L, "desc"));
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crear("Origen", -5, 1L, "desc"));
    }
}
