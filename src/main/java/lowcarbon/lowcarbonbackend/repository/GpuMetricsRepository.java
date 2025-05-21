package lowcarbon.lowcarbonbackend.repository;

import lowcarbon.lowcarbonbackend.model.GpuMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface GpuMetricsRepository extends JpaRepository<GpuMetrics, Long> {

    @Query("""
           select distinct gm.gpuDevice.id
             from GpuMetrics gm
            where gm.timestamp between :start and :end
           """)
    List<String> findDistinctGpuIdsBetween(@Param("start") Timestamp start,
                                           @Param("end")   Timestamp end);

    @Query("""
           select gm
             from GpuMetrics gm
            where gm.gpuDevice.id = :gpuId
              and gm.timestamp between :start and :end
            order by gm.timestamp asc
           """)
    List<GpuMetrics> findByGpuIdAndTimestampBetweenOrderByTimestampAsc(
            @Param("gpuId") String gpuId,
            @Param("start") Timestamp start,
            @Param("end")   Timestamp end);
}
