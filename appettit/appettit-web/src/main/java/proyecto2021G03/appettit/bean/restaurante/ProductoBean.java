package proyecto2021G03.appettit.bean.restaurante;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.UserBean;
import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanProducto")
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ProductoBean.class);

	private List<CategoriaDTO> categorias;
	private List<ProductoDTO> productos;

	private Long id;
	private String nombre;
	private Long id_categoria;
	private RestauranteDTO restaurante;
	private boolean globalFilterOnly;
	
	@EJB
	IUsuarioService usrSrv;

	@EJB
	ICategoriaService catSrv;
	
	@EJB
	IProductoService prodSrv;

	
	@PostConstruct
	public void init() {
		
		UserBean userBean = new UserBean();
		UsuarioDTO usuarioDTO = userBean.getUserSession();
		
		if(usuarioDTO==null) {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));	
		}else {
			try {
				categorias = catSrv.listar();
				productos = prodSrv.listarPorRestaurante(id);

			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}

		}

		
		
		
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
