package proyecto2021G03.appettit.business;

import io.jsonwebtoken.*;
import proyecto2021G03.appettit.dto.MailDTO;
import proyecto2021G03.appettit.util.Constantes;
import java.util.Date;
import javax.ejb.Stateless;

@Stateless
public class TokenService implements ITokenService {


//	@Override
//	public String tokenGenerator(MailDTO correo){
//		//TIMESTAMP DE AHORA PARA CALCULAR EXPIRACION
//		long timestamp = System.currentTimeMillis();
//		//GENERO JWT
//		String jwt = Jwts
//				.builder()
//				.setSubject("JWTTEST")
//				.setIssuedAt(new Date(timestamp))
//				.setExpiration(new Date(timestamp + (1*60*60*1000))) // + 1 HORA (EN MILISEGUNDOS)
//				.claim("userMail", correo.getCorreo())    //AGREGO MAIL DE USUARIO AL QUE SE LE CAMBIARA LA PASS
//				.signWith(SignatureAlgorithm.HS512, Constantes.JWT_KEY).compact();
//		return jwt;
//	}

	public Boolean tokenVerificator(String jwt){
		//VERIFICO ESTADO DEL TOKEN (VALIDEZ, EXPIRACION, ETC)
		try{
			Jwts.parser().setSigningKey(Constantes.JWT_KEY).parseClaimsJws(jwt).getBody();
			System.out.println("El token es válido");
			return true;
		}catch(ExpiredJwtException e){
			System.out.println("El token ha expirado");
			return false;
		}catch(MalformedJwtException e){
			System.out.println("El token no es valido");
			return false;
		}catch(Exception e){
			System.out.println("Algo ha salido mal");
			return false;
		}
	}

	public String tokenGetClaim(String jwt, String claim) throws Exception {
		//OBTENGO CLAIM DE UN JWT
		if(tokenVerificator(jwt)){
			Claims claims = Jwts.parser().setSigningKey(Constantes.JWT_KEY).parseClaimsJws(jwt).getBody();
			return claims.get(claim).toString();
		}else
			throw new Exception("Error");
	}
}




