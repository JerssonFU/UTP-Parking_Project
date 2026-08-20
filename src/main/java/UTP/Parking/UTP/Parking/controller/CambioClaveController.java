package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.CambioClaveDTO;
import UTP.Parking.UTP.Parking.repository.UsuarioRepository;
import UTP.Parking.UTP.Parking.service.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cambioClave")
public class CambioClaveController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String paginaCambioClave(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        cargarModelo(model, principal);
        return "app/usuarios/cambio-clave";
    }

    @GetMapping("/cambio-clave")
    public String cambioClave(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        cargarModelo(model, principal);
        return "app/usuarios/cambio-clave :: fragcambioClave";
    }

    @PostMapping("/guardarCambioClave")
    @ResponseBody
    public ResponseEntity<?> guardarCambioClave(CambioClaveDTO dto,
                                                  @AuthenticationPrincipal UserPrincipal principal) {
        Map<String, Object> response = new HashMap<>();
        if (principal == null) {
            response.put("status", "Error");
            response.put("data", "La sesión no es válida.");
            return ResponseEntity.status(401).body(response);
        }

        String password = dto.getPassword() == null ? "" : dto.getPassword().trim();
        if (password.length() < 4) {
            response.put("status", "Warning");
            response.put("data", "La contraseña debe tener al menos 4 caracteres.");
            return ResponseEntity.badRequest().body(response);
        }

        String celular = dto.getCelular() == null ? "" : dto.getCelular().trim();
        if (!celular.isEmpty() && !celular.matches("^[0-9]{9}$")) {
            response.put("status", "Warning");
            response.put("data", "El celular debe contener 9 dígitos.");
            return ResponseEntity.badRequest().body(response);
        }

        String encoded = passwordEncoder.encode(password);
        int actualizados = usuarioRepository.actualizarPassword(principal.getId(), encoded);
        if (actualizados != 1) {
            response.put("status", "Error");
            response.put("data", "No se pudo actualizar la contraseña.");
            return ResponseEntity.status(500).body(response);
        }

        jdbcTemplate.update(
                "INSERT INTO public.usuario_contacto (id_usuario, celular) VALUES (?, ?) " +
                        "ON CONFLICT (id_usuario) DO UPDATE SET celular = EXCLUDED.celular",
                principal.getId(), celular
        );

        principal.setPassword(encoded);
        response.put("status", "Done");
        response.put("data", "Datos actualizados correctamente.");
        return ResponseEntity.ok(response);
    }

    private void cargarModelo(Model model, UserPrincipal principal) {
        CambioClaveDTO dto = new CambioClaveDTO();
        if (principal != null) {
            dto.setCodUsuario(principal.getId());
            dto.setUserName(principal.getUsername());
            dto.setFullName(construirNombre(principal));
            dto.setDescOficina(valor(principal.getDescripcionCarrera()));
            dto.setEmail(valor(principal.getEmail_universitario()));
            String celular = obtenerCelular(principal.getId());
            dto.setCelular(celular);
            dto.setTelefono(celular);
        } else {
            dto.setCelular("");
            dto.setTelefono("");
        }
        dto.setDomicilio("");
        model.addAttribute("cambioClaveDTO", dto);
    }

    private String obtenerCelular(Long idUsuario) {
        List<String> celulares = jdbcTemplate.query(
                "SELECT celular FROM public.usuario_contacto WHERE id_usuario = ?",
                (rs, rowNum) -> rs.getString("celular"),
                idUsuario
        );
        return celulares.isEmpty() || celulares.get(0) == null ? "" : celulares.get(0);
    }

    private String construirNombre(UserPrincipal principal) {
        StringBuilder nombre = new StringBuilder();
        agregar(nombre, principal.getNombres());
        agregar(nombre, principal.getApellidoPaterno());
        agregar(nombre, principal.getApellidoMaterno());
        return nombre.toString().trim();
    }

    private void agregar(StringBuilder builder, String valor) {
        if (valor != null && !valor.trim().isEmpty()) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(valor.trim());
        }
    }

    private String valor(String valor) {
        return valor == null ? "" : valor;
    }
}
