package proyecto2021G03.appettit.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO extends UsuarioDTO  {

    @Builder
    public ClienteDTO(Long id, String nombre, String username, String password, String telefono, String correo,
                      String token, String tokenFireBase, Boolean bloqueado, List<DireccionDTO> direcciones) {
        super(id, nombre, username, password, telefono, correo, token, tokenFireBase);

        this.bloqueado = bloqueado;
        this.direcciones = direcciones;
    }

    private Boolean bloqueado;
    private List<DireccionDTO> direcciones;
}
