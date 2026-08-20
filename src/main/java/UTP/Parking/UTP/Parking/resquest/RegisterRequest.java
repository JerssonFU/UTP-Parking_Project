package UTP.Parking.UTP.Parking.resquest;

import UTP.Parking.UTP.Parking.model.RolesUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private Long  idUsuario;
    private String username;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email_universitario;
    private Integer estado;
    private String password;
    private String codigoUniversitario;
    private String descripcionCarrera;
    private RolesUsuario roles;

}
