package UTP.Parking.UTP.Parking.repository;

import UTP.Parking.UTP.Parking.model.RolesUsuario;
import UTP.Parking.UTP.Parking.model.Usuario;

import java.util.List;

public interface LoginUserDao {
	
	public Usuario loginUsuario(String username);
	
	public List<RolesUsuario> rolesByUser(Long idUsario);

}
