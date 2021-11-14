package proyecto2021G03.appettit.business;

import javax.ejb.Local;

@Local
public interface IMailService {
	void sendMail(String mail, String link);
}
