package proyecto2021G03.appettit.business;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import junit.framework.TestCase;
import proyecto2021G03.appettit.dto.NotificacionDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@RunWith(MockitoJUnitRunner.class)
public class NotificaionServiceTest extends TestCase {

	@Mock
	private Client client;
	
	@Mock
	private WebTarget webTarget;
	
	@Mock
	private Response response;
	
	@Mock
	private Builder mockBuilder;

	@InjectMocks
	// @Mock
	private NotificacionService notificacionService;

	@Before
	public void init() {
		// client = Mockito.mock(Client.class);
		// webTarget = Mockito.mock(WebTarget.class);
		// response = Mockito.mock(Response.class);
		notificacionService = new NotificacionService(client);

	}
/*
	@Test
	public void testenviarNotificacionFirebase() throws AppettitException {

					//notificacionService = new NotificacionService();
			
			NotificacionDTO notificacionFirebase = NotificacionDTO.builder()
					.title("")
					.body("")
					.build(); 
			
			Mockito.when(client.target(Constantes.FIREBASE_FCM_URL)).thenReturn(webTarget);
			Mockito.when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
			Mockito.when(mockBuilder.accept(MediaType.APPLICATION_JSON)).thenReturn(mockBuilder);
			Mockito.when(mockBuilder.header("Authorization", eq(Mockito.anyString()))).thenReturn(mockBuilder);
			Mockito.when(mockBuilder.post(Entity.entity(Mockito.anyString(), MediaType.TEXT_PLAIN), Response.class)).thenReturn(response);
			
			Mockito.when(webTarget.request(MediaType.APPLICATION_JSON)
					.post(Entity.json(notificacionFirebase))).thenReturn(response);
			
			Mockito.when(response.getStatus()).thenReturn(200);
			
			notificacionService.enviarNotificacionFirebase(Mockito.anyString(), Mockito.anyString(),
					Mockito.anyString());
		
	}
*/
	@Test(expected = AppettitException.class)
	public void testenviarNotificacionFirebase_err() throws AppettitException {

		notificacionService.enviarNotificacionFirebase("", "", "");
	}

}
