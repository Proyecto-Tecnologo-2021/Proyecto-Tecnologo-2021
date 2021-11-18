package proyecto2021G03.appettit.business;

import proyecto2021G03.appettit.converter.ReclamoConverter;
import proyecto2021G03.appettit.dao.IPedidoDao;
import proyecto2021G03.appettit.dao.IReclamoDao;
import proyecto2021G03.appettit.dto.ReclamoCDTO;
import proyecto2021G03.appettit.dto.ReclamoDTO;
import proyecto2021G03.appettit.entity.Pedido;
import proyecto2021G03.appettit.entity.Reclamo;
import proyecto2021G03.appettit.exception.AppettitException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class ReclamoService implements IReclamoService{
    @EJB
    public IReclamoDao iReclamoDao;

    @EJB
    public IPedidoDao iPedidoDao;
    
    @EJB
    public ReclamoConverter reclamoConverter;

    @Override
    public List<ReclamoDTO> listar() throws AppettitException {
        try {
            return reclamoConverter.fromEntity(iReclamoDao.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
	public List<ReclamoDTO> listarPorRestaurante(Long id) throws AppettitException {
    	try {
            return reclamoConverter.fromEntity(iReclamoDao.listarPorRestaurante(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
	}
    
    @Override
    public ReclamoDTO listarPorId(Long id) throws AppettitException {
        try {
            return reclamoConverter.fromEntity(iReclamoDao.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    }

    @Override
    public ReclamoDTO crear(ReclamoCDTO reclamoCDTO) throws AppettitException {
        Pedido pedido = iPedidoDao.listarPorId(reclamoCDTO.getId_pedido());
        if (pedido == null) {
        	throw new AppettitException("El pedido indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
        	Reclamo reclamo= pedido.getReclamo();
        	if (reclamo != null) {
        		throw new AppettitException("Ya existe un reclamo para el pedido.", AppettitException.EXISTE_REGISTRO);
        	} else {
	        	reclamo = reclamoConverter.fromCDTO(reclamoCDTO);
	            reclamo.setFecha(LocalDateTime.now());
	        	pedido.setReclamo(reclamo);
		        try {
		            return reclamoConverter.fromEntity(iReclamoDao.crear(reclamo));
		        } catch (Exception e) {
		            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
		        }    
	        }
	    }
    }
    
    @Override
    public ReclamoDTO editar(Long id, ReclamoDTO reclamoDTO) throws AppettitException {
        Reclamo reclamo = iReclamoDao.listarPorId(reclamoDTO.getId());
        if (reclamo == null)
            throw new AppettitException("El reclamo indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);

        try {

            reclamo.setDetalles(reclamoDTO.getDetalles());
            reclamo.setFecha((reclamoDTO.getFecha()));
            reclamo.setId(reclamoDTO.getId());
            reclamo.setMotivo(reclamoDTO.getMotivo());


            return reclamoConverter.fromEntity(iReclamoDao.editar(reclamo));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }    
    }
    
    @Override
    public void eliminar(Long id) throws AppettitException {
        Reclamo reclamo= iReclamoDao.listarPorId(id);
        if( reclamo== null) {
        	throw new AppettitException("El reclamo indicado no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } else {
        	try {
        		iReclamoDao.eliminar(reclamo);
        	} catch (Exception e) {
        		throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        	}
        }
    }

}
