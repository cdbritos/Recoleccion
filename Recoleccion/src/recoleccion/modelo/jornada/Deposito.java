package recoleccion.modelo.jornada;

import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;

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
	
	@Override
	protected String getTipo() {
		return "DEPOSITO";
	}
	
	@Override
	public void imprimir() {
		super.imprimir();
		if (CollectionUtils.isNotEmpty(flota))
			for (Vehiculo vehiculo : flota) {
				System.out.print("	"); vehiculo.imprimir();
			}
	}
	
}
