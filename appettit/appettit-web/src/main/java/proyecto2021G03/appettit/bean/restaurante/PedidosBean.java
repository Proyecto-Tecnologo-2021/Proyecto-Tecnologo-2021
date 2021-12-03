package proyecto2021G03.appettit.bean.restaurante;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.component.export.PDFOptions;
import org.primefaces.event.RowEditEvent;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.UserSession;
import proyecto2021G03.appettit.business.ICalificacionRRService;
import proyecto2021G03.appettit.business.IPedidoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanPedidos")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(PedidosBean.class);

	private List<PedidoDTO> pedidos = null;
	private List<PedidoDTO> filterPedidos = null;
	private PedidoDTO selPedido = null;
	private UsuarioDTO usuarioDTO;
	private Boolean disabledBloquedado = true;
	private Long id;
	private RestauranteDTO restaurante;
	private boolean globalFilterOnly;
	private PDFOptions pdfOpt;
	FacesContext facesContext;
	HttpSession session;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IPedidoService pedSrv;

	@EJB
	ICalificacionRRService califSrv;

	@EJB
	UserSession usrSession;

	@PostConstruct
	public void init() {
		try {
			
			facesContext = FacesContext.getCurrentInstance();
			//ExternalContext externalContext = facesContext.getExternalContext();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);
			
			usrSession.getRestauranteReg();
			
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
				} else {
					pedidos = pedSrv.listarPorRestaurante(usuarioDTO.getId());
					restaurante = (RestauranteDTO) usuarioDTO;
				}
			}
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
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

	public EstadoPedido[] getPedidoEstado() {
		return EstadoPedido.values();

	}

	public String getHoraPedido(Long id) throws AppettitException {

		return pedSrv.listarPorId(id).getFecha().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM"));
	}

	public String getNombreCliente(Long id) throws AppettitException {
		return usrSrv.buscarPorIdCliente(id).getNombre();
	}

	public String getTelCliente(Long id) throws AppettitException {
		return usrSrv.buscarPorIdCliente(id).getTelefono();
	}

	public DireccionDTO getDireccion(Long id) throws AppettitException {
		return usrSrv.buscarDireccionPorId(id);
	}

	public List<ItemDTO> getMenus() throws AppettitException {

		ItemDTO aux = null;
		List<ItemDTO> ret = new ArrayList<ItemDTO>();
		List<MenuDTO> menus = pedSrv.listarPorId(selPedido.getId()).getMenus();
		for (MenuDTO menu : menus) {
			if (existeEnItemDTO(ret, menu.getId())) {
				aux = obtenerItemDTO(ret, menu.getId());
				aux.setCantidad(aux.getCantidad() + 1);
				aux.setPrecio(aux.getPrecio() + menu.getPrecioSimple());
			} else {
				aux = new ItemDTO(1, menu.getId(), menu.getNombre(), menu.getPrecioSimple());
				ret.add(aux);
			}
		}

		return ret;
	}

	public List<ItemDTO> getPromos() throws AppettitException {

		ItemDTO aux = null;
		List<ItemDTO> ret = new ArrayList<ItemDTO>();
		List<PromocionDTO> promos = pedSrv.listarPorId(selPedido.getId()).getPromociones();
		for (PromocionDTO promo : promos) {
			if (existeEnItemDTO(ret, promo.getId())) {
				aux = obtenerItemDTO(ret, promo.getId());
				aux.setCantidad(aux.getCantidad() + 1);
				aux.setPrecio(aux.getPrecio() + promo.getPrecio());
			} else {
				aux = new ItemDTO(1, promo.getId(), promo.getNombre(), promo.getPrecio());
				ret.add(aux);
			}
		}

		return ret;
	}

	public CalificacionRPedidoDTO getCalificacion() throws AppettitException {

		CalificacionRPedidoDTO cali = califSrv.listarPorId(selPedido.getId(), selPedido.getId_cliente());
		if (cali == null) {
			cali = new CalificacionRPedidoDTO(0, 0, 0, "Pedido no calificado", selPedido.getId(),
					selPedido.getId_cliente());
		}
		return cali;
	}

	public void onRowSelect(RowEditEvent<PedidoDTO> event) {
		disabledBloquedado = false;
	}

	public void onRowEdit(RowEditEvent<PedidoDTO> event) {

		try {
			selPedido = event.getObject();
			pedSrv.editarEstadoPago(event.getObject());
			pedidos = pedSrv.listarPorRestaurante(usuarioDTO.getId());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage("Edición correcta", event.getObject().getId().toString()));

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void onRowCancel(RowEditEvent<PedidoDTO> event) {
		disabledBloquedado = true;

		FacesMessage msg = new FacesMessage("Edición cancelada", String.valueOf(event.getObject().getId().toString()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void customizationOptions() {
		pdfOpt = new PDFOptions();
		pdfOpt.setFacetBgColor("#f2a22c");
		pdfOpt.setFacetFontColor("#ffffff");
		pdfOpt.setFacetFontStyle("BOLD");
		pdfOpt.setCellFontSize("8");
		pdfOpt.setFontName("Verdana");
	}

	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		Document pdf = (Document) document;
		pdf.open();
		pdf.setMargins(1, 1, 1, 1);
		pdf.setPageSize(PageSize.A4);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		String separator = File.separator;
		String logo = externalContext.getRealPath("") + separator + "resources" + separator + "images" + separator
				+ "logo.png";

		pdf.add(Image.getInstance(logo));
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

	public boolean existeEnItemDTO(List<ItemDTO> items, Long id) {

		if (items == null) {
			return false;
		} else {
			for (ItemDTO item : items) {
				if (item.getId().compareTo(id) == 0) {
					return true;
				}
			}
			return false;
		}
	}

	public ItemDTO obtenerItemDTO(List<ItemDTO> items, Long id) {
		for (ItemDTO item : items) {
			if (item.getId().compareTo(id) == 0) {
				return item;
			}
		}
		return null;
	}

}
