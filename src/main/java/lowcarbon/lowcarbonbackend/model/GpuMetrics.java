package lowcarbon.lowcarbonbackend.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class GpuMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    @ManyToOne(optional = false)
    @JoinColumn(name = "gpu_device_id")
    private GpuDevice gpuDevice;
    private Double gpuUtilization;
    private Double memoryUtilization;
    private Double gpuPowerDraw;
    private Double gpuTemperature;
    private Double gpuFanSpeed;
    private Double gpuClockSpeed;
    private Double cpuUtilization;
    private Double memoryUsage;
    private Double serverPowerDraw;
    private Double serverTemperature;
    private Double diskUsage;
    private Double networkBandwidth;
}