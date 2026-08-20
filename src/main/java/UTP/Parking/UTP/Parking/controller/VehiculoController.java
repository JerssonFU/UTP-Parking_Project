package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.DtoVehiculo;
import UTP.Parking.UTP.Parking.interfaceService.IVehiculoService;
import UTP.Parking.UTP.Parking.resquest.DtoVehiculoRequest;
import UTP.Parking.UTP.Parking.service.RegistroVehiculoSolicitudService;
import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoController {

    private static final List<String> CATEGORIAS = Arrays.asList("Auto", "Camioneta", "Motocicleta", "Trimoto", "Moto eléctrica");

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private RegistroVehiculoSolicitudService registroVehiculoSolicitudService;

    @GetMapping
    public String mostrarRegistro(Model model) {
        return "app/registroVehiculo/formularioRegistro";
    }

    @GetMapping("/categorias")
    @ResponseBody
    public ResponseEntity<?> listarCategorias() {
        return new ResponseEntity<>(CATEGORIAS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getVehiculoId(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("vehiculo", vehiculoService.buscarVehiculoId(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        }
    }

    @GetMapping("/placa/{placa}")
    @ResponseBody
    public ResponseEntity<?> getVehiculoPlaca(@PathVariable String placa) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("vehiculo", vehiculoService.buscarVehiculo(placa));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        } catch (DataAccessException e) {
            response.put("mensaje", "No se pudo consultar el vehículo.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registro")
    @ResponseBody
    public ResponseEntity<?> registrarVehiculo(@RequestBody DtoVehiculoRequest request,
                                                @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("mensaje", "Usuario no autenticado.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String placa = request.getPlaca() == null ? "" : request.getPlaca().trim().toUpperCase();
        String categoria = request.getCategoria() == null ? "" : request.getCategoria().trim();
        if (!placa.matches("^[A-Z0-9-]{5,15}$")) {
            response.put("mensaje", "Ingrese una placa válida.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (!CATEGORIAS.contains(categoria)) {
            response.put("mensaje", "Seleccione un tipo de vehículo válido.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            request.setIdUsuario(Math.toIntExact(principal.getId()));
            request.setPlaca(placa);
            request.setCategoria(categoria);
            DtoVehiculo vehiculoRegistrado = registroVehiculoSolicitudService.registrar(request);
            response.put("mensaje", "Vehículo y solicitud registrados con éxito.");
            response.put("vehiculo", vehiculoRegistrado);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (IllegalArgumentException e) {
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        } catch (DataIntegrityViolationException e) {
            response.put("mensaje", "La placa ya se encuentra registrada.");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (DataAccessException e) {
            response.put("mensaje", "No se pudo registrar el vehículo.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/validar/{placa}")
    @ResponseBody
    public ResponseEntity<?> validarVehiculo(@PathVariable String placa) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean valido = vehiculoService.validarVehiculo(placa);
            response.put("mensaje", valido ? "El vehículo está autorizado" : "El vehículo no está autorizado");
            response.put("valido", valido);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (InvalidDataAccessResourceUsageException e) {
            return moduloNoDisponible(response);
        }
    }

    private ResponseEntity<?> moduloNoDisponible(Map<String, Object> response) {
        response.put("mensaje", "El módulo de registro no está disponible en la base de datos.");
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
