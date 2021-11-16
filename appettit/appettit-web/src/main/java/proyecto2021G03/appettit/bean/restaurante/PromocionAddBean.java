package proyecto2021G03.appettit.bean.restaurante;

import com.vividsolutions.jts.io.ParseException;
import lombok.*;
import org.apache.commons.math3.stat.descriptive.summary.Product;
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

    static Logger logger = Logger.getLogger(MenuAddBean.class);

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
            id_restaurante = usuarioDTO.getId();
            restaurante = (RestauranteDTO) usuarioDTO;
            menusById = new HashMap<Long, MenuDTO>();

            menusRestaurante = menuSrv.listarPorRestaurante(id_restaurante);

            if (!menusRestaurante.isEmpty()) {
                for (MenuDTO menu : menusRestaurante) {

                    menusById.put(menu.getId(), menu);
                }
            }

            loadMenusDelRestaurante();
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


            if (menusSelectedItems.length > 0) {

                for (String id : menusSelectedItems) {
                    MenuDTO menusSelected = menusById.get(Long.valueOf(id));

                    menus.add(menusSelected);
                }
            }

            PromocionDTO restDTO = new PromocionDTO(id, id_restaurante, nombre, restaurante, descripcion, descuento,
                    precio, menus, id_imagen, imagen);

            promSrv.crear(restDTO);

            if(loadImg) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Promocion " + restDTO.getNombre() + " creado con éxito.", null));

            }else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Promocion " + restDTO.getNombre() + " creado con éxito.", null));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Error", "No fue posible cargar la imagen."));
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
        setMenusSelectedItems(new String[]{});
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
    public List<SelectItem> getMenusItems() {
        return menusItems;
    }

    public void setMenusItems(List<SelectItem> menus) {
        this.menusItems = menus;
    }

    public void onItemUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage msg = new FacesMessage();
        msg.setSummary("Item unselected: " + event.getObject().toString());
        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        context.addMessage(null, msg);
    }

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

                count ++;
            }

            itemGroup.setSelectItems(items);
            menusItems.add(itemGroup);
        }
    }

}

