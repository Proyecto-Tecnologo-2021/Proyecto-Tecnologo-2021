package proyecto2021G03.appettit.beginning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.jboss.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import proyecto2021G03.appettit.beginning.AppInitSingleton;
import proyecto2021G03.appettit.business.IGeoService;
import proyecto2021G03.appettit.converter.DepartamentoConverter;
import proyecto2021G03.appettit.dto.LocalidadDTO;

import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.dto.CiudadDTO;
import proyecto2021G03.appettit.dto.DepartamentoDTO;
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

	WKTReader fromText;
	Geometry geom;
	MultiPolygon multiPolygon;
	Point gpoint;

	@PostConstruct
	public void init() {

		/*
		 * String polygon =
		 * "MULTIPOLYGON(((743238 2967416,743238 2967450,743265 2967450,743265.625 2967416,743238 2967416)))"
		 * ; String point = "POINT(743238 2967416)"; fromText = new WKTReader(new
		 * GeometryFactory(new PrecisionModel(), 32721)); Geometry geom = null; Geometry
		 * p_geom = null; multiPolygon = null; List<CiudadDTO> ciudades = new
		 * ArrayList<CiudadDTO>();
		 * 
		 * try {
		 * 
		 * geom = fromText.read(polygon); multiPolygon =
		 * (com.vividsolutions.jts.geom.MultiPolygon) geom; DepartamentoDTO
		 * departamentoDTO = new DepartamentoDTO((long) 1001, "MONTEVIDEO",
		 * multiPolygon, ciudades); DepartamentoDTO departamentoDTO2 = new
		 * DepartamentoDTO((long) 1002, "MONTEVIDEO", multiPolygon, ciudades);
		 * departamentoService.crear(departamentoDTO);
		 * departamentoService.crear(departamentoDTO2);
		 * 
		 * List<DepartamentoDTO> deptoDTO =
		 * departamentoService.buscarPorNombre("MONTEVIDEO");
		 * 
		 * if(deptoDTO !=null) { logger.info("Departamentos: " + deptoDTO.size()); // }
		 * else { logger.info("No existen Localidades"); // }
		 * 
		 * 
		 * p_geom = fromText.read(point); gpoint = (com.vividsolutions.jts.geom.Point)
		 * p_geom;
		 * 
		 * LocalidadDTO ldto = geoSrv.localidadPorPunto(gpoint);
		 * 
		 * if(ldto != null) { logger.info("Localidad: " + ldto.getNombre()); }else {
		 * logger.info("No existen Localidades"); }
		 * 
		 * 
		 * } catch (ParseException e) { logger.error(e.getMessage());
		 * 
		 * } catch (AppettitException e) { logger.error(e.getLocalizedMessage()); }
		 * catch (Exception e) { logger.error(e.getLocalizedMessage()); }
		 */
		fromText = new WKTReader(new GeometryFactory(new PrecisionModel(), 32721));

		try {
			
			parseDepto();
			
			parseCiudad();

			parseLocalidad();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

	}

	private void parseDepto() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		List<CiudadDTO> ciudades = new ArrayList<CiudadDTO>();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader
				.getResource("META-INF/gis/departamentos.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			String nombre = data[1].trim();
			String polygon = data[2].trim();

			geom = fromText.read(polygon);
			multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) geom;

			departamentoService.crear(new DepartamentoDTO(id, nombre, multiPolygon, ciudades));

		}

		bufferedReader.close();
		logger.info("Departamentos ingresados: " + departamentoService.listar().size());
	}

	private void parseCiudad() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		List<LocalidadDTO> localidades = new ArrayList<LocalidadDTO>();
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader
				.getResource("META-INF/gis/ciudades.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			Long idDepto = Long.valueOf(data[1].trim());
			String nombre = data[2].trim();
			String polygon = data[3].trim();

			geom = fromText.read(polygon);
			multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) geom;

			departamentoService.crearCiudad(new CiudadDTO(id, idDepto, nombre, multiPolygon, localidades));

		}

		bufferedReader.close();
		logger.info("Ciudades ingresadas");
	}

	private void parseLocalidad() throws IOException, ParseException, AppettitException {

		String linea;
		String[] data;
		
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL resource = classLoader
				.getResource("META-INF/gis/localidades.csv");
		File deptoFile = new File(resource.getFile());

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(deptoFile), "UTF-8"));

		while ((linea = bufferedReader.readLine()) != null) {
			data = linea.split(";");
			Long id = Long.valueOf(data[0].trim());
			Long idCiudad = Long.valueOf(data[1].trim());
			Long idDepto = Long.valueOf(data[2].trim());
			String nombre = data[3].trim();
			String polygon = data[4].trim();

			geom = fromText.read(polygon);
			multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) geom;

			departamentoService.crearLocalidad(new LocalidadDTO(id, idCiudad, idDepto, nombre, multiPolygon));

		}

		bufferedReader.close();
		
		logger.info("Localidades ingresadas");
	}

}
