package donaton.mslogistica.controller;

import donaton.mslogistica.model.CentroAcopio;
import donaton.mslogistica.model.Envio;
import donaton.mslogistica.service.LogisticaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/logistica")
@CrossOrigin(origins = "*")
public class LogisticaController {

    private final LogisticaService logisticaService;

    public LogisticaController(LogisticaService logisticaService) {
        this.logisticaService = logisticaService;
    }

    // ── Centros de Acopio ──────────────────────────────────────────────

    @GetMapping("/centros")
    public List<CentroAcopio> listarCentros() {
        return logisticaService.obtenerCentros();
    }

    @GetMapping("/centros/activos")
    public List<CentroAcopio> listarActivos() {
        return logisticaService.obtenerCentrosActivos();
    }

    @GetMapping("/centros/con-capacidad")
    public List<CentroAcopio> listarConCapacidad() {
        return logisticaService.obtenerCentrosConCapacidad();
    }

    @PostMapping("/centros")
    public ResponseEntity<CentroAcopio> crearCentro(@RequestBody CentroAcopio centro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logisticaService.agregarCentro(centro));
    }

    @PutMapping("/centros/{id}/ocupacion")
    public ResponseEntity<?> actualizarOcupacion(@PathVariable Long id,
                                                  @RequestParam Integer ocupacion) {
        return logisticaService.actualizarOcupacion(id, ocupacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/centros/{id}")
    public ResponseEntity<Map<String, String>> eliminarCentro(@PathVariable Long id) {
        if (logisticaService.eliminarCentro(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Centro eliminado"));
        }
        return ResponseEntity.notFound().build();
    }

    // ── Envíos (con Observer) ──────────────────────────────────────────

    @GetMapping("/envios")
    public List<Envio> listarEnvios() {
        return logisticaService.obtenerEnvios();
    }

    @GetMapping("/envios/estado/{estado}")
    public List<Envio> listarPorEstado(@PathVariable String estado) {
        return logisticaService.obtenerPorEstado(estado);
    }

    @PostMapping("/envios")
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logisticaService.crearEnvio(envio));
    }

    @PutMapping("/envios/{id}/despachar")
    public ResponseEntity<?> despachar(@PathVariable Long id) {
        return logisticaService.despacharEnvio(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/envios/{id}/entregar")
    public ResponseEntity<?> entregar(@PathVariable Long id,
                                       @RequestParam(defaultValue = "") String observaciones) {
        return logisticaService.confirmarEntrega(id, observaciones)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/envios/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id,
                                       @RequestParam(defaultValue = "") String motivo) {
        return logisticaService.cancelarEnvio(id, motivo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/envios/{id}")
    public ResponseEntity<Map<String, String>> eliminarEnvio(@PathVariable Long id) {
        if (logisticaService.eliminarEnvio(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Envío eliminado"));
        }
        return ResponseEntity.notFound().build();
    }
}
