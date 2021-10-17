package proyecto2021G03.appettit.bean.restaurante;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalTime;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
import com.vividsolutions.jts.geom.MultiPolygon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IImagenService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CalificacionRestauranteDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.ImagenDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanAddRestaurante")
@RequestScoped
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
	private String username;
	private String password;
	private String telefono;
	private String correo;
	private String token;
	private String tokenFireBase;
	private String rut;
	private EstadoRegistro estado;
	private Boolean bloqueado;
	private LocalTime horarioApertura;
	private LocalTime horarioCierre;
	private Boolean abierto;
	private Boolean abiertoAutom;
	private MultiPolygon areaentrega;
	private DireccionDTO direccion;
	private CalificacionRestauranteDTO calificacion;
	private String id_imagen;
	private ImagenDTO imagen;
	private UploadedFile imgfile;
	private CroppedImage croppedImage;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IImagenService imgSrv;

	@PostConstruct
	public void init() {
		clearParam();
	}

	public void addRestaurante() {

		logger.info("addRestaurante 'nombre': " + nombre);
		
		crop();
		
		RestauranteDTO restDTO = new RestauranteDTO(null, nombre, username, password, telefono, correo, null, null, rut,
				estado, bloqueado, horarioApertura, horarioCierre, abierto, abiertoAutom, areaentrega, direccion,
				id_imagen);

		try {
			restDTO = usrSrv.crearRestaurante(restDTO);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Restaurante " + restDTO.getCorreo() + " creado con éxito.", null));
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
		this.password = null;
		this.correo = null;
		this.telefono = null;
		this.username = null;
		this.token = null;
		this.tokenFireBase = null;
		this.rut = null;
		this.estado = EstadoRegistro.PENDIENTE;
		this.bloqueado = true;
		this.horarioApertura = null;
		this.horarioCierre = null;
		this.abierto = false;
		this.abiertoAutom = true;
		this.areaentrega = null;
		this.direccion = null;
		this.calificacion = null;
		this.imagen = null;
		this.id_imagen = null;
		this.imgfile = null;
		this.croppedImage = null;
		
	}


	public byte[] getImageAsByteArray() throws IOException {
		
		logger.info("EN getImageAsByteArray");
		if (imgfile != null) {
			logger.info(imgfile.getFileName());
			
			InputStream is = imgfile.getInputStream();
			byte[] array = new byte[is.available()];
			is.read(array);
			return array;
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
            FacesMessage msg = new FacesMessage("Exito", this.imgfile.getFileName() + " fue cargado.");
            //FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void crop() {
        if (this.croppedImage == null || this.croppedImage.getBytes() == null || this.croppedImage.getBytes().length == 0) {
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
            //        "Fall&oacute; recorte."));
        }
        else {
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
