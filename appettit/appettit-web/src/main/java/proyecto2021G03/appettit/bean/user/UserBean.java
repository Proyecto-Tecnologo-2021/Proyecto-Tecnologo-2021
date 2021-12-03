package proyecto2021G03.appettit.bean.user;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanUserLoged")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(UserBean.class);

	Constantes constantes;

	@EJB
	IUsuarioService usrSrv;

	FacesContext facesContext;
	HttpSession session;
	RestauranteDTO restaurante;
	AdministradorDTO admin;

	@PostConstruct
	public void init() {
		facesContext = FacesContext.getCurrentInstance();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);

	}

	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO = (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}

		return usuarioDTO;

	}

	/*
	public void getRestauranteReg() {
		String correo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("correo");

		try {
			restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
			createSession((UsuarioDTO) restaurante);

		} catch (Exception e) {
			logger.error("No se encontró el Restaurante");
		}
	}

	public void getAdministradorReg() {
		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			// ExternalContext externalContext = facesContext.getExternalContext();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

			Long id = null;

			for (Cookie cookie : request.getCookies()) {
				logger.info(cookie.getName());
				if (cookie.getName().equals(Constantes.COOKIE_NAME)) {
					logger.info(cookie.toString());
					logger.info(cookie.getValue());

					String decode = URLDecoder.decode(cookie.getValue(), "UTF-8");
					logger.info("Cokie decode: " + decode);
					
					JSONObject jsonObject = new JSONObject(decode);
					

					int s_id = jsonObject.getInt("idUsuario");
					id = Long.valueOf(s_id);
					
					break;
				}
			}
			// Long id =
			// Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
			logger.info("Cookie id: " + id);
			admin = usrSrv.buscarAdministradorPorId(id);
			logger.info("Cookie id: " + admin.getNombre());
			
			createSession((UsuarioDTO) admin);


		} catch (AppettitException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

	}

	public void createSession(UsuarioDTO usuarioDTO) {
		session.setAttribute(Constantes.LOGINUSUARIO, usuarioDTO);
	}
*/
	public void destroySession() {
		// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		
		try {
			
			for (Cookie cookie : request.getCookies()) {
				logger.info(cookie.getName());
				if (cookie.getName().equals(Constantes.COOKIE_NAME)) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath("/");
					break;
				}
			}
			
			
			externalContext.invalidateSession();
			externalContext.dispatch(Constantes.REDIRECT_URI);
			//externalContext.redirect(Constantes.REDIRECT_URI);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
	}
}
