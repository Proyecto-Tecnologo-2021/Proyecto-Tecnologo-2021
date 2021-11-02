package proyecto2021G03.appettit.bean.admin;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.jboss.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanCategorias")
@SessionScoped
@Getter
@Setter
public class CategoriasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(CategoriasBean.class);

	private List<CategoriaDTO> categorias;
	private List<CategoriaDTO> filterCategorias;

	private Long id;
	private String nombre;
	private boolean globalFilterOnly;

	@EJB
	ICategoriaService catSrv;

	@PostConstruct
	public void init() {
		try {
			categorias = catSrv.listar();

		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void addCategoria() {

		logger.info("Categoria 'nombre': " + nombre);

		CategoriaCrearDTO categoriaCDTO = new CategoriaCrearDTO(nombre);

		try {
			CategoriaDTO categoriaDTO = catSrv.crear(categoriaCDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categorìa " + categoriaDTO.getNombre() + " creada con éxito.", null));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				categorias = catSrv.listar();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}

	}
	
	/*ACTUALIZAR*/
	public void updCategoria() {

		logger.info("updCategoria 'nombre': " + nombre);

		CategoriaCrearDTO categoriaCDTO = new CategoriaCrearDTO(nombre);

		try {
			CategoriaDTO categoriaDTO = catSrv.editar(id, categoriaCDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Categoria " + categoriaDTO.getNombre() + " creada con éxito.", null));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				categorias = catSrv.listar();
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

}
