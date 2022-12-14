package presentacion.controladores;

import domain.organizaciones.Organizacion;
import domain.organizaciones.sectores.Sector;
import repositorios.RepositorioOrganizaciones;
import repositorios.RepositorioUsuarios;
import presentacion.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SectoresController {

  public ModelAndView index(Request request, Response response) {

    String username = request.session().attribute("usuario_logueado");
    Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);

    Organizacion organizacion = user.getOrg();

    return new ModelAndView(organizacion, "sectores.hbs");
  }

  public ModelAndView post(Request request, Response response) {
    String nombre = request.queryParams("nombre");
    String descripcion = request.queryParams("descripcion");

    String username = request.session().attribute("usuario_logueado");
    Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);

    Organizacion organizacion = user.getOrg();
    Sector sector = new Sector(nombre, descripcion);
    organizacion.agregarSector(sector);
    RepositorioOrganizaciones.getInstance().update(organizacion);

    return new ModelAndView(organizacion, "sectores.hbs");
  }

}
