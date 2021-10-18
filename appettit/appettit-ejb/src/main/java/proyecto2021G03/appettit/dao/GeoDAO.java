package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Restaurante;

@Singleton
public class GeoDAO implements IGeoDAO {

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    
	
	@Override
	public Localidad localidadPorPunto(Point point) {
		String strpoint = "POINT(" + point.getX() + " " + point.getY() +")";
		
		Query consulta = em.createNativeQuery("SELECT _l.* "
				+ " FROM localidades _l"
				+ " WHERE ST_Contains(geom,ST_GeomFromText('" + strpoint + "', 32721))");
		
		
		Localidad localidad = (Localidad) consulta.getResultList().get(0);
		
		return localidad;
	}

	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

}
