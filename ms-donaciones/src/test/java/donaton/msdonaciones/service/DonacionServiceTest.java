package donaton.msdonaciones.service;

import donaton.msdonaciones.factory.DonacionFactory;
import donaton.msdonaciones.factory.DonacionFactoryProvider;
import donaton.msdonaciones.model.Donacion;
import donaton.msdonaciones.repository.DonacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para DonacionService.
 *
 * Se utilizan mocks de DonacionRepository y DonacionFactoryProvider
 * para aislar el comportamiento del servicio (Repository Pattern).
 * Cobertura: creación, consulta, actualización de estado y eliminación.
 */
@DisplayName("DonacionService - Pruebas Unitarias")
class DonacionServiceTest {

    @Mock
    private DonacionRepository donacionRepository;

    @Mock
    private DonacionFactoryProvider factoryProvider;

    @Mock
    private DonacionFactory donacionFactory;

    @InjectMocks
    private DonacionService donacionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ─── PRUEBAS DE CREACIÓN (Factory Method) ───────────────────────────

    @Test
    @DisplayName("Crear donación ROPA debe usar la fábrica correspondiente y guardarla")
    void crearDonacion_tipoRopa_usaFactoriaYGuarda() {
        // Arrange
        Donacion donacionEsperada = new Donacion(null, "ROPA", "Santiago", 50, 1L, "Ropa de invierno");
        when(factoryProvider.obtenerFabrica("ROPA")).thenReturn(donacionFactory);
        when(donacionFactory.crear("Santiago", 50, 1L, "Ropa de invierno")).thenReturn(donacionEsperada);
        when(donacionRepository.save(donacionEsperada)).thenReturn(donacionEsperada);

        // Act
        Donacion resultado = donacionService.crearDonacion("ROPA", "Santiago", 50, 1L, "Ropa de invierno");

        // Assert
        assertNotNull(resultado);
        assertEquals("ROPA", resultado.getTipo());
        verify(factoryProvider).obtenerFabrica("ROPA");
        verify(donacionFactory).crear("Santiago", 50, 1L, "Ropa de invierno");
        verify(donacionRepository).save(donacionEsperada);
    }

    @Test
    @DisplayName("Crear donación con tipo inválido debe propagar IllegalArgumentException")
    void crearDonacion_tipoInvalido_lanzaExcepcion() {
        // Arrange
        when(factoryProvider.obtenerFabrica("INVALIDO"))
                .thenThrow(new IllegalArgumentException("Tipo no soportado: INVALIDO"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> donacionService.crearDonacion("INVALIDO", "Origen", 10, 1L, "desc"));
    }

    // ─── PRUEBAS DE CONSULTA (Repository Pattern) ───────────────────────

    @Test
    @DisplayName("ObtenerTodas debe retornar la lista completa del repositorio")
    void obtenerTodas_retornaListaCompleta() {
        // Arrange
        List<Donacion> listaMock = List.of(
                new Donacion(1L, "ROPA", "A", 10, 1L, "desc1"),
                new Donacion(2L, "MEDICO", "B", 5, 2L, "desc2")
        );
        when(donacionRepository.findAll()).thenReturn(listaMock);

        // Act
        List<Donacion> resultado = donacionService.obtenerTodas();

        // Assert
        assertEquals(2, resultado.size());
        verify(donacionRepository).findAll();
    }

    @Test
    @DisplayName("ObtenerPorId con ID existente debe retornar Optional con donación")
    void obtenerPorId_existente_retornaOptionalPresente() {
        // Arrange
        Donacion donacion = new Donacion(1L, "ALIMENTO", "Maipú", 30, 1L, "Conservas");
        when(donacionRepository.findById(1L)).thenReturn(Optional.of(donacion));

        // Act
        Optional<Donacion> resultado = donacionService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("ALIMENTO", resultado.get().getTipo());
    }

    @Test
    @DisplayName("ObtenerPorId con ID inexistente debe retornar Optional vacío")
    void obtenerPorId_inexistente_retornaOptionalVacio() {
        // Arrange
        when(donacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Donacion> resultado = donacionService.obtenerPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("ObtenerPorEstado debe filtrar correctamente usando el repositorio")
    void obtenerPorEstado_delegaAlRepositorio() {
        // Arrange
        List<Donacion> recibidas = List.of(
                new Donacion(1L, "ROPA", "A", 10, 1L, "d")
        );
        when(donacionRepository.findByEstado("RECIBIDA")).thenReturn(recibidas);

        // Act
        List<Donacion> resultado = donacionService.obtenerPorEstado("RECIBIDA");

        // Assert
        assertEquals(1, resultado.size());
        verify(donacionRepository).findByEstado("RECIBIDA");
    }

    // ─── PRUEBAS DE ACTUALIZACIÓN ────────────────────────────────────────

    @Test
    @DisplayName("ActualizarEstado con ID existente debe cambiar el estado y persistir")
    void actualizarEstado_existente_actualizaYGuarda() {
        // Arrange
        Donacion donacion = new Donacion(1L, "ROPA", "A", 10, 1L, "d");
        donacion.setEstado("RECIBIDA");
        when(donacionRepository.findById(1L)).thenReturn(Optional.of(donacion));
        when(donacionRepository.save(any())).thenReturn(donacion);

        // Act
        Optional<Donacion> resultado = donacionService.actualizarEstado(1L, "EN_PROCESO");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("EN_PROCESO", resultado.get().getEstado());
        verify(donacionRepository).save(donacion);
    }

    @Test
    @DisplayName("ActualizarEstado con ID inexistente debe retornar Optional vacío")
    void actualizarEstado_inexistente_retornaVacio() {
        // Arrange
        when(donacionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Donacion> resultado = donacionService.actualizarEstado(999L, "EN_PROCESO");

        // Assert
        assertFalse(resultado.isPresent());
        verify(donacionRepository, never()).save(any());
    }

    // ─── PRUEBAS DE ELIMINACIÓN ──────────────────────────────────────────

    @Test
    @DisplayName("Eliminar con ID existente debe retornar true")
    void eliminar_existente_retornaTrue() {
        // Arrange
        when(donacionRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean resultado = donacionService.eliminar(1L);

        // Assert
        assertTrue(resultado);
        verify(donacionRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Eliminar con ID inexistente debe retornar false")
    void eliminar_inexistente_retornaFalse() {
        // Arrange
        when(donacionRepository.existsById(999L)).thenReturn(false);

        // Act
        boolean resultado = donacionService.eliminar(999L);

        // Assert
        assertFalse(resultado);
        verify(donacionRepository, never()).deleteById(any());
    }
}
