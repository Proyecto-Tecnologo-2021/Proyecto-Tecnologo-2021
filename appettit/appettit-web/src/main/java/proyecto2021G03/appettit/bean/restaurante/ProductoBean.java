package proyecto2021G03.appettit.bean.restaurante;

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
import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

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
	private List<ProductoDTO> filterProductos;

	private Long id;
	private String nombre;
	private Long id_categoria;
	private RestauranteDTO restaurante;
	private CategoriaDTO categoria;
	private boolean globalFilterOnly;
	FacesContext facesContext;
	HttpSession session;
	private UsuarioDTO usuarioDTO;
	private Boolean disabledBloquedado = true;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	ICategoriaService catSrv;

	@EJB
	IProductoService prodSrv;

	@EJB
	IUserSession usrSession;

	@PostConstruct
	public void init() {
		try {
			
			facesContext = FacesContext.getCurrentInstance();
			//ExternalContext externalContext = facesContext.getExternalContext();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);
			
			//usrSession.getRestauranteReg();
			
			UsuarioDTO usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				//externalContext.invalidateSession();
				//externalContext.dispatch(Constantes.REDIRECT_URI);
				//externalContext.redirect(Constantes.REDIRECT_URI);
				usrSession.destroySession();
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
			} else {
				if(!(usuarioDTO instanceof RestauranteDTO)) {
					//externalContext.invalidateSession();
					//externalContext.dispatch(Constantes.REDIRECT_URI);
					//externalContext.redirect(Constantes.REDIRECT_URI);
					usrSession.destroySession();
					
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
				} else {					categorias = catSrv.listar();
					productos = prodSrv.listarPorRestaurante(usuarioDTO.getId());
					restaurante = (RestauranteDTO) usuarioDTO;

				}
			}
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		}

	}

	public void addProducto() {

		logger.info("addProducto 'nombre': " + nombre);
		ProductoDTO nprod = null;
		try {
			CategoriaDTO categoria = catSrv.listarPorId(id_categoria);

			nprod = ProductoDTO.builder().id_categoria(categoria.getId()).id_restaurante(restaurante.getId())
					.nombre(nombre).build();

			ProductoDTO productoDTO = prodSrv.crear(nprod);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Informaci??n", "Producto " + productoDTO.getNombre() + " creado con ??xito."));
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		} finally {
			try {
				productos = prodSrv.listarPorRestaurante(nprod.getId_restaurante());
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
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

	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO = (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}

		return usuarioDTO;

	}

	public void onRowSelect(RowEditEvent<ProductoDTO> event) {
		disabledBloquedado = false;
	}

	public void onRowEdit(RowEditEvent<ProductoDTO> event) {
		ProductoDTO nprod = null;

		try {
			logger.info("onRowEdit Producto - ID: " + event.getObject().getId());

			CategoriaDTO categoria = catSrv.listarPorId(event.getObject().getId_categoria());

			nprod = ProductoDTO.builder().id(event.getObject().getId())
					.id_categoria(event.getObject().getCategoria().getId()).id_restaurante(restaurante.getId())
					.categoria(categoria).nombre(event.getObject().getNombre())
					// .restaurante(restaurante)
					.build();

			prodSrv.editar(nprod);
			productos = prodSrv.listarPorRestaurante(nprod.getId_restaurante());

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Edici??n correcta", event.getObject().getNombre()));

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		}
	}

	public void onRowCancel(RowEditEvent<ProductoDTO> event) {
		disabledBloquedado = true;

		FacesMessage msg = new FacesMessage("Edici??n cancelada", String.valueOf(event.getObject().getNombre()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
