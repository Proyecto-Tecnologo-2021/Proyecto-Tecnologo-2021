package proyecto2021G03.appettit.converter;

import javax.ejb.Singleton;

import proyecto2021G03.appettit.dto.ClasificacionClienteDTO;
import proyecto2021G03.appettit.entity.ClasificacionCliente;

@Singleton
public class ClasificacionClienteConverter extends AbstractConverter<ClasificacionCliente, ClasificacionClienteDTO>{


    @Override
    public ClasificacionClienteDTO fromEntity(ClasificacionCliente clasificacionCliente) {
        if(clasificacionCliente == null) return null;
        return ClasificacionClienteDTO.builder()
                .clasificacion(clasificacionCliente.getClasificacion())
                .cliente(clasificacionCliente.getCliente())
                .comentario(clasificacionCliente.getComentario())
                .build();
    }

    @Override
    public ClasificacionCliente fromDTO(ClasificacionClienteDTO clasificacionClienteDTO) {
        if(clasificacionClienteDTO == null) return null;
        return ClasificacionCliente.builder()
                .clasificacion(clasificacionClienteDTO.getClasificacion())
                .cliente(clasificacionClienteDTO.getCliente())
                .comentario(clasificacionClienteDTO.getComentario())
                .build();    }
}
