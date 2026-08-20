package UTP.Parking.UTP.Parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SolicitudDecisionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public DecisionResult responder(Integer idSolicitud,
                                    String estado,
                                    Integer idUsuarioSae,
                                    String comentario) {

        if (idSolicitud == null || idUsuarioSae == null) {
            throw new IllegalArgumentException("Datos de solicitud no válidos.");
        }

        String estadoNormalizado = estado == null ? "" : estado.trim();
        boolean aceptar = "Aceptado".equalsIgnoreCase(estadoNormalizado);
        boolean rechazar = "Rechazado".equalsIgnoreCase(estadoNormalizado);

        if (!aceptar && !rechazar) {
            throw new IllegalArgumentException("Estado de solicitud no válido.");
        }

        Integer existeUsuario = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.usuario WHERE \"C_PERL_CODIGO\" = ?",
                Integer.class,
                idUsuarioSae
        );

        if (existeUsuario == null || existeUsuario == 0) {
            throw new IllegalArgumentException("Usuario responsable no encontrado.");
        }

        Map<String, Object> solicitud;
        try {
            solicitud = jdbcTemplate.queryForMap(
                    "SELECT id_solicitud, estado, id_vehiculo, id_sede, id_estacionamiento " +
                            "FROM public.solicitudes WHERE id_solicitud = ? FOR UPDATE",
                    idSolicitud
            );
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Solicitud no encontrada.");
        }

        String estadoActual = solicitud.get("estado") == null
                ? ""
                : String.valueOf(solicitud.get("estado")).trim();

        if (!"Por verificar".equalsIgnoreCase(estadoActual)) {
            throw new IllegalStateException("La solicitud ya fue atendida anteriormente.");
        }

        Integer idVehiculo = toInteger(solicitud.get("id_vehiculo"));
        if (idVehiculo == null) {
            throw new IllegalStateException("La solicitud no tiene un vehículo asociado.");
        }

        String comentarioLimpio = comentario == null ? "" : comentario.trim();

        if (rechazar && comentarioLimpio.isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar un motivo para rechazar la solicitud.");
        }

        if (rechazar) {
            jdbcTemplate.update(
                    "UPDATE public.solicitudes " +
                            "SET estado = 'Rechazado', fecha_respuesta = CURRENT_TIMESTAMP, " +
                            "comentario = ?, id_usuario_sae = ? " +
                            "WHERE id_solicitud = ?",
                    comentarioLimpio,
                    idUsuarioSae,
                    idSolicitud
            );

            jdbcTemplate.update(
                    "UPDATE public.vehiculos SET aprobado = false, activo = false WHERE id_vehiculo = ?",
                    idVehiculo
            );

            return new DecisionResult("Rechazado", comentarioLimpio, null, null);
        }

        Integer idSede = toInteger(solicitud.get("id_sede"));
        if (idSede == null) {
            idSede = buscarSedePorNombre("Lima Centro");
            jdbcTemplate.update(
                    "UPDATE public.solicitudes SET id_sede = ? WHERE id_solicitud = ?",
                    idSede,
                    idSolicitud
            );
        }

        String nombreSede = buscarNombreSede(idSede);

        List<Integer> espacios = jdbcTemplate.query(
                "SELECT id_estacionamiento " +
                        "FROM public.estacionamientos " +
                        "WHERE id_sede = ? AND disponible = true " +
                        "ORDER BY piso NULLS LAST, numero NULLS LAST, id_estacionamiento " +
                        "LIMIT 1 FOR UPDATE SKIP LOCKED",
                new Object[]{idSede},
                (rs, rowNum) -> rs.getInt("id_estacionamiento")
        );

        if (espacios.isEmpty()) {
            throw new IllegalStateException("No hay espacios disponibles en " + nombreSede + ".");
        }

        Integer idEstacionamiento = espacios.get(0);

        jdbcTemplate.update(
                "UPDATE public.estacionamientos SET disponible = false WHERE id_estacionamiento = ?",
                idEstacionamiento
        );

        jdbcTemplate.update(
                "UPDATE public.vehiculos SET aprobado = true, activo = true WHERE id_vehiculo = ?",
                idVehiculo
        );

        String comentarioAprobacion = comentarioLimpio.isEmpty()
                ? "Solicitud aprobada."
                : comentarioLimpio;

        jdbcTemplate.update(
                "UPDATE public.solicitudes " +
                        "SET estado = 'Aceptado', fecha_respuesta = CURRENT_TIMESTAMP, " +
                        "comentario = ?, id_usuario_sae = ?, id_estacionamiento = ? " +
                        "WHERE id_solicitud = ?",
                comentarioAprobacion,
                idUsuarioSae,
                idEstacionamiento,
                idSolicitud
        );

        actualizarCantidadSede(idSede);

        return new DecisionResult("Aceptado", comentarioAprobacion, idSede, idEstacionamiento);
    }

    private Integer buscarSedePorNombre(String nombre) {
        List<Integer> ids = jdbcTemplate.query(
                "SELECT id_sede FROM public.sedes WHERE UPPER(TRIM(nombre)) = UPPER(TRIM(?)) ORDER BY id_sede LIMIT 1",
                new Object[]{nombre},
                (rs, rowNum) -> rs.getInt("id_sede")
        );

        if (ids.isEmpty()) {
            throw new IllegalStateException("No se encontró la sede " + nombre + ".");
        }

        return ids.get(0);
    }

    private String buscarNombreSede(Integer idSede) {
        List<String> nombres = jdbcTemplate.query(
                "SELECT nombre FROM public.sedes WHERE id_sede = ?",
                new Object[]{idSede},
                (rs, rowNum) -> rs.getString("nombre")
        );

        return nombres.isEmpty() || nombres.get(0) == null
                ? "la sede seleccionada"
                : nombres.get(0);
    }

    private void actualizarCantidadSede(Integer idSede) {
        jdbcTemplate.update(
                "UPDATE public.sedes s " +
                        "SET cantidad = (SELECT COUNT(*) FROM public.estacionamientos e " +
                        "WHERE e.id_sede = s.id_sede AND e.disponible = true) " +
                        "WHERE s.id_sede = ?",
                idSede
        );
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    public static class DecisionResult {
        private final String estado;
        private final String comentario;
        private final Integer idSede;
        private final Integer idEstacionamiento;

        public DecisionResult(String estado,
                              String comentario,
                              Integer idSede,
                              Integer idEstacionamiento) {
            this.estado = estado;
            this.comentario = comentario;
            this.idSede = idSede;
            this.idEstacionamiento = idEstacionamiento;
        }

        public String getEstado() {
            return estado;
        }

        public String getComentario() {
            return comentario;
        }

        public Integer getIdSede() {
            return idSede;
        }

        public Integer getIdEstacionamiento() {
            return idEstacionamiento;
        }
    }
}
