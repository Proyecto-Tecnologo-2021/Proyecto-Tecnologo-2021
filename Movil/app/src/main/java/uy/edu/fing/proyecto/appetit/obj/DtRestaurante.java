package uy.edu.fing.proyecto.appetit.obj;
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
public class DtRestaurante {
    private Long id;
    private String nombre;
    private String username;
    private String telefono;
    private String correo;
    private DtDireccion direccion;
    private byte[] imagen;
    private DtRCalificacion calificacion;
    Boolean abierto;
    String horarioApertura;
    String horarioCierre;
}