package recoleccion.modelo.vehiculos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;

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
		List<Vehiculo> result = new ArrayList<Vehiculo>();
		
		if (CollectionUtils.isNotEmpty(vehiculos)){
			for (Vehiculo v : vehiculos) {
				if (v.getTipoVehiculo().equals(TipoVehiculo.CAMION)){
					Camion camion = new Camion(v);
					result.add(camion);
				}else if (v.getTipoVehiculo().equals(TipoVehiculo.CAMIONETA)){
					Camioneta camioneta = new Camioneta(v);
					result.add(camioneta);
				}
			}
		}
		return result;
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
