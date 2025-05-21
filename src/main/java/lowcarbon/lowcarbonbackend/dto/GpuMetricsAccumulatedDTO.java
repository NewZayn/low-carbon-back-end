package lowcarbon.lowcarbonbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class GpuMetricsAccumulatedDTO {

    private String gpuId;
    private int windowMinutes;
    private List<Long> timestamps;
    private List<Double> gpuUtil;
    private List<Double> memUtil;
    private List<Double> power;
    private List<Double> temperature;

}
