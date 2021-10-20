package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ExtraMenu;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Singleton

public class ExtraMenuDAO implements IExtraMenuDAO {
    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public List<ExtraMenu> listar() {
        Query consulta = em.createQuery("SELECT m FROM ExtraMenu m");
        return consulta.getResultList();
    }

    @Override
    public ExtraMenu listarPorId(Long id) {

        return em.find(ExtraMenu.class, id);
    }

    @Override
    public ExtraMenu crear(ExtraMenu extraMenu) {

        em.persist(extraMenu);
        return extraMenu;
    }

    @Override
    public ExtraMenu editar(ExtraMenu extraMenu) {
        em.persist(extraMenu);
        return extraMenu;
    }

    @Override
    public void eliminar(ExtraMenu extraMenu) {
    em.remove(extraMenu);
    }


}
