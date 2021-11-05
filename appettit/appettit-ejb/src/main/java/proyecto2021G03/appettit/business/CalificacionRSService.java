package proyecto2021G03.appettit.business;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.CalificacionRRestConverter;
import proyecto2021G03.appettit.dao.ICalificacionRDao;
import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class CalificacionRSService implements ICalificacionRRService {

    @EJB
    ICalificacionRDao iCalificacionRDao;
    @EJB
    CalificacionRRestConverter calificacionRRestConverter;



    @Override
    public CalificacionRPedidoDTO crear(CalificacionRPedidoDTO calificacionRPedidoDTO) throws AppettitException {
        ClasificacionPedido clasificacionPedido = calificacionRRestConverter.fromDTO(calificacionRPedidoDTO);
        try {

            return calificacionRRestConverter.fromEntity(iCalificacionRDao.crear(clasificacionPedido));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }



	@Override
	public CalificacionRPedidoDTO listarPorId(Long id_pedido, Long id_cliente) throws AppettitException {
		 try {
	            return calificacionRRestConverter.fromEntity(iCalificacionRDao.listarPorId(id_pedido, id_cliente));
	        } catch (Exception e) {
	            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
	        }
	}


	@Override
	public CalificacionRPedidoDTO editar(Long id, CalificacionRPedidoDTO calificacionPedidoDTO) throws AppettitException {
		ClasificacionPedido clasificacionPedido = iCalificacionRDao.listarPorId(calificacionPedidoDTO.getId_pedido(), calificacionPedidoDTO.getId_cliente());
        if (clasificacionPedido == null)
            throw new AppettitException("El pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            return calificacionRRestConverter.fromEntity(iCalificacionRDao.editar(clasificacionPedido));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
	}



	@Override
	public void eliminar(Long id_pedido, Long id_cliente) throws AppettitException {
		// TODO Auto-generated method stub
		
	}
}

