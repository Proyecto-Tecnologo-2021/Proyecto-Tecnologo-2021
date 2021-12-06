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

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest extends TestCase {

    @InjectMocks
    private MailService mailServiceI;

    @Mock
    private IUsuarioService mockIUsuarioService;

    @Before
    public void init(){
        mailServiceI = new MailService();
        mailServiceI.iUsuarioService = this.mockIUsuarioService;
    }

   @Test
    public void testSendMail() {
/*         DireccionDTO direccionDTO = new DireccionDTO(3L, "alias", "calle", "1234", "apartamento", "referencias", null, "-34.8844477,-56.1922389", 3);
        List<DireccionDTO> direccionesDto = new ArrayList<DireccionDTO>();
        direccionesDto.add(direccionDTO);
        ClienteDTO clienteDTO = new ClienteDTO(1L, "nombre", "usename", "pwd", "1234", "mail@mail.com", "token", false, direccionesDto, null);

        try {
            Mockito.when(mailServiceI.iUsuarioService.buscarPorCorreoCliente("mail@mail.com")).thenReturn(clienteDTO);
            mailServiceI.sendMail("mail@mail.com", "link");
        } catch (AppettitException e) {
            e.printStackTrace();
        }*/
    }
}
