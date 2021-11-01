package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.PedidoConverter;
import proyecto2021G03.appettit.converter.PedidoRConverter;
import proyecto2021G03.appettit.converter.UsuarioConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.PedidoRDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class PedidoService implements IPedidoService {

	@EJB
    IPedidoDao iPedidoDao;
    @EJB
    PedidoConverter pedidoConverter;
    @EJB
    UsuarioConverter usuarioConverter;

    @EJB
    PedidoRConverter pedidoRConverter;



    @Override
    public List<PedidoDTO> listar() throws AppettitException {
        try {
            return pedidoConverter.fromEntity(iPedidoDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public PedidoDTO listarPorId(Long id) {
        try {
            return pedidoConverter.fromEntity(iPedidoDao.listarPorId(id));
        } catch (Exception e) {
            //throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            return null;//verlo
        }
    }

    @Override
    public PedidoDTO crear(PedidoDTO pedidoDTO) throws AppettitException {
        Pedido PedidoService = iPedidoDao.listarPorId(pedidoDTO.getId());
        try {

            return pedidoConverter.fromEntity(iPedidoDao.crear(PedidoService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public PedidoDTO editar(Long id, PedidoDTO pedidoDTO) throws AppettitException {
        Pedido pedido = iPedidoDao.listarPorId(pedidoDTO.getId());
        if (pedido == null)
            throw new AppettitException("El departamento indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {
            pedido.setCliente(pedido.getCliente());
            pedido.setId(pedido.getId());
            pedido.setEntrega(pedido.getEntrega());
            pedido.setEstado(pedido.getEstado());
            pedido.setFecha(pedido.getFecha());
            pedido.setMenus(pedido.getMenus());
            pedido.setMotivo(pedido.getMotivo());
            pedido.setPago(pedido.getPago());
            pedido.setPromociones(pedido.getPromociones());
            pedido.setReclamo(pedido.getReclamo());
            pedido.setTiempoEstimado(pedido.getTiempoEstimado());
            pedido.setTotal(pedido.getTotal());
            pedido.setTipo(pedido.getTipo());

            return pedidoConverter.fromEntity(iPedidoDao.editar(pedido));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }



    @Override
    public void eliminar(Long id) throws AppettitException {

        Pedido pedido= iPedidoDao.listarPorId(id);
        if(pedido == null) {
            throw new AppettitException("El Pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
            try {
                iPedidoDao.eliminar(pedido);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }

    @Override
    public PedidoRDTO crearFront(PedidoRDTO pedidoRDTO) throws AppettitException {
        Pedido pedido = pedidoRConverter.fromDTO(pedidoRDTO);
        try {

            return pedidoRConverter.fromEntity(iPedidoDao.crear(pedido));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

}
