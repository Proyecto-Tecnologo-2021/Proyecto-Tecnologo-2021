package proyecto2021G03.appettit.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteMDTO extends UsuarioDTO  {

    @Builder
    public ClienteMDTO(Long id, String nombre, String username, String password, String telefono, String correo,
                      String tokenFireBase, Boolean bloqueado, List<DireccionDTO> direcciones,
                      CalificacionGralClienteDTO calificacion) {

        super(id, nombre, username, password, telefono, correo, tokenFireBase);

        this.bloqueado = bloqueado;
        this.direcciones = direcciones;
        this.calificacion = calificacion;
  
    }

    private String jwt;
    private Boolean bloqueado;
    private List<DireccionDTO> direcciones;

    private CalificacionGralClienteDTO calificacion;
  
}
