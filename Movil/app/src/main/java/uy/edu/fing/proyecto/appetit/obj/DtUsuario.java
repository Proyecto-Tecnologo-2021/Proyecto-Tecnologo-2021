package uy.edu.fing.proyecto.appetit.obj;

public class DtUsuario {
    private static DtUsuario uInstance= null;
    private String correo;
    private String telefono;
    private String token;
    private String tokenFirebase;

    protected DtUsuario(){}

    public static synchronized DtUsuario getInstance() {
        if(uInstance == null){
            uInstance = new DtUsuario();
        }
        return uInstance;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenFirebase() {
        return tokenFirebase;
    }

    public void setTokenFirebase(String tokenFirebase) {
        this.tokenFirebase = tokenFirebase;
    }
}
