package proyecto2021G03.appetit.obj;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtUsuario {
    private static DtUsuario uInstance= null;
    private Long id;
    private String nombre;
    private String username;
    private String password;
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


}