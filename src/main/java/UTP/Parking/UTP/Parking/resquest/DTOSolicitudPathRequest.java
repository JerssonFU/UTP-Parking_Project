package UTP.Parking.UTP.Parking.resquest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DTOSolicitudPathRequest {
    private String estado;
    private Integer idSae;
    private String comentario;
}
