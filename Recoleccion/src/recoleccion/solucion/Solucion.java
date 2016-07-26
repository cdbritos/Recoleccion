package recoleccion.solucion;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import recoleccion.ecj.IntegerVectorIndividualRecoleccion;
import recoleccion.modelo.vehiculos.Vehiculo;
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
		System.out.println(ind.genotypeToStringForHumans());
	}
	
	public Solucion(){
		
	}

	// viajes sin domicilio se dejan en la solucion tiene fitness 0
	public double fitness(){
		double fitness = 0;
		List<Vehiculo> vehiculosUtilizados = new ArrayList<Vehiculo>();
		
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.doViaje();
				if (!vehiculosUtilizados.contains(viaje.getVehiculo()))
					vehiculosUtilizados.add(viaje.getVehiculo());
			}
		}
		
		for (Vehiculo vehiculo : vehiculosUtilizados){
			if (vehiculo != null)
				fitness += vehiculo.getCostoJornada();
		}		
		return fitness;
	}
	
	public void imprimir(){
		if (CollectionUtils.isNotEmpty(viajes)){
			for (Viaje viaje : viajes) {
				viaje.imprimir();
			}
		}
	}
	
	public Solucion corregirSolucion(List<Vehiculo> vehiculosUtilizados){
		Solucion sol=new Solucion();
		for (Viaje viaje : viajes) {
			List<Viaje> viajesCorregidos=new ArrayList<>();
			viajesCorregidos=viaje.corregir(vehiculosUtilizados);
			for (Viaje viajeCorregido : viajesCorregidos){
				viajes.add(viajeCorregido);
			}
		}
		sol.setViajes(viajes);
		return sol;
		//this.setViajes(viajes);
	}
	
}
