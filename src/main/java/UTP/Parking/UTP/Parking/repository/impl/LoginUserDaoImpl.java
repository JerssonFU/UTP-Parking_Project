package UTP.Parking.UTP.Parking.repository.impl;

import UTP.Parking.UTP.Parking.base.JpaGenericRepository;
import UTP.Parking.UTP.Parking.model.RolesUsuario;
import UTP.Parking.UTP.Parking.model.Usuario;
import UTP.Parking.UTP.Parking.repository.LoginUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class LoginUserDaoImpl extends JpaGenericRepository implements LoginUserDao {

    private static final Logger logger = LoggerFactory.getLogger(LoginUserDaoImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Usuario loginUsuario(String username) {
        logger.info("Intento de inicio de sesión para usuario {}", username);

        List<Usuario> usuarios = entityManager
                .createNativeQuery("SELECT * FROM public.login_usuario(:username)", Usuario.class)
                .setParameter("username", username)
                .getResultList();

        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RolesUsuario> rolesByUser(Long idUsuario) {
        return entityManager
                .createNativeQuery("SELECT * FROM public.roles_usuario(:idUsuario)", RolesUsuario.class)
                .setParameter("idUsuario", idUsuario.intValue())
                .getResultList();
    }
}
