package lowcarbon.lowcarbonbackend.model;
import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import lowcarbon.lowcarbonbackend.dto.PredictionData;
import lowcarbon.lowcarbonbackend.model.Enum.RiskLevel;
import lowcarbon.lowcarbonbackend.model.Enum.TrendType;


@Entity
@Table(name = "predictions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gpu_device_id")
    private GpuDevice gpuDevice;

    private Double nextHourAverageUtilization;
    private Double nextHourMaximumUtilization;
    private Double nextHourMinimumUtilization;
    private Double peakProbabilityPercentage;
    private Double confidenceScore;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    private TrendType trend;

    private Timestamp createdDate;

    public static Prediction fromApiResponse(GpuDevice gpuDevice, PredictionData predictionData) {
        if (gpuDevice == null || predictionData == null) {
            throw new IllegalArgumentException("GPU device and prediction data cannot be null");
        }
        Prediction prediction = new Prediction();
        prediction.setGpuDevice(gpuDevice);
        prediction.setNextHourAverageUtilization(predictionData.getNextHourAverageUtilization());
        prediction.setNextHourMaximumUtilization(predictionData.getNextHourMaximumUtilization());
        prediction.setNextHourMinimumUtilization(predictionData.getNextHourMinimumUtilization());
        prediction.setPeakProbabilityPercentage(predictionData.getPeakProbabilityPercentage());
        prediction.setConfidenceScore(predictionData.getConfidenceScore());
        prediction.setRiskLevel(predictionData.getRiskLevelEnum());
        prediction.setTrend(predictionData.getTrendEnum());

        prediction.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        return prediction;
    }
}