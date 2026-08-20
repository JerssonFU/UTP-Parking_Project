package UTP.Parking.UTP.Parking.interfaceService;

import UTP.Parking.UTP.Parking.dto.SolicitudExportDTO;
import UTP.Parking.UTP.Parking.model.Solicitud;
import UTP.Parking.UTP.Parking.resquest.DTOSolicitudRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ISolicitudService {
    List<Solicitud> listarSolicitudesId(Long usuarioId);
    List<Solicitud> listarSolicitudes();
    void registrarSolicitud(DTOSolicitudRequest dtoSolicitud);
    Solicitud actualizarSolicitud(Integer idSolicitud, String estado, Integer idUsuarioSae);
    void registrarComentario(Integer idSolicitud, String comentario);
    List<SolicitudExportDTO> getAllSolicitudesForExport();
    List<SolicitudExportDTO> getSolicitudesPorIntervaloFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Solicitud> findBySolicitudByUsername(String username);
}
