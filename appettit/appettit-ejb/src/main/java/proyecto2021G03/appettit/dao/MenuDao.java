package proyecto2021G03.appettit.dao;

import proyecto2021G03.appettit.entity.ExtraMenu;
import proyecto2021G03.appettit.entity.Menu;
import proyecto2021G03.appettit.entity.Producto;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

 @Singleton
public class MenuDao implements IMenuDao{

    @PersistenceContext(name = "Proyecto2021G03")
    private EntityManager em;

    @Override
    public List<Menu> listar() {
        Query consulta = em.createQuery("SELECT m FROM Menu m");
        return consulta.getResultList();
    }

    @Override
    public Menu listarPorId(Long id) {return em.find(Menu.class, id);

    }

    @Override
    public Menu crear(Menu menu) {
        em.persist(menu);
        return menu;
    }

    @Override
    public Menu editar(Menu menu) {
        em.persist(menu);
        return menu;
    }

    @Override
    public void eliminar(Menu menu) {
        em.remove(menu);
    }
}
