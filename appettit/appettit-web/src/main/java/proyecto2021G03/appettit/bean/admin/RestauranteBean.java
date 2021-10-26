package proyecto2021G03.appettit.bean.admin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAdminRestaurante")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestauranteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RestauranteBean.class);

	private List<RestauranteDTO> restaurantes = null;
	private List<RestauranteDTO> filterRestaurantes = null;
	private RestauranteDTO selRestaurante = null;
	private Boolean menuDeshabilitado = null;
	private Boolean disabledBloquedado = true;
	private Long id;
	private boolean globalFilterOnly;
	private PDFOptions pdfOpt;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IMenuService menuSRV;

	@PostConstruct
	public void init() {
		customizationOptions();
		try {
			restaurantes = usrSrv.listarRestaurantes();
			logger.info("Restaurantes: " + restaurantes.size());

			// menuRestaurante = null;
			menuDeshabilitado = false;
			selRestaurante = null;

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
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

	public EstadoRegistro[] getRestauranteEstado() {
		return EstadoRegistro.values();

	}

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

	public void onRowSelect(RowEditEvent<RestauranteDTO> event) {
		disabledBloquedado = false;
	}

	public void onRowEdit(RowEditEvent<RestauranteDTO> event) {

		try {
			selRestaurante = event.getObject();

			logger.info("onRowEdit - Bloqueddo: " + event.getObject().getBloqueado());

			if (!event.getObject().getBloqueado() && getMenuRestaurante().size() < 3) {
				restaurantes = usrSrv.listarRestaurantes();

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error",
						"El Restaurante tiene menos de 3 menús, no es posible desbloquearlo."));

			} else {
				usrSrv.editarRestaurante(event.getObject());
				restaurantes = usrSrv.listarRestaurantes();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Edición correcta", event.getObject().getNombre()));

			}

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void onRowCancel(RowEditEvent<RestauranteDTO> event) {
		disabledBloquedado = true;

		FacesMessage msg = new FacesMessage("Edición cancelada", String.valueOf(event.getObject().getNombre()));
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

}
