package lowcarbon.lowcarbonbackend.service;
import lowcarbon.lowcarbonbackend.dto.GpuMetricsCreation;
import lowcarbon.lowcarbonbackend.model.GpuDevice;
import lowcarbon.lowcarbonbackend.model.GpuMetrics;
import lowcarbon.lowcarbonbackend.model.Prevision;
import lowcarbon.lowcarbonbackend.repository.GpuDeviceRepository;
import lowcarbon.lowcarbonbackend.repository.GpuMetricsRepository;
import lowcarbon.lowcarbonbackend.repository.PrevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    GpuMetricsRepository gpuMetricsRepository;

    PrevisionRepository previsionRepository;

    GpuDeviceRepository gpuDeviceRepository;

    @Autowired
    public MetricsService(GpuMetricsRepository gpuMetricsRepository,PrevisionRepository previsionRepository, GpuDeviceRepository gpuDeviceRepository) {
       this.gpuMetricsRepository = gpuMetricsRepository;
       this.previsionRepository = previsionRepository;
       this.gpuDeviceRepository = gpuDeviceRepository;
    }


    public GpuMetrics addMetrics(GpuMetricsCreation metrics) {
        GpuMetrics gpuMetrics = fromGpuCreationDTO(metrics);

        gpuMetricsRepository.save(gpuMetrics);
        return gpuMetrics;
    }

    public Prevision prevision(Prevision prevision) {
        previsionRepository.save(prevision);
        return prevision;
    }

    public GpuMetrics fromGpuCreationDTO(GpuMetricsCreation metrics) {
          GpuDevice gpuDevice =  gpuDeviceRepository.findByCode(metrics.getGpuId()).orElse(new GpuDevice(
                  null,
                     metrics.getGpuId()
          ));
          gpuDeviceRepository.save(gpuDevice);
        return new GpuMetrics(
                null,
                metrics.getTimestamp(),
                gpuDevice,
                metrics.getGpuUtilization(),
                metrics.getMemoryUtilization(),
                metrics.getCpuUtilization(),
                metrics.getGpuPowerDraw(),
                metrics.getGpuTemperature(),
                metrics.getGpuFanSpeed(),
                metrics.getGpuClockSpeed(),
                metrics.getMemoryUsage(),
                metrics.getServerPowerDraw(),
                metrics.getServerTemperature(),
                metrics.getDiskUsage(),
                metrics.getNetworkBandwidth()
        );

    }
}
