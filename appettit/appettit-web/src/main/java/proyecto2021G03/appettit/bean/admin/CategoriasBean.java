package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.event.RowEditEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.IUserSession;
import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanCategorias")
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(CategoriasBean.class);

	private List<CategoriaDTO> categorias;
	private List<CategoriaDTO> filterCategorias;

	private Long id;
	private String nombre;
	private boolean globalFilterOnly;
	FacesContext facesContext;
	HttpSession session;
	private Boolean disabledBloquedado = true;

	@EJB
	ICategoriaService catSrv;
	
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

					categorias = catSrv.listar();
				}
			}
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		}

	}

	public void addCategoria() {

		//logger.info("addCategoria 'nombre': " + nombre);
		//CategoriaDTO nprod = null;
		try {
			CategoriaCrearDTO categoriaCDTO = new CategoriaCrearDTO(nombre);

			CategoriaDTO categoriaDTO = catSrv.crear(categoriaCDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Informaci??n", "Categor??a " + categoriaDTO.getNombre() + " creada con ??xito."));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		} finally {
			clearParam();
			try {
				categorias = catSrv.listar();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
			}
		}

	}

	private void clearParam() {
		this.id = null;
		this.nombre = null;
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

	public void onRowSelect(RowEditEvent<CategoriaDTO> event) {
		disabledBloquedado = false;
	}

	public void onRowEdit(RowEditEvent<CategoriaDTO> event) {

		try {
			logger.info("onRowEdit Categoria - ID: " + event.getObject().getId());

			CategoriaCrearDTO categoriaCDTO = new CategoriaCrearDTO(event.getObject().getNombre());
			catSrv.editar(event.getObject().getId(), categoriaCDTO);
			categorias = catSrv.listar();

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Edici??n correcta", event.getObject().getNombre()));

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		}
	}

	public void onRowCancel(RowEditEvent<CategoriaDTO> event) {
		disabledBloquedado = true;

		FacesMessage msg = new FacesMessage("Edici??n cancelada", String.valueOf(event.getObject().getNombre()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
