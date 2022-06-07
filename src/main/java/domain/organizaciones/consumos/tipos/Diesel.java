package domain.organizaciones.consumos.tipos;

import static domain.organizaciones.consumos.Actividad.COMBUSTIONFIJA;
import static domain.organizaciones.consumos.Alcance.DIRECTAS;
import static domain.organizaciones.consumos.Unidad.LT;

public class Diesel extends TipoDeConsumo {
  public Diesel() {
    unidad = LT;
    actividad = COMBUSTIONFIJA;
    alcance = DIRECTAS;
  }
}

