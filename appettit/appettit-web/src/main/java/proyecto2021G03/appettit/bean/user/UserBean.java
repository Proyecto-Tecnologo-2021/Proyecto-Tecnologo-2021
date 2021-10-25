package proyecto2021G03.appettit.bean.user;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.restaurante.HomeRestauranteBean;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanUserLoged")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBean implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(HomeRestauranteBean.class);
	
	
	@EJB
	IUsuarioService usrSrv;

	FacesContext facesContext;
	HttpSession session;
	RestauranteDTO restaurante;
	
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
	
	public void getRestauranteReg() {
		String correo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("correo");
		
		try {
			restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
			createSession((UsuarioDTO) restaurante);
			
		} catch (Exception e) {
			logger.error("No se encontró el Restaurante");
		}
		
		
	}
	
	public void createSession(UsuarioDTO usuarioDTO) {
		session.setAttribute(Constantes.LOGINUSUARIO, usuarioDTO);
	}

	public String destroySession() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return Constantes.URL_HOME;		
	}
}
