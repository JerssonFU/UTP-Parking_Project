package UTP.Parking.UTP.Parking.repository;

import UTP.Parking.UTP.Parking.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiculoRespository extends JpaRepository<Vehiculo, Integer> {
    Vehiculo findByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCase(String placa);
}
