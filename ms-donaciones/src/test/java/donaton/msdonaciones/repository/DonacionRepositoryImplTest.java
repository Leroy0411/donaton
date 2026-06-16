package donaton.msdonaciones.repository;

import donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para DonacionRepositoryImpl.
 * Valida el comportamiento del Repository Pattern con almacenamiento en memoria.
 */
@DisplayName("DonacionRepositoryImpl - Pruebas Unitarias")
class DonacionRepositoryImplTest {

    private DonacionRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        repository = new DonacionRepositoryImpl();
    }

    @Test
    @DisplayName("Save asigna ID autoincremental a donación nueva")
    void save_donacionNueva_asignaId() {
        Donacion d = new Donacion(null, "ROPA", "Origen", 10, 1L, "desc");
        Donacion guardada = repository.save(d);
        assertNotNull(guardada.getId());
        assertEquals(1L, guardada.getId());
    }

    @Test
    @DisplayName("FindAll retorna todas las donaciones guardadas")
    void findAll_variasGuardadas_retornaTodasCorrectamente() {
        repository.save(new Donacion(null, "ROPA", "A", 10, 1L, "d1"));
        repository.save(new Donacion(null, "MEDICO", "B", 5, 1L, "d2"));
        List<Donacion> resultado = repository.findAll();
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("FindById con ID existente retorna Optional presente")
    void findById_existente_retornaPresente() {
        Donacion guardada = repository.save(new Donacion(null, "ALIMENTO", "X", 20, 2L, "d"));
        Optional<Donacion> resultado = repository.findById(guardada.getId());
        assertTrue(resultado.isPresent());
        assertEquals("ALIMENTO", resultado.get().getTipo());
    }

    @Test
    @DisplayName("FindById con ID inexistente retorna Optional vacío")
    void findById_inexistente_retornaVacio() {
        Optional<Donacion> resultado = repository.findById(999L);
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("FindByEstado filtra correctamente por estado")
    void findByEstado_filtraCorrectamente() {
        Donacion d1 = repository.save(new Donacion(null, "ROPA", "A", 10, 1L, "d1"));
        Donacion d2 = repository.save(new Donacion(null, "MEDICO", "B", 5, 1L, "d2"));
        d2.setEstado("EN_PROCESO");
        repository.save(d2);

        List<Donacion> recibidas = repository.findByEstado("RECIBIDA");
        assertEquals(1, recibidas.size());
        assertEquals(d1.getId(), recibidas.get(0).getId());
    }

    @Test
    @DisplayName("DeleteById con ID existente elimina y retorna true")
    void deleteById_existente_eliminaYRetornaTrue() {
        Donacion guardada = repository.save(new Donacion(null, "HIGIENE", "A", 5, 1L, "d"));
        boolean resultado = repository.deleteById(guardada.getId());
        assertTrue(resultado);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("DeleteById con ID inexistente retorna false")
    void deleteById_inexistente_retornaFalse() {
        boolean resultado = repository.deleteById(999L);
        assertFalse(resultado);
    }
}
