package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

//import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.entity.Restaurante;

@Local
public interface IGeoDAO {

	public Localidad localidadPorPunto(String point);
	public List<Restaurante> repartoRestaurantesPorPunto(String point);
	public List<Menu> menuRestaurantesPorPunto(String point);
	public List<Promocion> promocionRestaurantesPorPunto(String point);
}
