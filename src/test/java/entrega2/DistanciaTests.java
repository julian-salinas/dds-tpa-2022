package entrega2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import domain.miembros.Miembro;
import domain.miembros.TipoDeDocumento;
import domain.servicios.geodds.ServicioGeoDds;
import domain.trayecto.Tramo;
import domain.trayecto.Trayecto;
import domain.trayecto.TrayectoCompartido;
import domain.trayecto.transporte.*;
import domain.ubicaciones.Distancia;
import domain.ubicaciones.Ubicacion;
import domain.ubicaciones.UnidadDeDistancia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistanciaTests {

  ServicioGeoDds apiClient;

  Linea lineaA;

  Tramo tramoLineaA;
  Tramo tramoLineaAinverso;
  Tramo tramoAPie;
  Tramo tramoEnBici;
  Tramo tramoEnAuto;
  Tramo tramoEnTaxi;

  // VP -> Vehiculo Particular
  // SC -> Servicio Contratado

  Ubicacion ubicacionInicioPie;
  Ubicacion ubicacionInicioBici;
  Ubicacion ubicacionInicioVP;
  Ubicacion ubicacionInicioSC;
  Ubicacion ubicacionFinPie;
  Ubicacion ubicacionFinBici;
  Ubicacion ubicacionFinVP;
  Ubicacion ubicacionFinSC;

  @BeforeEach
  public void init() throws IOException {
    apiClient = mock(ServicioGeoDds.class);

    Parada sanPedrito = new Parada("San Pedrito", distanciaMts(300.0));
    Parada flores     = new Parada("San Jose de Flores", distanciaMts(200.0));
    Parada carabobo   = new Parada("Carabobo", distanciaMts(150.0));
    Parada puan       = new Parada("Puan", distanciaMts(150.0));
    List<Parada> paradasLineaA = Stream.of(sanPedrito, flores, carabobo, puan)
        .collect(Collectors.toList());

    lineaA = new Linea("Linea A", paradasLineaA, TipoTransportePublico.SUBTE);
    lineaA.setUnidireccional();

    // Creo un recorrido que es solo de San Pedrito a Carabobo
    TransportePublico recorridoConLineaA = new TransportePublico(TipoTransportePublico.SUBTE,
        lineaA, sanPedrito, carabobo);
    tramoLineaA = new Tramo(recorridoConLineaA);

    // Con un recorrido que es solo de Carabobo a San Pedrito
    TransportePublico recorridoConLineaAinverso = new TransportePublico(TipoTransportePublico.SUBTE,
        lineaA, carabobo, sanPedrito);
    tramoLineaAinverso = new Tramo(recorridoConLineaAinverso);

    ubicacionInicioPie = crearUbicacion("Directorio", 1700);
    ubicacionInicioBici = crearUbicacion("Directorio", 100);
    ubicacionInicioVP = crearUbicacion("Directorio", 2300);
    ubicacionInicioSC = crearUbicacion("Directorio", 600);
    ubicacionFinPie = crearUbicacion("Yapeyu", 600);
    ubicacionFinBici = crearUbicacion("Yapeyu", 200);
    ubicacionFinVP = crearUbicacion("Yapeyu", 2400);
    ubicacionFinSC = crearUbicacion("Yapeyu", 700);

    Pie recorridoAPie = new Pie(ubicacionInicioPie, ubicacionFinPie);
    tramoAPie   = new Tramo(recorridoAPie);

    Bicicleta recorridoEnBici = new Bicicleta(ubicacionInicioBici, ubicacionFinBici);
    tramoEnBici = new Tramo(recorridoEnBici);

    VehiculoParticular recorridoEnAuto = new VehiculoParticular(TipoDeVehiculo.AUTO,
        TipoDeCombustible.GASOIL,
        ubicacionInicioVP, ubicacionFinVP,
        400.0);
    tramoEnAuto = new Tramo(recorridoEnAuto);

    TipoServicioContratado taxi = new TipoServicioContratado("taxi");
    ServicioContratado recorridoEnTaxi = new ServicioContratado(taxi, ubicacionInicioSC,
        ubicacionFinSC, 200.0);
    tramoEnTaxi = new Tramo(recorridoEnTaxi);

  }

  // Tests con Transporte Publico (la dist. de cada tramo se indica a mano)

  // Unidireccional:
  //  - Las paradas estan seteadas de forma circular (despues de la ultima esta la primera)
  //  - Se circula solo en un sentido (para adelante)
  //  - Lo importante en este caso es ver si se tiene que pasar de la ultima a la primera (cruzar)

  // Bidireccional:
  //  - Se circula en ambos sentidos
  //  - Lo importante en este caso es ver si se tiene que ir para atras (el sentido)

  @Test
  public void laDistanciaDeUnTramoDeTransportePublicoIdaDistintaDeVueltaSeCalculaBien() {
    //San Pedrito -> Carabobo, Unidireccional
    assertEquals(500.0, tramoLineaA.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoInversoDeTransportePublicoIdaDistintaDeVueltaSeCalculaBien() {
    //Carabobo -> San Pedrito, Unidireccional
    assertEquals(300.0, tramoLineaAinverso.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoDeTransportePublicoIdaIgualAVueltaSeCalculaBien() {
    //San Pedrito -> Carabobo, Bidireccional
    lineaA.setBidireccional();
    assertEquals(500.0, tramoLineaA.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoInversoDeTransportePublicoIdaIgualAVueltaSeCalculaBien() {
    //Carabobo -> San Pedrito, Bidireccional
    lineaA.setBidireccional();
    assertEquals(500.0, tramoLineaAinverso.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTrayectoDeTramosDeTransportePublicoSeCalculaBien() {
    Parada carabobo1200 = new Parada("Carabobo1200", distanciaMts(55.0));
    Parada carabobo1400 = new Parada("Carabobo1400", distanciaMts(55.0));
    List<Parada> paradas132 = Stream.of(carabobo1200, carabobo1400).collect(Collectors.toList());
    Linea linea132 = new Linea("132", paradas132, TipoTransportePublico.COLECTIVO);
    TransportePublico recorridoCon132 = new TransportePublico(TipoTransportePublico.COLECTIVO, linea132, carabobo1200, carabobo1400);
    Tramo tramoCon132 = new Tramo(recorridoCon132);

    Trayecto trayecto = new Trayecto();
    trayecto.agregarTramos(tramoLineaA, tramoCon132);

    assertEquals(555.0, trayecto.distanciaTotal().valorEnMetros());
  }

  // Tests con los demas tipos de Transporte (la dist. de cada tramo se la indica la API)

  @Test
  public void laDistanciaDeUnTramoAPieSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioPie, ubicacionFinPie))
        .thenReturn(73.6);

    assertEquals(73.6, tramoAPie.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoEnBiciSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioBici, ubicacionFinBici))
        .thenReturn(313.47);

    assertEquals(313.47, tramoEnBici.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoEnVehiculoParticularSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioVP, ubicacionFinVP))
        .thenReturn(10002.2);

    assertEquals(10002.2, tramoEnAuto.distancia().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTramoEnServicioContratadoSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioSC, ubicacionFinSC))
        .thenReturn(750.0);

    assertEquals(750.0, tramoEnTaxi.distancia().valorEnMetros());
  }

  @Test                                               //TP = Transporte Publico
  public void laDistanciaDeUnTrayectoDeTramosDeTodoMenosTPSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioPie, ubicacionFinPie)).thenReturn(73.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioBici, ubicacionFinBici)).thenReturn(313.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioVP, ubicacionFinVP)).thenReturn(10000.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioSC, ubicacionFinSC)).thenReturn(750.0);

    Trayecto trayecto = new Trayecto();
    trayecto.agregarTramos(tramoAPie, tramoEnBici, tramoEnAuto, tramoEnTaxi);

    // 73.0 + 313.0 + 10000.0 + 750.0 = 11136.0
    assertEquals(11136.0, trayecto.distanciaTotal().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTrayectoDeTramosDeTodosLosTiposSeCalculaBien() throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioPie, ubicacionFinPie)).thenReturn(73.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioBici, ubicacionFinBici)).thenReturn(313.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioVP, ubicacionFinVP)).thenReturn(10000.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioSC, ubicacionFinSC)).thenReturn(750.0);

    Trayecto trayecto = new Trayecto();
    trayecto.agregarTramos(tramoAPie, tramoEnBici, tramoEnAuto, tramoEnTaxi, tramoLineaA);

    // 73.0 + 313.0 + 10000.0 + 750.0 + 500.0 = 11786.0
    assertEquals(11636.0, trayecto.distanciaTotal().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTrayectoDeTramosDeTodosLosTiposMasDeUnaVezSeCalculaBien()
      throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioPie, ubicacionFinPie)).thenReturn(73.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioBici, ubicacionFinBici)).thenReturn(313.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioVP, ubicacionFinVP)).thenReturn(10000.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioSC, ubicacionFinSC)).thenReturn(750.0);

    Trayecto trayecto = new Trayecto();
    trayecto.agregarTramos(tramoAPie, tramoEnBici, tramoEnAuto,
        tramoEnTaxi, tramoLineaA, tramoAPie, tramoEnAuto);

    // 73.0 + 313.0 + 10000.0 + 750.0 + 500.0 + 73.0 + 10000.0 = 21859.0
    assertEquals(21709.0, trayecto.distanciaTotal().valorEnMetros());
  }

  @Test
  public void laDistanciaDeUnTrayectoCompartidoDeTramosDeTodosLosTiposMasDeUnaVezSeCalculaBien()
      throws IOException {

    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioVP, ubicacionFinVP)).thenReturn(10000.0);
    when(apiClient.distanciaEntreUbicaciones(ubicacionInicioSC, ubicacionFinSC)).thenReturn(750.0);

    Miembro miembro = new Miembro("Crayon", "Lambert", TipoDeDocumento.DNI, 23666920);
    Miembro miembro2 = new Miembro("El", "Pibe", TipoDeDocumento.DNI, 50501502);
    List<Miembro> miembros = Stream.of(miembro, miembro2).collect(Collectors.toList());

    TrayectoCompartido trayecto = new TrayectoCompartido(miembros, new ArrayList<>());
    trayecto.agregarTramos(tramoEnAuto,
        tramoEnTaxi, tramoEnAuto);

    // 10000.0 + 750.0 + 10000.0 = 20750.0
    assertEquals(20750.0, trayecto.distanciaTotal().valorEnMetros());
  }



  // Metodos aux.

  private Distancia distanciaMts(double valor) {
    return new Distancia(valor, UnidadDeDistancia.MTS);
  }

  private Distancia distanciaKm(double valor) {
    return new Distancia(valor, UnidadDeDistancia.KM);
  }

  private Ubicacion crearUbicacion(String calle, int altura) throws IOException {
    Ubicacion ubicacion;
    //ServicioGeoDds api = mock(ServicioGeoDds.class);
    when(apiClient.verificarNombreLocalidad(anyString())).thenReturn(2);  //id Localidad = 2
    when(apiClient.nombreMunicipio(2)).thenReturn("Valcheta");
    when(apiClient.verificarNombreMunicipio("Valcheta")).thenReturn(4);   //id Municipio = 4
    when(apiClient.nombreProvincia(4)).thenReturn("Rio Negro");
    when(apiClient.verificarNombreProvincia("Rio Negro")).thenReturn(7);  //id Provincia = 7

    ubicacion = new Ubicacion(calle, altura, "Chacabuco", apiClient);
    return ubicacion;
  }

}