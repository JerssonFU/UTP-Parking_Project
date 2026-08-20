
package UTP.Parking.UTP.Parking.interfaceService;
import UTP.Parking.UTP.Parking.dto.UsuarioExportDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface IUsuarioService {
    List<UsuarioExportDTO> exportAllUsuarios();
    Resource exportUsuariosSeguridadToExcel() throws IOException;
    List<UsuarioExportDTO> findUsuariosByRoleSeguridad();
    List<UsuarioExportDTO> findUsuariosByRoleAlumno();

}
