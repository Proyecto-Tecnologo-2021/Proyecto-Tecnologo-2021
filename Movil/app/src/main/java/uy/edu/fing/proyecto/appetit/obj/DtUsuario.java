package uy.edu.fing.proyecto.appetit.obj;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @Builder.Default
    private List<DtDireccion> direcciones = new ArrayList<DtDireccion>();

    public static synchronized DtUsuario getInstance() {
        if(uInstance == null){
            uInstance = new DtUsuario();
        }
        return uInstance;
    }
}
