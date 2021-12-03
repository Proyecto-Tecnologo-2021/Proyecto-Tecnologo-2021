package proyecto2021G03.appettit.bean.restaurante;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.UserSession;
import proyecto2021G03.appettit.business.IImagenService;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IPromocionService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanAddPromocion")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromocionAddBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(PromocionAddBean.class);

	private Long id_restaurante;
	private String nombre;
	private RestauranteDTO restaurante;
	private String descripcion;
	private Double subTotal;
	private Double descuento;
	private Double precioTotal;
	private List<MenuDTO> menus;
	private List<MenuDTO> menuSel;
	private String id_imagen;
	private ImagenDTO imagen;

	// Variables auxiliares y contexto
	private UploadedFile imgfile;
	private CroppedImage croppedImage;
	FacesContext facesContext;
	HttpSession session;
	UsuarioDTO usuarioDTO;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IMenuService menuSrv;

	@EJB
	IImagenService imgSrv;

	@EJB
	IPromocionService promoSrv;

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
				} else {					id_restaurante = usuarioDTO.getId();
					restaurante = (RestauranteDTO) usuarioDTO;
					menus = menuSrv.listarPorRestaurante(id_restaurante);
					logger.info("Menus promociones: " + menus.size());

					menuSel = new ArrayList<MenuDTO>();
				}
			}
		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
		}
	}

	public void addPromocion() {
		String id_imagen = null;
		Boolean loadImg = false;
		imagen = null;

		try {
			crop();

			try {
				byte[] bimg = getImageAsByteArray();
				if (bimg != null) {
					String identificador = "promocion." + this.restaurante.getId() + "." + +System.currentTimeMillis();

					imagen = imgSrv.buscarPorIdentificador(identificador);

					if (imagen == null) {
						imagen = new ImagenDTO();
						imagen.setIdentificador(identificador);
						imagen.setImagen(bimg);

						imgSrv.crear(imagen);
						imagen = imgSrv.buscarPorIdentificador(identificador);
					}

					id_imagen = imagen.getId();
					loadImg = true;
				}

			} catch (IOException e) {
				logger.info(e.getMessage().trim());
				loadImg = false;
			}

			PromocionDTO promocionDTO = new PromocionDTO(null, restaurante.getId(), nombre, null, descripcion,
					descuento, precioTotal, menuSel, id_imagen, null);

			promocionDTO = promoSrv.crear(promocionDTO);

			if (loadImg) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Promoci&oacute;n " + promocionDTO.getNombre() + " creada con éxito.", null));

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Promoci&oacute;n " + promocionDTO.getNombre() + " creada con éxito.", null));
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No fue posible cargar la imagen."));
			}

		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} finally {
			clearParam();
			try {
				menus = menuSrv.listarPorRestaurante(id_restaurante);
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}

	}

	private void clearParam() {

		this.nombre = null;
		this.descripcion = null;
		this.croppedImage = null;
		// this.menuSel = new ArrayList<MenuDTO>();
		this.descuento = 0D;
		this.subTotal = 0D;
		this.precioTotal = 0D;
		this.menus = new ArrayList<MenuDTO>();
		this.id_imagen = null;

	}

	public byte[] getImageAsByteArray() throws IOException {

		if (this.croppedImage != null) {
			return this.croppedImage.getBytes();
		} else {
			return null;
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		this.imgfile = null;
		this.croppedImage = null;
		UploadedFile file = event.getFile();
		if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
			this.imgfile = file;
		}
	}

	public void crop() {
		if (!(this.croppedImage == null || this.croppedImage.getBytes() == null
				|| this.croppedImage.getBytes().length == 0)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Recorte exitoso."));
		}
	}

	public StreamedContent getImage() {
		return DefaultStreamedContent.builder().contentType(imgfile == null ? null : imgfile.getContentType())
				.stream(() -> {
					if (imgfile == null || imgfile.getContent() == null || imgfile.getContent().length == 0) {
						return null;
					}

					try {
						return new ByteArrayInputStream(imgfile.getContent());
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).build();
	}

	public StreamedContent getCropped() {
		return DefaultStreamedContent.builder().contentType(imgfile == null ? null : imgfile.getContentType())
				.stream(() -> {
					if (croppedImage == null || croppedImage.getBytes() == null
							|| croppedImage.getBytes().length == 0) {
						return null;
					}

					try {
						return new ByteArrayInputStream(this.croppedImage.getBytes());
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}).build();
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

	public void calcularPrecio() {
		this.subTotal = 0D;
		this.precioTotal = 0D;

		for (MenuDTO m : menuSel) {
			this.subTotal = this.subTotal + m.getPrecioTotal();
		}

		this.precioTotal = subTotal * (100 - this.descuento) / 100;

	}

	public void addMenu(MenuDTO menu) {
		menuSel.add(menu);
		calcularPrecio();

	}

	public void deleteMenu(MenuDTO menu) {
		int id = 0;
		for (MenuDTO m : menuSel) {
			if (m.getId() == menu.getId()) {
				menuSel.remove(id);
				break;
			}
			id++;
		}

		calcularPrecio();
	}
}
