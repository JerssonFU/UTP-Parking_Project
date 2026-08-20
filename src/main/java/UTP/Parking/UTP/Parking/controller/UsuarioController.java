package UTP.Parking.UTP.Parking.controller;

import UTP.Parking.UTP.Parking.dto.UsuarioExportDTO;
import UTP.Parking.UTP.Parking.interfaceService.IUsuarioService;
import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/roles")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;


}


