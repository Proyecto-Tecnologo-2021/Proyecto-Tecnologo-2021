package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import org.hibernate.engine.config.spi.ConfigurationService.Converter;
import org.postgresql.util.GT;

@Singleton
public class PGgeometryConverter implements Converter<GT> {

	@Override
	public GT convert(Object value) {
		// TODO Auto-generated method stub
		return null;
	}




}
