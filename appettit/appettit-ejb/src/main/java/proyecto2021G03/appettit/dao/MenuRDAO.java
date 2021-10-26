package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.Menu;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
@Singleton
public class MenuRDAO implements IMenuRDAO{

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public List<Menu> listar() {
            Query consulta = em.createQuery("SELECT m FROM Menu m");
            return consulta.getResultList();
        }
    }
