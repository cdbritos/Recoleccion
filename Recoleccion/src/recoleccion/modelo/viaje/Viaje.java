package recoleccion.modelo.viaje;

import java.util.Set;

import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.vehiculos.Vehiculo;

public class Viaje {
	
	private Vehiculo vehiculo;
	
	private Set<Domicilio> domicilios;

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public Set<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(Set<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
	
	
	
	

}
