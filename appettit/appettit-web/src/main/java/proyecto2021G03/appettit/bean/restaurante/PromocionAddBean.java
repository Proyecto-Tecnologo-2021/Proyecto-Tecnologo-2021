package proyecto2021G03.appettit.bean.restaurante;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IExtraMenuService;
import proyecto2021G03.appettit.business.IImagenService;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.business.IPromocionService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.converter.GeoConverter;
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
//@RequestScoped
public class PromocionAddBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(PromocionAddBean.class);

	// Variables Menu
	private Long id;
	private Long id_restaurante;
	private String nombre;
	private RestauranteDTO restaurante;
	private String descripcion;
	private Double descuento;
	private Double precio;
	private List<MenuDTO> menus;
	private String id_imagen;
	private ImagenDTO imagen;

	// Variables auxiliares y contexto
	private Double subtotal;

	private UploadedFile imgfile;
	private CroppedImage croppedImage;
	FacesContext facesContext;
	HttpSession session;
	UsuarioDTO usuarioDTO;
	private List<MenuDTO> menusRestaurante;
	private Map<Long, MenuDTO> menusById;

	// Variables de SelectCheckboxMenu
	List<SelectItem> menusItems;
	String[] menusSelectedItems;
	
	
	RestauranteDTO restauranteDTO;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IMenuService menuSrv;

	@EJB
	IProductoService prodSrv;

	@EJB
	IExtraMenuService extraSrv;

	@EJB
	IPromocionService promSrv;

	@EJB
	IImagenService imgSrv;

	@EJB
	IDepartamentoService deptoSrv;

	@EJB
	GeoConverter geoConverter;
	
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
			
				menusRestaurante = menuSrv.listarPorRestaurante(usuarioDTO.getId());
				restaurante = (RestauranteDTO) usuarioDTO;
				
				menusById = new HashMap<Long, MenuDTO>();
				descuento = 0D;
				precio = 0D;
				subtotal = 0D;
				menus = new ArrayList<MenuDTO>();
				
				logger.info(menusRestaurante.size());
				//clearParam();
				
				loadMenusDelRestaurante();
				
				if (!menusRestaurante.isEmpty()) {
					for (MenuDTO menu : menusRestaurante) {
						menusById.put(menu.getId(), menu);
					}
				}


			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
			}

		}
	}

	
	public Double getPrecio() {

		Double res = 0D;
		//recalculatePrecios();
		//return this.precio;
		res = (subtotal *(100-descuento))/100;
		
		return res;

	}

	
	public Double getSubtotal() {

		Double res = 0D; 
		menus = new ArrayList<MenuDTO>();
		
		//recalculatePrecios();
		//logger.info("getSubtotal: " + this.subtotal);
		if(menusSelectedItems != null) {
			for (String id : menusSelectedItems) {
				MenuDTO menu = menusById.get(Long.valueOf(id)); 
				menus.add(menu);
				res = res + menu.getPrecioTotal();
			}	
		}
		
		return res;
	}
	

	public void recalculatePrecios() {

		this.precio = 0.0;
		this.subtotal = 0.0;
		List<MenuDTO> menus = new ArrayList<MenuDTO>();

		if (this.menusSelectedItems != null && this.menusSelectedItems.length > 0) {

			logger.info("ENTRA");
			for (String id : this.menusSelectedItems) {
				menus.add(menusById.get(Long.valueOf(id)));
			}

			if (!menus.isEmpty()) {
				logger.info("SIZE : " + menus.size());
				for (MenuDTO menu : menus) {
					this.subtotal = this.subtotal + menu.getPrecioTotal();
				}

				this.precio = (this.subtotal * (100 - this.descuento)) / 100;

				logger.info("SUB : " + this.subtotal);
				logger.info("PRE : " + this.precio);
			}

		}

	}
	public void addPromo() {
		String id_imagen = null;
		Boolean loadImg = false;
		imagen = null;

		menus = new ArrayList<MenuDTO>();

		try {
			crop();

			try {
				byte[] bimg = getImageAsByteArray();
				if (bimg != null) {

					String identificador = "Promocion." + restaurante.getCorreo().trim() + System.currentTimeMillis();

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

			if (menusSelectedItems.length > 0) {

				for (String id : menusSelectedItems) {
					MenuDTO menusSelected = menusById.get(Long.valueOf(id));

					menus.add(menusSelected);
				}
			}

			PromocionDTO restDTO = new PromocionDTO(id, id_restaurante, nombre, restaurante, descripcion, descuento,
					precio, menus, id_imagen, imagen);

			promSrv.crear(restDTO);

			if (loadImg) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Promocion " + restDTO.getNombre() + " creado con éxito.", null));

			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Promocion " + restDTO.getNombre() + " creado con éxito.", null));
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No fue posible cargar la imagen."));
			}

		} catch (AppettitException e) {
			logger.info(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));

		} finally {
			clearParam();
		}
	}

	private void clearParam() {

		this.id = null;
		this.nombre = null;
		this.descripcion = null;
		this.descuento = null;
		this.precio = null;
		this.imagen = null;
		this.imgfile = null;
		this.croppedImage = null;
		setMenusSelectedItems(new String[] {});
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

	///////////////////////////////////////////////
	public List<SelectItem> getMenusItems() {
		return menusItems;
	}

	public void setMenusItems(List<SelectItem> menus) {
		this.menusItems = menus;
	}

/*
	@SuppressWarnings("rawtypes")
	public void onItemUnselect(UnselectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage();
		msg.setSummary("Item unselected: " + event.getObject().toString());
		msg.setSeverity(FacesMessage.SEVERITY_INFO);

		context.addMessage(null, msg);

		recalculatePrecios();
	}
	
	
	@SuppressWarnings("rawtypes")
	public void onItemSelect(SelectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage();
		msg.setSummary("Item selected: " + event.getObject().toString());
		msg.setSeverity(FacesMessage.SEVERITY_INFO);

		context.addMessage(null, msg);
		
		recalculatePrecios();

	}
	
*/
	/////////////////////////////////////////////////

	private void loadMenusDelRestaurante() {

		menusItems = new ArrayList<>();

		if (!menusRestaurante.isEmpty()) {

			SelectItemGroup itemGroup = new SelectItemGroup("Menus");
			SelectItem[] items = new SelectItem[menusRestaurante.size()];

			Integer count = 0;

			for (MenuDTO menu : menusRestaurante) {

				SelectItem item = new SelectItem(menu.getId(), menu.getNombre());
				items[count] = item;

				count++;
			}

			itemGroup.setSelectItems(items);
			menusItems.add(itemGroup);
		}
	}

}
