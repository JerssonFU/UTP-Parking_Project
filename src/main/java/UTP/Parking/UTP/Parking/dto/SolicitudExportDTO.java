
package UTP.Parking.UTP.Parking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudExportDTO {
    
    private Integer idSolicitud;
    private String estado;
    private LocalDateTime fechaRespuesta;
    private LocalDateTime fechaSolicitud;
    private String usuarioUsername;
    private String vehiculoPlaca;
    private String fechaSolicitudFormatted;
    private String horaSolicitudFormatted;
    
    public String getFechaSolicitudFormatted() {
        return fechaSolicitud == null ? "" : fechaSolicitud.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String getHoraSolicitudFormatted() {
        return fechaSolicitud == null ? "" : fechaSolicitud.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
}
