package donaton.msdonaciones.controller;

import donaton.msdonaciones.model.Donacion;
import donaton.msdonaciones.service.DonacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controlador REST del microservicio MS-Donaciones.
 * Expone endpoints bajo /api/donaciones para el BFF y la API Gateway.
 */
@RestController
@RequestMapping("/api/donaciones")
@CrossOrigin(origins = "*")
public class DonacionController {

    private final DonacionService donacionService;

    public DonacionController(DonacionService donacionService) {
        this.donacionService = donacionService;
    }

    @GetMapping
    public List<Donacion> listarTodas() {
        return donacionService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donacion> obtenerPorId(@PathVariable Long id) {
        return donacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public List<Donacion> listarPorEstado(@PathVariable String estado) {
        return donacionService.obtenerPorEstado(estado);
    }

    @GetMapping("/centro/{centroId}")
    public List<Donacion> listarPorCentro(@PathVariable Long centroId) {
        return donacionService.obtenerPorCentro(centroId);
    }

    /**
     * Crea una donación. El campo "tipo" activa el Factory Method correspondiente.
     * Body esperado:
     * {
     *   "tipo": "ROPA|ALIMENTO|MEDICO|HIGIENE",
     *   "origen": "string",
     *   "cantidad": number,
     *   "centroAcopioId": number,
     *   "descripcion": "string"
     * }
     */
    @PostMapping
    public ResponseEntity<?> crearDonacion(@RequestBody Map<String, Object> body) {
        try {
            String tipo        = (String) body.get("tipo");
            String origen      = (String) body.get("origen");
            Integer cantidad   = (Integer) body.get("cantidad");
            Long centroId      = ((Number) body.get("centroAcopioId")).longValue();
            String descripcion = (String) body.getOrDefault("descripcion", "");

            Donacion creada = donacionService.crearDonacion(tipo, origen, cantidad, centroId, descripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                               @RequestParam String estado) {
        Optional<Donacion> resultado = donacionService.actualizarEstado(id, estado);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        if (donacionService.eliminar(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Donación eliminada correctamente"));
        }
        return ResponseEntity.notFound().build();
    }
}
