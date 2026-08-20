package UTP.Parking.UTP.Parking.service;


import UTP.Parking.UTP.Parking.interfaceService.IEstacionamientoService;
import UTP.Parking.UTP.Parking.model.Estacionamiento;
import UTP.Parking.UTP.Parking.repository.EstacionamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstacionamientoService implements IEstacionamientoService {

    @Autowired
    private EstacionamientoRepository repository;

    @Override
    public List<Estacionamiento> listarEstacionamientos(int idSede, int piso) {
        return repository.findByIdSedeAndPiso(idSede, piso);
    }

    @Override
    public Estacionamiento buscarEstacionamiento(int idEstacionamiento) {
        return repository.findById(idEstacionamiento).orElse(null);
    }
}
