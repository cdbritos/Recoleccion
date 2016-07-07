package recoleccion.modelo.jornada;

import java.util.List;

import recoleccion.modelo.data.Coordenable;
import recoleccion.modelo.vehiculos.Vehiculo;

public class Deposito extends Coordenable {
	
	private List<Vehiculo> flota;

	public List<Vehiculo> getFlota() {
		return flota;
	}

	public void setFlota(List<Vehiculo> flota) {
		this.flota = flota;
	}
}
