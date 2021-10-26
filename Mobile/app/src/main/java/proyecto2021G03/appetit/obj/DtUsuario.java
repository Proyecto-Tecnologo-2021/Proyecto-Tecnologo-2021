package proyecto2021G03.appetit.obj;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private List<DtDireccion> direcciones;

    public static synchronized DtUsuario getInstance() {
        if(uInstance == null){
            uInstance = new DtUsuario();
        }
        return uInstance;
    }
}
