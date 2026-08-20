package UTP.Parking.UTP.Parking.interfaceService;

import UTP.Parking.UTP.Parking.model.Sede;

import java.util.List;

public interface ISedeService {
    List<Sede> listarSedes();
    Sede buscarSede(int id);
    void actualizarCantidadEspacios(Sede sede);
}
