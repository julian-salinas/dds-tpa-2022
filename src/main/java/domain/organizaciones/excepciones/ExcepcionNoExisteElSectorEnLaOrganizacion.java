package domain.organizaciones.excepciones;

public class ExcepcionNoExisteElSectorEnLaOrganizacion extends RuntimeException {
  public ExcepcionNoExisteElSectorEnLaOrganizacion() {
    super("El sector al que se quiere unir no existe en la organizacion");
  }
}
