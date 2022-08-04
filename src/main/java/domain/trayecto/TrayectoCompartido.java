package domain.trayecto;

import domain.miembros.Miembro;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

public class TrayectoCompartido extends Trayecto{

  private List<Miembro> miembros = new ArrayList<>();
  private List<Tramo> tramos = new ArrayList<>();
  @Getter @Setter private Miembro owner;

  public TrayectoCompartido(List<Miembro> miembros, List<Tramo> tramos) {
    tramos.forEach(this::validacionTrayectoCompartido);
    this.miembros = miembros;
    this.tramos = tramos;
    agregarTrayCompAmiembros(miembros);
  }

  private void validacionTrayectoCompartido(Tramo tramo) {
    if(!tramo.admiteTrayectoCompartido()) {
      throw new RuntimeException("No se puede hacer un trayecto compartido que no sea "
          + "de tipo Servico Contratado o Vehiculo Particular");
    }
  }

  private void agregarTrayCompAmiembros(List<Miembro> miembros) {
    miembros
        .stream()
        .filter(miembro -> !miembro.equals(owner)) //Por las dudas
        .forEach(miembro -> miembro.agregarTrayecto(this));
  }

  @Override
  public List<Miembro> miembros() {
    if(owner!=null && !miembros.contains(owner)) {
      // Junto al miembro que registro con los que compartio
      List<Miembro> miembrosQueMeCargaron = new ArrayList<>();
      List<Miembro> ownerList = new ArrayList<>();
      ownerList.add(owner);
      miembrosQueMeCargaron.addAll(ownerList);
      miembrosQueMeCargaron.addAll(miembros);
      return miembrosQueMeCargaron;
    } else if(owner==null) {
      throw new RuntimeException("Todavia no se registro el trayecto");
    }
    // Si por alguna razon, el que registro el trayecto esta incluido en los que compartieron,
    // devolve los que compartierion directamente
    return miembros;
  }

  @Override
  public void agregarTramo(Tramo tramo) {
    validacionTrayectoCompartido(tramo);
    tramos.add(tramo);
  }

}
