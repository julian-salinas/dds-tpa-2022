package domain.ubicaciones.sectores;

import domain.database.PersistenceEntity;
import domain.organizaciones.hc.HC;
import domain.organizaciones.Organizacion;
import domain.organizaciones.hc.UnidadHC;
import lombok.Getter;
import lombok.Setter;
import repositorios.RepositorioOrganizaciones;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity(name = "agente_sectorial")
@Getter @Setter
public class AgenteSectorial extends PersistenceEntity {
  String nombreAgente;
  int idSectorTerritorial;
  String nombreSectorTerritorial;
  @Enumerated(EnumType.STRING)
  TipoSectorTerritorial tipoSectorTerritorial;

  public AgenteSectorial() {

  }

  public AgenteSectorial(TipoSectorTerritorial tipo, int id, String nombre) {
    this.tipoSectorTerritorial = tipo;
    this.idSectorTerritorial = id;
    this.nombreSectorTerritorial = nombre;
    //this.sectorTerritorial = encontrarSectorTerritorial();
  }

  public List<Organizacion> encontrarOrgs() {
    if(tipoSectorTerritorial.equals(TipoSectorTerritorial.MUNICIPIO)) {
      return RepositorioOrganizaciones.getInstance().inMunicipio(idSectorTerritorial);
    }
    else {  // tipoSecTerritorial.equals(TipoSectorTerritorial.PROVINCIA)
      return RepositorioOrganizaciones.getInstance().inProvincia(idSectorTerritorial);
    }
  }

  public HC hcSectorMensual(String mes) {
    List<Organizacion> orgInSectorTerr = encontrarOrgs();
    double hcEnKgCO2 = orgInSectorTerr.stream().mapToDouble(org -> org.hcMensual(mes).enKgCO2()).sum();
    return new HC(hcEnKgCO2, UnidadHC.kgCO2);
  }

  public HC hcSectorAnual(String anio) {
    List<Organizacion> orgInSectorTerr = encontrarOrgs();
    double hcEnKgCO2 = orgInSectorTerr.stream().mapToDouble(org -> org.hcAnual(anio).enKgCO2()).sum();
    return new HC(hcEnKgCO2, UnidadHC.kgCO2);
  }

}
