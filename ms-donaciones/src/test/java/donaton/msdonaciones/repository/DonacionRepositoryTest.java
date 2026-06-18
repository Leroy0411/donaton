package donaton.msdonaciones.repository;

import donaton.msdonaciones.model.Donacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para DonacionRepository usando @DataJpaTest.
 * Levanta una base H2 en memoria (autoconfigurada por Spring Boot Test)
 * para validar que la persistencia real mediante JPA/Hibernate funciona
 * correctamente: guardado, búsqueda por id, por estado, por centro y borrado.
 */
@DataJpaTest
@DisplayName("DonacionRepository (JPA) - Pruebas de Integración")
class DonacionRepositoryTest {

    @Autowired
    private DonacionRepository repository;

    @Test
    @DisplayName("Save persiste la donación y le asigna un ID autogenerado")
    void save_donacionNueva_asignaId() {
        Donacion d = new Donacion(null, "ROPA", "Origen", 10, 1L, "desc");
        Donacion guardada = repository.save(d);

        assertNotNull(guardada.getId());
        assertTrue(repository.findById(guardada.getId()).isPresent());
    }

    @Test
    @DisplayName("FindAll retorna todas las donaciones persistidas")
    void findAll_variasGuardadas_retornaTodasCorrectamente() {
        repository.save(new Donacion(null, "ROPA", "A", 10, 1L, "d1"));
        repository.save(new Donacion(null, "MEDICO", "B", 5, 1L, "d2"));

        List<Donacion> resultado = repository.findAll();
        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("FindById con ID inexistente retorna Optional vacío")
    void findById_inexistente_retornaVacio() {
        Optional<Donacion> resultado = repository.findById(999L);
        assertFalse(resultado.isPresent());
    }

    @Test
    @DisplayName("FindByEstado filtra correctamente por estado persistido")
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
    @DisplayName("FindByCentroAcopioId filtra correctamente por centro")
    void findByCentroAcopioId_filtraCorrectamente() {
        repository.save(new Donacion(null, "ROPA", "A", 10, 1L, "d1"));
        repository.save(new Donacion(null, "MEDICO", "B", 5, 2L, "d2"));

        List<Donacion> resultado = repository.findByCentroAcopioId(1L);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getCentroAcopioId());
    }

    @Test
    @DisplayName("DeleteById elimina la donación de la base de datos")
    void deleteById_existente_elimina() {
        Donacion guardada = repository.save(new Donacion(null, "HIGIENE", "A", 5, 1L, "d"));
        repository.deleteById(guardada.getId());

        assertTrue(repository.findById(guardada.getId()).isEmpty());
    }

    @Test
    @DisplayName("ExistsById refleja correctamente la presencia del registro")
    void existsById_existenteEInexistente() {
        Donacion guardada = repository.save(new Donacion(null, "ALIMENTO", "X", 20, 2L, "d"));

        assertTrue(repository.existsById(guardada.getId()));
        assertFalse(repository.existsById(999L));
    }
}
