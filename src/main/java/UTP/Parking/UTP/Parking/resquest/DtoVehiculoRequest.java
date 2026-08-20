package UTP.Parking.UTP.Parking.resquest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoVehiculoRequest {
    private String categoria;
    private String placa;
    private Integer idUsuario;
    private String campus;
}
