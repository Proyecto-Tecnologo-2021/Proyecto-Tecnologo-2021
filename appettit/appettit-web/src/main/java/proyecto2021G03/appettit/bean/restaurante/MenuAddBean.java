package proyecto2021G03.appettit.bean.restaurante;

import com.vividsolutions.jts.io.ParseException;
import lombok.*;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import proyecto2021G03.appettit.business.*;
import proyecto2021G03.appettit.converter.GeoConverter;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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

    private Long id;
    private String nombre;
    private String descripcion;
    private ImagenDTO imagen;
    private UploadedFile imgfile;
    private CroppedImage croppedImage;
    FacesContext facesContext;
    HttpSession session;
    UsuarioDTO usuarioDTO;
    RestauranteDTO restaurante;
    List<ProductoDTO> productosRestaurante;
    List<ExtraMenuDTO> extrasRestaurante;

    // Variables de SelectCheckboxMenu
    private List<SelectItem> products;
    private String[] selectedProducts;
    private List<SelectItem> extras;
    private String[] selectedExtras;

    private String correo;

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
    public void init() throws AppettitException {
        clearParam();

        facesContext = FacesContext.getCurrentInstance();
        session = (HttpSession) facesContext.getExternalContext().getSession(true);

        usuarioDTO = getUserSession();

        if (usuarioDTO == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "USUARIO NO LOGUEADO"));
        }
        else {

            restaurante = (RestauranteDTO) usuarioDTO;
            productosRestaurante = prodSrv.listarPorRestaurante(usuarioDTO.getId());
            extrasRestaurante = extraSrv.listarPorRestaurante(usuarioDTO.getId());

            loadProductosDelMenu();
            loadExtrasDelMenu();
        }

    }

    public void addMenu() {
        String id_imagen = null;
        Boolean loadImg = false;
        imagen = null;

        logger.info("PRODS: " + selectedProducts.length);
        logger.info("EXTRAS: " + selectedExtras.length);

        try {
            crop();

            try {
                byte[] bimg = getImageAsByteArray();
                if (bimg != null) {
                    String identificador = "menu." + this.getNombre().trim();

                    imagen = imgSrv.buscarPorIdentificador(identificador);
 
                    if(imagen == null) {
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
                //***** PREGUNTAR DATOS DE CONST
                MenuDTO restDTO = new MenuDTO(); // new MenuDTO(null, nombre, descripcion);


                restDTO = menuSrv.crear(restDTO);

                if(loadImg) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Menu " + restDTO.getNombre() + " creado con éxito.", null));

                }else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Menu " + restDTO.getNombre() + " creado con éxito.", null));
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                                "No fue posible cargar la imagen."));
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
            //} catch (IOException e) {
            //	logger.info(e.getMessage().trim());
            //	FacesContext.getCurrentInstance().addMessage(null,
            //			new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
        } finally {
            clearParam();
        }
    }

    private void clearParam() {
        this.id = null;
        this.nombre = null;
        this.descripcion = null;
        this.imagen = null;
        this.imgfile = null;
        this.croppedImage = null;

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
        if (!(this.croppedImage == null || this.croppedImage.getBytes() == null || this.croppedImage.getBytes().length == 0)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito",
                    "Recorte exitoso."));
        }
    }

    public StreamedContent getImage() {
        return DefaultStreamedContent.builder()
                .contentType(imgfile == null ? null : imgfile.getContentType())
                .stream(() -> {
                    if (imgfile == null
                            || imgfile.getContent() == null
                            || imgfile.getContent().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(imgfile.getContent());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
    }

    public StreamedContent getCropped() {
        return DefaultStreamedContent.builder()
                .contentType(imgfile == null ? null : imgfile.getContentType())
                .stream(() -> {
                    if (croppedImage == null
                            || croppedImage.getBytes() == null
                            || croppedImage.getBytes().length == 0) {
                        return null;
                    }

                    try {
                        return new ByteArrayInputStream(this.croppedImage.getBytes());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .build();
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
    public List<SelectItem> getProducts() {
        return products;
    }

    public void setProducts(List<SelectItem> products) {
        this.products = products;
    }

    public String[] getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(String[] selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public void onItemUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage msg = new FacesMessage();
        msg.setSummary("Item unselected: " + event.getObject().toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        context.addMessage(null, msg);
    }

    /////////////////////////////////////////////////

    private void loadProductosDelMenu() throws AppettitException {

        products = new ArrayList<>();

        if (!productosRestaurante.isEmpty()) {

            Map<String, List<ProductoDTO>> productsByCategory = new HashMap<String, List<ProductoDTO>>();

            for (ProductoDTO prod : productosRestaurante) {

                List<ProductoDTO> existingProductList = new ArrayList<>();

                if (productsByCategory.keySet().contains(prod.getCategoria().getNombre())) {
                    existingProductList =  productsByCategory.get(prod.getCategoria().getNombre());
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

                    count ++;
                }

                itemGroup.setSelectItems(items);
                products.add(itemGroup);
            }

        }
    }

    private void loadExtrasDelMenu() {

        extras = new ArrayList<>();

        if (!extrasRestaurante.isEmpty()) {

            SelectItemGroup itemGroup = new SelectItemGroup("Extras");
            SelectItem[] items = new SelectItem[extrasRestaurante.size()];

            Integer count = 0;

            for (ExtraMenuDTO extra : extrasRestaurante) {

                SelectItem item = new SelectItem(extra.getId(), extra.getProducto().getNombre());
                items[count] = item;

                count ++;
            }

            itemGroup.setSelectItems(items);
            extras.add(itemGroup);
        }
    }

}
