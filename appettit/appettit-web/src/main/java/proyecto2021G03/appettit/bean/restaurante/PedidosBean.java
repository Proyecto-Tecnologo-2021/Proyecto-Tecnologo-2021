package proyecto2021G03.appettit.bean.restaurante;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
import proyecto2021G03.appettit.business.IPedidoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.PedidoDTO;
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

	@PostConstruct
	public void init() {

		facesContext = FacesContext.getCurrentInstance();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);

		usuarioDTO = getUserSession();

		if (usuarioDTO == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
		} else {
			try {
				
				//pedidos = pedSrv.listarPorRestaurante(usuarioDTO.getId());
				pedidos = pedSrv.listarPorRestaurante(usuarioDTO.getId());
				restaurante = (RestauranteDTO) usuarioDTO;

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

	public EstadoPedido[] getPedidoEstado() {
		return EstadoPedido.values();

	}

	public String getNombreCliente (Long id) throws AppettitException {
		return usrSrv.buscarPorIdCliente(id).getNombre();
	}
	
	public  DireccionDTO getDireccion (Long id) throws AppettitException {
		return usrSrv.buscarDireccionPorId(id);
	}
	
	/*
	public List<MenuDTO> getMenuRestaurante() {
		List<MenuDTO> menus = new ArrayList<MenuDTO>();

		try {
			menus = menuSRV.listarPorRestaurante(getSelRestaurante().getId());

			if (menus.size() < 3)
				menuDeshabilitado = true;

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
		}

		return menus;
	}
	*/
	
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
	
}
