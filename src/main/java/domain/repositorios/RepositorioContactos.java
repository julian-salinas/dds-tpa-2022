package domain.repositorios;

import domain.notificaciones.contactos.Contacto;
import domain.repositorios.daos.DAO;
import domain.repositorios.daos.DAO;
import domain.repositorios.daos.DAOHibernate;

public class RepositorioContactos extends Repositorio<Contacto> {
    private static final RepositorioContactos instance = null;

    private RepositorioContactos(DAO<Contacto> dao) {
        super(dao);
    }

    public static RepositorioContactos getInstance() {
        return new RepositorioContactos(new DAOHibernate<>(Contacto.class));
    }
}
