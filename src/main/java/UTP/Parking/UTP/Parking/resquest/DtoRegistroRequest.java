package UTP.Parking.UTP.Parking.resquest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoRegistroRequest {
    private Integer idEstacionamiento;
    private Long idUsuario;
    private Integer idUsuarioSeguridad;
    private String placa;
}
