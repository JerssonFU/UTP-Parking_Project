package UTP.Parking.UTP.Parking.repository;

import UTP.Parking.UTP.Parking.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    @Query("SELECT s FROM Solicitud s WHERE s.usuario.idUsuario = :usuarioId ORDER BY s.fechaSolicitud DESC")
    List<Solicitud> findByIdUsuario(@Param("usuarioId") Long usuarioId);

    @Query("SELECT s FROM Solicitud s WHERE s.fechaSolicitud BETWEEN :start AND :end")
    List<Solicitud> findByFechaSolicitudBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Solicitud> findByUsuarioUsernameOrderByFechaSolicitudDesc(String username);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Solicitud s " +
            "WHERE s.vehiculo.id_vehiculo = :idVehiculo AND UPPER(s.estado) = UPPER(:estado)")
    boolean existsByVehiculoAndEstado(@Param("idVehiculo") Integer idVehiculo, @Param("estado") String estado);
}
