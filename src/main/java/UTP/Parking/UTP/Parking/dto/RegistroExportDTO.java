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
public class RegistroExportDTO {
    
    private Integer idRegistro;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private String observacion;
    private String estacionamientoNombre;
    private String vehiculoPlaca;
    private String usuariocodigoUniversitario;
    private String usuarioSeguridadUsername;
    private String fechaIngresoFormatted;
    private String horaIngresoFormatted;

    public String getFechaIngresoFormatted() {
        return fechaIngreso == null ? "" : fechaIngreso.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String getHoraIngresoFormatted() {
        return fechaIngreso == null ? "" : fechaIngreso.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
