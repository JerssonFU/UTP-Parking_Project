package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.DtoEstacionamiento;
import UTP.Parking.UTP.Parking.interfaceService.IEstacionamientoService;
import UTP.Parking.UTP.Parking.model.Estacionamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/estacionamientos")
public class EstacionamientoController {

    @Autowired
    private IEstacionamientoService service;

    @GetMapping("/{idSede}/{piso}")
    public ResponseEntity<?> listarEstacionamientosDisponiblesPorSede(@PathVariable Integer idSede,
                                                                       @PathVariable Integer piso) {
        Map<String, Object> response = new HashMap<>();
        List<DtoEstacionamiento> estacionamientos = new ArrayList<>();
        try {
            for (Estacionamiento estacionamiento : service.listarEstacionamientos(idSede, piso)) {
                estacionamientos.add(new DtoEstacionamiento(
                        estacionamiento.getId_estacionamiento(),
                        estacionamiento.getPiso(),
                        estacionamiento.getNumero(),
                        estacionamiento.getDisponible(),
                        estacionamiento.getNombre()
                ));
            }
        } catch (InvalidDataAccessResourceUsageException ignored) {
            response.put("disponible", false);
        }
        response.put("estacionamientos", estacionamientos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
