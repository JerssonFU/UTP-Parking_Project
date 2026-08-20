package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.DtoSolicitud;
import UTP.Parking.UTP.Parking.interfaceService.ISolicitudService;
import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.Solicitud;
import UTP.Parking.UTP.Parking.resquest.DTOComentarioRequest;
import UTP.Parking.UTP.Parking.resquest.DTOSolicitudPathRequest;
import UTP.Parking.UTP.Parking.resquest.DTOSolicitudRequest;
import UTP.Parking.UTP.Parking.service.SolicitudDecisionService;
import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;

    @Autowired
    private SolicitudDecisionService solicitudDecisionService;

    @GetMapping
    public String verSolicitudes(Model model) {
        return "app/solicitudesVehiculo/solicitudes";
    }

    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<?> listarSolicitudes(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            List<Solicitud> solicitudes = puedeVerTodas(principal)
                    ? solicitudService.listarSolicitudes()
                    : solicitudService.listarSolicitudesId(principal.getId());
            return respuestaSolicitudes(solicitudes);
        } catch (InvalidDataAccessResourceUsageException e) {
            return respuestaSolicitudes(Collections.emptyList());
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> listarSolicitudesId(@PathVariable int id,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        long idConsulta = puedeVerTodas(principal) ? id : principal.getId();
        try {
            return respuestaSolicitudes(solicitudService.listarSolicitudesId(idConsulta));
        } catch (InvalidDataAccessResourceUsageException e) {
            return respuestaSolicitudes(Collections.emptyList());
        }
    }

    @PostMapping("/registro")
    @ResponseBody
    public ResponseEntity<?> registrarSolicitud(@RequestBody DTOSolicitudRequest request,
                                                 @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("mensaje", "Usuario no autenticado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        if (request.getId_vehiculo() == null) {
            response.put("mensaje", "Vehículo no válido.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            request.setIdUsuario(Math.toIntExact(principal.getId()));
            solicitudService.registrarSolicitud(request);
            response.put("mensaje", "Solicitud registrada con éxito.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        } catch (DataAccessException e) {
            response.put("mensaje", "No se pudo registrar la solicitud.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/respuesta/{id}")
    @ResponseBody
    public ResponseEntity<?> respuestaSolicitud(@PathVariable int id,
                                                 @RequestBody DTOSolicitudPathRequest request,
                                                 @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();

        if (principal == null) {
            response.put("mensaje", "Usuario no autenticado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (!puedeResponder(principal)) {
            response.put("mensaje", "No tiene permisos para responder solicitudes.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        if (request == null || request.getEstado() == null || request.getEstado().trim().isEmpty()) {
            response.put("mensaje", "Debe indicar el estado de la solicitud.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            SolicitudDecisionService.DecisionResult resultado = solicitudDecisionService.responder(
                    id,
                    request.getEstado(),
                    Math.toIntExact(principal.getId()),
                    request.getComentario()
            );

            response.put("estado", resultado.getEstado());
            response.put("comentario", resultado.getComentario());
            response.put("idSede", resultado.getIdSede());
            response.put("idEstacionamiento", resultado.getIdEstacionamiento());

            if ("Aceptado".equalsIgnoreCase(resultado.getEstado())) {
                response.put("mensaje", "Solicitud aceptada correctamente. Se asignó un espacio de estacionamiento.");
            } else {
                response.put("mensaje", "Solicitud rechazada correctamente.");
            }

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalStateException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataAccessException e) {
            response.put("mensaje", "No se pudo procesar la solicitud en la base de datos.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/respuesta/comentario/{id}")
    @ResponseBody
    public ResponseEntity<?> registrarComentario(@PathVariable int id,
                                                  @RequestBody DTOComentarioRequest request,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null || !puedeResponder(principal)) {
            response.put("mensaje", "No tiene permisos para comentar solicitudes.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        try {
            solicitudService.registrarComentario(id, request.getComentario());
            response.put("comentario", request.getComentario());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        } catch (DataAccessException e) {
            response.put("mensaje", "No se pudo registrar el comentario.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> respuestaSolicitudes(List<Solicitud> solicitudes) {
        List<DtoSolicitud> lista = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        for (Solicitud solicitud : solicitudes) {
            if (solicitud == null || solicitud.getUsuario() == null || solicitud.getVehiculo() == null) {
                continue;
            }
            Integer idUsuarioSae = solicitud.getUsuarioSae() == null
                    ? null
                    : Math.toIntExact(solicitud.getUsuarioSae().getIdUsuario());
            String comentario = solicitud.getComentario();
            if (comentario == null || comentario.trim().isEmpty()) {
                comentario = "No hay comentarios para esta solicitud.";
            }
            DtoSolicitud dto = new DtoSolicitud();
            dto.setIdSolicitud(solicitud.getId_solicitud());
            dto.setFechaSolicitud(solicitud.getFechaSolicitud());
            dto.setFechaRespuesta(solicitud.getFechaRespuesta());
            dto.setEstado(solicitud.getEstado());
            dto.setComentario(comentario);
            dto.setIdUsuario(Math.toIntExact(solicitud.getUsuario().getIdUsuario()));
            dto.setIdVehiculo(solicitud.getVehiculo().getId_vehiculo());
            dto.setIdUsuarioSae(idUsuarioSae);
            dto.setPlaca(solicitud.getVehiculo().getPlaca());
            dto.setCategoria(solicitud.getVehiculo().getCategoria());
            lista.add(dto);
        }

        response.put("solicitudes", lista);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private boolean puedeVerTodas(UserPrincipal principal) {
        Role role = principal.getRole();
        return role == Role.PERSONAL_SAE || role == Role.ADMINISTRATIVO || role == Role.JEFE_SEGURIDAD;
    }

    private boolean puedeResponder(UserPrincipal principal) {
        Role role = principal.getRole();
        return role == Role.PERSONAL_SAE || role == Role.ADMINISTRATIVO;
    }

    private ResponseEntity<?> moduloNoDisponible(Map<String, Object> response) {
        response.put("mensaje", "El módulo de solicitudes no está disponible en la base de datos.");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
