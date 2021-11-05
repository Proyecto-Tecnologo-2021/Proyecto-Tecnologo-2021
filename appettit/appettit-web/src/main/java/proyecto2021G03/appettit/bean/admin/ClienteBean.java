package proyecto2021G03.appettit.bean.admin;

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
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAdminCliente")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ClienteBean.class);

	private List<ClienteDTO> clientes = null;
	private List<ClienteDTO> filterClientes = null;
	//private ClienteDTO selCliente = null;
	private Boolean disabledBloquedado = true;
	private Long id;
	private boolean globalFilterOnly;
	private PDFOptions pdfOpt;

	@EJB
	IUsuarioService usrSrv;

	@PostConstruct
	public void init() {
		customizationOptions();
		try {
			clientes = usrSrv.listarClientes();
			logger.info("Clientes: " + clientes.size());
			//selCliente = null;

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

	public void onRowSelect(RowEditEvent<ClienteDTO> event) {
		disabledBloquedado = false;
	}

	public void onRowEdit(RowEditEvent<ClienteDTO> event){

		try {
			//selCliente = event.getObject();

			logger.info("onRowEdit - Bloqueado: " + event.getObject().getBloqueado());
			
			if ((event.getObject().getBloqueado() == true)) {
				usrSrv.bloquearCliente(event.getObject().getId());
			} else {
				usrSrv.desbloquearCliente(event.getObject().getId());
			}
			clientes = usrSrv.listarClientes();
			FacesContext.getCurrentInstance().addMessage(null,
			new FacesMessage("Edición correcta", event.getObject().getNombre()));

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}
	}

	public void onRowCancel(RowEditEvent<ClienteDTO> event) {
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
