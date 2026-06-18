package donaton.msdonaciones.controller;

import donaton.msdonaciones.model.Donacion;
import donaton.msdonaciones.service.DonacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Donaciones", description = "Gestión del ciclo de vida de donaciones (ROPA, ALIMENTO, MEDICO, HIGIENE)")
public class DonacionController {

    private final DonacionService donacionService;

    public DonacionController(DonacionService donacionService) {
        this.donacionService = donacionService;
    }

    @Operation(summary = "Listar todas las donaciones registradas")
    @GetMapping
    public List<Donacion> listarTodas() {
        return donacionService.obtenerTodas();
    }

    @Operation(summary = "Obtener una donación por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Donacion> obtenerPorId(@PathVariable Long id) {
        return donacionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar donaciones filtradas por estado (RECIBIDA, EN_PROCESO, DISTRIBUIDA)")
    @GetMapping("/estado/{estado}")
    public List<Donacion> listarPorEstado(@PathVariable String estado) {
        return donacionService.obtenerPorEstado(estado);
    }

    @Operation(summary = "Listar donaciones asociadas a un centro de acopio")
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
    @Operation(summary = "Registrar una nueva donación (activa el Factory Method según el tipo)")
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

    @Operation(summary = "Actualizar el estado de una donación existente")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                               @RequestParam String estado) {
        Optional<Donacion> resultado = donacionService.actualizarEstado(id, estado);
        if (resultado.isPresent()) {
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar una donación por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        if (donacionService.eliminar(id)) {
            return ResponseEntity.ok(Map.of("mensaje", "Donación eliminada correctamente"));
        }
        return ResponseEntity.notFound().build();
    }
}
