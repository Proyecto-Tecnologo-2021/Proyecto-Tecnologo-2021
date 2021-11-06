package proyecto2021G03.appettit.bean.restaurante;

import com.vividsolutions.jts.io.ParseException;
import lombok.*;
import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import proyecto2021G03.appettit.business.*;
import proyecto2021G03.appettit.converter.GeoConverter;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

//    private List<ProductoDTO> productos;
//    private List<ExtraMenuDTO> extras;

    private Long id;
    private String nombre;
    private String descripcion;
    private ImagenDTO imagen;
    private UploadedFile imgfile;
    private CroppedImage croppedImage;

    // Variables de SelectCheckboxMenu
    private List<SelectItem> products;
    private String[] selectedProducts;

    private String correo;

    @EJB
    IUsuarioService usrSrv;

    @EJB
    IMenuService menuSrv;

    @EJB
    IImagenService imgSrv;

    @EJB
    IDepartamentoService deptoSrv;

    @EJB
    GeoConverter geoConverter;

    @PostConstruct
    public void init() {
        clearParam();

        ////////////////////////////////////////////////////////
//        cities = new ArrayList<>();
//        cities.add("Miami");
//        cities.add("London");
//        cities.add("Paris");
//        cities.add("Istanbul");
//        cities.add("Berlin");
//        cities.add("Barcelona");
//        cities.add("Rome");
//        cities.add("Brasilia");
//        cities.add("Amsterdam");

        products = new ArrayList<>();
        SelectItemGroup pastaProducts = new SelectItemGroup("Pastas");
        pastaProducts.setSelectItems(new SelectItem[]{
                new SelectItem("pasta1", "Ravioles"),
                new SelectItem("pasta2", "Tallarines"),
                new SelectItem("pasta3", "Ñoquis")
        });

        SelectItemGroup fastFoodProducts = new SelectItemGroup("Comida Rapida");
        fastFoodProducts.setSelectItems(new SelectItem[]{
                new SelectItem("ff1", "Hamburguesa Completa"),
                new SelectItem("ff2", "Papas Fritas"),
                new SelectItem("ff3", "Chorizo al Pan")
        });

        products.add(pastaProducts);
        products.add(fastFoodProducts);

    }

    public void addMenu() {
        String id_imagen = null;
        Boolean loadImg = false;
        imagen = null;

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
}
