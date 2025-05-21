package lowcarbon.lowcarbonbackend.repository;

import lowcarbon.lowcarbonbackend.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrevisionRepository extends JpaRepository<Prevision, Long> {
    
    @Query("SELECT p FROM Prevision p WHERE p.gpuDevice.code = :gpuCode ORDER BY p.createdDate DESC")
    List<Prevision> findByGpuDeviceCodeOrderByCreatedDateDesc(@Param("gpuCode") String gpuCode);
    
    @Query("SELECT p FROM Prevision p WHERE p.gpuDevice.id = :gpuId ORDER BY p.createdDate DESC")
    List<Prevision> findByGpuDeviceIdOrderByCreatedDateDesc(@Param("gpuId") Long gpuId);
    
    @Query("SELECT p FROM Prevision p WHERE p.gpuDevice.code = :gpuCode ORDER BY p.createdDate DESC LIMIT 1")
    Prevision findLatestByGpuDeviceCode(@Param("gpuCode") String gpuCode);
}
