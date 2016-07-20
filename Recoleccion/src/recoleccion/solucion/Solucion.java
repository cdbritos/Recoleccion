package recoleccion.solucion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.vector.IntegerVectorIndividual;
import ec.vector.VectorIndividual;
import recoleccion.modelo.viaje.Viaje;

public class Solucion {
	
	private List<Viaje> viajes;

	public List<Viaje> getViajes() {
		return viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		this.viajes = viajes;
	}

	public Solucion(RecoleccionIntegerVectorIndividual ind) {
		
		viajes = new ArrayList<Viaje>();
		int[][] viajesInd = ind.getViajesIndividuo();
		for (int i = 0; i < viajesInd.length; i++) {
			Viaje v = new Viaje(viajesInd[i]);
			viajes.add(v);	
		}
		
		
	}

	
	
}
