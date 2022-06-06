import domain.excepciones.ExcepcionNoExisteElSectorEnLaOrganizacion;
import domain.organizaciones.ClasificacionOrganizacion;
import domain.miembros.Miembro;
import domain.organizaciones.Organizacion;
import domain.organizaciones.Sector;
import domain.organizaciones.TipoOrganizacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MiembroTest {
  private Organizacion organizacionDefault;
  private Sector sectorDefault;
  private Miembro miembroDefault;
  private ClasificacionOrganizacion ministerio;

  @BeforeEach
  void init() {
    ministerio = new ClasificacionOrganizacion("ministerio");
    organizacionDefault = new Organizacion("?", TipoOrganizacion.EMPRESA, "Rosario", ministerio);
    sectorDefault = new Sector();
    miembroDefault = new Miembro("Juan", "Martin", "Crack?", "43-208-556");
  }

  @Test
  public void unMiembroSePuedeVincularAunaOrganizacion(){
    organizacionDefault.agregarSector(sectorDefault);
    miembroDefault.vincularTrabajadorConOrg(organizacionDefault, sectorDefault);
    assertTrue(sectorDefault.containsMiembroParaAceptar(miembroDefault));
    assertFalse(sectorDefault.containsMiembro(miembroDefault));
  }

  @Test
  public void unMiembroSePuedeVincularAunaOrganizacionYestaLoPuedeAceptar(){
    organizacionDefault.agregarSector(sectorDefault);
    miembroDefault.vincularTrabajadorConOrg(organizacionDefault, sectorDefault);
    organizacionDefault.aceptarVinculacionDeTrabajador(miembroDefault, sectorDefault);
    assertTrue(sectorDefault.containsMiembro(miembroDefault));
    assertFalse(sectorDefault.containsMiembroParaAceptar(miembroDefault));
    assertEquals(miembroDefault.getSectorDondeTrabaja(), sectorDefault);
  }

  @Test
  public void unMiembroNoSePuedeVincularAunSectorQueNoExisteEnLaOrganizacion(){
    // Existe la org y el sector, sin embargo el sector NO es un sector de la org.
    assertThrows(ExcepcionNoExisteElSectorEnLaOrganizacion.class,
        () -> miembroDefault.vincularTrabajadorConOrg(organizacionDefault,sectorDefault));
  }

}
