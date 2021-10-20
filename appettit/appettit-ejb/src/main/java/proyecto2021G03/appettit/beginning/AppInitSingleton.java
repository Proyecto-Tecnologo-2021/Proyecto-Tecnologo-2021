package proyecto2021G03.appettit.beginning;

import java.io.Serializable;
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
		String polygon = "MULTIPOLYGON(((743238 2967416,743238 2967450,743265 2967450,743265.625 2967416,743238 2967416)))";
		String point = "POINT(743238 2967416)";
		fromText = new WKTReader(new GeometryFactory(new PrecisionModel(), 32721));
		Geometry geom = null;
		Geometry p_geom = null;
		multiPolygon = null;
		List<CiudadDTO> ciudades = new ArrayList<CiudadDTO>();
		
		try {

			geom = fromText.read(polygon);
			multiPolygon = (com.vividsolutions.jts.geom.MultiPolygon) geom;
			DepartamentoDTO departamentoDTO = new DepartamentoDTO((long) 1001, "MONTEVIDEO", multiPolygon, ciudades);
			DepartamentoDTO departamentoDTO2 = new DepartamentoDTO((long) 1002, "MONTEVIDEO", multiPolygon, ciudades);
			departamentoService.crear(departamentoDTO);
			departamentoService.crear(departamentoDTO2);
			
			List<DepartamentoDTO> deptoDTO =  departamentoService.buscarPorNombre("MONTEVIDEO");
			
			if(deptoDTO !=null) {
				logger.info("Departamentos: " + deptoDTO.size()); //	
			} else {
				logger.info("No existen Localidades"); //
			}
			
			
			p_geom = fromText.read(point);
			gpoint = (com.vividsolutions.jts.geom.Point) p_geom;
			
			LocalidadDTO ldto = geoSrv.localidadPorPunto(gpoint); 
			
			if(ldto != null) {
				logger.info("Localidad: " + ldto.getNombre()); //
			}else {
				logger.info("No existen Localidades"); //
			}
			

		} catch (ParseException e) {
			logger.error(e.getMessage()); //

		} catch (AppettitException e) {
			logger.error(e.getLocalizedMessage()); //
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage()); //
		}

	}
}
