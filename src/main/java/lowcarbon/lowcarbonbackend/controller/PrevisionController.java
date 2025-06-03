package lowcarbon.lowcarbonbackend.controller;

import lowcarbon.lowcarbonbackend.model.Prediction;
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
    public ResponseEntity<List<Prediction>> getPrevisionsByGpuCode(@PathVariable String gpuCode) {
        List<Prediction> predictions = previsionService.getAllPrevisionsByGpuCode(gpuCode);
        return ResponseEntity.ok(predictions);
    }
    
    @GetMapping("/gpu/id/{gpuId}")
    public ResponseEntity<List<Prediction>> getPrevisionsByGpuId(@PathVariable Long gpuId) {
        List<Prediction> predictions = previsionService.getAllPrevisionsByGpuId(gpuId);
        return ResponseEntity.ok(predictions);
    }
    
    @GetMapping("/gpu/latest/{gpuCode}")
    public ResponseEntity<Prediction> getLatestPrevision(@PathVariable String gpuCode) {
        Prediction latestPrediction = previsionService.getLatestPrevisionByGpuCode(gpuCode);
        if (latestPrediction == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(latestPrediction);
    }
}
