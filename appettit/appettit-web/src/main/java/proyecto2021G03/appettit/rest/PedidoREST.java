package proyecto2021G03.appettit.rest;

import proyecto2021G03.appettit.business.IPedidoService;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/pedido")
@Consumes("application/json")
@Produces("application/json")
public class PedidoREST {

    @EJB
    IPedidoService iPedidoService;

    @POST
    //@RecursoProtegidoJWT
    public Response crear(PedidoDTO request) {
        RespuestaREST<PedidoDTO> respuesta = null;
        try {
            PedidoDTO pedido = iPedidoService.crear(request);
            respuesta = new RespuestaREST<PedidoDTO>(true, "Pedido creado con éxito.", pedido);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<PedidoDTO>(false, e.getLocalizedMessage());
            if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
            }
        }
    }
}
