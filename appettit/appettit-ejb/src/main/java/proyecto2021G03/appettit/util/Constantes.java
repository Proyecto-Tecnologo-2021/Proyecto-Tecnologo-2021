package proyecto2021G03.appettit.util;

import java.util.Properties;


public class Constantes {
	
	/* JsonWebToken */
	public static final String JWT_KEY = "sh281$2JKshazn21Lm9=";
	
	/*Interface*/
	public static final Integer CALIFICACION = 5;
	
	/*PROPIEDADES*/
	public static final String LOGINUSUARIO = "LOGINUSUARIO";
	public static final String URL_HOME = "/appettit-web";

	/* gub.uy 
	public static final String CLIENT_ID = "890192";
	public static final String CLIENT_SECRET = "457d52f181bf11804a3365b49ae4d29a2e03bbabe74997a2f510b179";
	public static final String AUTHORIZATION_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/authorize";
	public static final String ACCESSTOKEN_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/token";
	public static final String USERINFO_URL = "https://auth-testing.iduruguay.gub.uy/oidc/v1/userinfo";
	//public static final String REDIRECT_URI = "https://vacunasuy.web.elasticloud.uy/rest/autenticaciongubuy/procesarTokens";
	//public static final String REDIRECT_URI = "http://localhost:8080";
	public static final String REDIRECT_URI = "https://vacunasuy.web.elasticloud.uy";*/
	
	/* Nodos periféricos 
//	public static final String NODOS_PERIFERICOS_REST_URL = "http://localhost:8081";
//	public static final String NODOS_EXTERNOS_REST_URL = "http://localhost:8082";
	public static final String NODOS_PERIFERICOS_REST_URL = "https://nodos-perifericos.herokuapp.com";
	public static final String NODOS_EXTERNOS_REST_URL = "https://nodos-externos.herokuapp.com";*/
	
	/* Notificaciones Firebase*/ 
	public static final String FIREBASE_API_KEY = "AAAA1RJCocI:APA91bGfs3tLn4xetOnQYtyjEWsLXRGQFm8h9ik6CJeL0JiNjde4Hdqz_RobPmWPE_sgeET2WE35eQpy4PQq_RD3UIQKYyR1vuoi4RIthbecPF9UejualHzAjUUgk7KJCulv6h26_x80";
	public static final String FIREBASE_FCM_URL = "https://fcm.googleapis.com/fcm/send";

	// INFORMACION APPETIT SUPPORT MAIL
	public static final String USER_NAME = "info.appetit.g3@gmail.com";
	public static final String USER_PASS = "appetit123";

	public static final Properties MAIL_PROPS(){
		String host = "smtp.gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		return props;
	};

	// LINK DE REDIRECCION PARA ENVIAR EN EL MAIL, ENVIA AL SERVICIO Q VERIFICA EL TOKEN Y REDIRIGE AL FRONT
	public static final String VERIFY_LINK = "http://localhost:8080/appettit-web/rest/usuarios/verifyMailLink/";

	// LINK DE REDIRECCION PARA IR AL FRONT HACIA LA VENTANA DE CAMBIO DE PASS
	public static final String FRONT_PASS_CHANGE_LINK = "http://localhost:3000/change-pass/";
}
