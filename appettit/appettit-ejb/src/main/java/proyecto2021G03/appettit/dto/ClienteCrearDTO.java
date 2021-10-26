package proyecto2021G03.appettit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCrearDTO {

    private String nombre;
    private String username;
    private String password;
    private String telefono;
    private String correo;
    private String tokenFireBase;
    private DireccionCrearDTO direccion;
    
}
