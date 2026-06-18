package donaton.mslogistica.repository;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para los repositorios de logística usando @DataJpaTest.
 * Levanta una base H2 en memoria para validar la persistencia real mediante
 * JPA/Hibernate tanto de centros de acopio como de envíos.
 */
@DataJpaTest
@DisplayName("Repositorios de Logística (JPA) - Pruebas de Integración")
class LogisticaRepositoryTest {

    @Autowired
    private CentroAcopioRepository centroRepository;

    @Autowired
    private EnvioRepository envioRepository;

    @Test
    @DisplayName("CentroAcopioRepository: save asigna ID autogenerado")
    void centroRepository_save_asignaId() {
        CentroAcopio centro = new CentroAcopio(null, "Centro Test", "Dir 123", "Maipú",
                100, "Resp", "contacto@test.cl");
        CentroAcopio guardado = centroRepository.save(centro);

        assertNotNull(guardado.getId());
        assertTrue(centroRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    @DisplayName("CentroAcopioRepository: findByEstado filtra correctamente")
    void centroRepository_findByEstado_filtraCorrectamente() {
        CentroAcopio activo = new CentroAcopio(null, "A", "Dir", "Maipú", 100, "R", "c");
        CentroAcopio saturado = new CentroAcopio(null, "B", "Dir", "Maipú", 50, "R", "c");
        saturado.setEstado("SATURADO");
        centroRepository.save(activo);
        centroRepository.save(saturado);

        List<CentroAcopio> activos = centroRepository.findByEstado("ACTIVO");
        assertEquals(1, activos.size());
        assertEquals("A", activos.get(0).getNombre());
    }

    @Test
    @DisplayName("EnvioRepository: save asigna ID autogenerado")
    void envioRepository_save_asignaId() {
        Envio envio = new Envio(null, 1L, 1L, "Pudahuel", "Juan", "AB-1234");
        Envio guardado = envioRepository.save(envio);

        assertNotNull(guardado.getId());
        assertTrue(envioRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    @DisplayName("EnvioRepository: findByEstado filtra correctamente")
    void envioRepository_findByEstado_filtraCorrectamente() {
        Envio planificado = new Envio(null, 1L, 1L, "Destino A", "Juan", "AB-1234");
        Envio enCamino = new Envio(null, 1L, 2L, "Destino B", "Pedro", "CD-5678");
        enCamino.marcarDespachado();
        envioRepository.save(planificado);
        envioRepository.save(enCamino);

        List<Envio> planificados = envioRepository.findByEstado("PLANIFICADO");
        assertEquals(1, planificados.size());
        assertEquals("Destino A", planificados.get(0).getDestinoDescripcion());
    }

    @Test
    @DisplayName("EnvioRepository: deleteById elimina el registro persistido")
    void envioRepository_deleteById_elimina() {
        Envio envio = envioRepository.save(new Envio(null, 1L, 1L, "Destino", "Resp", "ZZ-9999"));
        envioRepository.deleteById(envio.getId());

        assertTrue(envioRepository.findById(envio.getId()).isEmpty());
    }
}
