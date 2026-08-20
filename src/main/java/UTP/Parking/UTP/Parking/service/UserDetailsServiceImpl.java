package UTP.Parking.UTP.Parking.service;

import UTP.Parking.UTP.Parking.model.Role;
import UTP.Parking.UTP.Parking.model.RolesUsuario;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.repository.LoginUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private LoginUserDao loginUserDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizedUsername = username == null ? "" : username.trim();
        Usuario usuario = loginUserDao.loginUsuario(normalizedUsername);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        List<RolesUsuario> roles = loginUserDao.rolesByUser(usuario.getIdUsuario());
        if (roles == null || roles.isEmpty()) {
            throw new UsernameNotFoundException("El usuario no tiene un rol asignado");
        }

        usuario.setRoles(roles);
        usuario.setRole(resolveRole(roles.get(0).getAlias()));

        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new UsernameNotFoundException("El usuario no tiene contraseña configurada");
        }

        logger.info("Usuario {} cargado correctamente con rol {}", usuario.getUsername(), usuario.getRole());
        return UserPrincipal.build(usuario);
    }

    private Role resolveRole(String alias) {
        if (alias == null || alias.trim().isEmpty()) {
            return null;
        }

        String normalizedAlias = alias.trim().toUpperCase(Locale.ROOT);
        if (normalizedAlias.startsWith("ROLE_")) {
            normalizedAlias = normalizedAlias.substring(5);
        }

        try {
            return Role.valueOf(normalizedAlias);
        } catch (IllegalArgumentException ex) {
            logger.warn("El alias de rol '{}' no coincide con los roles de la aplicación", alias);
            return null;
        }
    }
}
