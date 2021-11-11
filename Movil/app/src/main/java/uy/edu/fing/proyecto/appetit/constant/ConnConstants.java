package uy.edu.fing.proyecto.appetit.constant;

public class ConnConstants {
    //GENERAL
    final public static String USER_AGENT = "Appetit.Android/1.0";
    final public static String WEB_CLIENT_ID = "915134390722-avo66p6v51lvs5u7q5ob7snq5gr0o4t9.apps.googleusercontent.com";

    //PAYPAL
    //final public static String PAYPAL_CLIENT_ID = "AX1tidD2Nabve-nXf9dR6UqFx6LtKK4-FJZ9QCzXJgoM8pjYrDNCCPF6KzOLgDDrGZSpaDA2psnjfP5l";
    //final public static String PAYPAL_WEB_ID = "http://192.168.56.1:8080/appettit-web/restaurante/paypal.html";
    final public static String PAYPAL_CLIENT_ID = "AWnJhlBsiTCohUxd2HKlE_vZCOZErmodDR7uJXUlFk26yDj5VTpS-JNZTaUJYE_mUFQToNhxq6nir-mh";
    final public static String PAYPAL_SECRET_ID = "EBBtfhbFk86oJopT6ICkFi7QmkOvcYSpCddIjDnTJ-y_Ql2uEn0GQm5GJy7dudDPrjLl7DQcgyvkt7zp";
    final public static String PAYPAL_URLTOKEN_ID = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
    final public static String PAYPAL_URLPAYMENT_ID = "https://api-m.sandbox.paypal.com/v1/payments/payment";


    //COTIZACION
    final public static String GET_COTIZACION = "https://cotizaciones-brou.herokuapp.com/api/currency/{date}";

    //USUARIO
    final public static String API_USRLOGIN_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/loginMobile";
    final public static String API_USRLOGINFIREBASE_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/loginFireBase";
    final public static String API_ADDCLIENT_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios";
    final public static String API_ADDCLIENTADDRERSS_URL = "http://192.168.56.1:8080/appettit-web/rest/usuarios/agregarDireccion";

    //MENUS
    final public static String API_GETMENUS_URL = "http://192.168.56.1:8080/appettit-web/rest/menu";
    final public static String API_GETMENUSPOINT_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getZona/{point}";
    final public static String API_GETMENU_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getMenu/{id_restaurante}/{id}";
    final public static String API_GETMENUSRESTAURANTE_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getMenu/{id_restaurante}";

    //PROMOS
    final public static String API_GETPROMOS_URL = "http://192.168.56.1:8080/appettit-web/rest/promo";
    final public static String API_GETPROMOSPOINT_URL = "http://192.168.56.1:8080/appettit-web/rest/promo/getZona/{point}";
    final public static String API_GETPROMO_URL = "http://192.168.56.1:8080/appettit-web/rest/promo/getPromo/{id_restaurante}/{id}";
    final public static String API_GETPROMOSRESTAURANTE_URL = "http://192.168.56.1:8080/appettit-web/rest/promo/getPromo/{id_restaurante}";

    //MENUS=PROMO
    final public static String API_GETMENUSPROMOPOINT_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getZonaAll/{point}";
    final public static String API_GETMENUSPROMORESTAURANTE_URL = "http://192.168.56.1:8080/appettit-web/rest/menu/getMenuAll/{id_restaurante}";

    //RESTAURANTE
    final public static String API_GETRESTAURANTE_URL = "http://192.168.56.1:8080/appettit-web/rest/restaurante/listar/{id}";

    //PEDIDOS
    final public static String API_GETPEDIODSPORID_URL = "http://192.168.56.1:8080/appettit-web/rest/pedido/listarpedidos/{id}";
    final public static String API_ADDPEDIDO_URL = "http://192.168.56.1:8080/appettit-web/rest/pedido/pedido2";
}
