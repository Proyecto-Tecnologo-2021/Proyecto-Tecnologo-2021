package uy.edu.fing.proyecto.appetit.constant;

public class ConnConstants {
    //GENERAL
    final public static String USER_AGENT = "Appetit.Android/1.0";
    //final public static String WEB_CLIENT_ID = "915134390722-llhugtdt7li5qp12o0dja8sb4vu8ua8n.apps.googleusercontent.com";
    final public static String WEB_CLIENT_ID = "915134390722-avo66p6v51lvs5u7q5ob7snq5gr0o4t9.apps.googleusercontent.com";

    //USUARIO
    final public static String API_USRLOGIN_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/loginMobile";
    final public static String API_USRLOGINFIREBASE_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/loginFireBase";
    final public static String API_ADDCLIENT_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios";
    final public static String API_ADDCLIENTADDRERSS_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/agregarDireccion";

    //MENUS
    final public static String API_GETMENUS_URL = "http://192.168.56.1:8080/appettit-web/rest/menu";
    final public static String API_GETMENU_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getMenu/{id_restaurante}/{id}";
    final public static String API_GETMENUSRESTAURANTE_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getMenu/{id_restaurante}";
}
