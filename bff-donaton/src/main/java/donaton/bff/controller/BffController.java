package donaton.bff.controller;

import donaton.bff.dto.DashboardResumenDTO;
import donaton.bff.service.BffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador del BFF.
 * Expone endpoints agregados para el frontend React.
 * Cada endpoint compone datos de múltiples microservicios.
 */
@RestController
@RequestMapping("/bff")
@CrossOrigin(origins = "*")
public class BffController {

    private final BffService bffService;

    public BffController(BffService bffService) {
        this.bffService = bffService;
    }

    /**
     * Dashboard principal: agrega estadísticas de donaciones, centros y envíos.
     * El frontend React realiza UNA sola llamada en lugar de tres.
     *
     * GET /bff/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResumenDTO> getDashboard() {
        return ResponseEntity.ok(bffService.obtenerResumenDashboard());
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("BFF-Donaton OK");
    }
}
