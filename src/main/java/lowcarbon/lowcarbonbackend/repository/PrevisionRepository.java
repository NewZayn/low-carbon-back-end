package lowcarbon.lowcarbonbackend.repository;

import lowcarbon.lowcarbonbackend.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrevisionRepository extends JpaRepository<Prediction, Long> {
    
    @Query("SELECT p FROM Prediction p WHERE p.gpuDevice.code = :gpuCode ORDER BY p.createdDate DESC")
    List<Prediction> findByGpuDeviceCodeOrderByCreatedDateDesc(@Param("gpuCode") String gpuCode);
    
    @Query("SELECT p FROM Prediction p WHERE p.gpuDevice.id = :gpuId ORDER BY p.createdDate DESC")
    List<Prediction> findByGpuDeviceIdOrderByCreatedDateDesc(@Param("gpuId") Long gpuId);
    
    @Query("SELECT p FROM Prediction p WHERE p.gpuDevice.code = :gpuCode ORDER BY p.createdDate DESC LIMIT 1")
    Prediction findLatestByGpuDeviceCode(@Param("gpuCode") String gpuCode);
}
