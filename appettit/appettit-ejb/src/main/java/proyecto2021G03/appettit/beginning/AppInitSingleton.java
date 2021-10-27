package proyecto2021G03.appettit.beginning;

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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

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

@Startup
@Singleton
public class AppInitSingleton implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AppInitSingleton.class);

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
	

	WKTReader fromText;
	Geometry geom;
	MultiPolygon multiPolygon;
	Point gpoint;

	@PostConstruct
	public void init() {

		
		fromText = new WKTReader(new GeometryFactory(new PrecisionModel(), 32721));

		try {
			
			/*
			parseDepto();
			
			parseCiudad();

			parseLocalidad();
			
			parseAdministrador();
			
			parseRestaurante();
			
			parseCategoria();
			
			parseProducto();
			*/
			
			/*
			LocalidadDTO ldto = geoSrv.localidadPorPunto("POINT(575052.1054146929 6140591.11704534)");
			
			if(ldto != null) { 
				logger.info("Localidad: " + ldto.getNombre()); 
			}else {
				logger.info("No existen Localidades"); 
			}		
					
			*/
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

	}

	private void parseDepto() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		List<CiudadDTO> ciudades = new ArrayList<CiudadDTO>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/departamentos.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			String nombre = data[1].trim();
			String polygon = data[2].trim();

			geom = fromText.read(polygon);
			multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) geom;

			departamentoService.crear(new DepartamentoDTO(id, nombre, polygon, ciudades));

		}

		bufferedReader.close();
		logger.info("Departamentos ingresados: " + departamentoService.listar().size());
	}

	private void parseCiudad() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		List<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/ciudades.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			Long idDepto = Long.valueOf(data[1].trim());
			String nombre = data[2].trim();
			String polygon = data[3].trim();

			departamentoService.crearCiudad(new CiudadDTO(id, idDepto, nombre, polygon, localidades));

		}

		bufferedReader.close();
		logger.info("Ciudades ingresadas");
	}

	private void parseLocalidad() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/localidades.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			Long idCiudad = Long.valueOf(data[1].trim());
			Long idDepto = Long.valueOf(data[2].trim());
			String nombre = data[3].trim();
			String polygon = data[4].trim();

			departamentoService.crearLocalidad(new LocalidadDTO(id, idCiudad, idDepto, nombre, polygon));
		}

		bufferedReader.close();

		logger.info("Localidades ingresadas");
	}

	private void parseAdministrador() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/administradores.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			String nombre = data[0].trim();
			String correo = data[1].trim();
			String telefono = data[2].trim();
			String password = data[3].trim();
			
			
			usrSrv.crear(new AdministradorDTO(null, nombre, correo, password, telefono, correo, null));
		}

		bufferedReader.close();

		logger.info("Administradores ingresadas");
	}

	
	private void parseRestaurante() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/restaurantes.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

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
			String id_imagen = data[7].trim();;
			
			LocalidadDTO localidad = departamentoService.localidadPorId(id, idCiudad, idDepto);
			DireccionDTO direccion = new DireccionDTO(null, "Restaurante", calle, numero, null, null, localidad, geometry, 0);
			
			
			
			RestauranteDTO restDTO = new RestauranteDTO(null, nombre, correo, password, telefono, correo, null, rut,
					EstadoRegistro.PENDIENTE, true, horarioApertura, horarioCierre, false, abiertoAutom, strareaentrega, direccion,
					id_imagen);

			usrSrv.crearRestaurante(restDTO);
		}

		bufferedReader.close();

		logger.info("Restaurantes ingresados");
	}

	private void parseCategoria() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/categorias.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			String nombre = data[0].trim();

			srvCategoria.crear(new CategoriaCrearDTO(nombre));
		}

		bufferedReader.close();

		logger.info("Categorías ingresadas");
	}

	private void parseProducto() throws IOException, ParseException, AppettitException {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader.getResource("META-INF/input/productos.json");
		File deptoFile = new File(resource.getFile());
		
		String correo = null;
		String nombre = null;
		String str_categoria = null;
		CategoriaDTO categoria = null;
		List<CategoriaDTO> categorias = srvCategoria.listar();
		Iterator<CategoriaDTO> it = categorias.iterator();

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));
		
		logger.info("Producto: " + bufferedReader.readLine().toString());
		
		JSONTokener tokener = new JSONTokener(bufferedReader);
		JSONObject jsonObject = new JSONObject(tokener);
		
		JSONArray jsonArray = (JSONArray) jsonObject.get("restaurantes");
		
		for (Object res: jsonArray) {
			JSONObject data = (JSONObject) res;
			
			correo = (String) data.getString("id");
			RestauranteDTO restaurante = usrSrv.buscarPorCorreoRestaurante(correo);
			
			JSONArray productos = (JSONArray) data.get("articulos");
			
			for(Object art : productos) {
				JSONObject pdata = (JSONObject) art;
				nombre =  (String) pdata.getString("nombre");
				str_categoria =  (String) pdata.getString("categoria");
				
				
				while (it.hasNext()) {
					
					CategoriaDTO cDTO = it.next();
					
					if(cDTO.getNombre().equalsIgnoreCase(str_categoria)) {
						categoria = cDTO;
						break;
					}
				}
				
				ProductoDTO productoDTO = ProductoDTO.builder()
						.id_categoria(categoria.getId())
						.id_restaurante(restaurante.getId())
						.nombre(nombre).build();;
						
				prodSrv.crear(productoDTO );
				
			}
			
		}
		
		
		bufferedReader.close();

		logger.info("Productos ingresadas");
	}

}
