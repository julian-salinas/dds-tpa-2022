package domain.ubicaciones.sectores;

import domain.servicios.geodds.ServicioGeoDds;
import java.io.IOException;

import lombok.Getter;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.sound.midi.MidiChannel;

public class Localidad {
  @Getter private int id;
  @Getter private String nombre;
  @Getter private Municipio municipio;
  private ServicioGeoDds apiClient;

  @Deprecated
  public Localidad(String nombreLocalidad, ServicioGeoDds apiClient) throws RuntimeException, IOException {
    //this.apiClient = ServicioGeoDds.getInstancia();
    this.apiClient = apiClient;
    this.id = this.apiClient.verificarNombreLocalidad(nombreLocalidad);
    this.municipio = new Municipio(apiClient.nombreMunicipio(id), apiClient);
    this.nombre = nombreLocalidad.toUpperCase();
  }

  public Localidad(String nombreLocalidad, Municipio municipio, ServicioGeoDds apiClient) throws RuntimeException, IOException {
    //this.apiClient = ServicioGeoDds.getInstancia();
    this.apiClient = apiClient;
    this.id = this.apiClient.verificarNombreLocalidad(nombreLocalidad, municipio.getId());
    this.nombre = nombreLocalidad.toUpperCase();
    this.municipio = municipio;
  }

}
