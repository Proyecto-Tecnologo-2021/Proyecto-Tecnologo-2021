package proyecto2021G03.appettit.business;

import javax.ejb.Local;

import com.vividsolutions.jts.io.ParseException;

import proyecto2021G03.appettit.dto.LocalidadDTO;
import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface IGeoService {

	public LocalidadDTO localidadPorPunto(String point) throws AppettitException, ParseException;
}
