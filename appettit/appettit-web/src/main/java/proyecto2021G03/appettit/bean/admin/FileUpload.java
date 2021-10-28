package proyecto2021G03.appettit.bean.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
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
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import lombok.Getter;
import lombok.Setter;
import proyecto2021G03.appettit.business.ICategoriaService;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IExtraMenuService;
import proyecto2021G03.appettit.business.IGeoService;
import proyecto2021G03.appettit.business.IMenuService;
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
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
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

	@EJB
	IExtraMenuService extraMenuSrv;

	@EJB
	IMenuService menuSrv;

	private UploadedFile filed;
	private UploadedFile filec;
	private UploadedFile filel;
	private UploadedFile filea;
	private UploadedFile filer;
	private UploadedFile filecat;
	private UploadedFile fileprod;
	private UploadedFile filemenu;
	private UploadedFile fileextramenu;

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

	public void parseAdministrador() {
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
		if (filer != null) {
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
		if (filecat != null) {
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

	public void parseProducto() {
		if (fileprod != null) {
			BufferedReader bufferedReader = null;
			try {
				String linea;
				String[] data;

				List<CategoriaDTO> categorias = srvCategoria.listar();

				bufferedReader = new BufferedReader(new InputStreamReader(fileprod.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");

					String correo = data[0].trim();
					String nombre = data[1].trim();
					String str_categoria = data[2].trim();

					CategoriaDTO categoria = null;

					RestauranteDTO restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
					Iterator<CategoriaDTO> it = categorias.iterator();

					while (it.hasNext()) {

						CategoriaDTO cDTO = it.next();

						if (cDTO.getNombre().equalsIgnoreCase(str_categoria)) {
							categoria = cDTO;
							break;
						}
					}

					ProductoDTO productoDTO = ProductoDTO.builder().id_categoria(categoria.getId())
							.id_restaurante(restaurante.getId()).nombre(nombre).build();

					productoDTO = prodSrv.crear(productoDTO);
				}

				bufferedReader.close();

				logger.info("Productos ingresados");
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

	public void parseExtraMenu() {
		if (fileextramenu != null) {
			BufferedReader bufferedReader = null;
			try {
				String linea;
				String[] data;

				bufferedReader = new BufferedReader(new InputStreamReader(fileextramenu.getInputStream(), "UTF-8"));

				while ((linea = bufferedReader.readLine()) != null) {
					data = linea.split(";");

					String correo = data[0].trim();
					String nombre = data[1].trim();
					Double precio = Double.valueOf(data[2].trim());

					ProductoDTO producto = null;

					RestauranteDTO restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
					List<ProductoDTO> productos = prodSrv.listarPorRestaurante(restaurante.getId());
					Iterator<ProductoDTO> it = productos.iterator();

					while (it.hasNext()) {

						ProductoDTO pDTO = it.next();

						if (pDTO.getNombre().equalsIgnoreCase(nombre)) {
							producto = pDTO;
							break;
						}
					}

					ExtraMenuDTO extraMenuDTO = ExtraMenuDTO.builder()
							.id_producto(producto.getId())
							.id_restaurante(restaurante.getId())
							.producto(producto)
							.precio(precio).build();

					extraMenuDTO = extraMenuSrv.crear(extraMenuDTO);
				}

				bufferedReader.close();

				logger.info("Extra Productos ingresados");
				FacesMessage message = new FacesMessage("Successful", fileextramenu.getFileName() + " is uploaded.");
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

	public void parseMenu() {
		if (filemenu != null) {
			BufferedReader bufferedReader = null;
			try {

				bufferedReader = new BufferedReader(new InputStreamReader(filemenu.getInputStream(), "UTF-8"));

				String linea;
				String strJson = "";
				while ((linea = bufferedReader.readLine()) != null) {
					strJson += linea;
				}

				logger.info("Menu: " + strJson);
				String correo = null;
				String nombre = null;
				String descripcion = null;
				Double precioSimple = null;
				Double precioTotal = null;
				String id_imagen = null;
				List<ProductoDTO> productos = new ArrayList<ProductoDTO>();
				List<ExtraMenuDTO> extras = new ArrayList<ExtraMenuDTO>();

				JSONObject jsonObject = new JSONObject(strJson);

				if (jsonObject != null) {
					logger.info(jsonObject.toString());

					JSONArray jsonArray = jsonObject.getJSONArray("menus");

					for (Object res : jsonArray) {
						JSONObject data = (JSONObject) res;

						correo = (String) data.get("restaurante");
						RestauranteDTO restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
						productos = prodSrv.listarPorRestaurante(restaurante.getId());
						extras = extraMenuSrv.listarPorRestaurante(restaurante.getId());

						JSONArray menus = data.getJSONArray("menus");

						for (Object menu : menus) {
							JSONObject omenu = (JSONObject) menu;

							nombre = omenu.getString("nombre");
							descripcion = omenu.getString("descripcion");
							precioSimple = omenu.getDouble("preciosimple");
							precioTotal = omenu.getDouble("preciototal");
							id_imagen = omenu.getString("imagen");

							JSONArray aprod = omenu.getJSONArray("productos");
							JSONArray aextra = omenu.getJSONArray("extras");
							List<ProductoDTO> m_productos = new ArrayList<ProductoDTO>();
							List<ExtraMenuDTO> m_extras = new ArrayList<ExtraMenuDTO>();


							for (Object prod : aprod) {
								String pname = (String) prod;

								Iterator<ProductoDTO> it = productos.iterator();

								while (it.hasNext()) {
									ProductoDTO pDTO = it.next();
									if (pDTO.getNombre().equalsIgnoreCase(pname)) {
										m_productos.add(pDTO);
										break;
									}
								}

							}

							for (Object oextra : aextra) {
								String pextra = (String) oextra;

								Iterator<ExtraMenuDTO> it = extras.iterator();

								while (it.hasNext()) {
									ExtraMenuDTO eDTO = it.next();
									if (eDTO.getProducto().getNombre().equalsIgnoreCase(pextra)) {
										m_extras.add(eDTO);
										break;
									}
								}

							}

							try {
								MenuDTO mDTO = new MenuDTO(null, restaurante.getId(), nombre, restaurante, descripcion,
										precioSimple, precioTotal, m_productos, m_extras, id_imagen, null);
								mDTO = menuSrv.crear(mDTO);
							} catch (AppettitException e) {
								logger.info(e.getMessage().trim());
								FacesContext.getCurrentInstance().addMessage(null,
										new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
							}
						}

					}

				}

				bufferedReader.close();

				logger.info("Productos ingresados");
				FacesMessage message = new FacesMessage("Successful", filemenu.getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);

			} catch (IOException e) {
				logger.info(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			} catch (AppettitException e) {
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
