package domain.organizaciones;

import domain.PersistenceEntity;
import domain.organizaciones.excepciones.ExcepcionNoExisteElMiembroAacptarEnLaOrg;
import domain.organizaciones.excepciones.ExcepcionNoExisteElSectorEnLaOrganizacion;
import domain.organizaciones.miembros.Miembro;
import domain.notificaciones.contactos.Contacto;
import domain.organizaciones.datos.actividades.DatosActividades;
import domain.organizaciones.hc.HC;
import domain.organizaciones.hc.UnidadHC;
import domain.organizaciones.sectores.Sector;
import domain.ubicaciones.sectores.Localidad;
import domain.ubicaciones.sectores.Municipio;
import domain.ubicaciones.sectores.Provincia;
import domain.ubicaciones.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Organizacion extends PersistenceEntity {
  private String nombre;
  private String razonSocial;

  @Enumerated(EnumType.STRING)
  private TipoOrganizacion tipo;

  @Enumerated(EnumType.STRING)
  @Getter
  private ClasificacionOrg clasificacion;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Ubicacion ubicacion;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL) @JoinColumn(name = "org_id")
  private List<Sector> sectores = new ArrayList<>();

  @Transient
  @Setter
  private List<DatosActividades> datosActividades = new ArrayList<>();

  @Transient
  private List<Contacto> contactos = new ArrayList<>();

  @OneToMany
  private List<HC> historialHC = new ArrayList<>();

  public Organizacion() {}

  public Organizacion(String razonSocial, TipoOrganizacion tipo, String nombre,
                      Ubicacion ubicacion, ClasificacionOrg clasificacion) {
    this.nombre = nombre;
    this.razonSocial = razonSocial;
    this.tipo = tipo;
    this.ubicacion = ubicacion;
    this.clasificacion = clasificacion;
  }

  // ------------- 1era entrega ---------------------------------

  public boolean containsSector(Sector sector) {
    return sectores.contains(sector);
  }

  public void agregarSector(Sector sector) {
    //sector.setOrgAlaQuePertenezco(this);
    sectores.add(sector);
  }

  public void aceptarVinculacionDeTrabajador(Miembro miembro, Sector sector) {
    if (sector.containsMiembroParaAceptar(miembro) && this.containsSector(sector)) {
      sector.sacarMiembroParaAceptar(miembro);
      sector.agregarMiembro(miembro);
    } else if(!this.containsSector(sector)) {
      throw new ExcepcionNoExisteElSectorEnLaOrganizacion();
    } else { //if(!sector.containsMiembroParaAceptar(miembro))
      throw new ExcepcionNoExisteElMiembroAacptarEnLaOrg();
    }
  }

  // ------------- 2da entrega ---------------------------------

  public void cargarMediciones(String pathCSV) {
    String linea;

    datosActividades.clear();
    try {
      BufferedReader buffer = new BufferedReader(new FileReader(pathCSV));
      buffer.readLine();
      buffer.readLine(); //Para saltear las dos primeras lineas
      while((linea = buffer.readLine()) != null) {
        String[] fila = linea.split(";");
        datosActividades.add(new DatosActividades(fila[0],fila[1],fila[2],fila[3]));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public List<DatosActividades> getDatosActividades() {
    return datosActividades;
  }

  // ------------- 3era entrega ---------------------------------

  public Ubicacion getUbicacion() {
    return ubicacion;
  }
  public Localidad sectorLocalidad() {
    return ubicacion.getLocalidad();
  }
  public Municipio sectorMunicipio() {
    return ubicacion.getLocalidad().getMunicipio();
  }
  public Provincia sectorProvincia() {
    return ubicacion.getLocalidad().getMunicipio().getProvincia();
  }

  public void agregarContactos(Contacto ... nuevosContactos) {
    Collections.addAll(contactos, nuevosContactos);
  }

  public List<Contacto> getContactos() {
    return contactos;
  }

  public void cargarDATransladoMiembros(){
    double combustibleTransporteMiembros = 30 * sectores.stream().mapToDouble(Sector::combustibleConsumidoTransporteMiembros).sum();
    //SimpleDateFormat formatFecha = new SimpleDateFormat("MM/yyyy");
    DateTimeFormatter formatFecha = DateTimeFormatter.ofPattern("MM/yyyy");
    // Multiplico por 30, para obtener el valor mensual dado que el trayecto que recorren los miembros es a diario
    datosActividades.add(new DatosActividades("Distancia media",
        String.valueOf(combustibleTransporteMiembros),
        "Mensual",
        formatFecha.format(LocalDate.now())));
  }

  private double calculoHCMensual(){
    //this.cargarDATransladoMiembros();
    return datosActividades.stream().mapToDouble(DatosActividades::impactoHC).sum();
  }

  public HC hcMensual(){
    double hcDatosActividad = calculoHCMensual();
    HC hc = new HC(hcDatosActividad, UnidadHC.kgCO2);
    this.historialHC.add(hc);
    return hc;
  }

  public HC hcAnual(){
    double hcDatosActividad = calculoHCMensual() * 12;
    return new HC(hcDatosActividad, UnidadHC.kgCO2);
  }

}
