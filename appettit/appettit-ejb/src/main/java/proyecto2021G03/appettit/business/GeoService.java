package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.vividsolutions.jts.geom.Point;

import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class GeoService implements IGeoService {

	@EJB
	IGeoDAO geoSrv;
	
	@EJB
	LocalidadConverter locConverter;
	
	@Override
	public LocalidadDTO localidadPorPunto(Point point) throws AppettitException {
		return locConverter.fromEntity(geoSrv.localidadPorPunto(point));
	}

	@Override
	public List<RestauranteDTO> repartoRestaurantesPorPunto(Point point) throws AppettitException {
		// TODO Auto-generated method stub
		return null;
	}

}
