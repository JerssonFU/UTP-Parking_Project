package UTP.Parking.UTP.Parking.service;

import UTP.Parking.UTP.Parking.dto.RegistroExportDTO;
import UTP.Parking.UTP.Parking.interfaceService.IRegistroService;
import UTP.Parking.UTP.Parking.model.Registro;
import UTP.Parking.UTP.Parking.model.Vehiculo;
import UTP.Parking.UTP.Parking.repository.RegistroRepository;
import UTP.Parking.UTP.Parking.repository.VehiculoRespository;
import UTP.Parking.UTP.Parking.resquest.DtoRegistro;
import UTP.Parking.UTP.Parking.resquest.DtoRegistroRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegistroService implements IRegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    @Autowired
    private VehiculoRespository vehiculoRespository;

    @Override
    public void registrarIngreso(DtoRegistroRequest request) {
        Vehiculo vehiculo = vehiculoRespository.findByPlacaIgnoreCase(request.getPlaca() == null ? null : request.getPlaca().trim());
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehículo no encontrado: " + request.getPlaca());
        }

        registroRepository.insertarRegistro(
                request.getIdEstacionamiento(),
                Math.toIntExact(request.getIdUsuario()),
                request.getIdUsuarioSeguridad(),
                vehiculo.getId_vehiculo()
        );
    }

    @Override
    public Registro registrarSalida(Integer idVehiculo) {
        Registro registro = registroRepository.findRegistro(idVehiculo);
        if (registro == null) {
            throw new IllegalArgumentException("No existe un ingreso activo para el vehículo indicado.");
        }
        registro.setFecha_salida(LocalDateTime.now());
        return registroRepository.save(registro);
    }

    @Override
    public void registrarObservacion(String placa, String observacion) {
        for (Registro registro : registroRepository.findRegistrosNoSalida()) {
            if (registro.getVehiculo() != null && placa.equalsIgnoreCase(registro.getVehiculo().getPlaca())) {
                registro.setObservacion(observacion);
                registroRepository.save(registro);
                return;
            }
        }
        throw new IllegalArgumentException("El vehículo no tiene un ingreso activo.");
    }

    @Override
    public List<DtoRegistro> obtenerRegistrosNoSalida() {
        List<DtoRegistro> registros = new ArrayList<>();
        for (Registro registro : registroRepository.findRegistrosNoSalida()) {
            mapRegistro(registros, registro);
        }
        return registros;
    }

    @Override
    public List<DtoRegistro> obtenerRegistroConObservacion() {
        List<DtoRegistro> registros = new ArrayList<>();
        for (Registro registro : registroRepository.findRegistroConObservacion()) {
            mapRegistro(registros, registro);
        }
        return registros;
    }

    @Override
    public List<RegistroExportDTO> getAllRegistrosForExport() {
        return mapRegistrosToDTOs(registroRepository.findAll());
    }

    @Override
    public List<RegistroExportDTO> getRegistrosPorIntervaloFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return mapRegistrosToDTOs(registroRepository.findByFechaIngresoBetween(fechaInicio, fechaFin));
    }

    private List<RegistroExportDTO> mapRegistrosToDTOs(List<Registro> registros) {
        return registros.stream().map(registro -> RegistroExportDTO.builder()
                .idRegistro(registro.getId_registro())
                .fechaIngreso(registro.getFecha_ingreso())
                .fechaSalida(registro.getFecha_salida())
                .observacion(registro.getObservacion())
                .estacionamientoNombre(registro.getEstacionamiento() != null ? registro.getEstacionamiento().getNombre() : "")
                .vehiculoPlaca(registro.getVehiculo() != null ? registro.getVehiculo().getPlaca() : "")
                .usuariocodigoUniversitario(registro.getUsuario() != null ? registro.getUsuario().getCodigoUniversitario() : "")
                .usuarioSeguridadUsername(registro.getUsuarioSeguridad() != null ? registro.getUsuarioSeguridad().getUsername() : "")
                .build())
                .collect(Collectors.toList());
    }

    private void mapRegistro(List<DtoRegistro> registros, Registro registro) {
        DtoRegistro dto = new DtoRegistro();
        dto.setId_registro(registro.getId_registro());
        dto.setFecha_ingreso(registro.getFecha_ingreso());
        dto.setFecha_salida(registro.getFecha_salida());
        dto.setObservacion(registro.getObservacion());

        if (registro.getEstacionamiento() != null && registro.getEstacionamiento().getSede() != null) {
            dto.setNombreSede(registro.getEstacionamiento().getSede().getNombre());
        }
        if (registro.getUsuario() != null) {
            dto.setCodigoUsuario(registro.getUsuario().getUsername());
            dto.setNombreUsuario(registro.getUsuario().getApellidoPaterno() + ", " + registro.getUsuario().getNombres());
            dto.setIdUsuario(registro.getUsuario().getIdUsuario());
        }
        if (registro.getUsuarioSeguridad() != null) {
            dto.setIdUsuarioSeguridad(Math.toIntExact(registro.getUsuarioSeguridad().getIdUsuario()));
        }
        if (registro.getVehiculo() != null) {
            dto.setPlacaVehiculo(registro.getVehiculo().getPlaca());
        }
        registros.add(dto);
    }
}
