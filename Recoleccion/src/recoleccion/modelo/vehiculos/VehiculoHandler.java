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
		int idVehiculo = Math.abs(id);
		for (Vehiculo vehiculo : vehiculos) {
			if (vehiculo.getIdentificador().equals(String.valueOf(idVehiculo)))
				return vehiculo;
		}
		
		return null;
		
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
