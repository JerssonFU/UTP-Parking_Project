package UTP.Parking.UTP.Parking.repository;


import UTP.Parking.UTP.Parking.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO registros (fecha_ingreso, fecha_salida, observacion, id_estacionamiento, id_usuario, id_usuario_seguridad, id_vehiculo) " +
                    "VALUES (CURRENT_TIMESTAMP, NULL, NULL, :id_estacionamiento, :id_usuario, :id_usuario_seguridad, :id_vehiculo)",
            nativeQuery = true)
    void insertarRegistro(@Param("id_estacionamiento") int id_estacionamiento,
                          @Param("id_usuario") int id_usuario,
                          @Param("id_usuario_seguridad") int id_usuario_seguridad,
                          @Param("id_vehiculo") int id_vehiculo);

    @Query("SELECT r from Registro r where r.vehiculo.id_vehiculo = :idVehiculo AND r.fecha_salida IS NULL")
    Registro findRegistro(int idVehiculo);

    @Query("SELECT r from Registro r where r.fecha_salida IS NULL")
    List<Registro> findRegistrosNoSalida();

    @Query("SELECT r from Registro r where r.observacion IS NOT NULL")
    List<Registro> findRegistroConObservacion();

    @Query("SELECT r FROM Registro r WHERE r.fecha_ingreso BETWEEN :start AND :end")
    List<Registro> findByFechaIngresoBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
