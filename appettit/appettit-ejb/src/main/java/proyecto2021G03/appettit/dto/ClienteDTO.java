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
                      String token, String tokenFireBase, Boolean bloqueado, List<DireccionDTO> direcciones,
                      CalificacionClienteDTO calificacion, String idImagen, ImagenDTO imagen) {

        super(id, nombre, username, password, telefono, correo, token, tokenFireBase);

        this.bloqueado = bloqueado;
        this.direcciones = direcciones;
        this.calificacion = calificacion;
        this.idImagen = idImagen;
        this.imagen = imagen;
    }

    private Boolean bloqueado;
    private List<DireccionDTO> direcciones;

    private CalificacionClienteDTO calificacion;
    private String idImagen;
    private ImagenDTO imagen;
}
