package recoleccion.modelo.vehiculos;

import java.util.List;
import java.util.Random;

public class VehiculoHandler {

	private static VehiculoHandler instance = null;
	private List<Vehiculo> vehiculos;
	
	private VehiculoHandler() {
		
	}
	
	public static VehiculoHandler getInstance(){
		
		if (instance == null)
			instance = new VehiculoHandler();
		
		return instance;
	}
	
	public Vehiculo get(int id){
		//String identificador = String.valueOf(Math.abs(id));
		
		return Math.abs(id) % 2 == 0 ? new Camion(id) : new Camioneta(id);
	}

	public Vehiculo get() {
		//retorna un vehiculo aletoria, solo se llamaria si es el primer viaje 
		// una solucion ya cruzada
		
		return new Camion(Long.valueOf(Math.round(Math.random()*100)).intValue());
	}

	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}

	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public Vehiculo randomVehiculo(){
		try {
	        return vehiculos.get((new Random()).nextInt(vehiculos.size()));
	    }
	    catch (Throwable e){
	        return null;
	    }
    }
}
