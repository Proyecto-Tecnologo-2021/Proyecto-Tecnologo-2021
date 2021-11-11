package proyecto2021G03.appettit.converter;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import proyecto2021G03.appettit.dao.IUsuarioDAO;
import proyecto2021G03.appettit.dto.DireccionDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.ExtraMenuDTO;
import proyecto2021G03.appettit.dto.MenuDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.TipoPago;
import proyecto2021G03.appettit.entity.Cliente;
import proyecto2021G03.appettit.entity.Pedido;

@Singleton
public class PedidoRConverter extends AbstractConverter<Pedido, PedidoRDTO>{

    @EJB
    DireccionConverter dirConverter;

    @EJB
    MenuConverter menuConverter;

    @EJB
    MenuRConverter menuRConverter;

    @EJB
    PromocionConverter promocionConverter;

    @EJB
    ReclamoConverter reclamoConverter;

    @EJB
    UsuarioConverter usrConverter;

    @EJB
    IUsuarioDAO isuarioDAO;

    @EJB
    PromocionRConverter promocionRConverter;

    @EJB
    ExtraMenuConverter extraConverter;
    
    @Override
    public PedidoRDTO fromEntity(Pedido pedido) {
        if(pedido== null) return null;
        PedidoRDTO pedidofinal = PedidoRDTO.builder()
        		.id(pedido.getId())
                .idcli(pedido.getId_cliente())
                .iddir(pedido.getId_entrega())
                .menus(menuRConverter.fromEntityList(pedido.getMenus()))
                .pago(pedido.getPago())
                .tipo(pedido.getTipo())
                .total(pedido.getTotal())
                .idrest(pedido.getRestaurante().getId())
                .fecha(LocalDateTime.now())
                .estado(pedido.getEstado())
                .extras(extraConverter.fromEntityToRDTO(pedido.getExtraMenus()))
                .id_paypal(pedido.getId_paypal())
                .cotizacion(pedido.getCotizacion())
                .build();
        
               return pedidofinal;


    }

    @Override
    public Pedido fromDTO(PedidoRDTO pedidoRDTO) {
        if(pedidoRDTO== null) return null;
        return Pedido.builder()
        		.id(null)
        		.estado(pedidoRDTO.getEstado())
        		.tipo(pedidoRDTO.getTipo())
        		.pago(pedidoRDTO.getPago())
        		.fecha(pedidoRDTO.getFecha())
        		.total(pedidoRDTO.getTotal())
        		.restaurante(isuarioDAO.buscarRestaurantePorId(pedidoRDTO.getIdrest()))
        		.id_restaurante(pedidoRDTO.getIdrest())
        		.cliente(isuarioDAO.buscarPorIdCliente(pedidoRDTO.getIdcli()))
        		.id_cliente(pedidoRDTO.getIdcli())
        		.menus(menuRConverter.fromDTO(pedidoRDTO.filtroMenu()))
                .promociones(promocionConverter.fromRDTO(pedidoRDTO.filtroPromo()))
                .extraMenus(extraConverter.fromRDTO(pedidoRDTO.getExtras()))
                .entrega(isuarioDAO.buscarDireccionPorId(pedidoRDTO.getIddir()))
                .id_entrega(pedidoRDTO.getIddir())
                .reclamo(null)
                .id_paypal(pedidoRDTO.getId_paypal())
                .cotizacion(pedidoRDTO.getCotizacion())
        		.build();
        
    }
}

/*
 *  private Long id;
    private EstadoPedido estado;
    private TipoPago tipo;
    private Boolean pago;
    private Integer tiempoEstimado;
    private String motivo;
    private LocalDateTime fecha;
    private Double total;
    private RestauranteDTO restaurante;
    private Cliente cliente;
    private List<MenuDTO> menus;
    private List<PromocionDTO> promociones;
    private List<ExtraMenuDTO> extraMenu;
    private DireccionDTO entrega;
    private ReclamoDTO reclamo;
    private Long id_restaurante;
	private Long id_cliente;
	private Long id_entrega;
	private Long id_reclamo;

 */

