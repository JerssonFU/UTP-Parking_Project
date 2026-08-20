package UTP.Parking.UTP.Parking.service;



import UTP.Parking.UTP.Parking.dto.UsuarioExportDTO;
import UTP.Parking.UTP.Parking.interfaceService.IUsuarioService;
import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.repository.UsuarioRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioExportDTO> exportAllUsuarios() {
        return usuarioRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Resource exportUsuariosSeguridadToExcel() throws IOException {
        List<Usuario> usuariosSeguridad = usuarioRepository.findByRoleAlias(Role.SEGURIDAD.name());
        List<UsuarioExportDTO> dtos = usuariosSeguridad.stream().map(this::convertToDTO).collect(Collectors.toList());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios Seguridad");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID Usuario", "Username", "Nombres", "Apellido paterno", "Apellido materno", "Correo Institucional", "Código universitario", "Role"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (UsuarioExportDTO dto : dtos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(dto.getIdUsuario());
            row.createCell(1).setCellValue(dto.getUsername());
            row.createCell(2).setCellValue(dto.getNombres());
            row.createCell(3).setCellValue(dto.getApellidoPaterno());
            row.createCell(4).setCellValue(dto.getApellidoMaterno());
            row.createCell(5).setCellValue(dto.getEmail_universitario());
            row.createCell(6).setCellValue(dto.getCodigoUniversitario());
            row.createCell(7).setCellValue(dto.getRole() != null ? dto.getRole().toString() : "");
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return new ByteArrayResource(outputStream.toByteArray());
    }

    private UsuarioExportDTO convertToDTO(Usuario usuario) {
        UsuarioExportDTO dto = new UsuarioExportDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());
        dto.setNombres(usuario.getNombres());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setEmail_universitario(usuario.getEmail_universitario());
        dto.setCodigoUniversitario(usuario.getCodigoUniversitario());
        dto.setEstado(usuario.getEstado());
        dto.setRole(usuario.getRole());
        return dto;
    }

    @Override
    public List<UsuarioExportDTO> findUsuariosByRoleSeguridad() {
        List<Usuario> usuariosSeguridad = usuarioRepository.findByRoleAlias(Role.SEGURIDAD.name());
        return usuariosSeguridad.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<UsuarioExportDTO> findUsuariosByRoleAlumno() {
        List<Usuario> usuariosAlumno = usuarioRepository.findByRoleAlias(Role.ALUMNO.name());
        return usuariosAlumno.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
