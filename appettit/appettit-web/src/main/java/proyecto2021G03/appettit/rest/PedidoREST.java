package proyecto2021G03.appettit.rest;

import proyecto2021G03.appettit.business.ICalificacionRService;
import proyecto2021G03.appettit.business.IPedidoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.*;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Path("/pedido")
@Consumes("application/json")
@Produces("application/json")
public class PedidoREST {

    @EJB
    IPedidoService iPedidoService;
    @EJB
    IUsuarioService iUsuarioService;
    @EJB
    ICalificacionRService iCalificacionRService;


    /*@POST
    @Path("/pedido1")
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


     */
    @POST
    @Path("/pedido2")
    public Response crear(PedidoRDTO request){
        RespuestaREST<PedidoRDTO> respuesta = null;
        try {
            PedidoRDTO pedido = iPedidoService.crearFront(request);
            respuesta = new RespuestaREST<PedidoRDTO>(true, "Pedido creado con éxito.", pedido);
            return Response.ok(respuesta).build();
        } catch (AppettitException e) {
            respuesta = new RespuestaREST<PedidoRDTO>(false, e.getLocalizedMessage());
            if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
            }
        }
        }

    @GET
    @Path("/listarpedidos/{id_usuario}")
        public  Response listarPedidos(@PathParam("id_usuario") Long id_usuario) {
        RespuestaREST<List<PedidoDTO>> respuesta = null;
        List<PedidoDTO> pedidoDTO = (List<PedidoDTO>) iPedidoService.listarPorId(id_usuario);
        respuesta = new RespuestaREST<List<PedidoDTO>>(true, "Pedidos listados con éxito.", pedidoDTO);
        return Response.ok(respuesta).build();
    }


    @PUT
    @Path("/calificar/")
    //@RecursoProtegidoJWT
    public Response calificar(CalificacionPedidoDTO request) {
        RespuestaREST<CalificacionPedidoDTO> respuesta = null;
        try{
            CalificacionPedidoDTO pedido = iCalificacionRService.crear(request);
            respuesta = new RespuestaREST<CalificacionPedidoDTO>(true, "Categoria editada con éxito.", pedido);
            return Response.ok(respuesta).build();
        }catch (AppettitException e) {
            respuesta = new RespuestaREST<CalificacionPedidoDTO>(false, e.getLocalizedMessage());
            if(e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO || e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
            }
        }
    }



        }



