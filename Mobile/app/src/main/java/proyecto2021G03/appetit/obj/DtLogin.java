package proyecto2021G03.appetit.obj;

public class DtLogin {
    Boolean ok;
    String mensaje;
    DtUsuario usuario;

    public DtLogin(Boolean ok, String mensaje, DtUsuario usuario) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

}
