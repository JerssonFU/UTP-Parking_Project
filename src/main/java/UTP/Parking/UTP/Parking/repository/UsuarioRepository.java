package UTP.Parking.UTP.Parking.repository;

import UTP.Parking.UTP.Parking.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    @Query(value = "SELECT u.* FROM public.usuario u " +
            "INNER JOIN public.rolesusuario r ON r.id_rol = u.\"ID_ROL\" " +
            "WHERE UPPER(TRIM(r.alias_rol)) = UPPER(TRIM(:alias))", nativeQuery = true)
    List<Usuario> findByRoleAlias(@Param("alias") String alias);

    @Modifying
    @Transactional
    @Query(value = "UPDATE public.usuario SET \"N_MST_PASSWORD\" = :password WHERE \"C_PERL_CODIGO\" = :idUsuario", nativeQuery = true)
    int actualizarPassword(@Param("idUsuario") Long idUsuario, @Param("password") String password);
}
