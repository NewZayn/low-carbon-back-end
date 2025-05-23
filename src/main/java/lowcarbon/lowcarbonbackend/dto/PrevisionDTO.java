package lowcarbon.lowcarbonbackend.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PrevisionDTO {
    float prediction;
    Timestamp timestamp;
    String model_version;
    int imput_data_points;
}
