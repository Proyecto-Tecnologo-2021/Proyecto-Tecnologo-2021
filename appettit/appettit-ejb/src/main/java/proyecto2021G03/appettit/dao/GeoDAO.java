package proyecto2021G03.appettit.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

//import com.vividsolutions.jts.geom.Point;

import org.jboss.logging.Logger;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.entity.Restaurante;


@Singleton
public class GeoDAO implements IGeoDAO {

	static Logger logger = Logger.getLogger(GeoDAO.class);
	
	@EJB
	IDepartamentoDAO deptoDAO;
	
	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    	
	
	@Override
	public Localidad localidadPorPunto(String point) {
		Localidad localidad = null;

		try {
			Query consulta = em.createNativeQuery("select l.id, l.id_ciudad, l.id_departamento, l.nombre, l.geom "
					+ "from localidades l "
					+ "where st_contains(ST_GeometryFromText(l.geom), ST_GeometryFromText(:point)) = true");
			consulta.setParameter("point", point);
			
			Object[] data = (Object[]) consulta.getSingleResult();
			
			localidad = new Localidad(Long.valueOf(data[0].toString()), 
					Long.valueOf(data[1].toString()), 
					Long.valueOf(data[2].toString()),
					data[3].toString(),
					deptoDAO.ciudadPorId(Long.valueOf(data[1].toString()), Long.valueOf(data[2].toString())),
					data[4].toString());
			
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return localidad;
	}

	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(String point) {
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
	public List<Menu> menuRestaurantesPorPunto(String point) {
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
	public List<Promocion> promocionRestaurantesPorPunto(String point) {
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
