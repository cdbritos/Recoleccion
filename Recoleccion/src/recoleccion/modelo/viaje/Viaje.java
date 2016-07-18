package recoleccion.modelo.viaje;

import java.util.List;

import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.vehiculos.Vehiculo;

public class Viaje {
	
	private Vehiculo vehiculo;
	
	private List<Domicilio> domicilios;

	public Vehiculo getVehiculo() {
		return vehiculo;
	}

	public void setVehiculo(Vehiculo vehiculo) {
		this.vehiculo = vehiculo;
	}

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
	
	
	
	

}
