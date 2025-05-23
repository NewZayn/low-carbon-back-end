package lowcarbon.lowcarbonbackend.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lowcarbon.lowcarbonbackend.dto.GpuMetricsAccumulatedDTO;
import lowcarbon.lowcarbonbackend.dto.PrevisionDTO;
import lowcarbon.lowcarbonbackend.model.GpuDevice;
import lowcarbon.lowcarbonbackend.model.GpuMetrics;
import lowcarbon.lowcarbonbackend.model.Prevision;
import lowcarbon.lowcarbonbackend.repository.GpuDeviceRepository;
import lowcarbon.lowcarbonbackend.repository.GpuMetricsRepository;
import lowcarbon.lowcarbonbackend.repository.PrevisionRepository;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrevisionService {
    private final GpuMetricsRepository metricsRepository;
    private final PrevisionRepository previsionRepository;
    private final GpuDeviceRepository gpuDeviceRepository;
    private static final String IA_ENDPOINT = "http://localhost:5000/predict";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();


    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void predictAllGpus() {
        Instant end = Instant.now();
        Instant start = end.minus(1, ChronoUnit.HOURS);
        Timestamp sqlStart = Timestamp.from(start);
        Timestamp sqlEnd = Timestamp.from(end);
        
        System.out.println("Buscando GPUs entre " + sqlStart + " e " + sqlEnd);
        List<String> gpuIds = metricsRepository.findDistinctGpuIdsBetween(sqlStart, sqlEnd);
        System.out.println("GPUs encontradas: " + gpuIds.size() + " - " + gpuIds);
        
        if (gpuIds.isEmpty()) {
            System.out.println("Nenhuma GPU encontrada no período. Verifique se existem métricas registradas recentemente.");
            return;
        }

        gpuIds.forEach(id -> {
            List<GpuMetrics> window = metricsRepository.findByGpuIdAndTimestampBetweenOrderByTimestampAsc(id, sqlStart, sqlEnd);
            if (window.isEmpty()) {
                System.out.println("Nenhuma métrica encontrada para GPU " + id);
                return;
            }
            System.out.println("Construindo DTO para GPU id = " + id + ", janela size = " + window.size());
            GpuMetricsAccumulatedDTO dto = buildWindowDto(id, window);
            try {
                String jsonBody = objectMapper.writeValueAsString(dto);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> req = new HttpEntity<>(jsonBody, headers);
                System.out.println("Enviando requisição para " + IA_ENDPOINT);
                ResponseEntity<PrevisionDTO> resp = restTemplate.exchange(
                        IA_ENDPOINT,
                        HttpMethod.POST,
                        req,
                        PrevisionDTO.class
                );
                 PrevisionDTO previsionDTO = resp.getBody();
                 String g = window.getFirst().getGpuDevice().getCode();
                 GpuDevice gpuDevice = gpuDeviceRepository.findByCode(g).orElseThrow();
                 addPrevision(new Prevision(null, gpuDevice, previsionDTO.getPrediction(), previsionDTO.getTimestamp()));
                System.out.println("IA response for gpuId = " + id + " : " + resp.getStatusCode());
            } catch (Exception e) {
                System.err.println("Erro ao enviar para IA: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private GpuMetricsAccumulatedDTO buildWindowDto(String id, List<GpuMetrics> list) {
        GpuMetricsAccumulatedDTO dto = new GpuMetricsAccumulatedDTO();
        dto.setGpuId(id);
        dto.setWindowMinutes(60);
        dto.setTimestamps(list.stream().map(m -> m.getTimestamp().getTime()).collect(Collectors.toList()));
        dto.setGpuUtil(list.stream().map(GpuMetrics::getGpuUtilization).collect(Collectors.toList()));
        dto.setMemUtil(list.stream().map(GpuMetrics::getMemoryUtilization).collect(Collectors.toList()));
        dto.setPower(list.stream().map(GpuMetrics::getGpuPowerDraw).collect(Collectors.toList()));
        dto.setTemperature(list.stream().map(GpuMetrics::getGpuTemperature).collect(Collectors.toList()));
        return dto;
    }


    public Prevision addPrevision(Prevision prevision) {
        previsionRepository.save(prevision);
        return prevision;
    }

    public List<Prevision> getAllPrevisionsByGpuCode(String gpuCode) {
        return previsionRepository.findByGpuDeviceCodeOrderByCreatedDateDesc(gpuCode);
    }

    public List<Prevision> getAllPrevisionsByGpuId(Long gpuId) {
        return previsionRepository.findByGpuDeviceIdOrderByCreatedDateDesc(gpuId);
    }

    public Prevision getLatestPrevisionByGpuCode(String gpuCode) {
        return previsionRepository.findLatestByGpuDeviceCode(gpuCode);
    }
}