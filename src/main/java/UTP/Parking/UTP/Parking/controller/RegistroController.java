package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.interfaceService.IRegistroService;
import UTP.Parking.UTP.Parking.interfaceService.ISedeService;
import UTP.Parking.UTP.Parking.interfaceService.IVehiculoService;
import UTP.Parking.UTP.Parking.model.Estacionamiento;
import UTP.Parking.UTP.Parking.model.Registro;
import UTP.Parking.UTP.Parking.model.Sede;
import UTP.Parking.UTP.Parking.resquest.DTOComentarioRequest;
import UTP.Parking.UTP.Parking.resquest.DtoRegistro;
import UTP.Parking.UTP.Parking.resquest.DtoRegistroRequest;
import UTP.Parking.UTP.Parking.service.EstacionamientoService;
import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    private IRegistroService registroService;

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private ISedeService sedeService;

    @Autowired
    private EstacionamientoService estacionamientoService;

    @PostMapping("/ingreso")
    public ResponseEntity<?> registrarIngreso(@RequestBody DtoRegistroRequest request,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (principal == null) {
                response.put("mensaje", "Usuario no autenticado.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            request.setPlaca(request.getPlaca() == null ? null : request.getPlaca().trim().toUpperCase());
            request.setIdUsuario(vehiculoService.buscarVehiculo(request.getPlaca()).getUsuario().getIdUsuario());
            request.setIdUsuarioSeguridad(Math.toIntExact(principal.getId()));

            if (!vehiculoService.validarVehiculo(request.getPlaca())) {
                response.put("mensaje", "El vehículo no se encuentra autorizado para ingresar.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            for (DtoRegistro registro : registroService.obtenerRegistrosNoSalida()) {
                if (request.getPlaca().equalsIgnoreCase(registro.getPlacaVehiculo())) {
                    response.put("mensaje", "El vehículo ya se encuentra registrado dentro del estacionamiento.");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
            }

            Estacionamiento estacionamiento = estacionamientoService.buscarEstacionamiento(request.getIdEstacionamiento());
            if (estacionamiento == null || estacionamiento.getSede() == null) {
                response.put("mensaje", "Estacionamiento no encontrado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            registroService.registrarIngreso(request);
            Sede sede = sedeService.buscarSede(estacionamiento.getSede().getId_sede());
            if (sede != null && sede.getCantidad() != null && sede.getCantidad() > 0) {
                sede.setCantidad(sede.getCantidad() - 1);
                sedeService.actualizarCantidadEspacios(sede);
            }

            response.put("mensaje", "El vehículo ingresó al estacionamiento.");
            response.put("registro", request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/salida/{placa}")
    public ResponseEntity<?> registrarSalida(@PathVariable String placa) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer idVehiculo = vehiculoService.buscarVehiculo(placa.trim().toUpperCase()).getId_vehiculo();
            Registro registro = registroService.registrarSalida(idVehiculo);
            if (registro.getEstacionamiento() != null && registro.getEstacionamiento().getSede() != null) {
                Sede sede = sedeService.buscarSede(registro.getEstacionamiento().getSede().getId_sede());
                if (sede != null && sede.getCantidad() != null) {
                    sede.setCantidad(sede.getCantidad() + 1);
                    sedeService.actualizarCantidadEspacios(sede);
                }
            }
            response.put("mensaje", "El vehículo salió del estacionamiento.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sin-salida")
    public ResponseEntity<?> listarRegistrosSinFechaDeSalida() {
        Map<String, Object> response = new HashMap<>();
        response.put("registros", registroService.obtenerRegistrosNoSalida());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/con-observacion")
    public ResponseEntity<?> listarRegistrosConObservacion() {
        Map<String, Object> response = new HashMap<>();
        response.put("registros", registroService.obtenerRegistroConObservacion());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/observacion/{placa}")
    public ResponseEntity<?> registrarObservacion(@PathVariable String placa,
                                                  @RequestBody DTOComentarioRequest observacion) {
        Map<String, Object> response = new HashMap<>();
        try {
            registroService.registrarObservacion(placa.trim().toUpperCase(), observacion.getComentario());
            response.put("comentario", "Observación registrada.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("comentario", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
