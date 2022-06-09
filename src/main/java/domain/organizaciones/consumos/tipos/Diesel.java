package domain.organizaciones.consumos.tipos;

import static domain.organizaciones.consumos.Actividad.COMBUSTION_FIJA;
import static domain.organizaciones.consumos.Alcance.DIRECTAS;
import static domain.organizaciones.consumos.Unidad.LT;

public class Diesel extends TipoDeConsumo {
  public Diesel() {
    unidad = LT;
    actividad = COMBUSTION_FIJA;
    alcance = DIRECTAS;
  }
}
