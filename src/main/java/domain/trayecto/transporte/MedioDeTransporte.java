package domain.trayecto.transporte;

import domain.PersistenceEntity;
import domain.ubicaciones.distancia.Distancia;
import lombok.Getter;

import javax.persistence.*;

@Entity(name = "medio_de_transporte")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MedioDeTransporte extends PersistenceEntity
{

  public abstract Distancia distancia();
  public abstract Boolean admiteTrayectoCompartido();
  public abstract double getCombustibleConsumidoPorKM();
}
