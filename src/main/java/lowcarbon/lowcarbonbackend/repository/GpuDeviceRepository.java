package lowcarbon.lowcarbonbackend.repository;

import lowcarbon.lowcarbonbackend.model.GpuDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface GpuDeviceRepository extends JpaRepository<GpuDevice, Long> {
    Optional<GpuDevice> findByCode(String code);
}