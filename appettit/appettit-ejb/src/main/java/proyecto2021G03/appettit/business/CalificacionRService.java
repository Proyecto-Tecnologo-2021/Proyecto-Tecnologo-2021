package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.CalificacionRConverter;
import proyecto2021G03.appettit.dao.ICalificacionRDao;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import java.util.List;

@Stateless
public class CalificacionRService implements ICalificacionRService{

	@EJB
    public ICalificacionRDao iCalificacionRDao;

    @EJB
    public CalificacionRConverter calificacionRConverter;


    @Override
    public List<CalificacionPedidoDTO> listar() throws AppettitException {
        try {
            return calificacionRConverter.fromEntity(iCalificacionRDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public CalificacionPedidoDTO listarPorId(Long id) {

        try {
            return calificacionRConverter.fromEntity(iCalificacionRDao.listarPorId(id));
        } catch (Exception e) {
            return null; //throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public CalificacionPedidoDTO crear(CalificacionPedidoDTO calificacionPedidoDTO) throws AppettitException {
        ClasificacionPedido PedidoService = iCalificacionRDao.listarPorId(calificacionPedidoDTO.getPedido().getId());
        try {

            return calificacionRConverter.fromEntity(iCalificacionRDao.crear(PedidoService));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }


    @Override
    public CalificacionPedidoDTO editar(Long id, CalificacionPedidoDTO calificacionPedidoDTO) throws AppettitException {
        ClasificacionPedido clasificacionPedido = iCalificacionRDao.listarPorId(calificacionPedidoDTO.getPedido().getId());
        if (clasificacionPedido == null)
            throw new AppettitException("El pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            return calificacionRConverter.fromEntity(iCalificacionRDao.editar(clasificacionPedido));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public void eliminar(Long id) throws AppettitException {
        ClasificacionPedido clasificacionPedido= iCalificacionRDao.listarPorId(id);
        if(clasificacionPedido == null) {
            throw new AppettitException("El Pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
            try {
                iCalificacionRDao.eliminar(clasificacionPedido);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }
}
