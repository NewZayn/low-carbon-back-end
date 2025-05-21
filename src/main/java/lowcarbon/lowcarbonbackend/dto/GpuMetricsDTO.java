package lowcarbon.lowcarbonbackend.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lowcarbon.lowcarbonbackend.model.GpuMetrics;

import java.util.Date;

public class GpuMetricsDTO {

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


    public GpuMetricsDTO(GpuMetrics metrics) {
            this.gpuUtilization = metrics.getGpuUtilization();
            this.memoryUtilization = metrics.getMemoryUtilization();
            this.gpuPowerDraw = metrics.getGpuPowerDraw();
            this.gpuTemperature = metrics.getGpuTemperature();
            this.gpuFanSpeed = metrics.getGpuFanSpeed();
            this.gpuClockSpeed = metrics.getGpuClockSpeed();
            this.cpuUtilization = metrics.getCpuUtilization();
            this.memoryUsage = metrics.getMemoryUsage();
            this.serverPowerDraw = metrics.getServerPowerDraw();
            this.serverTemperature = metrics.getServerTemperature();
            this.diskUsage = metrics.getDiskUsage();
            this.networkBandwidth = metrics.getNetworkBandwidth();
        }

}

