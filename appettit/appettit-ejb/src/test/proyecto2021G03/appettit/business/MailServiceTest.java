package proyecto2021G03.appettit.business;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import junit.framework.TestCase;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest extends TestCase {

	@InjectMocks
	private MailService mailServiceI;

	@Mock
	private Transport transport;

	@Mock
	private TransportListener listener;

	@Mock
	private Message message;

	@Mock
	private IUsuarioService mockIUsuarioService;

	@Before
	public void init() {
		mailServiceI = new MailService();
		mailServiceI.iUsuarioService = this.mockIUsuarioService;
	}

	@Test
	public void testSendMail() {
		Properties props = Constantes.MAIL_PROPS();
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constantes.USER_NAME, Constantes.USER_PASS);
			}
		});

		ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false,
				null, null);

		try {
			Mockito.when(mailServiceI.iUsuarioService.buscarPorCorreoCliente("mail@mail.com")).thenReturn(clienteDTO);
			mailServiceI.sendMail("mail@mail.com", "link");
		} catch (AppettitException e) {
			e.printStackTrace();
		}
	}

	@Test(expected = Exception.class)
	public void testSendMail_Exception() {
		mailServiceI.sendMail("mail", "link");
	}

}
