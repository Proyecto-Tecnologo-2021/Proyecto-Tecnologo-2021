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
	
	public static final String REDIRECT_URI = "https://appetit.eastus.cloudapp.azure.com:3000/";
	//public static final String REDIRECT_URI = "/appettit-web/FalseLogin.xhtml";
	public static final String REDIRECT_RESTAURANTE_HOME_URI = "/appettit-web/restaurante/home.xhtml";
	public static final String REDIRECT_ADMINISTRADOR_HOME_URI = "/appettit-web/admin/home.xhtml";
	
	/*Sesiones*/
	public static final String COOKIE_NAME = "__FOsession";
	
	
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
	//public static final String VERIFY_LINK = "http://localhost:8080/appettit-web/rest/usuarios/verifyMailLink/";
	public static final String VERIFY_LINK = "https://appetit.eastus.cloudapp.azure.com:8443/appettit-web/rest/usuarios/verifyMailLink/";
	
	// LINK DE REDIRECCION PARA IR AL FRONT HACIA LA VENTANA DE CAMBIO DE PASS
	//public static final String FRONT_PASS_CHANGE_LINK = "http://localhost:3000/change-pass/";
	public static final String FRONT_PASS_CHANGE_LINK = "https://appetit.eastus.cloudapp.azure.com:3000/change-pass/";
}
