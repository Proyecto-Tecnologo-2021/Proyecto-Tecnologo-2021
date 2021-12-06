package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class NotificacionServiceTest extends TestCase {

    @InjectMocks
    private NotificacionService notificacionServiceI;

    @Before
    public void init(){
        notificacionServiceI = new NotificacionService();
    }

    @Test
    public void testEnviarNotificacionFirebase() {
        try {
            notificacionServiceI.enviarNotificacionFirebase("destinatario", "titulo", "mensaje");
        } catch (AppettitException e) {
            e.printStackTrace();
        }
    }
}