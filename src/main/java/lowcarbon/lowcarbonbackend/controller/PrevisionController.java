package lowcarbon.lowcarbonbackend.controller;

import lowcarbon.lowcarbonbackend.model.Prevision;
import lowcarbon.lowcarbonbackend.service.PrevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prevision")
public class PrevisionController {

    private final PrevisionService previsionService;

    @Autowired
    public PrevisionController(PrevisionService previsionService) {
        this.previsionService = previsionService;
    }

    @GetMapping("/gpu/code/{gpuCode}")
    public ResponseEntity<List<Prevision>> getPrevisionsByGpuCode(@PathVariable String gpuCode) {
        List<Prevision> previsions = previsionService.getAllPrevisionsByGpuCode(gpuCode);
        return ResponseEntity.ok(previsions);
    }
    
    @GetMapping("/gpu/id/{gpuId}")
    public ResponseEntity<List<Prevision>> getPrevisionsByGpuId(@PathVariable Long gpuId) {
        List<Prevision> previsions = previsionService.getAllPrevisionsByGpuId(gpuId);
        return ResponseEntity.ok(previsions);
    }
    
    @GetMapping("/gpu/latest/{gpuCode}")
    public ResponseEntity<Prevision> getLatestPrevision(@PathVariable String gpuCode) {
        Prevision latestPrevision = previsionService.getLatestPrevisionByGpuCode(gpuCode);
        if (latestPrevision == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestPrevision);
    }
}
