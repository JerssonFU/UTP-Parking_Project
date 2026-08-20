package UTP.Parking.UTP.Parking.service;

import UTP.Parking.UTP.Parking.dto.DtoVehiculo;
import UTP.Parking.UTP.Parking.resquest.DtoVehiculoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RegistroVehiculoSolicitudService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public DtoVehiculo registrar(DtoVehiculoRequest request) {
        if (request == null || request.getIdUsuario() == null) {
            throw new IllegalArgumentException("Usuario no válido.");
        }

        String placa = request.getPlaca() == null ? "" : request.getPlaca().trim().toUpperCase();
        String categoria = request.getCategoria() == null ? "" : request.getCategoria().trim();
        String campus = request.getCampus() == null ? "" : request.getCampus().trim();

        if (campus.isEmpty()) {
            throw new IllegalArgumentException("Seleccione un campus válido.");
        }

        Integer usuarios = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.usuario WHERE \"C_PERL_CODIGO\" = ?",
                Integer.class,
                request.getIdUsuario()
        );
        if (usuarios == null || usuarios == 0) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        List<Integer> sedes = jdbcTemplate.query(
                "SELECT id_sede FROM public.sedes " +
                        "WHERE UPPER(TRIM(nombre)) = UPPER(TRIM(?)) ORDER BY id_sede LIMIT 1",
                new Object[]{campus},
                (rs, rowNum) -> rs.getInt("id_sede")
        );
        if (sedes.isEmpty()) {
            throw new IllegalArgumentException("Campus no encontrado.");
        }
        Integer idSede = sedes.get(0);

        Integer vehiculos = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.vehiculos WHERE UPPER(TRIM(placa)) = UPPER(TRIM(?))",
                Integer.class,
                placa
        );
        if (vehiculos != null && vehiculos > 0) {
            throw new IllegalStateException("La placa ya se encuentra registrada.");
        }

        Integer idVehiculo = jdbcTemplate.queryForObject(
                "INSERT INTO public.vehiculos (activo, aprobado, categoria, placa, id_usuario) " +
                        "VALUES (false, false, ?, ?, ?) RETURNING id_vehiculo",
                new Object[]{categoria, placa, request.getIdUsuario()},
                Integer.class
        );

        if (idVehiculo == null) {
            throw new IllegalStateException("No se pudo registrar el vehículo.");
        }

        jdbcTemplate.update(
                "INSERT INTO public.solicitudes " +
                        "(estado, fecha_respuesta, fecha_solicitud, id_usuario, id_vehiculo, comentario, id_usuario_sae, id_sede, id_estacionamiento) " +
                        "VALUES (?, NULL, CURRENT_TIMESTAMP, ?, ?, NULL, NULL, ?, NULL)",
                "Por verificar",
                request.getIdUsuario(),
                idVehiculo,
                idSede
        );

        return DtoVehiculo.builder()
                .id_vehiculo(idVehiculo)
                .placa(placa)
                .aprobado(false)
                .categoria(categoria)
                .activo(false)
                .usuario(null)
                .build();
    }
}
