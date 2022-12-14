package domain.organizaciones.miembros;

import domain.database.PersistenceEntity;
import domain.organizaciones.datos.actividades.tipos.TipoDeConsumo;
import domain.organizaciones.excepciones.ExcepcionNoExisteElSectorEnLaOrganizacion;
import domain.organizaciones.Organizacion;
import domain.organizaciones.sectores.Sector;
import domain.organizaciones.hc.UnidadHC;
import domain.organizaciones.datos.actividades.tipos.FactorEmision;
import domain.organizaciones.hc.HC;
import repositorios.RepositorioConsumos;
import domain.trayecto.Trayecto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;

@Getter
@Entity
public class Miembro extends PersistenceEntity {

  private String nombre;
  private String apellido;

  @Enumerated(EnumType.STRING)
  private TipoDeDocumento tipoDeDocumento; // no se especifica nada

  private int nroDeDocumento; // era Integer, lo paso a int (a debatir)

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) @JoinTable(name = "trayecto_x_miembro")
  private final List<Trayecto> trayectos = new ArrayList<>();

  public Miembro() {}

  public Miembro(String nombre, String apellido, TipoDeDocumento tipoDeDocumento, int nroDeDocumento) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.tipoDeDocumento = tipoDeDocumento;
    this.nroDeDocumento = nroDeDocumento;
  }

  public void agregarTrayecto(Trayecto trayecto) {
    trayectos.add(trayecto);
  }

  public void registrarTrayecto(Trayecto trayecto) {
    // Puede que tenga que ser con muchos trayectos en vez de uno
    trayecto.setOwner(this);
    trayectos.add(trayecto);
  }

  public boolean containsTrayecto(Trayecto trayecto) {
    return trayectos.contains(trayecto);
  }

  public void vincularTrabajadorConOrg(Organizacion org, Sector sector) {
    if (org.containsSector(sector)) {
      sector.agregarMiembroParaAceptar(this);
    } else {
      throw new ExcepcionNoExisteElSectorEnLaOrganizacion();
    }
  }

  // Deprecated
  public HC calculoHCPersonal(FactorEmision fe){
    return new HC(fe.getValor() * trayectos.stream().mapToDouble(Trayecto::combustibleTotalUtilizado).sum(), UnidadHC.kgCO2);
  }

  public HC calculoHCPersonal(){
    TipoDeConsumo tipoDeConsumo = RepositorioConsumos.getInstance().findByName("Distancia media");
    FactorEmision fe = tipoDeConsumo.getFe();
    return new HC(fe.getValor() * trayectos.stream().mapToDouble(Trayecto::combustibleTotalUtilizado).sum(), UnidadHC.kgCO2);
  }

}
