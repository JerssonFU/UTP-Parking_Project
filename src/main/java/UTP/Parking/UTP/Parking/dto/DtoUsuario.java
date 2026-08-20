package UTP.Parking.UTP.Parking.dto;

import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.RolesUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoUsuario {

    private Long  idUsuario;
    private String username;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email_universitario;
    private Integer estado;
    private String codigoUniversitario;
    private String descripcionCarrera;
    private RolesUsuario roles;
    private Role role;

}
