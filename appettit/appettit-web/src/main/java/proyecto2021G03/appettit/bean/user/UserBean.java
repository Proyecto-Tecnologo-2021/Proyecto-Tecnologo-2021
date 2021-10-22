package proyecto2021G03.appettit.bean.user;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.restaurante.HomeRestauranteBean;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.util.Constantes;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBean implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HomeRestauranteBean.class);

	FacesContext facesContext;
	HttpSession session;
	
	@PostConstruct
	public void init() {
		facesContext = FacesContext.getCurrentInstance();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);
		
	}
	
	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO =  (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);	
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}
		
		return usuarioDTO;
		
	}
	
	public void createSession(UsuarioDTO usuarioDTO) {
		session.setAttribute(Constantes.LOGINUSUARIO, usuarioDTO);
	}

	public String destroySession() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return Constantes.URL_HOME;		
	}
}
