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
		for (Vehiculo vehiculo : vehiculos) {
			if (vehiculo.getIdentificador().equals(String.valueOf(id)))
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

	public long getCapacidadMinima() {
		long capacidadMinima = Long.MAX_VALUE;
		for (Vehiculo v : vehiculos) {
			if (v.getCapacidad() < capacidadMinima)
				capacidadMinima = v.getCapacidad();
		}
		
		return capacidadMinima;
	}

	public static int doMutate(int id1, int id2) {
		
		Vehiculo vehiculo1= VehiculoHandler.getInstance().get(id1);
		Vehiculo vehiculo2= VehiculoHandler.getInstance().get(id2);
		
		if (vehiculo2.esMejor(vehiculo1))
			return id2;
		
		if (!vehiculo1.getTipoVehiculo().equals(vehiculo2.getTipoVehiculo()) && vehiculo1.recolectaLoMismo(vehiculo2))
				return id2;
		
		return id1;
	}
}
