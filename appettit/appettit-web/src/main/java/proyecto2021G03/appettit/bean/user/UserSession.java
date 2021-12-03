package proyecto2021G03.appettit.bean.user;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.shaded.json.JSONObject;

import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Stateless
public class UserSession {
	
	static Logger logger = Logger.getLogger(UserSession.class);

	@EJB
	IUsuarioService usrSrv;
	
	FacesContext facesContext = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
	RestauranteDTO restaurante;
	AdministradorDTO admin;
	

	public void getRestauranteReg() {
		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

			Long id = null;

			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(Constantes.COOKIE_NAME)) {
					String decode = URLDecoder.decode(cookie.getValue(), "UTF-8");
					JSONObject jsonObject = new JSONObject(decode);

					int s_id = jsonObject.getInt("idUsuario");
					id = Long.valueOf(s_id);

					break;
				}
			}
			// Long id =
			// Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
			restaurante = usrSrv.buscarRestaurantePorId(id);
			
			createSession((UsuarioDTO) restaurante);

		} catch (AppettitException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

	}

	public void getAdministradorReg() {
		try {

			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();

			Long id = null;

			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals(Constantes.COOKIE_NAME)) {
					String decode = URLDecoder.decode(cookie.getValue(), "UTF-8");
					JSONObject jsonObject = new JSONObject(decode);

					int s_id = jsonObject.getInt("idUsuario");
					id = Long.valueOf(s_id);

					break;
				}
			}
			// Long id =
			// Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
			admin = usrSrv.buscarAdministradorPorId(id);
			
			createSession((UsuarioDTO) admin);

		} catch (AppettitException | UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

	}

	public void createSession(UsuarioDTO usuarioDTO) {
		session.setAttribute(Constantes.LOGINUSUARIO, usuarioDTO);
	}
	
	public void destroySession() {
		// FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

		logger.info("En destroySession");
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		
		try {
			
			for (Cookie cookie : request.getCookies()) {
				logger.info(cookie.getName());
				if (cookie.getName().equals(Constantes.COOKIE_NAME)) {
					//cookie.setValue("");
					//cookie.setMaxAge(0);
					//cookie.setPath("/");
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
