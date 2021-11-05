package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.CalificacionRConverter;
import proyecto2021G03.appettit.converter.CalificacionRRestConverter;
import proyecto2021G03.appettit.dao.ICalificacionRDao;
import proyecto2021G03.appettit.dto.CalificacionRPedidoDTO;
import proyecto2021G03.appettit.entity.ClasificacionPedido;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

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
}

