package UTP.Parking.UTP.Parking.resquest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DTOSolicitudRequest {
    private Integer idUsuario;
    private Integer id_vehiculo;
}
