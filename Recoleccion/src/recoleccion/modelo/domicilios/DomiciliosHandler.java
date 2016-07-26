package recoleccion.modelo.domicilios;

import java.util.List;
import java.util.Random;

import recoleccion.modelo.vehiculos.Vehiculo;


public class DomiciliosHandler {

	private static DomiciliosHandler instance = null;
	
	private List<Domicilio> domicilios;
	
	private DomiciliosHandler() {
		
	}
	
	public static DomiciliosHandler getInstance(){
		
		if (instance == null)
			instance = new DomiciliosHandler();
		
		return instance;
	}
	
	public Domicilio get(int id){
		Domicilio d = new Domicilio(id);
		return domicilios.get(domicilios.indexOf(d));
	}

	public List<Domicilio> getDomicilios() {
		return domicilios;
	}

	public void setDomicilios(List<Domicilio> domicilios) {
		this.domicilios = domicilios;
	}
	
	public Domicilio randomVehiculo(){
		try {
	        return domicilios.get((new Random()).nextInt(domicilios.size()));
	    }
	    catch (Throwable e){
	        return null;
	    }
    }
}


