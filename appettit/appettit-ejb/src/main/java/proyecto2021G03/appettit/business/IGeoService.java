package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.Local;

import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IGeoService {

	public LocalidadDTO localidadPorPunto(Point point) throws AppettitException;
	public List<RestauranteDTO> repartoRestaurantesPorPunto(Point point) throws AppettitException;
}
