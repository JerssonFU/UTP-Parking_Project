package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.DtoSede;
import UTP.Parking.UTP.Parking.interfaceService.ISedeService;
import UTP.Parking.UTP.Parking.model.Sede;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sedes")
public class SedeController {

    @Autowired
    private ISedeService service;

    @GetMapping
    public ResponseEntity<?> listarSedes() {
        Map<String, Object> response = new HashMap<>();
        List<DtoSede> sedes = new ArrayList<>();
        try {
            for (Sede sede : service.listarSedes()) {
                sedes.add(new DtoSede(sede.getId_sede(), sede.getNombre(), sede.getDireccion(), sede.getCantidad()));
            }
        } catch (DataAccessException ignored) {
            response.put("disponible", false);
        }
        response.put("sedes", sedes);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarSedeId(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Sede sede = service.buscarSede(id);
            if (sede == null) {
                response.put("mensaje", "Sede no encontrada.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("sede", new DtoSede(sede.getId_sede(), sede.getNombre(), sede.getDireccion(), sede.getCantidad()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException ignored) {
            response.put("mensaje", "No hay sedes disponibles.");
            return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
