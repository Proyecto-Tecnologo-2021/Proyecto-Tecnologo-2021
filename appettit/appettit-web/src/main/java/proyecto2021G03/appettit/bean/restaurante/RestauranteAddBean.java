package proyecto2021G03.appettit.bean.restaurante;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalTime;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IGeoService;
import proyecto2021G03.appettit.business.IImagenService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.converter.GeoConverter;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAddRestaurante")
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestauranteAddBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(RestauranteAddBean.class);

	private Long id;
	private String nombre;
	private String password;
	private String telefono;
	private String correo;
	private String rut;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	private Boolean abiertoAutom;
	private DireccionDTO direccion;
	private ImagenDTO imagen;
	private UploadedFile imgfile;
	private CroppedImage croppedImage;
	private String point;
	private String strareaentrega;
	
	
	@EJB
	IUsuarioService usrSrv;

	@EJB
	IImagenService imgSrv;
	
	@EJB
	IGeoService geoSrv;
	
	@EJB
	IDepartamentoService deptoSrv;
	
	@EJB
	GeoConverter geoConverter;
	
	WKTReader fromText;

	@PostConstruct
	public void init() {
		clearParam();
		fromText = new WKTReader(new GeometryFactory(new PrecisionModel(), 32721));
		
		
	}

	public void addRestaurante() {
		String id_imagen = null;
		Boolean loadImg = false; 
		imagen = null;
		
		try {
			crop();
			
			try {
				byte[] bimg = getImageAsByteArray();
				if (bimg != null) {
					String identificador = "restaurante." + this.getCorreo().trim() + "." + this.getTelefono().trim();

					imagen = imgSrv.buscarPorIdentificador(identificador);
					
					if(imagen == null) {
						imagen = new ImagenDTO();
						imagen.setIdentificador(identificador);
						imagen.setImagen(bimg);
						
						imgSrv.crear(imagen);
						imagen = imgSrv.buscarPorIdentificador(identificador);
						id_imagen = imagen.getId();
					}

					loadImg = true;
				} 
				
			} catch (IOException e) {
				logger.info(e.getMessage().trim());
				loadImg = false;
			}
			
			
			try {
				LocalidadDTO ldto = geoSrv.localidadPorPunto(point);
				
				if(ldto != null) {
					direccion.setGeometry(point);
					direccion.setBarrio(ldto);
					
					RestauranteDTO restDTO = new RestauranteDTO(null, nombre, correo, password, telefono, correo, null, null, rut,
							EstadoRegistro.PENDIENTE, true, horarioApertura, horarioCierre, false, abiertoAutom, strareaentrega, direccion,
							id_imagen);

					
					restDTO = usrSrv.crearRestaurante(restDTO);

					if(loadImg) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Restaurante " + restDTO.getCorreo() + " creado con éxito.", null));
						
					}else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Restaurante " + restDTO.getCorreo() + " creado con éxito.", null));
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
								"No fue posible cargar la imagen."));
					}
						
					//	FacesContext.getCurrentInstance().getExternalContext().redirect("/appettit-web/restaurante/home.xhtml");	
					
				} else {
					 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
		                    "Direcci\00F3n incorrecta."));
					logger.info("Localidad vacia");
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
		this.password = null;
		this.correo = null;
		this.telefono = null;
		this.rut = null;
		this.horarioApertura = null;
		this.horarioCierre = null;
		this.abiertoAutom = true;
		this.direccion = new DireccionDTO();
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
    
}
