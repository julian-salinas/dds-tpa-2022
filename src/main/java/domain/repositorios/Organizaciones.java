package domain.repositorios;

import domain.organizaciones.Organizacion;
import domain.ubicaciones.SectorTerritorial;

import java.util.Collections;
import java.util.List;

public class Organizaciones {
  private static final Organizaciones INSTANCE = new Organizaciones();
  private List<Organizacion> organizaciones;

  public static Organizaciones instance() {
    return INSTANCE;
  }

  public void agregarOrganizacion(Organizacion ... organizaciones) {
    Collections.addAll(this.organizaciones, organizaciones);
  }

  public List<Organizacion> getOrganizaciones() {
    return this.organizaciones;
  }

  public List<Organizacion> inSectorTerritorial(SectorTerritorial sectorTerritorial) {
    return organizaciones;
  }

}
