package proyecto2021G03.appettit.business;

import com.vividsolutions.jts.io.ParseException;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ITokenService {
//	String tokenGenerator(MailDTO correo);
	Boolean tokenVerificator(String jwt);
	String tokenGetClaim(String jwt, String claim) throws Exception;
}
