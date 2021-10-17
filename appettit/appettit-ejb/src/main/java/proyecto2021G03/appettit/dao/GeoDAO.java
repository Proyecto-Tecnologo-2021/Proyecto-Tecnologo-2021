package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Restaurante;

@Singleton
public class GeoDAO implements IGeoDAO {

	@PersistenceContext(name = "Proyecto2021G03")
	private EntityManager em;	
    
	
	@Override
	public Localidad localidadPorPunto(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Restaurante> repartoRestaurantesPorPunto(Point point) {
		// TODO Auto-generated method stub
		return null;
	}

}
