package lowcarbon.lowcarbonbackend.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lowcarbon.lowcarbonbackend.dto.GpuMetricsAccumulatedDTO;
import lowcarbon.lowcarbonbackend.dto.PredictionData;
import lowcarbon.lowcarbonbackend.model.GpuDevice;
import lowcarbon.lowcarbonbackend.model.GpuMetrics;
import lowcarbon.lowcarbonbackend.model.Prediction;
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


    @Scheduled(fixedRate = 1000 * 60 * 60)
    @Transactional
    public void predictAllGpus() {
        Instant end = Instant.now();
        Instant start = end.minus(1, ChronoUnit.HOURS);
        Timestamp sqlStart = Timestamp.from(start);
        Timestamp sqlEnd = Timestamp.from(end);

        System.out.println("Find GPUs between " + sqlStart + " e " + sqlEnd);
        List<String> gpuIds = metricsRepository.findDistinctGpuIdsBetween(sqlStart, sqlEnd);
        System.out.println("GPUs  : " + gpuIds.size() + " - " + gpuIds);

        if (gpuIds.isEmpty()) {
            System.out.println("GPUs not found in database. Skipping prediction.");
            return;
        }

        gpuIds.forEach(id -> {
            try {
                List<GpuMetrics> window = metricsRepository.findByGpuIdAndTimestampBetweenOrderByTimestampAsc(id, sqlStart, sqlEnd);

                if (window.isEmpty()) {
                    System.out.println("Nenhuma métrica encontrada para GPU " + id);
                    return;
                }

                if (window.size() < 100) {
                    System.out.println("Dados insuficientes para GPU " + id + " (apenas " + window.size() + " pontos)");
                    return;
                }

                System.out.println("Construindo DTO para GPU id = " + id + ", janela size = " + window.size());
                GpuMetricsAccumulatedDTO dto = buildWindowDto(id, window);

                String jsonBody = objectMapper.writeValueAsString(dto);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> req = new HttpEntity<>(jsonBody, headers);

                System.out.println("Enviando requisição para " + IA_ENDPOINT);

                ResponseEntity<PredictionData> resp = restTemplate.exchange(
                        IA_ENDPOINT,
                        HttpMethod.POST,
                        req,
                        PredictionData.class
                );

                // ✅ VERIFICAR RESPOSTA
                if (!resp.getStatusCode().is2xxSuccessful()) {
                    System.err.println("❌ API error for GPU " + id + ": " + resp.getStatusCode());
                    return;
                }

                PredictionData predictionData = resp.getBody();
                if (predictionData == null) {
                    System.err.println("❌ Response body is null for GPU " + id);
                    return;
                }
                System.out.println("🔍 DEBUG INFO:");
                System.out.println("Response status: " + resp.getStatusCode());
                System.out.println("Response body: " + predictionData);
                System.out.println("Avg Utilization: " + predictionData.getNextHourAverageUtilization());
                System.out.println("Risk Level: " + predictionData.getRiskLevel());
                System.out.println("Trend: " + predictionData.getTrend());

                if (predictionData.getNextHourAverageUtilization() == null) {
                    System.err.println("❌ Essential prediction data is null for GPU " + id);
                    return;
                }

                String gpuCode = window.get(0).getGpuDevice().getCode();
                Optional<GpuDevice> gpuDeviceOpt = gpuDeviceRepository.findByCode(gpuCode);

                if (gpuDeviceOpt.isEmpty()) {
                    System.err.println("❌ GPU device not found with code: " + gpuCode);
                    return;
                }

                GpuDevice gpuDevice = gpuDeviceOpt.get();
                Prediction prediction = Prediction.fromApiResponse(gpuDevice, predictionData);
                addPrevision(prediction);

                System.out.println("✅ IA prediction saved successfully for GPU " + id +
                        " | Avg: " + prediction.getNextHourAverageUtilization() + "%" +
                        " | Risk: " + prediction.getRiskLevel() +
                        " | Trend: " + prediction.getTrend());

            } catch (Exception e) {
                System.err.println("❌ Erro ao processar GPU " + id + ": " + e.getMessage());
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


    public Prediction addPrevision(Prediction prediction) {
        previsionRepository.save(prediction);
        return prediction;
    }

    public List<Prediction> getAllPrevisionsByGpuCode(String gpuCode) {
        return previsionRepository.findByGpuDeviceCodeOrderByCreatedDateDesc(gpuCode);
    }

    public List<Prediction> getAllPrevisionsByGpuId(Long gpuId) {
        return previsionRepository.findByGpuDeviceIdOrderByCreatedDateDesc(gpuId);
    }

    public Prediction getLatestPrevisionByGpuCode(String gpuCode) {
        return previsionRepository.findLatestByGpuDeviceCode(gpuCode);
    }
}








