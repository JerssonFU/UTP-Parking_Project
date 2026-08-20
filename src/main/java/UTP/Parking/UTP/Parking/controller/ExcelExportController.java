package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.Solicitud;
import UTP.Parking.UTP.Parking.service.SolicitudService;
import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/export")
public class ExcelExportController {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/solicitudes")
    public ResponseEntity<byte[]> exportarSolicitudes(@AuthenticationPrincipal UserPrincipal principal) throws IOException {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        List<Solicitud> solicitudes = puedeVerTodas(principal)
                ? solicitudService.listarSolicitudes()
                : solicitudService.listarSolicitudesId(principal.getId());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Solicitudes");
            Row header = sheet.createRow(0);
            String[] columnas = {"ID", "Fecha solicitud", "Placa", "Tipo de vehículo", "Estado", "Comentario", "Fecha respuesta"};
            for (int i = 0; i < columnas.length; i++) {
                header.createCell(i).setCellValue(columnas[i]);
            }

            int fila = 1;
            for (Solicitud solicitud : solicitudes) {
                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(solicitud.getId_solicitud() == null ? 0 : solicitud.getId_solicitud());
                row.createCell(1).setCellValue(solicitud.getFechaSolicitud() == null ? "" : solicitud.getFechaSolicitud().format(FECHA));
                row.createCell(2).setCellValue(solicitud.getVehiculo() == null ? "" : valor(solicitud.getVehiculo().getPlaca()));
                row.createCell(3).setCellValue(solicitud.getVehiculo() == null ? "" : valor(solicitud.getVehiculo().getCategoria()));
                row.createCell(4).setCellValue(valor(solicitud.getEstado()));
                row.createCell(5).setCellValue(valor(solicitud.getComentario()));
                row.createCell(6).setCellValue(solicitud.getFechaRespuesta() == null ? "" : solicitud.getFechaRespuesta().format(FECHA));
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=solicitudes.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());
        }
    }

    private boolean puedeVerTodas(UserPrincipal principal) {
        Role role = principal.getRole();
        return role == Role.PERSONAL_SAE || role == Role.ADMINISTRATIVO || role == Role.JEFE_SEGURIDAD;
    }

    private String valor(String valor) {
        return valor == null ? "" : valor;
    }
}
