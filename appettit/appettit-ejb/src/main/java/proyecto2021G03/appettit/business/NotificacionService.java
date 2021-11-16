package proyecto2021G03.appettit.business;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import proyecto2021G03.appettit.dto.NotificacionDTO;
import proyecto2021G03.appettit.dto.NotificacionFirebaseDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Stateless
public class NotificacionService implements INotificacionService {

	static Logger logger = Logger.getLogger(NotificacionService.class);

	
	@Override
	@Asynchronous
	public void enviarNotificacionFirebase(String destinatario, String titulo, String mensaje)
			throws AppettitException {
		
		try {
			Client cliente = ClientBuilder.newClient();
			WebTarget target = cliente.target(Constantes.FIREBASE_FCM_URL);
			NotificacionDTO notificacion = NotificacionDTO.builder()
					.title(titulo)
					.body(mensaje)
					.build();
			NotificacionFirebaseDTO notificacionFirebase = NotificacionFirebaseDTO.builder()
					.to(destinatario)
					.notification(notificacion)
					.build();
			Response response = target.request(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON)
	                .header(HttpHeaders.AUTHORIZATION, "key=" + Constantes.FIREBASE_API_KEY)
	                .post(Entity.json(notificacionFirebase));
			if(response.getStatus() != 200) {
				logger.error("Error al enviar notificación por Firebase.");
			}
		} catch (Exception e) {
			throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		}


	}

}
