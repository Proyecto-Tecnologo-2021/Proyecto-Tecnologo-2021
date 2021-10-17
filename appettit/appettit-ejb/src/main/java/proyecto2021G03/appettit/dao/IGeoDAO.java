package proyecto2021G03.appettit.dao;

import java.util.List;

import javax.ejb.Local;

import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.entity.Localidad;
import proyecto2021G03.appettit.entity.Restaurante;

@Local
public interface IGeoDAO {

	public Localidad localidadPorPunto(Point point);
	public List<Restaurante> repartoRestaurantesPorPunto(Point point);
}
