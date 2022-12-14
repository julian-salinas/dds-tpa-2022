package presentacion.controladores;

import domain.organizaciones.miembros.Miembro;
import repositorios.RepositorioMiembros;
import repositorios.RepositorioTransportes;
import repositorios.RepositorioUsuarios;
import domain.trayecto.Tramo;
import domain.trayecto.Trayecto;
import domain.trayecto.TrayectoCompartido;
import domain.trayecto.transporte.MedioDeTransporte;
import domain.ubicaciones.Ubicacion;
import presentacion.Usuario;
import presentacion.errores.Error;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrayectoController {

  public ModelAndView index(Request request, Response response) {
    String username = request.session().attribute("usuario_logueado");
    Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);
    Miembro miembro = user.getMiembro();
    Map<String, Object> model = new HashMap<>();
    model.put("trayectos", miembro.getTrayectos());
    return new ModelAndView(model, "trayecto.hbs");
  }

  public ModelAndView post(Request request, Response response) {
    String tipo = request.queryParams("trayecto");
    Trayecto trayecto;

    if(tipo.equals("trayecto")) {
      trayecto = new Trayecto();
      request.session().attribute("trayecto", trayecto);
      request.session().attribute("compartido", false);
     }
    else if (tipo.equals("trayecto-comp")) {
      trayecto = new TrayectoCompartido();
      request.session().attribute("trayecto", trayecto);
      request.session().attribute("compartido", true);

      response.redirect("/agregarMiembro");
      return null;

    } else {
      return null;
    }

    response.redirect("/tramo");
    return null;
  }

  public ModelAndView indexTramo(Request request, Response response) {
    List<MedioDeTransporte> transportes = RepositorioTransportes.getInstance().all();
    Trayecto trayecto = request.session().attribute("trayecto");
    boolean compartido = request.session().attribute("compartido");
    Map<String, Object> model = new HashMap<>();
    if(compartido) {
      transportes = transportes.stream().filter(MedioDeTransporte::admiteTrayectoCompartido).collect(Collectors.toList());
    }
    List<Tramo> tramos = trayecto.getTramos();
    model.put("transportes", transportes);
    model.put("tramos", tramos);
    return new ModelAndView(model, "tramo.hbs");
  }

  public ModelAndView postTramo(Request request, Response response) {
    Trayecto trayecto = request.session().attribute("trayecto");

    String paisInicio = request.queryParams("paisInicio");
    String provinciaInicio = request.queryParams("provinciaInicio");
    String municipioInicio = request.queryParams("municipioInicio");
    String localidadInicio = request.queryParams("localidadInicio");
    String calleInicio = request.queryParams("calleInicio");
    int alturaInicio = Integer.parseInt(request.queryParams("alturaInicio"));

    String paisFin = request.queryParams("paisFin");
    String provinciaFin = request.queryParams("provinciaFin");
    String municipioFin = request.queryParams("municipioFin");
    String localidadFin = request.queryParams("localidadFin");
    String calleFin = request.queryParams("calleFin");
    int alturaFin = Integer.parseInt(request.queryParams("alturaFin"));

    String boton = request.queryParams("tramo");

    Ubicacion ubicacionInicial  = new Ubicacion(calleInicio, alturaInicio, paisInicio,
        provinciaInicio, municipioInicio, localidadInicio);

    Ubicacion ubicacionFin  = new Ubicacion(calleFin, alturaFin, paisFin,
        provinciaFin, municipioFin, localidadFin);


    // Validacion de Ubicaciones
    Error error = new Error();
    String cualUbicacion = "Ubicacion de Inicio";
    try {
      ubicacionInicial.getLocalidad();
      cualUbicacion = "Ubicacion de Fin";
      ubicacionFin.getLocalidad();
    } catch (RuntimeException e) {
      e.printStackTrace();
      error.setError(true);
      error.setDescripcion(e.getMessage() + " en " + cualUbicacion);

      List<MedioDeTransporte> transportes = RepositorioTransportes.getInstance().all();
      if (request.session().attribute("compartido")) {
        transportes = transportes.stream().filter(MedioDeTransporte::admiteTrayectoCompartido).collect(Collectors.toList());
      }

      Map<String, Object> model = new HashMap<>();
      model.put("transportes", transportes);
      model.put("error", error);
      return new ModelAndView(model, "tramo.hbs");
    }

    int medioid = Integer.parseInt(request.queryParams("medio"));
    MedioDeTransporte medio = RepositorioTransportes.getInstance().get(medioid);
    Tramo tramo = new Tramo(medio, ubicacionInicial, ubicacionFin);

    trayecto.agregarTramo(tramo);

    if(boton.equals("fin")) {
      String username = request.session().attribute("usuario_logueado");
      Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);

      Miembro miembro = user.getMiembro();
      miembro.registrarTrayecto(trayecto);

      RepositorioMiembros.getInstance().update(miembro);

      response.redirect("/trayecto");
      return null;
    }
    else
    {
      response.redirect("/tramo");
      return null;
    }
  }

  public ModelAndView indexAgregarMiembros(Request request, Response response) {
    String username = request.session().attribute("usuario_logueado");
    Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);
    Map<String, Object> model = new HashMap<>();
    List<Miembro> miembros = RepositorioMiembros.getInstance().miembrosMismaOrg(user.getMiembro());
    if (miembros!=null)
      miembros.remove(user.getMiembro()); // Todos menos e'l mismo
    model.put("miembros",miembros);
    return new ModelAndView(model, "miembroTramo.hbs");
  }

  public ModelAndView postAgregarMiembros(Request request, Response response) {
    int miembroID = Integer.parseInt(request.queryParams("miembro"));
    Miembro miembro = RepositorioMiembros.getInstance().get(miembroID);
    TrayectoCompartido trayecto = request.session().attribute("trayecto");

    trayecto.agregarAcompanantes(miembro);

    String boton = request.queryParams("agregar");

    if(boton.equals("fin")) {
      response.redirect("/tramo");
      return null;
    }
    else
    {
      /*String username = request.session().attribute("usuario_logueado");
      Usuario user = RepositorioUsuarios.getInstance().findByUsername(username);
      List<Miembro> miembros = RepositorioMiembros.getInstance().miembrosMismaOrg(user.getMiembro());
      model.put("miembros",miembros);
      return new ModelAndView(model, "miembroTramo.hbs");*/
      response.redirect("/agregarMiembro");
      return null;
    }
  }

}
