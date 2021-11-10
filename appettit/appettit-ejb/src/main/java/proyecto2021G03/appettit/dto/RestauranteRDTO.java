package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteRDTO {
   private Long id;
    private String nombre;
    private LocalTime horarioApertura;
    private LocalTime horarioCierra;
    private DireccionCrearDTO direccion;
    private ImagenDTO imagen;
}
