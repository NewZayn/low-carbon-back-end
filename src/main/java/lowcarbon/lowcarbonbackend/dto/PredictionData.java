package lowcarbon.lowcarbonbackend.dto;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lowcarbon.lowcarbonbackend.model.Enum.RiskLevel;
import lowcarbon.lowcarbonbackend.model.Enum.TrendType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionData {

    private Double nextHourAverageUtilization;
    private Double nextHourMaximumUtilization;
    private Double nextHourMinimumUtilization;
    private Double peakProbabilityPercentage;
    private Double confidenceScore;
    private String riskLevel;
    private String trend;

    public RiskLevel getRiskLevelEnum() {
        if (riskLevel == null) return null;
        try {
            return RiskLevel.valueOf(riskLevel.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Valor inválido para RiskLevel: " + riskLevel);
            return RiskLevel.LOW; // Valor padrão
        }
    }

    public TrendType getTrendEnum() {
        if (trend == null) return null;
        try {
            return TrendType.valueOf(trend.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Valor inválido para TrendType: " + trend);
            return TrendType.STABLE; // Valor padrão
        }
    }
}
