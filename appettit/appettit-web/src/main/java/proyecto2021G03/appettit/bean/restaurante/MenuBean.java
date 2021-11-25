package proyecto2021G03.appettit.bean.restaurante;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.component.export.PDFOptions;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanMenuRestaurante")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(MenuBean.class);
	
	private List<MenuDTO> menus;
	private List<ProductoDTO> productos;
	private List<MenuDTO> filterMenus;

	
	private Long id;
    private Long id_restaurante;
    private String nombre;
    private RestauranteDTO restaurante;
    private String descripcion;
    private Double precioSimple;
    private Double precioTotal;
    private List<ExtraMenuDTO> extras;
    private String id_imagen;
    private boolean globalFilterOnly;
	FacesContext facesContext;
	HttpSession session;
	private Boolean disabledBloquedado = true;
	private MenuDTO selMenu;
	private PDFOptions pdfOpt;
	
    
	@EJB
	IUsuarioService usrSrv;

	@EJB
	IProductoService prodSrv;
	
	@EJB
	IMenuService menuSrv;

	@PostConstruct
	public void init() {

		facesContext = FacesContext.getCurrentInstance();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);

		UsuarioDTO usuarioDTO = getUserSession();

		if (usuarioDTO == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
		} else {
			try {
			
				productos = prodSrv.listarPorRestaurante(usuarioDTO.getId());
				menus = menuSrv.listarPorRestaurante(usuarioDTO.getId());
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

	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO = (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}

		return usuarioDTO;

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

}
