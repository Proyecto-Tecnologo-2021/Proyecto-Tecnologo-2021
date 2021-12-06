package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.IUserSession;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanAdmin")
@SessionScoped
@Getter
@Setter
public class AdministradorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(AdministradorBean.class);

	private List<AdministradorDTO> administradores;
	private List<AdministradorDTO> filterAdministradores;

	private Long id;
	private String nombre;
	private String username;
	private String password;
	private String telefono;
	private String correo;
	private String token;
	private String tokenFireBase;
	private boolean globalFilterOnly;
	FacesContext facesContext;
	HttpSession session;
	

	@EJB
	IUsuarioService usrSrv;
	
	@EJB
	IUserSession usrSession;

	@PostConstruct
	public void init() {
		try {

			facesContext = FacesContext.getCurrentInstance();
			//ExternalContext externalContext = facesContext.getExternalContext();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);

			UsuarioDTO usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				//externalContext.invalidateSession();
				//externalContext.dispatch(Constantes.REDIRECT_URI);
				//externalContext.redirect(Constantes.REDIRECT_URI);
				usrSession.destroySession();
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
			} else {
				if (!(usuarioDTO instanceof AdministradorDTO)) {
					//externalContext.invalidateSession();
					//externalContext.dispatch(Constantes.REDIRECT_URI);
					//externalContext.redirect(Constantes.REDIRECT_URI);
					usrSession.destroySession();

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));

				} else {

					administradores = usrSrv.listarAdminsitradores();
				}
			}
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void addAdministrador() {

		logger.info("addAdministrador 'nombre': " + nombre);

		AdministradorDTO adminDTO = new AdministradorDTO(null, nombre, username, password, telefono, correo, null);

		try {
			adminDTO = usrSrv.crear(adminDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Administrador " + adminDTO.getCorreo() + " creado con éxito.", null));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				administradores = usrSrv.listarAdminsitradores();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}

	}

	/* ACTUALIZAR */
	public void updAdministrador() {

		logger.info("updAdministrador 'nombre': " + nombre);

		AdministradorDTO adminDTO = new AdministradorDTO(id, nombre, username, password, telefono, correo, null);

		try {
			adminDTO = usrSrv.crear(adminDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Administrador " + adminDTO.getCorreo() + " creado con éxito.", null));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				administradores = usrSrv.listarAdminsitradores();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}

	}

	private void clearParam() {
		this.id = null;
		this.nombre = null;
		this.password = null;
		this.correo = null;
		this.telefono = null;
		this.username = null;
		this.token = null;
		this.tokenFireBase = null;

	}

	public void toggleGlobalFilter() {
		setGlobalFilterOnly(!isGlobalFilterOnly());
	}

	public boolean isGlobalFilterOnly() {
		return globalFilterOnly;
	}

	public void setGlobalFilterOnly(boolean globalFilterOnly) {
		this.globalFilterOnly = globalFilterOnly;
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

}
