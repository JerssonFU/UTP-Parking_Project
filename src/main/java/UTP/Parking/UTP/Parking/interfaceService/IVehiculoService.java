package UTP.Parking.UTP.Parking.interfaceService;

import UTP.Parking.UTP.Parking.dto.DtoVehiculo;
import UTP.Parking.UTP.Parking.dto.VehiculoExportDTO;
import UTP.Parking.UTP.Parking.resquest.DtoVehiculoRequest;

import java.util.List;

public interface IVehiculoService {
    DtoVehiculo buscarVehiculoId(Integer id);
    void registrarVehiculo(DtoVehiculoRequest v);
    DtoVehiculo buscarVehiculo(String placa);
    Boolean validarVehiculo(String placa);
    void actualizarEstadoVehiculo(Integer id);
    List<VehiculoExportDTO> getAllVehiculos();
}
