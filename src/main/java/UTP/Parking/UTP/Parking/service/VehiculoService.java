package UTP.Parking.UTP.Parking.service;

import UTP.Parking.UTP.Parking.dto.DtoUsuario;
import UTP.Parking.UTP.Parking.dto.DtoVehiculo;
import UTP.Parking.UTP.Parking.dto.VehiculoExportDTO;
import UTP.Parking.UTP.Parking.interfaceService.IVehiculoService;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.model.Vehiculo;
import UTP.Parking.UTP.Parking.repository.UsuarioRepository;
import UTP.Parking.UTP.Parking.repository.VehiculoRespository;
import UTP.Parking.UTP.Parking.resquest.DtoVehiculoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService implements IVehiculoService {

    @Autowired
    private VehiculoRespository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public DtoVehiculo buscarVehiculoId(Integer id) {
        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado."));
        return toDto(vehiculo);
    }

    @Override
    @Transactional
    public void registrarVehiculo(DtoVehiculoRequest request) {
        String placa = normalizarPlaca(request.getPlaca());
        if (placa.isEmpty()) {
            throw new IllegalArgumentException("Ingrese una placa válida.");
        }
        if (repository.existsByPlacaIgnoreCase(placa)) {
            throw new IllegalStateException("La placa ya se encuentra registrada.");
        }
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placa);
        vehiculo.setCategoria(request.getCategoria());
        vehiculo.setAprobado(false);
        vehiculo.setActivo(false);
        vehiculo.setUsuario(usuario);
        repository.saveAndFlush(vehiculo);
    }

    @Override
    public DtoVehiculo buscarVehiculo(String placa) {
        Vehiculo vehiculo = repository.findByPlacaIgnoreCase(normalizarPlaca(placa));
        if (vehiculo == null) {
            throw new IllegalArgumentException("Vehículo no encontrado: " + placa);
        }
        return toDto(vehiculo);
    }

    @Override
    public Boolean validarVehiculo(String placa) {
        Vehiculo vehiculo = repository.findByPlacaIgnoreCase(normalizarPlaca(placa));
        return vehiculo != null && vehiculo.isAprobado() && vehiculo.isActivo();
    }

    @Override
    @Transactional
    public void actualizarEstadoVehiculo(Integer id) {
        Vehiculo vehiculo = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado."));
        vehiculo.setAprobado(true);
        vehiculo.setActivo(true);
        repository.save(vehiculo);
    }

    @Override
    public List<VehiculoExportDTO> getAllVehiculos() {
        return repository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private DtoVehiculo toDto(Vehiculo vehiculo) {
        Usuario usuario = vehiculo.getUsuario();
        DtoUsuario dtoUsuario = null;
        if (usuario != null) {
            dtoUsuario = DtoUsuario.builder()
                    .idUsuario(usuario.getIdUsuario())
                    .username(usuario.getUsername())
                    .nombres(usuario.getNombres())
                    .apellidoPaterno(usuario.getApellidoPaterno())
                    .apellidoMaterno(usuario.getApellidoMaterno())
                    .email_universitario(usuario.getEmail_universitario())
                    .codigoUniversitario(usuario.getCodigoUniversitario())
                    .descripcionCarrera(usuario.getDescripcionCarrera() == null ? "-" : usuario.getDescripcionCarrera())
                    .estado(usuario.getEstado())
                    .role(usuario.getRole())
                    .build();
        }

        return DtoVehiculo.builder()
                .id_vehiculo(vehiculo.getId_vehiculo())
                .placa(vehiculo.getPlaca())
                .aprobado(vehiculo.isAprobado())
                .categoria(vehiculo.getCategoria())
                .activo(vehiculo.isActivo())
                .usuario(dtoUsuario)
                .build();
    }

    private VehiculoExportDTO convertToDTO(Vehiculo vehiculo) {
        VehiculoExportDTO dto = new VehiculoExportDTO();
        dto.setIdVehiculo(vehiculo.getId_vehiculo());
        dto.setPlaca(vehiculo.getPlaca());
        dto.setAprobado(vehiculo.isAprobado());
        dto.setCategoria(vehiculo.getCategoria());
        dto.setActivo(vehiculo.isActivo());
        dto.setUsername(vehiculo.getUsuario() != null ? vehiculo.getUsuario().getUsername() : "");
        return dto;
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? "" : placa.trim().toUpperCase();
    }
}
