package proyecto2021G03.appettit.business;

import javax.ejb.Local;

import proyecto2021G03.appettit.exception.AppettitException;

@Local
public interface INotificacionService {
	
	public void enviarNotificacionFirebase(String destinatario, String titulo, String mensaje) throws AppettitException;
}
