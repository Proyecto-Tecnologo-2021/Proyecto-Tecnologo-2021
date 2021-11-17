package proyecto2021G03.appettit.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import proyecto2021G03.appettit.business.ICalificacionRRService;
import proyecto2021G03.appettit.business.ICalificacionRService;
import proyecto2021G03.appettit.business.IPedidoService;
import proyecto2021G03.appettit.business.IReclamoService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.ReclamoCDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.exception.AppettitException;

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
	@EJB
	ICalificacionRRService iCalificacionRRService;
	@EJB
	IReclamoService iReclamoService;
	/*
	 * @POST
	 * 
	 * @Path("/pedido1") //@RecursoProtegidoJWT public Response crear(PedidoDTO
	 * request) { RespuestaREST<PedidoDTO> respuesta = null; try { PedidoDTO pedido
	 * = iPedidoService.crear(request); respuesta = new
	 * RespuestaREST<PedidoDTO>(true, "Pedido creado con éxito.", pedido); return
	 * Response.ok(respuesta).build(); } catch (AppettitException e) { respuesta =
	 * new RespuestaREST<PedidoDTO>(false, e.getLocalizedMessage());
	 * if(e.getCodigo() == AppettitException.EXISTE_REGISTRO) { return
	 * Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build(); }
	 * else { return
	 * Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).
	 * build(); } } }
	 * 
	 * 
	 */


	@GET
	@Path("/ultimo/{peid}")
	public Response ultimopedido(@PathParam("peid") Long peid) {
		RespuestaREST<PedidoRDTO> respuesta = null;
		PedidoRDTO pedido = null;
		try {
			pedido = iPedidoService.ultimo(peid);
		} catch (AppettitException e) {
			e.printStackTrace();
		}
		respuesta = new RespuestaREST<PedidoRDTO>(true, "Ultimo pedido traido con éxito.", pedido);
		return Response.ok(respuesta).build();
	}


	@POST
	@Path("/pedido2")
	public Response crear(PedidoRDTO request) {
		RespuestaREST<PedidoRDTO> respuesta = null;
		try {

			PedidoRDTO pedido = iPedidoService.crearFront(request);
			respuesta = new RespuestaREST<PedidoRDTO>(true, "Pedido creado con éxito.", pedido);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<PedidoRDTO>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	@GET
	@Path("/listarpedidos/{id_cliente}")
	public Response listarPedidos(@PathParam("id_cliente") Long id_cliente) {
		RespuestaREST<List<PedidoRDTO>> respuesta = null;
		try {
			List<PedidoRDTO> pedidoDTO = (List<PedidoRDTO>) iPedidoService.listarPorClienteREST(id_cliente);
			respuesta = new RespuestaREST<List<PedidoRDTO>>(true, "Pedidos listados con éxito.", pedidoDTO);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<List<PedidoRDTO>>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	@PUT
	@Path("/calificar/")
	// @RecursoProtegidoJWT
	public Response calificar(CalificacionRPedidoDTO request) {
		RespuestaREST<CalificacionRPedidoDTO> respuesta = null;
		try {
			CalificacionRPedidoDTO pedido = iCalificacionRRService.crear(request);
			respuesta = new RespuestaREST<CalificacionRPedidoDTO>(true, "Calificación creada con éxito.", pedido);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<CalificacionRPedidoDTO>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO
					|| e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	@PUT
	@Path("/reclamar/")
	// @RecursoProtegidoJWT
	public Response reclamar(ReclamoCDTO request) {
		RespuestaREST<ReclamoDTO> respuesta = null;
		try {
			ReclamoDTO reclamo = iReclamoService.crear(request);
			respuesta = new RespuestaREST<ReclamoDTO>(true, "Reclamo creado con éxito.", reclamo);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<ReclamoDTO>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO
					|| e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
	
	@PUT
	@Path("/calificarUPD/")
	// @RecursoProtegidoJWT
	public Response actualizarCalificar(CalificacionRPedidoDTO request) {
		RespuestaREST<CalificacionRPedidoDTO> respuesta = null;
		try {
			CalificacionRPedidoDTO pedido = iCalificacionRRService.editar(request.getId_pedido(), request);
			respuesta = new RespuestaREST<CalificacionRPedidoDTO>(true, "Calificación editada con éxito.", pedido);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<CalificacionRPedidoDTO>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.NO_EXISTE_REGISTRO
					|| e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}

	
	@GET
	@Path("/listar/{id_pedido}")
	public Response listarPedido(@PathParam("id_pedido") Long id_pedido) {
		RespuestaREST<PedidoRDTO> respuesta = null;
		try {
			PedidoRDTO pedidoDTO =  iPedidoService.listarPorIdREST(id_pedido);
			respuesta = new RespuestaREST<PedidoRDTO>(true, "Pedido listado con éxito.", pedidoDTO);
			return Response.ok(respuesta).build();
		} catch (AppettitException e) {
			respuesta = new RespuestaREST<PedidoRDTO>(false, e.getLocalizedMessage());
			if (e.getCodigo() == AppettitException.EXISTE_REGISTRO) {
				return Response.status(Response.Status.BAD_REQUEST).entity(respuesta).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(respuesta).build();
			}
		}
	}
}
