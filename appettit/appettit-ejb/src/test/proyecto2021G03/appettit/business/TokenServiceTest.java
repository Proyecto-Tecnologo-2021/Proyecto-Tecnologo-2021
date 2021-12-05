package proyecto2021G03.appettit.business;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TokenServiceTest extends TestCase {

    @InjectMocks
    private TokenService tokenServiceI;

    @Before
    public void init(){
        tokenServiceI = new TokenService();
    }

    @Test
    public void testTokenVerificator() {
    }

    public void testTokenGetClaim() {
    }
}