package proyecto2021G03.appettit.business;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

import java.util.Date;

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
        String token = "falso";
        Boolean esperado = false;

        Boolean obtenido = tokenServiceI.tokenVerificator(token);
        assertEquals(esperado,obtenido);
    }

    @Test
    public void testTokenVerificator_vacio() {
        String token = "";
        Boolean esperado = false;

        Boolean obtenido = tokenServiceI.tokenVerificator(token);
        assertEquals(esperado,obtenido);
    }

    @Test
    public void testTokenVerificator_vencido() {
        long timestamp = System.currentTimeMillis();
        String token = Jwts
				.builder()
				.setSubject("JWTTEST")
				.setIssuedAt(new Date(timestamp))
				.setExpiration(new Date(timestamp - (10)))
				.claim("userMail", "mail@mail.com")
				.signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY).compact();
        Boolean esperado = false;

        Boolean obtenido = tokenServiceI.tokenVerificator(token);
        assertEquals(esperado,obtenido);
    }

    @Test
    public void testTokenVerificator_valido() {
        long timestamp = System.currentTimeMillis();
        String token = Jwts
                .builder()
                .setSubject("JWTTEST")
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + (1*60*60*1000)))
                .claim("userMail", "mail@mail.com")
                .signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY).compact();
        Boolean esperado = true;

        Boolean obtenido = tokenServiceI.tokenVerificator(token);
        assertEquals(esperado,obtenido);
    }

    @Test
    public void testTokenGetClaim() {
        long timestamp = System.currentTimeMillis();
        String jwt = Jwts
                .builder()
                .setSubject("JWTTEST")
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + (1*60*60*1000)))
                .claim("userMail", "mail@mail.com")
                .signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY).compact();
        String esperado = "mail@mail.com";

        try {
            String obtenido = tokenServiceI.tokenGetClaim(jwt, "userMail");
            assertEquals(esperado, obtenido);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = Exception.class)
    public void testTokenGetClaim_err() throws Exception {
        String jwt = "pepe";
        tokenServiceI.tokenGetClaim(jwt, "userMail");
    }
}