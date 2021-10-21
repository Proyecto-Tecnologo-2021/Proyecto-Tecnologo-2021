package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.vividsolutions.jts.geom.Point;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.entity.Restaurante;

@Singleton
public class GeoDAO implements IGeoDAO {

	static Logger logger = Logger.getLogger(GeoDAO.class);
	
	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    
	
	@Override
	public Localidad localidadPorPunto(Point point) {
		Localidad localidad = null;
		//try {
			localidad =  em.createQuery("select l "
					+ "from Localidad l "
					+ "where contains(l.geometry, :point) = true", Localidad.class)
					.setParameter("point", point)
					.getSingleResult();
			
		///} catch (Exception e) {
			//logger.error(e.getLocalizedMessage());
			logger.error(point.getGeometryType());
			logger.error(point.getSRID());
			logger.error(point.getCoordinates().toString());
		
			
		//}
			
		
		
		return localidad;
	}

	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(Point point) {
		List<Restaurante> restaurantes = new ArrayList<Restaurante>();
		
		try {
			restaurantes =  em.createQuery("select r "
					+ "from Restaurante r "
					+ "where contains(r.areaentrega, :point) = true", Restaurante.class)
					.setParameter("point", point)
					.getResultList();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
			
		
		return restaurantes;
	}

	@Override
	public List<Menu> menuRestaurantesPorPunto(Point point) {
		List<Menu> menus = new ArrayList<Menu>();
		
		try {
			menus =  em.createQuery("select m "
					+ "from Menu m "
					+ "inner join m.restaurante "
					+ "where contains(m.restaurante.areaentrega, :point) = true", Menu.class)
					.setParameter("point", point)
					.getResultList();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return menus;
	}

	@Override
	public List<Promocion> promocionRestaurantesPorPunto(Point point) {
		List<Promocion> promociones = new ArrayList<Promocion>();
		
		try {
			promociones =  em.createQuery("select p "
					+ "from Promocion p "
					+ "inner join p.restaurante "
					+ "where contains(m.restaurante.areaentrega, :point) = true", Promocion.class)
					.setParameter("point", point)
					.getResultList();
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return promociones;	
	}

}
