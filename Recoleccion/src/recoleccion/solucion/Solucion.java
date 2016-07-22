package recoleccion.solucion;

import java.util.ArrayList;
import java.util.List;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.viaje.Viaje;

public class Solucion {
	
	private List<Viaje> viajes;

	public List<Viaje> getViajes() {
		return viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		this.viajes = viajes;
	}

	public Solucion(IntegerVectorIndividualRecoleccion ind) {
		
		this.viajes = new ArrayList<Viaje>();
		int[][] viajesInd = ind.getViajesIndividuo();
		for (int i = 0; i < viajesInd.length; i++) {
			Viaje v = new Viaje(viajesInd[i]);
			this.viajes.add(v);	
		}		
	}
	
	public Solucion(){
		
	}

	// viajes sin domicilio se dejan en la solucion tiene fitness 0
	public double fitness(){
		double fitness = 0;
		if (viajes != null){
			for (Viaje viaje : viajes) {
				fitness += viaje.fitness();
			}
		}
		return fitness;
	}
	
	public void imprimir(){
		if (viajes != null){
			for (Viaje viaje : viajes) {
				viaje.imprimir();
			}
		}
	}
	
}
