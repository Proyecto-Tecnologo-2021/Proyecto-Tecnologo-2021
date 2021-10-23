package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.CalificacionClienteDTO;
import proyecto2021G03.appettit.entity.ClasificacionCliente;

@Singleton
public class CalificacionClienteConverter extends AbstractConverter<ClasificacionCliente, CalificacionClienteDTO>{


    @Override
    public CalificacionClienteDTO fromEntity(ClasificacionCliente clasificacionCliente) {
        if(clasificacionCliente == null) return null;
        return CalificacionClienteDTO.builder()
                .clasificacion(clasificacionCliente.getClasificacion())
                .cliente(clasificacionCliente.getCliente())
                .comentario(clasificacionCliente.getComentario())
                .build();
    }

    @Override
    public ClasificacionCliente fromDTO(CalificacionClienteDTO calificacionClienteDTO) {
        if(calificacionClienteDTO == null) return null;
        return ClasificacionCliente.builder()
                .clasificacion(calificacionClienteDTO.getClasificacion())
                .cliente(calificacionClienteDTO.getCliente())
                .comentario(calificacionClienteDTO.getComentario())
                .build();    }
}
