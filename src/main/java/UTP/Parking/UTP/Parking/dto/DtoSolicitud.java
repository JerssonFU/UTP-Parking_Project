package UTP.Parking.UTP.Parking.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DtoSolicitud {
    private Integer idSolicitud;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaRespuesta;
    private String estado;
    private String comentario;
    private Integer idUsuario;
    private Integer idVehiculo;
    private Integer idUsuarioSae;
    private String placa;
    private String categoria;
}
