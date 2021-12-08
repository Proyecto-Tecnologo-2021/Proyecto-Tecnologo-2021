package proyecto2021G03.appettit.business;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import junit.framework.TestCase;
import proyecto2021G03.appettit.exception.AppettitException;

@RunWith(MockitoJUnitRunner.class)
public class NotificaionServiceTest extends TestCase {

	@Mock
	private Client client;
	@Mock
	private WebTarget webTarget;
	@Mock
	private Request requestEntity;
	@Mock
	private Response response;
	
	
	@InjectMocks
	private NotificacionService notificacionService;

	@Before
	public void init() {
		client = Mockito.mock(Client.class);
		webTarget = Mockito.mock(WebTarget.class);
		response = Mockito.mock(Response.class);
		notificacionService = new NotificacionService(client);
		
	}

	
	@Test(expected = AppettitException.class)
	public void testenviarNotificacionFirebase_err() throws AppettitException {

		notificacionService.enviarNotificacionFirebase("", "", "");
	}

}
