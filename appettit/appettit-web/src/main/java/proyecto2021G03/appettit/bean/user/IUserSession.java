package proyecto2021G03.appettit.bean.user;

import javax.ejb.Local;

import proyecto2021G03.appettit.dto.UsuarioDTO;

@Local
public interface IUserSession {

	void getRestauranteReg();

	void getAdministradorReg();

	void createSession(UsuarioDTO usuarioDTO);

	void destroySession();

}
