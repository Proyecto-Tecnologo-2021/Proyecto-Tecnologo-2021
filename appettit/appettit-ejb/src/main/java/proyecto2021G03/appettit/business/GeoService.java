package proyecto2021G03.appettit.business;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.converter.GeoConverter;
import proyecto2021G03.appettit.converter.LocalidadConverter;
import proyecto2021G03.appettit.dao.IGeoDAO;
import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class GeoService implements IGeoService {

	@EJB
	IGeoDAO geoSrv;
	
	@EJB
	LocalidadConverter locConverter;
	
	@EJB
	GeoConverter gConverter;
	
	@Override
	public LocalidadDTO localidadPorPunto(String point) throws AppettitException, ParseException {
		
		return locConverter.fromEntity(geoSrv.localidadPorPunto(point));
	}

}
