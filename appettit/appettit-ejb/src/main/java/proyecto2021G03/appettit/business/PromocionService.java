package proyecto2021G03.appettit.business;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import proyecto2021G03.appettit.converter.PromocionConverter;
import proyecto2021G03.appettit.dao.ICategoriaDAO;
import proyecto2021G03.appettit.dao.IPromocionDAO;
import proyecto2021G03.appettit.dto.ProductoCrearDTO;
import proyecto2021G03.appettit.dto.PromocionDTO;
import proyecto2021G03.appettit.entity.Promocion;
import proyecto2021G03.appettit.exception.AppettitException;

@Stateless
public class PromocionService implements IPromocionService {

    @EJB
    public IPromocionDAO pDAO;

    @EJB
    public ICategoriaDAO cDAO;

    @EJB
    public PromocionConverter pConverter;

    @Override
    public List<PromocionDTO> listar() throws AppettitException {
        try {
            return pConverter.fromEntity(pDAO.listar());
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public PromocionDTO listarPorId(Long id) throws AppettitException {
        try {
            return pConverter.fromEntity(pDAO.listarPorId(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public PromocionDTO editar(Long id, ProductoCrearDTO pcDTO) throws AppettitException {

        // HACER EL CONTROL POR RESTAURANTE Y NO GENERAL
        if (existeNombreProductoExcluirId(id, pcDTO.getNombre())) {
            throw new AppettitException("Ya existe un producto con ese nombre.", AppettitException.EXISTE_REGISTRO);
        } else {
            try {
                Promocion promo = pDAO.listarPorId(id);
                promo.setNombre(pcDTO.getNombre());
                //Categoria categoria = cDAO.listarPorId(pcDTO.getId_categoria());
                
                return pConverter.fromEntity(pDAO.editar(promo));
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }

    @Override
    public void eliminar(Long id) throws AppettitException {
        /* Se valida que exista la promocion */
        Promocion promo = pDAO.listarPorId(id);
        if (promo == null) {
            throw new AppettitException("La promocion indicada no existe.", AppettitException.NO_EXISTE_REGISTRO);
        } 
        else {
            try {
                pDAO.eliminar(promo);
            } catch (Exception e) {
                throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
            }
        }
    }

    @Override
    public List<PromocionDTO> listarPorRestaurante(Long id) throws AppettitException {
        try {
            return pConverter.fromEntity(pDAO.listarPorRestaurante(id));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public PromocionDTO crear(PromocionDTO pcDTO) throws AppettitException {

        try {
            Promocion promo = pConverter.fromDTO(pcDTO);
            return pConverter.fromEntity(pDAO.crear(promo));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    @Override
    public PromocionDTO editar(PromocionDTO ccDTO) throws AppettitException {
        try {
            Promocion promo = pConverter.fromDTO(ccDTO);
            return pConverter.fromEntity(pDAO.editar(promo));
        } catch (Exception e) {
            throw new AppettitException(e.getLocalizedMessage(), AppettitException.ERROR_GENERAL);
        }
    }

    // AUXILIARES
//    public boolean existeNombreProducto(String nombre) {
//
//        List<PromocionDTO> promoData = pConverter.fromEntity(pDAO.listar());
//        for (PromocionDTO p : promoData) {
//            if (p.getNombre().equals(nombre)) {
//                return true;
//            }
//        }
//        return false;
//    }

    public boolean existeNombreProductoExcluirId(Long id, String nombre) {

        List<PromocionDTO> promoData = pConverter.fromEntity(pDAO.listar());
        for (PromocionDTO p : promoData) {
            if (!p.getId().equals(id)) {
                if (p.getNombre().equals(nombre)) {
                    return true;
                }
            }
        }
        return false;
    }
}
