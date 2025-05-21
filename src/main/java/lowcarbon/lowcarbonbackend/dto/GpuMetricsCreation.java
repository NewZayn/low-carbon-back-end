package lowcarbon.lowcarbonbackend.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class GpuMetricsCreation {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    @JsonProperty("gpu_id")
    private String gpuId;

    @JsonProperty("gpu_utilization")
    private Double gpuUtilization;

    @JsonProperty("memory_utilization")
    private Double memoryUtilization;

    @JsonProperty("gpu_power_draw")
    private Double gpuPowerDraw;

    @JsonProperty("gpu_temperature")
    private Double gpuTemperature;

    @JsonProperty("gpu_fan_speed")
    private Double gpuFanSpeed;

    @JsonProperty("gpu_clock_speed")
    private Double gpuClockSpeed;

    @JsonProperty("cpu_utilization")
    private Double cpuUtilization;

    @JsonProperty("memory_usage")
    private Double memoryUsage;

    @JsonProperty("server_power_draw")
    private Double serverPowerDraw;

    @JsonProperty("server_temperature")
    private Double serverTemperature;

    @JsonProperty("disk_usage")
    private Double diskUsage;

    @JsonProperty("network_bandwidth")
    private Double networkBandwidth;
}
