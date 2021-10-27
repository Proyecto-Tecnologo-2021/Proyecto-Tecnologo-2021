package proyecto2021G03.appettit.bean.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.primefaces.model.file.UploadedFile;

import com.vividsolutions.jts.io.ParseException;

import lombok.Getter;
import lombok.Setter;
import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IGeoService;
import proyecto2021G03.appettit.business.IProductoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.converter.DepartamentoConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.CategoriaCrearDTO;
import proyecto2021G03.appettit.dto.CategoriaDTO;
import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoRegistro;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.ProductoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Named("beanFileUpload")
@RequestScoped
@Getter
@Setter
public class FileUpload implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(FileUpload.class);

	@EJB
	IDepartamentoService departamentoService;

	@EJB
	IGeoService geoSrv;

	@EJB
	DepartamentoConverter dConverter;

	@EJB
	IUsuarioService usrSrv;

	@EJB
	UsuarioConverter usrConverter;

	@EJB
	ICategoriaService srvCategoria;

	@EJB
	IProductoService prodSrv;

	private UploadedFile filed;
	private UploadedFile filec;
	private UploadedFile filel;
	private UploadedFile filea;
	private UploadedFile filer;
	private UploadedFile filecat;
	private UploadedFile fileprod;

	public void parseDepto() {
		if (filed != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;
				List<CiudadDTO> ciudades = new ArrayList<CiudadDTO>();

				bufferedReader = new BufferedReader(new InputStreamReader(filed.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					Long id = Long.valueOf(data[0].trim());
					String nombre = data[1].trim();
					String polygon = data[2].trim();

					departamentoService.crear(new DepartamentoDTO(id, nombre, polygon, ciudades));

				}

				logger.info("Departamentos ingresados: " + departamentoService.listar().size());

				FacesMessage message = new FacesMessage("Successful", filed.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}
	}

	public void parseCiudad() {
		if (filec != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;
				List<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();

				bufferedReader = new BufferedReader(new InputStreamReader(filec.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					Long id = Long.valueOf(data[0].trim());
					Long idDepto = Long.valueOf(data[1].trim());
					String nombre = data[2].trim();
					String polygon = data[3].trim();

					departamentoService.crearCiudad(new CiudadDTO(id, idDepto, nombre, polygon, localidades));

				}

				logger.info("Ciudades ingresadas");
				FacesMessage message = new FacesMessage("Successful", filec.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}

	}

	public void parseLocalidad() {
		if (filel != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;

				bufferedReader = new BufferedReader(new InputStreamReader(filel.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					Long id = Long.valueOf(data[0].trim());
					Long idCiudad = Long.valueOf(data[1].trim());
					Long idDepto = Long.valueOf(data[2].trim());
					String nombre = data[3].trim();
					String polygon = data[4].trim();

					departamentoService.crearLocalidad(new LocalidadDTO(id, idCiudad, idDepto, nombre, polygon));
				}

				logger.info("Localidades ingresadas");
				FacesMessage message = new FacesMessage("Successful", filel.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}
		}
	}

	public void parseAdministrador(){
		if (filea != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;

				bufferedReader = new BufferedReader(new InputStreamReader(filea.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					String nombre = data[0].trim();
					String correo = data[1].trim();
					String telefono = data[2].trim();
					String password = data[3].trim();

					usrSrv.crear(new AdministradorDTO(null, nombre, correo, password, telefono, correo, null));
				}

				logger.info("Administradores ingresadas");
				FacesMessage message = new FacesMessage("Successful", filea.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}
	}

	public void parseRestaurante() {
		if (filea != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;

				bufferedReader = new BufferedReader(new InputStreamReader(filer.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					String nombre = data[0].trim();
					String correo = data[1].trim();
					String telefono = data[3].trim();
					String password = data[2].trim();
					String rut = data[8].trim();
					LocalTime horarioApertura = LocalTime.parse(data[5].trim());
					LocalTime horarioCierre = LocalTime.parse(data[6].trim());
					Boolean abiertoAutom = true;
					String strareaentrega = data[4].trim();

					Long id = Long.valueOf(data[12].trim());
					Long idCiudad = Long.valueOf(data[13].trim());
					Long idDepto = Long.valueOf(data[14].trim());
					String calle = data[9].trim();
					String numero = data[10].trim();
					String geometry = data[11].trim();
					String id_imagen = data[7].trim();
					;

					LocalidadDTO localidad = departamentoService.localidadPorId(id, idCiudad, idDepto);
					DireccionDTO direccion = new DireccionDTO(null, "Restaurante", calle, numero, null, null, localidad,
							geometry, 0);

					RestauranteDTO restDTO = new RestauranteDTO(null, nombre, correo, password, telefono, correo, null,
							rut, EstadoRegistro.PENDIENTE, true, horarioApertura, horarioCierre, false, abiertoAutom,
							strareaentrega, direccion, id_imagen);

					usrSrv.crearRestaurante(restDTO);
				}

				logger.info("Restaurantes ingresados");
				FacesMessage message = new FacesMessage("Successful", filer.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}
	}

	public void parseCategoria() {
		if (filea != null) {
			BufferedReader bufferedReader = null;
			try {

				String linea;
				String[] data;

				bufferedReader = new BufferedReader(new InputStreamReader(filecat.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");
					String nombre = data[0].trim();

					srvCategoria.crear(new CategoriaCrearDTO(nombre));
				}

				bufferedReader.close();

				logger.info("Categorías ingresadas");
				FacesMessage message = new FacesMessage("Successful", filecat.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}
	}

	public void parseProducto() throws IOException, ParseException, AppettitException {
		if (filea != null) {
			BufferedReader bufferedReader = null;
			try {

				String correo = null;
				String nombre = null;
				String str_categoria = null;
				CategoriaDTO categoria = null;
				List<CategoriaDTO> categorias = srvCategoria.listar();
				Iterator<CategoriaDTO> it = categorias.iterator();

				bufferedReader = new BufferedReader(new InputStreamReader(fileprod.getInputStream(), "UTF-8"));

				logger.info("Producto: " + bufferedReader.readLine().toString());

				JSONTokener tokener = new JSONTokener(bufferedReader);
				JSONObject jsonObject = new JSONObject(tokener);

				JSONArray jsonArray = (JSONArray) jsonObject.get("restaurantes");

				for (Object res : jsonArray) {
					JSONObject data = (JSONObject) res;

					correo = (String) data.getString("id");
					RestauranteDTO restaurante = usrSrv.buscarPorCorreoRestaurante(correo);

					JSONArray productos = (JSONArray) data.get("articulos");

					for (Object art : productos) {
						JSONObject pdata = (JSONObject) art;
						nombre = (String) pdata.getString("nombre");
						str_categoria = (String) pdata.getString("categoria");

						while (it.hasNext()) {

							CategoriaDTO cDTO = it.next();

							if (cDTO.getNombre().equalsIgnoreCase(str_categoria)) {
								categoria = cDTO;
								break;
							}
						}

						ProductoDTO productoDTO = ProductoDTO.builder().id_categoria(categoria.getId())
								.id_restaurante(restaurante.getId()).nombre(nombre).build();
						;

						prodSrv.crear(productoDTO);

					}

				}

				bufferedReader.close();

				logger.info("Productos ingresadas");
				FacesMessage message = new FacesMessage("Successful", fileprod.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (Exception e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.info(e.getMessage().trim());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
				}
			}

		}
	}

}
