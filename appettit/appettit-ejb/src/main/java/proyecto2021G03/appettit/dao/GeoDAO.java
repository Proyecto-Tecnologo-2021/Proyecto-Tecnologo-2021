package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import com.vividsolutions.jts.geom.Point;

import org.jboss.logging.Logger;

//Geometry Object is from the following import
import com.vividsolutions.jts.geom.*;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Restaurante;

@Singleton
public class GeoDAO implements IGeoDAO {

	static Logger logger = Logger.getLogger(GeoDAO.class);
	
	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    
	
	@Override
	public Localidad localidadPorPunto(Point point) {
		 
		Localidad localidad =  em.createQuery("select l "
				+ "from Localidad l "
				+ "where contains(l.geometry, :point) = true", Localidad.class)
				.setParameter("point", (com.vividsolutions.jts.geom.Point) point)
				.getSingleResult();
		
		
		return localidad;
	}

	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

}
