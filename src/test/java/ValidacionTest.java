/*

import static org.junit.jupiter.api.Assertions.*;

import administrador.contrasenia.*;
import administrador.contrasenia.excepciones.*;

import dominio.organizacionymiembros.*;

import domain.miembros.ClasificacionOrganizacion;
import dominio.organizacionymiembros.excepciones.ExcepcionNoExisteElMiembroAacptarEnLaOrg;
import dominio.organizacionymiembros.excepciones.ExcepcionNoExisteElSectorEnLaOrganizacion;
import domain.trayecto.Trayecto;
import domain.trayecto.transporte.Linea;
import domain.trayecto.transporte.Parada;
import domain.trayecto.transporte.TipoTransportePublico;
import domain.trayecto.transporte.TransportePublico;

import domain.trayecto.transporte.excepciones.ExcepcionParadasTransporteNoIncluidasEnLinea;
import domain.trayecto.transporte.excepciones.ExcepcionTipoTransporteNoIgualAtipoDeLinea;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ValidacionTest {
  ClasificacionOrganizacion ministerio = new ClasificacionOrganizacion("ministerio");
  ClasificacionOrganizacion universidad = new ClasificacionOrganizacion("universidad");
  ClasificacionOrganizacion escuela = new ClasificacionOrganizacion("escuela");

  Organizacion organizacionDefault = new Organizacion("?", TipoOrganizacion.EMPRESA, "Rosario", ministerio);

  Sector sectorDefault = new Sector();
  Sector sectorParaAgregar = new Sector();
  Miembro miembroDefault = new Miembro("Juan", "Martin", "Crack?", "43-208-556");
  Miembro miembroError = new Miembro("Pedro", "Gonzales", "Crack?", "43-218-556");

  Trayecto trayectoDefault = new Trayecto();
  Parada paradaDefault1 = new Parada("Carabobo");
  Parada paradaDefault2 = new Parada("Puan");
  List<Parada> paradasLineaA = new ArrayList<>();
  // En los tests agregar paradaDefault1 y paradaDefault2 a paradasLineaA. (si se piensa usar)
  Linea lineaDefault = new Linea("A", paradasLineaA, TipoTransportePublico.SUBTE);
  //Tramo tramoTransportePublico = new Tramo(subteLineaA);
  // En los tests agregar tramoTransportePublico a trayectoDefault. (si se piensa usar)

  // sumar tramos de otros tipos de transporte.


  // Tests generales





  @Test
  public void agregoParadaAUnaLinea(){
    lineaDefault.agregarParada(paradaDefault1);
    assertTrue(lineaDefault.getParadas().contains(paradaDefault1));
  }

  @Test
  public void puedoCrearUnTransportePublicoSiSuTipoYparadasCoincidenConLosDeLaLinea(){
    lineaDefault.agregarParada(paradaDefault1);
    lineaDefault.agregarParada(paradaDefault2);
    assertDoesNotThrow(() -> new TransportePublico(TipoTransportePublico.SUBTE, lineaDefault, paradaDefault1, paradaDefault2));
  }

  @Test
  public void crearUnTransportePublicoCuyoTipoYtipoDeLineaNoCoincidenTiraError(){
    // lineaDefault.tipo es SUBTE
    lineaDefault.agregarParada(paradaDefault1);
    lineaDefault.agregarParada(paradaDefault2);
    assertThrows(ExcepcionTipoTransporteNoIgualAtipoDeLinea.class, () -> new TransportePublico(TipoTransportePublico.TREN, lineaDefault, paradaDefault1, paradaDefault2));
  }

  @Test
  public void crearUnTransportePublicoCuyasParadasNoEstenEnLaLineaTiraError(){
    lineaDefault.agregarParada(paradaDefault1);
    lineaDefault.agregarParada(paradaDefault2);
    Parada paradaError1 = new Parada("Acoyte");
    Parada paradaError2 = new Parada("Castro Barros");
    // Nunca los agrego a lineaDefault
    assertThrows(ExcepcionParadasTransporteNoIncluidasEnLinea.class, () -> new TransportePublico(TipoTransportePublico.SUBTE, lineaDefault, paradaError1, paradaError2));
  }


}
*/
