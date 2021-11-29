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
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.vividsolutions.jts.io.ParseException;

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
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.converter.GeoConverter;
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanAddMenu")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@RequestScoped
public class MenuAddBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(MenuAddBean.class);

	// Variables Menu
	private Long id;
	private Long id_restaurante;
	private String nombre;
	private RestauranteDTO restaurante;
	private String descripcion;
	private Double precioSimple;
	private Double precioTotal;
	private List<ProductoDTO> productos;
	private List<ExtraMenuDTO> extras;
	private String id_imagen;
	private ImagenDTO imagen;

	// Variables auxiliares y contexto
	private UploadedFile imgfile;
	private CroppedImage croppedImage;
	FacesContext facesContext;
	HttpSession session;
	UsuarioDTO usuarioDTO;
	private List<ProductoDTO> productosRestaurante;
	private List<ExtraMenuDTO> extrasRestaurante;
	private Map<Long, ProductoDTO> productosById;
	private Map<Long, ExtraMenuDTO> extrasById;

	// Variables de SelectCheckboxMenu
	List<SelectItem> productsItems;
	String[] productsSelectedItems;
	List<SelectItem> extrasItems;
	String[] extrasSelectedItems;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IMenuService menuSrv;

	@EJB
	IProductoService prodSrv;

	@EJB
	IExtraMenuService extraSrv;

	@EJB
	IImagenService imgSrv;

	@EJB
	IDepartamentoService deptoSrv;

	@EJB
	GeoConverter geoConverter;

	@PostConstruct
	public void init() {
		//clearParam();

		facesContext = FacesContext.getCurrentInstance();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);

		usuarioDTO = getUserSession();

		if (usuarioDTO == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
		} else {
			try {
				id_restaurante = usuarioDTO.getId();
				restaurante = (RestauranteDTO) usuarioDTO;
				productosById = new HashMap<Long, ProductoDTO>();
				extrasById = new HashMap<Long, ExtraMenuDTO>();

				productosRestaurante = prodSrv.listarPorRestaurante(id_restaurante);
				extrasRestaurante = extraSrv.listarPorRestaurante(id_restaurante);

				if (!productosRestaurante.isEmpty()) {
					for (ProductoDTO prod : productosRestaurante) {

						productosById.put(prod.getId(), prod);
					}
				}

				if (!extrasRestaurante.isEmpty()) {
					for (ExtraMenuDTO ext : extrasRestaurante) {

						extrasById.put(ext.getId(), ext);
					}
				}

				loadProductosDelRestaurante();
				loadExtrasDelRestaurante();
			} catch (AppettitException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage().trim()));
			}
		}

	}

	public void addMenu() {
		String id_imagen = null;
		Boolean loadImg = false;
		imagen = null;

		productos = new ArrayList<ProductoDTO>();
		extras = new ArrayList<ExtraMenuDTO>();

		try {
			crop();

			try {
				byte[] bimg = getImageAsByteArray();
				if (bimg != null) {

					String identificador = "menu." + restaurante.getCorreo().trim() + System.currentTimeMillis();

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

			try {

				if (productsSelectedItems.length > 0) {

					for (String id : productsSelectedItems) {
						ProductoDTO prodSelected = productosById.get(Long.valueOf(id));

						productos.add(prodSelected);
					}
				}

				if (extrasSelectedItems.length > 0) {
					for (String id : extrasSelectedItems) {
						ExtraMenuDTO extraSelected = extrasById.get(Long.valueOf(id));

						extras.add(extraSelected);
					}
				}

				MenuDTO restDTO = new MenuDTO(id, id_restaurante, nombre, restaurante, descripcion, precioSimple,
						precioTotal, productos, extras, id_imagen, imagen);

				menuSrv.crear(restDTO);

				if (loadImg) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Menu " + restDTO.getNombre() + " creado con éxito.", null));

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Menu " + restDTO.getNombre() + " creado con éxito.", null));
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No fue posible cargar la imagen."));
				}

			} catch (ParseException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
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
		this.precioTotal = null;
		this.croppedImage = null;
		this.productosRestaurante = null;
		this.extrasRestaurante = null;
		setProductsSelectedItems(new String[] {});
		setExtrasSelectedItems(new String[] {});
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
	public List<SelectItem> getProductsItems() {
		return productsItems;
	}

	public void setProductsItems(List<SelectItem> products) {
		this.productsItems = products;
	}

	public String[] getProductsSelectedItems() {
		return productsSelectedItems;
	}

	public void setProductsSelectedItems(String[] selectedProducts) {
		this.productsSelectedItems = selectedProducts;
	}

	@SuppressWarnings("rawtypes")
	public void onItemUnselect(UnselectEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();

		FacesMessage msg = new FacesMessage();
		msg.setSummary("Item unselected: " + event.getObject().toString());
		msg.setSeverity(FacesMessage.SEVERITY_INFO);

		context.addMessage(null, msg);
	}

	/////////////////////////////////////////////////

	private void loadProductosDelRestaurante() throws AppettitException {

		productsItems = new ArrayList<>();

		if (!productosRestaurante.isEmpty()) {

			Map<String, List<ProductoDTO>> productsByCategory = new HashMap<String, List<ProductoDTO>>();

			for (ProductoDTO prod : productosRestaurante) {

				List<ProductoDTO> existingProductList = new ArrayList<>();

				if (productsByCategory.keySet().contains(prod.getCategoria().getNombre())) {
					existingProductList = productsByCategory.get(prod.getCategoria().getNombre());
				}

				existingProductList.add(prod);
				productsByCategory.put(prod.getCategoria().getNombre(), existingProductList);
			}

			for (String catName : productsByCategory.keySet()) {

				SelectItemGroup itemGroup = new SelectItemGroup(catName);
				SelectItem[] items = new SelectItem[productsByCategory.get(catName).size()];

				Integer count = 0;

				for (ProductoDTO prod : productsByCategory.get(catName)) {

					SelectItem item = new SelectItem(prod.getId(), prod.getNombre());
					items[count] = item;

					count++;
				}

				itemGroup.setSelectItems(items);
				productsItems.add(itemGroup);
			}

		}
	}

	private void loadExtrasDelRestaurante() {

		extrasItems = new ArrayList<>();

		if (!extrasRestaurante.isEmpty()) {

			SelectItemGroup itemGroup = new SelectItemGroup("Extras");
			SelectItem[] items = new SelectItem[extrasRestaurante.size()];

			Integer count = 0;

			for (ExtraMenuDTO extra : extrasRestaurante) {

				SelectItem item = new SelectItem(extra.getId(), extra.getProducto().getNombre());
				items[count] = item;

				count++;
			}

			itemGroup.setSelectItems(items);
			extrasItems.add(itemGroup);
		}
	}

}
