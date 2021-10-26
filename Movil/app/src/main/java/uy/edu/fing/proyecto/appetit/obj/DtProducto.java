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
public class DtProducto {
    private Long id;
    private Long id_restaurante;
    private String nombre;
    private Long id_categoria;
    private DtRestaurante restaurante;
    private DtCategoria categoria;
}
