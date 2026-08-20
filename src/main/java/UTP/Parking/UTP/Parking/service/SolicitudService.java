package UTP.Parking.UTP.Parking.service;

import UTP.Parking.UTP.Parking.dto.SolicitudExportDTO;
import UTP.Parking.UTP.Parking.interfaceService.ISolicitudService;
import UTP.Parking.UTP.Parking.model.Solicitud;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.model.Vehiculo;
import UTP.Parking.UTP.Parking.repository.SolicitudRepository;
import UTP.Parking.UTP.Parking.repository.UsuarioRepository;
import UTP.Parking.UTP.Parking.repository.VehiculoRespository;
import UTP.Parking.UTP.Parking.resquest.DTOSolicitudRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService implements ISolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VehiculoRespository vehiculoRepository;

    @Override
    public List<Solicitud> listarSolicitudesId(Long usuarioId) {
        return solicitudRepository.findByIdUsuario(usuarioId);
    }

    @Override
    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }

    @Override
    @Transactional
    public void registrarSolicitud(DTOSolicitudRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        Vehiculo vehiculo = vehiculoRepository.findById(request.getId_vehiculo())
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado."));
        if (vehiculo.getUsuario() == null || !vehiculo.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El vehículo no pertenece al usuario autenticado.");
        }
        if (solicitudRepository.existsByVehiculoAndEstado(vehiculo.getId_vehiculo(), "Por verificar")) {
            throw new IllegalStateException("Ya existe una solicitud pendiente para este vehículo.");
        }
        Solicitud solicitud = new Solicitud();
        solicitud.setEstado("Por verificar");
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setFechaRespuesta(null);
        solicitud.setComentario(null);
        solicitud.setUsuario(usuario);
        solicitud.setVehiculo(vehiculo);
        solicitud.setUsuarioSae(null);
        solicitudRepository.saveAndFlush(solicitud);
    }

    @Override
    @Transactional
    public Solicitud actualizarSolicitud(Integer idSolicitud, String estado, Integer idUsuarioSae) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));
        Usuario usuarioSae = usuarioRepository.findById(idUsuarioSae.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Usuario SAE no encontrado."));

        solicitud.setEstado(estado);
        solicitud.setFechaRespuesta(LocalDateTime.now());
        solicitud.setUsuarioSae(usuarioSae);
        return solicitudRepository.save(solicitud);
    }

    @Override
    @Transactional
    public void registrarComentario(Integer idSolicitud, String comentario) {
        Solicitud solicitud = solicitudRepository.findById(idSolicitud)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud no encontrada."));
        solicitud.setComentario(comentario == null ? null : comentario.trim());
        solicitudRepository.save(solicitud);
    }

    @Override
    public List<SolicitudExportDTO> getAllSolicitudesForExport() {
        return mapSolicitudesExportDTOs(solicitudRepository.findAll());
    }

    @Override
    public List<SolicitudExportDTO> getSolicitudesPorIntervaloFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return mapSolicitudesExportDTOs(solicitudRepository.findByFechaSolicitudBetween(fechaInicio, fechaFin));
    }

    @Override
    public List<Solicitud> findBySolicitudByUsername(String username) {
        return solicitudRepository.findByUsuarioUsernameOrderByFechaSolicitudDesc(username);
    }

    private List<SolicitudExportDTO> mapSolicitudesExportDTOs(List<Solicitud> solicitudes) {
        return solicitudes.stream().map(solicitud -> SolicitudExportDTO.builder()
                .idSolicitud(solicitud.getId_solicitud())
                .estado(solicitud.getEstado())
                .fechaRespuesta(solicitud.getFechaRespuesta())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .usuarioUsername(solicitud.getUsuario() != null ? solicitud.getUsuario().getUsername() : "")
                .vehiculoPlaca(solicitud.getVehiculo() != null ? solicitud.getVehiculo().getPlaca() : "")
                .build())
                .collect(Collectors.toList());
    }
}
