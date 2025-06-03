package lowcarbon.lowcarbonbackend.controller;
import lowcarbon.lowcarbonbackend.dto.GpuMetricsCreation;
import lowcarbon.lowcarbonbackend.service.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    @Autowired
    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @PostMapping("/addMetrics")
    public ResponseEntity<GpuMetricsCreation> addMetrics(@RequestBody GpuMetricsCreation metrics) {
        metrics.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
        metricsService.addMetrics(metrics);
        return ResponseEntity.status(HttpStatus.CREATED).body(metrics);
    }

}
