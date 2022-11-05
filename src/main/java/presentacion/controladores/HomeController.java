package presentacion.controladores;

import repositorios.RepositorioUsuarios;
import presentacion.TipoUsuario;
import presentacion.Usuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


public class HomeController {

  public ModelAndView index(Request request, Response response) {

    String username = request.session().attribute("usuario_logueado");

    if (username == null) {
      response.redirect("/login");
      return null;
    }

    request.session().attribute("usuario_logueado", username);
    Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);

    if (user.getTipo().equals(TipoUsuario.ORGANIZACION)) {
      Object model = user.getOrg();
      return new ModelAndView(model, "homeOrganizacion.hbs");
    }
    else if (user.getTipo().equals(TipoUsuario.MIEMBRO)) {
      Object model = user.getMiembro();
      return new ModelAndView(model, "homeMiembro.hbs");
    }
    else if (user.getTipo().equals(TipoUsuario.AGENTE_SECTORIAL)) {
      Object model = user.getAgenteSectorial();
      return new ModelAndView(model, "homeAgenteSectorial.hbs");
    }
    else {
      return null;
    }
  }

}
