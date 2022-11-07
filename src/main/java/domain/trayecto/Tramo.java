package domain.trayecto;

import domain.database.PersistenceEntity;
import domain.trayecto.transporte.MedioDeTransporte;
import domain.trayecto.transporte.publico.Parada;
import domain.ubicaciones.Ubicacion;
import domain.ubicaciones.distancia.Distancia;
import lombok.Getter;

import javax.persistence.*;
import java.io.IOException;

@Entity
public class Tramo extends PersistenceEntity {

  @ManyToOne(cascade = CascadeType.ALL)
  private MedioDeTransporte medio;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @JoinColumn(name = "ubicacion_inicio")
  @Getter private Ubicacion ubicacionInicio;
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) @JoinColumn(name = "ubicacion_fin")
  @Getter private Ubicacion ubicacionFin;

  public Tramo() {}

  public Tramo(MedioDeTransporte medio, Ubicacion ubicacionInicio, Ubicacion ubicacionFin) {
    this.medio = medio;
    this.ubicacionInicio = ubicacionInicio;
    this.ubicacionFin = ubicacionFin;
  }

  public Tramo(MedioDeTransporte medio, Parada paradaInicio, Parada paradaFin) {
    this.medio = medio;
    this.ubicacionInicio = paradaInicio.getUbicacionParada();
    this.ubicacionFin = paradaFin.getUbicacionParada();
  }

  public boolean admiteTrayectoCompartido() {
    return medio.admiteTrayectoCompartido();
  }

  public Distancia distancia() {
    return medio.distancia(ubicacionInicio, ubicacionFin);
  }

  public double combustibleUtilizado() {
    return medio.getCombustibleConsumidoPorKM() * (this.distancia().valorEnMetros() / 1000);
  }

}
