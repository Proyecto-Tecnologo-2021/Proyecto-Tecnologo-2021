package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.ClienteDTO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest extends TestCase {

    @InjectMocks
    private MailService mailServiceI;

    //@Mock
    //private Session mockSession;
    
    @Mock
    private Transport transport;

    @Mock
    private Message message;
    
    @Mock
    private IUsuarioService mockIUsuarioService;

    @Before
    public void init(){
        mailServiceI = new MailService();
        mailServiceI.iUsuarioService = this.mockIUsuarioService;
    }

   @Test
    public void testSendMail() {
	   /*
         DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);

        Session mockSession = Mockito.mock(Session.class);
        when(mockSession.finalMethod()).thenReturn(1);
        Properties props = Constantes.MAIL_PROPS();
         
        
        try {
            Mockito.when(mailServiceI.iUsuarioService.buscarPorCorreoCliente("mail@mail.com")).thenReturn(clienteDTO);
            
            Mockito.doNothing().when(transport).connect();
            Mockito.doNothing().when(transport).close();
            //Mockito.when(mockSession.getTransport(Mockito.anyString())).thenReturn(transport);
            Mockito.when(Session.getInstance(props)).thenReturn(mockSession);

            Message message = new MimeMessage(mockSession);
            Mockito.doNothing().when(transport).sendMessage(Mockito.eq(message), Mockito.any());
            
            mailServiceI.sendMail("mail@mail.com", "link");
        } catch (AppettitException | MessagingException e) {
            e.printStackTrace();
        }
    }
   
   */
   
}
