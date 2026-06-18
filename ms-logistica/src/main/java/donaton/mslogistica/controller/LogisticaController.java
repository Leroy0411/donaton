package donaton.mslogistica.controller;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import donaton.mslogistica.service.LogisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logistica")
@CrossOrigin(origins = "*")
@Tag(name = "Logística", description = "Gestión de centros de acopio y envíos (con patrón Observer)")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    // ── Centros de Acopio ──────────────────────────────────────────────

    @Operation(summary = "Listar todos los centros de acopio")
    @GetMapping("/centros")
    public List<CentroAcopio> listarCentros() {
        return logisticaService.obtenerCentros();
    }

    @Operation(summary = "Listar centros de acopio en estado ACTIVO")
    @GetMapping("/centros/activos")
    public List<CentroAcopio> listarActivos() {
        return logisticaService.obtenerCentrosActivos();
    }

    @Operation(summary = "Listar centros activos que aún tienen capacidad disponible")
    @GetMapping("/centros/con-capacidad")
    public List<CentroAcopio> listarConCapacidad() {
        return logisticaService.obtenerCentrosConCapacidad();
    }

    @Operation(summary = "Registrar un nuevo centro de acopio")
    @PostMapping("/centros")
    public ResponseEntity<CentroAcopio> crearCentro(@RequestBody CentroAcopio centro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logisticaService.agregarCentro(centro));
    }

    @Operation(summary = "Actualizar la ocupación actual de un centro de acopio")
    @PutMapping("/centros/{id}/ocupacion")
    public ResponseEntity<?> actualizarOcupacion(@PathVariable Long id,
                                                  @RequestParam Integer ocupacion) {
        return logisticaService.actualizarOcupacion(id, ocupacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un centro de acopio por su ID")
    @DeleteMapping("/centros/{id}")
    public ResponseEntity<Map<String, String>> eliminarCentro(@PathVariable Long id) {
        if (logisticaService.eliminarCentro(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Centro eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    // ── Envíos (con Observer) ──────────────────────────────────────────

    @Operation(summary = "Listar todos los envíos")
    @GetMapping("/envios")
    public List<Envio> listarEnvios() {
        return logisticaService.obtenerEnvios();
    }

    @Operation(summary = "Listar envíos filtrados por estado")
    @GetMapping("/envios/estado/{estado}")
    public List<Envio> listarPorEstado(@PathVariable String estado) {
        return logisticaService.obtenerPorEstado(estado);
    }

    @Operation(summary = "Crear un nuevo envío (notifica a los observadores registrados)")
    @PostMapping("/envios")
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logisticaService.crearEnvio(envio));
    }

    @Operation(summary = "Marcar un envío como despachado (EN_CAMINO)")
    @PutMapping("/envios/{id}/despachar")
    public ResponseEntity<?> despachar(@PathVariable Long id) {
        return logisticaService.despacharEnvio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Confirmar la entrega de un envío")
    @PutMapping("/envios/{id}/entregar")
    public ResponseEntity<?> entregar(@PathVariable Long id,
                                       @RequestParam(defaultValue = "") String observaciones) {
        return logisticaService.confirmarEntrega(id, observaciones)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cancelar un envío")
    @PutMapping("/envios/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id,
                                       @RequestParam(defaultValue = "") String motivo) {
        return logisticaService.cancelarEnvio(id, motivo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un envío por su ID")
    @DeleteMapping("/envios/{id}")
    public ResponseEntity<Map<String, String>> eliminarEnvio(@PathVariable Long id) {
        if (logisticaService.eliminarEnvio(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Envío eliminado"));
        }
        return ResponseEntity.notFound().build();
    }
}
