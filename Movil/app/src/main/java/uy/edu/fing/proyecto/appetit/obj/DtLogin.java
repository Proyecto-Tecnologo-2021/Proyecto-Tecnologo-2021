package uy.edu.fing.proyecto.appetit.obj;

public class DtLogin {
    Boolean ok;
    String mensaje;
    DtUsuario usuario;

    public DtLogin(Boolean ok, String mensaje, DtUsuario usuario) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.usuario = usuario;
    }

    public Boolean getOk() {
        return ok;
    }

    public String getMensaje() {
        return mensaje;
    }

    public DtUsuario getUsuario() {
        return usuario;
    }
}
