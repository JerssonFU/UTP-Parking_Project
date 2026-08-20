package UTP.Parking.UTP.Parking.interfaceService;


import UTP.Parking.UTP.Parking.dto.RegistroExportDTO;
import UTP.Parking.UTP.Parking.model.Registro;
import UTP.Parking.UTP.Parking.resquest.DtoRegistro;
import UTP.Parking.UTP.Parking.resquest.DtoRegistroRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface IRegistroService {
    void registrarIngreso(DtoRegistroRequest request);
    Registro registrarSalida(Integer idVehiculo);
    void registrarObservacion(String placa, String observacion);
    List<DtoRegistro> obtenerRegistrosNoSalida();
    List<DtoRegistro> obtenerRegistroConObservacion();
    List<RegistroExportDTO> getAllRegistrosForExport();
    List<RegistroExportDTO> getRegistrosPorIntervaloFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
