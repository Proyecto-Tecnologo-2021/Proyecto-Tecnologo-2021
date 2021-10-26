package proyecto2021G03.appettit.rest;
/*
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/promo")
@Consumes("application/json")
@Produces("application/json")
public class PromocionREST {
    @EJB

    IServicioPromocion  iservicioPromocion;

    @GET

    public Response listar() {
        RespuestaREST<List<PromocionDTO>> respuesta = null;
        try {
            List<PromocionDTO> promocionDTOS = iservicioPromocion.listar();
            respuesta = new RespuestaREST<List<PromocionDTO>>(true, "Promociones listadas con éxito.", promocionDTOS);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<>(false, e.getLocalizedMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
        }
    }


}
*/