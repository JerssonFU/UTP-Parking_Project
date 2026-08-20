package UTP.Parking.UTP.Parking.interfaceService;

import UTP.Parking.UTP.Parking.model.Estacionamiento;

import java.util.List;

public interface IEstacionamientoService {
    List<Estacionamiento> listarEstacionamientos(int idSede, int piso);
    Estacionamiento buscarEstacionamiento(int idEstacionamiento);
}
